package cn.rivamed.log.springboot.instrument;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 描述: mzt-biz-log增强类
 * 直接跳过框架原始的方法 不然在AOP自定义实现时会执行两次
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/10/24 10:34
 */
public class MztBizLogInstrumentation {

    public static final String ENHANCE_MZTBIZLOG_TEMPLATE_CLASS = "com.mzt.logapi.starter.support.aop.LogRecordInterceptor"; // 增强的类
    public static final String ENHANCE_INVOKE_METHOD = "invoke"; // 增强的方法

    public static boolean enhance() throws NotFoundException, CannotCompileException {

        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));

        //拦截send方法
        CtClass ctClass= classPool.getOrNull(ENHANCE_MZTBIZLOG_TEMPLATE_CLASS);
        if (ctClass == null) {
            return false;
        }
        CtClass invocationClass = classPool.get(MethodInvocation.class.getName());
        CtClass[] params = new CtClass[]{invocationClass};
        CtMethod doExecuteMethod = ctClass.getDeclaredMethod(ENHANCE_INVOKE_METHOD, params);
        String sb = "{return $1.proceed();}"; // 调用封装的方法
        doExecuteMethod.setBody(sb); // 植入代码片段
        ctClass.toClass();

        return true;
    }

}
