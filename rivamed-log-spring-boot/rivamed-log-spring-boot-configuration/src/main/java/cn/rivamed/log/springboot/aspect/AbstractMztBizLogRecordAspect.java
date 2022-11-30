package cn.rivamed.log.springboot.aspect;

import brave.propagation.TraceContext;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.constant.LogMessageContextConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.entity.LogRecordMessage;
import cn.rivamed.log.core.entity.TraceId;
import cn.rivamed.log.core.factory.MessageAppenderFactory;
import cn.rivamed.log.core.rpc.RivamedLogRecordHandler;
import cn.rivamed.log.core.util.IpGetter;
import cn.rivamed.log.core.util.JsonUtil;
import cn.rivamed.log.core.util.LogTemplateUtil;
import cn.rivamed.log.core.util.RivamedClassUtils;
import cn.rivamed.log.springboot.handler.RivamedMztBizLogRecordHandler;
import com.google.common.collect.Lists;
import com.mzt.logapi.beans.LogRecordOps;
import com.mzt.logapi.beans.MethodExecuteResult;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.IFunctionService;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.support.aop.LogRecordOperationSource;
import com.mzt.logapi.starter.support.parse.LogFunctionParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * AbstractMztBizLogRecordAspect
 * mzt-biz-log日志解析切面
 */
public abstract class AbstractMztBizLogRecordAspect extends RivamedMztBizLogRecordHandler implements Serializable, SmartInitializingSingleton {

    private static Logger logger = LoggerFactory.getLogger(RivamedLogRecordHandler.class);

    private static final String API_OPERATION_CLASS_NAME = "io.swagger.annotations.ApiOperation";
    private static final String API_OPERATION_FIELD_NAME = "value";

    private LogRecordOperationSource logRecordOperationSource;

    public Object aroundExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        //初始化信息
        LogRecordMessage message = new LogRecordMessage();
        List<String> actionList = null;
        Method method = null;
        String methodName = null;
        String methodDesc;
        Object returnValue;
        MethodExecuteResult methodExecuteResult = null;

        Collection<LogRecordOps> operations = new ArrayList<>();
        Map<String, String> functionNameAndReturnMap = new HashMap<>();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            //参数解析
            final List<Object> params = new ArrayList<>();
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (sra == null) {
                return null;
            }
            HttpServletRequest request = sra.getRequest();
            TraceContext context = (TraceContext) request.getAttribute(TraceContext.class.getName());
            message.setTraceId(context.traceIdString());
            message.setSpanId(context.spanIdString());
            TraceId.logTraceID.set(context.traceIdString());
            TraceId.logSpanID.set(context.spanIdString());
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                Object object = args[i];
                if (object instanceof HttpServletResponse || object instanceof HttpServletRequest) {
                    continue;
                }
                params.add(object);
            }
            MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            method = ms.getMethod();
            methodName = joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + method.getName();
            Class<?> targetClass = ms.getClass();

            String cloneParams;
            try {
                cloneParams = JsonUtil.toJSONString(params);
            } catch (Exception e) {
                cloneParams = params.toString();
            }
            if (RivamedLogContext.isRequestEnable()) {
                logger.info(request.getRequestURI() + " param: {}", cloneParams);
            }
            //设置基础数据
            message.setMethod(methodName);
            message.setUrl(request.getRequestURI());
            message.setSysName(RivamedLogContext.getSysName());
            message.setEnv(RivamedLogContext.getEnv());
            message.setClassName(ms.getMethod().getDeclaringClass().getName());
            message.setThreadName(Thread.currentThread().getName());
            message.setBizIP(IpGetter.CURRENT_IP);
            message.setLogType(LogMessageConstant.LOG_TYPE_RECORD);

            //mzt-biz-log注解解析
            methodExecuteResult = new MethodExecuteResult(method, args, targetClass);
            LogRecordContext.putEmptySpan();
            LogRecordContext.putVariable(LogMessageContextConstant.CONTEXT_OPERATOR, RivamedLogContext.getCurrentUser());
            try {
                operations = logRecordOperationSource.computeLogRecordOperations(method, targetClass);
                List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operations);
                functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, targetClass, method, args);
            } catch (Exception e) {
                log.error("log record parse before function exception", e);
            }
            //执行目标方法
            returnValue = joinPoint.proceed(joinPoint.getArgs());
            methodExecuteResult.setResult(returnValue);
            methodExecuteResult.setSuccess(true);

            String result;
            try {
                result = JsonUtil.toJSONString(returnValue);
            } catch (Exception e) {
                result = returnValue.toString();
            }
            if (RivamedLogContext.isResponseEnable()) {
                logger.info(request.getRequestURI() + " result: {}", result);
            }
            message.setLevel(LogLevel.INFO.name());
            message.setResponseCode(String.valueOf(HttpStatus.OK.value()));
            return returnValue;
        } catch (Exception e) {
            message.setLevel(LogLevel.ERROR.name());
            message.setResponseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            methodExecuteResult.setSuccess(false);
            methodExecuteResult.setThrowable(e);
            methodExecuteResult.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            stopWatch.stop();
            message.setCostTime(stopWatch.getTime()).setBizTime(new Date());
            //获取mzt-biz-log注解解析结果
            try {
                if (!CollectionUtils.isEmpty(operations)) {
                    actionList = recordExecute(methodExecuteResult, functionNameAndReturnMap, operations);
                }
            } catch (Exception t) {
                log.error("log record parse exception", t);
            }
            //设置方法描述方法描述取swagger配置的值，如果没有生成默认的
            String value = RivamedClassUtils.getAnnotationValue(API_OPERATION_CLASS_NAME, method, API_OPERATION_FIELD_NAME);
            if (StringUtils.isNotBlank(value)) {
                methodDesc = value;
                message.setLogRecordType(LogMessageConstant.LOG_RECORD_TYPE_SWAGGER);
            } else {
                methodDesc = methodName;
                message.setLogRecordType(LogMessageConstant.LOG_RECORD_TYPE_SYSTEM);
            }
            message.setMethodDesc(value);

            //根据解析结果设置语义化日志 语义化日志优先级 mztbiz >> swagger >> system
            if (CollectionUtils.isEmpty(actionList)) {
                //根据成功失败填充对应的模板
                if (methodExecuteResult.isSuccess()) {
                    message.setBizDetail(String.format(LogTemplateUtil.LOG_RECORD_SUCCESS_FORMAT, methodDesc, stopWatch.getTime()));
                } else {
                    message.setBizDetail(String.format(LogTemplateUtil.LOG_RECORD_FAIL_FORMAT, methodDesc));
                }
            } else {
                message.setBizDetail(actionList.get(0));
                message.setLogRecordType(LogMessageConstant.LOG_RECORD_TYPE_MZTBIZ);
            }
            //设置额外信息并推送消息
            RivamedLogContext.buildLogMessage(message);
            MessageAppenderFactory.push(message);
            cleanThreadLocal();
        }
    }

    private List<String> recordExecute(MethodExecuteResult methodExecuteResult, Map<String, String> functionNameAndReturnMap,
                                       Collection<LogRecordOps> operations) {
        List<String> actionList = Lists.newArrayList();
        for (LogRecordOps operation : operations) {
            try {
                if (org.springframework.util.StringUtils.isEmpty(operation.getSuccessLogTemplate())
                        && org.springframework.util.StringUtils.isEmpty(operation.getFailLogTemplate())) {
                    continue;
                }

                if (exitsCondition(methodExecuteResult, functionNameAndReturnMap, operation)) continue;

                if (!methodExecuteResult.isSuccess()) {
                    String action = failRecordExecute(methodExecuteResult, functionNameAndReturnMap, operation);
                    if (StringUtils.isNotBlank(action)) {
                        actionList.add(action);
                    }
                } else {
                    String action = successRecordExecute(methodExecuteResult, functionNameAndReturnMap, operation);
                    if (StringUtils.isNotBlank(action)) {
                        actionList.add(action);
                    }
                }
            } catch (Exception t) {
                log.error("log record execute exception", t);
            }
        }
        return actionList;
    }

    private boolean exitsCondition(MethodExecuteResult methodExecuteResult,
                                   Map<String, String> functionNameAndReturnMap, LogRecordOps operation) {
        if (!org.springframework.util.StringUtils.isEmpty(operation.getCondition())) {
            String condition = singleProcessTemplate(methodExecuteResult, operation.getCondition(), functionNameAndReturnMap);
            if (org.springframework.util.StringUtils.endsWithIgnoreCase(condition, "false")) return true;
        }
        return false;
    }

    private String successRecordExecute(MethodExecuteResult methodExecuteResult, Map<String, String> functionNameAndReturnMap,
                                        LogRecordOps operation) {
        // 若存在 isSuccess 条件模版，解析出成功/失败的模版
        String action;
        if (!org.springframework.util.StringUtils.isEmpty(operation.getIsSuccess())) {
            String condition = singleProcessTemplate(methodExecuteResult, operation.getIsSuccess(), functionNameAndReturnMap);
            if (org.springframework.util.StringUtils.endsWithIgnoreCase(condition, "true")) {
                action = operation.getSuccessLogTemplate();
            } else {
                action = operation.getFailLogTemplate();
            }
        } else {
            action = operation.getSuccessLogTemplate();
        }
        if (org.springframework.util.StringUtils.isEmpty(action)) {
            // 没有日志内容则忽略
            return null;
        }
        List<String> spElTemplates = getSpElTemplates(operation, action);
        Map<String, String> expressionValues = processTemplate(spElTemplates, methodExecuteResult, functionNameAndReturnMap);
        expressionValues.put("totalTimeMillis", functionNameAndReturnMap.get("totalTimeMillis"));
        expressionValues.put("param", functionNameAndReturnMap.get("param"));
        return getAction(action, expressionValues);
    }

    private String failRecordExecute(MethodExecuteResult methodExecuteResult, Map<String, String> functionNameAndReturnMap,
                                     LogRecordOps operation) {
        if (org.springframework.util.StringUtils.isEmpty(operation.getFailLogTemplate())) return null;

        String action = operation.getFailLogTemplate();
        List<String> spElTemplates = getSpElTemplates(operation, action);

        Map<String, String> expressionValues = processTemplate(spElTemplates, methodExecuteResult, functionNameAndReturnMap);
        expressionValues.put("totalTimeMillis", functionNameAndReturnMap.get("totalTimeMillis"));
        expressionValues.put("param", functionNameAndReturnMap.get("param"));
        return getAction(action, expressionValues);
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.setLogFunctionParser(new LogFunctionParser(beanFactory.getBean(IFunctionService.class)));
        this.setDiffParseFunction(beanFactory.getBean(DiffParseFunction.class));
        this.logRecordOperationSource = beanFactory.getBean(LogRecordOperationSource.class);
    }

    private List<String> getBeforeExecuteFunctionTemplate(Collection<LogRecordOps> operations) {
        List<String> spElTemplates = new ArrayList<>();
        for (LogRecordOps operation : operations) {
            //执行之前的函数，失败模版不解析
            List<String> templates = getSpElTemplates(operation, operation.getSuccessLogTemplate());
            if (!CollectionUtils.isEmpty(templates)) {
                spElTemplates.addAll(templates);
            }
        }
        return spElTemplates;
    }

    private List<String> getSpElTemplates(LogRecordOps operation, String... actions) {
        List<String> spElTemplates = Lists.newArrayList(operation.getType(), operation.getBizNo(), operation.getSubType(), operation.getExtra());
        spElTemplates.addAll(Arrays.asList(actions));
        return spElTemplates;
    }

    private String getAction(String action, Map<String, String> expressionValues) {
        if (org.springframework.util.StringUtils.isEmpty(expressionValues.get(action)) || Objects.equals(action, expressionValues.get(action))) {
            return null;
        }
        return expressionValues.get(action);
    }
}
