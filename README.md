# Rivamed Log使用方法

* 使用前请耐心的按照步骤把文档看完，需要对logback,log4j两大日志框架基本配置有一定了解

## 使用前注意事项

* Rivamed Log使用的是RabbitMQ + Disruptor + Sleuth。
* 原理是增加一个新的 RabbitMQAppender,在配置完成后，启动项目时将日志推送到RabbitMQAppender,
RabbitMQAppender内部实例化了一个 BatchingRabbitTemplate客户端和一个Disruptor队列，日志数据先存到Disruptor,
然后通过BatchingRabbitTemplate将数据批量推送给RabbitMQ服务器端, Sleuth用来在链路中生成TraceId和SpanId。

## 一、客户端使用

### （1）注意事项

* 1.只支持spring-boot项目，基于spring-boot(2.3.12.RELEASE)版本开发，请尽量使用高版本的spring-boot,低版本里面的RabbitMQ可能会不兼容。
比如SimpleBatchingStrategy的路径就不一样，会导致启动报错~

* 2.示例中是基本配置，更多使用方法请看文章下面详解
  

### （2）客户端配置

#### 1. 基础配置

* 客户项目先加上rabbitmq日志基础配置 


* application.yml中添加配置

```yml

rivamed:
  log:
    rabbitmq:
      host: 192.168.111.222
      port: 5672
      virtualHost: jishou
      username: rivamed
      password: rivamed
      exchange: log.rm-th
      routingKey: log.rm-th
      queueName: log.rm-th

```  

|  字段值   | 用途  |
|  ----  | ----  |
| sysName  | 自定义应用名称 |
| env  | 环境 默认是dev |
| host  | RabbitMQ 主机 |
| port  | RabbitMQ 端口 |
| virtualHost  | RabbitMQ 虚拟主机名 |
| username  | RabbitMQ 账号 |
| password  | RabbitMQ 密码 |
| exchange  | RabbitMQ 交换机名称 |
| routingKey  | RabbitMQ 路由键 |
| queueName  | RabbitMQ 队列名称 |

#### 2.根据项目需求使用情况导入对应的日志包并加上配置

#### 注意：如果使用了配置中心，则需要把日志路径配置移到配置中心下统一配置，不在每个项目下单独配置。配置示例:

```yml
# 日志配置
logging:
  config: classpath:log4j2-spring.xml
```



#### 2.1 log4j

* 引入

```xml

<dependency>
    <groupId>cn.rivamed</groupId>
    <artifactId>rivamed-log-log4j-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```                       

* 配置log4j配置文件，增加下面这个Appender,示例如下：

```properties
#三选一加入到root
log4j.rootLogger=INFO,stdout,rabbitmq
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=[%-5p] %d{yyyy-MM-dd HH:mm:ss,SSS} [%c.%t]%n%m%n

#rabbitmq做为中间件
log4j.appender.rabbitmq=cn.rivamed.log.log4j.appender.RabbitMQAppender

```

同理如果使用logback,和log4j2配置如下,示例如下：

#### 2.2 logback

* 引入

```xml

<dependency>
    <groupId>cn.rivamed</groupId>
    <artifactId>rivamed-log-logback-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>

```  

* logback-spring.xml配置

```xml


<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration>
<configuration>
	<!--引用默认日志配置-->
	<include resource="org/springframework/boot/logging/logback/defaults.xml"/>
	<!--使用默认的控制台日志输出实现-->
	<include resource="org/springframework/boot/logging/logback/console-appender.xml"/>
	
	<!--日志文件保存路径-->
	<property name="LOG_FILE_PATH" value="${LOG_FILE:-${LOG_PATH:-${LOG_TEMP:-${java.io.tmpdir:-/tmp}}}/logs}"/>

	<appender name="RabbitMQAppender" class="cn.rivamed.log.logback.appender.RabbitMQAppender">
	</appender>

	<root level="INFO">
		<appender-ref ref="RabbitMQAppender"/>
	</root>
</configuration>

```   

* 小提示：为什么Spring Boot推荐使用logback-spring.xml来替代logback.xml来配置logback日志的问题分析

  即，logback.xml加载早于application.properties，所以如果你在logback.xml使用了变量时，而恰好这个变量是写在application.properties时，那么就会获取不到，只要改成logback-spring.xml就可以解决。

  这就是为什么有些人用了nacos等配置中心，不能加载远程配置的原因，是因为加载优先级的问题



#### 2.3 log4j2

* 引入

```xml

<dependency>
    <groupId>cn.rivamed</groupId>
    <artifactId>rivamed-log-log4j2-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
   
```   

* 配置,示例如下：

```xml

<?xml version="1.0" encoding="UTF-8"?>
<!--      Configuration后面的status，这个用于设置log4j2自身内部的信息输出，可以不设置，
            当设置成trace时，可以看到log4j2内部各种详细输出
-->
<!-- monitorInterval：Log4j能够自动检测修改配置 文件和重新配置本身，设置间隔秒数 -->
<configuration monitorInterval="30">

    <!-- 日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->

    <!-- 变量配置 -->
    <Properties>
        <!--
            格式化输出：
            %d表示日期，
            %thread表示线程名，
            %X{X-B3-TraceId表示traceId
            %X{X-B3-SpanId表示spanId
            %-5level：级别从左显示5个字符宽度
            %msg：日志消息，%n是换行符
            %logger{36} 表示 Logger 名字最长36个字符
        -->
        <property name="LOG_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level [%logger{50}:%L] [%X{X-B3-TraceId},%X{X-B3-SpanId}] - %msg%n" />

        <!-- 定义日志存储的路径 -->
        <property name="LOG_PATH" value="./var/logs" />
        <property name="LOG_NAME" value="rm-th" />
        <property name="LOG_MAX_SIZE" value="1MB" />
        <property name="LOG_DAYS" value="50" />
        <property name="TIME_BASED_INTERVAL" value="1" />
    </Properties>

    <appenders>
        <RabbitMQAppender name="RabbitMQAppender"/>
    </appenders>

    <!--Logger节点用来单独指定日志的形式，比如要为指定包下的class指定不同的日志级别等。-->
    <!--然后定义loggers，只有定义了logger并引入的appender，appender才会生效-->
    <loggers>
        <root level="info">
            <appender-ref ref="RabbitMQAppender"/>
        </root>
    </loggers>
</configuration>

``` 

### （3）自定义日志记录

#### 3.1 推送AOP接口日志（logType: logRecord）

* 系统创建入了一个默认的抽象AOP类（AbstractLogRecordAspect.java），只需要在业务系统中创建一个AOP继承该基础类，然后设置对应的额外信息。

AbstractLogRecordAspect.java

```java

package cn.rivamed.log.web.aspect;

import brave.propagation.TraceContext;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.entity.LogRecordMessage;
import cn.rivamed.log.core.entity.TraceId;
import cn.rivamed.log.core.factory.MessageAppenderFactory;
import cn.rivamed.log.core.rpc.RivamedLogRecordHandler;
import cn.rivamed.log.core.util.JsonUtil;
import cn.rivamed.log.core.util.IpGetter;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * className：AbstractLogRecordAspect
 * description： 链路追踪打点拦截
 * time：2022-10-14.11:17
 *
 * @author Zuo Yang
 * @version 1.2.0
 */
public abstract class AbstractLogRecordAspect extends RivamedLogRecordHandler {

    private static Logger logger = LoggerFactory.getLogger(RivamedLogRecordHandler.class);

    /**
     * 序列生成器：当日志在一毫秒内打印多次时，发送到服务端排序时无法按照正常顺序显示，因此加一个序列保证同一毫秒内的日志按顺序显示
     * 使用AtomicLong不要使用LongAdder，LongAdder在该场景高并发下无法严格保证顺序性，也不需要考虑Long是否够用，假设每秒打印10万日志，也需要两百多万年才能用的完
     */
    private static final AtomicLong SEQ_BUILDER = new AtomicLong(1);

    public Object aroundExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object returnValue;
            final List<Object> params = new ArrayList<>();
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (sra == null) {
                return null;
            }
            LogRecordMessage message = new LogRecordMessage();
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
            Method m = ms.getMethod();
            String cloneParams;
            try {
                cloneParams = JsonUtil.toJSONString(params);
            } catch (Exception e) {
                cloneParams = params.toString();
            }
            message.setMethod(joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + m.getName());
            message.setUrl(request.getRequestURI());

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            returnValue = joinPoint.proceed(joinPoint.getArgs());
            stopWatch.stop();
            message.setSysName(RivamedLogRecordContext.getSysName());
            message.setEnv(RivamedLogRecordContext.getEnv());
            message.setClassName(ms.getMethod().getDeclaringClass().getName());
            message.setThreadName(Thread.currentThread().getName());
            message.setSeq(SEQ_BUILDER.getAndIncrement());
            message.setCostTime(stopWatch.getTime());
            message.setBizDetail(cloneParams);
            message.setLevel(LogLevel.INFO.name());
            message.setResponseCode(HttpStatus.OK.name());
            message.setBizIP(IpGetter.CURRENT_IP);
            message.setLogType(LogMessageConstant.LOG_TYPE_RECORD);
            message.setLogRecordType(LogMessageConstant.LOG_RECORD_TYPE_USER_LOG);
            message.setResponseCode(String.valueOf(HttpStatus.OK.value()));
            //设置额外信息并推送消息
            RivamedLogRecordContext.buildLogMessage(message);
            MessageAppenderFactory.push(message);
            return returnValue;
        } finally {
            cleanThreadLocal();
        }
    }
}

```

* RestAop.java 示例

```java

@Aspect
@Component
public class RestAop extends AbstractLogRecordAspect {
    
     @Pointcut("execution(public * cn.rivamed.*.*.web.rest..*.*(..))")
        public void pointCutRest() {
            throw new UnsupportedOperationException();
        }
        
    @Around("pointCutRest()")
        public Object processRst(ProceedingJoinPoint point) {
        
            RivamedLogRecordLabel rivamedLogLabel = new RivamedLogRecordLabel();
            rivamedLogLabel.setUserId("userId")
                    .setUserName("userName")
                    .setDeviceId("deviceId")
                    .setSn("sn")
                    .setBizId("bizId")
                    .setBizProd("bizProd")
                    .setBizAction("bizAction")
                    .setLogRecord("logRecord")
                    .setTenantId("tenantId")
                    .setTokenId("tokenId");
          
            processProviderSide(rivamedLogLabel);

            return aroundExecute(point);
    }
}

```

#### 3.2 推送登录日志（logType: loginLog）

* 如果是推送登录日志，那么只需要在业务系统的登录接口下先封装对应的实体参数，然后打印一下带前缀的日志

* 示例

```java


public class LoginRest {
    
    public void login() {
        LoginLogMessage loginLogMessage = new LoginLogMessage()
                            .setSystemLogId("111111111")
                            .setSystemType("rm-th")
                            .setSystemName("rm-th")
                            .setTenantId("11111")
                            .setTokenId("TOKEN")
                            .setAccountId("张三111")
                            .setAccountName("张三444")
                            .setUserName("张三222")
                            .setLoginType("pda");
            log.info(LogMessageConstant.LOG_TYPE_LOGIN_LOG + JsonUtil.toJSONString(loginLogMessage));
    }
}


```

#### 3.3 推送RabbitMQ监听日志（logType: rabbitMQ）

* 不需要单独配置，框架默认已实现，下面是对应的实现方案，其他类似的监听也可以通过这种方式处理~

* 因为RabbitMQ所有的发送消息最后都会调用org.springframework.amqp.rabbit.core.RabbitTemplate.send()方法，
  所有的接收消息最后都会调用org.springframework.amqp.support.converter.MessagingMessageConverter.fromMessage()方法。
  所以我们只需要通过javassist去增强这两个方法就好了~
  使用javassist对类进行增强的时候需要优先执行增强的代码，不然在执行ctClass.toClass()方法的时候会报下面这种错：attempted  duplicate class definition，是因为同一个类加载器不能重复加载相同名字的类，所以我们需要在Spring初始化之前就进行增强~

##### 3.3.1  首先引入最新的jar包

```xml
<dependency>
  <groupId>org.javassist</groupId>
  <artifactId>javassist</artifactId>
  <version>3.29.2-GA</version>
</dependency>
```


##### 3.3.2  创建增强类

```java
package cn.rivamed.log.rabbitmq.instrument;

import com.rabbitmq.client.Channel;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;

/**
 * 描述: RabbitMQ增强类
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/10/24 10:34
 */
public class RabbitMQInstrumentation {

    public static final String ENHANCE_RABBITMQ_SEND_INTERCEPTOR_PATH = "cn.rivamed.log.rabbitmq.interceptor.RabbitMQInterceptor.sendInterceptor"; // 发送拦截器

    public static final String ENHANCE_RABBITMQ_RECEIVE_INTERCEPTOR_PATH = "cn.rivamed.log.rabbitmq.interceptor.RabbitMQInterceptor.receiveInterceptor"; // 接收拦截器

    public static final String ENHANCE_RABBIT_TEMPLATE_CLASS = "org.springframework.amqp.rabbit.core.RabbitTemplate"; // 增强的类
    public static final String ENHANCE_RABBIT_RECEIVE_CLASS = "org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter"; // 增强的类
    public static final String ENHANCE_SEND_METHOD = "send"; // 增强的方法
    public static final String ENHANCE_RECEIVE_METHOD = "invokeHandler"; // 增强的方法

    public static boolean sendEnhance() throws NotFoundException, CannotCompileException {

        ClassPool classPool = ClassPool.getDefault();

        //拦截send方法
        CtClass ctClass = classPool.getCtClass(ENHANCE_RABBIT_TEMPLATE_CLASS);
        if (ctClass == null) {
            System.out.println("RabbitMQ client not found");
            return false;
        }
        CtClass strClass = classPool.get(String.class.getName());
        CtClass messageClass = classPool.get(Message.class.getName());
        CtClass correlationDataClass = classPool.get(CorrelationData.class.getName());
        CtClass[] params = new CtClass[]{strClass, strClass, messageClass, correlationDataClass};
        CtMethod doExecuteMethod = ctClass.getDeclaredMethod(ENHANCE_SEND_METHOD, params);
        String sb = "{" + ENHANCE_RABBITMQ_SEND_INTERCEPTOR_PATH + "($0, $args);" + "}"; // 调用封装的方法
        doExecuteMethod.insertBefore(sb); // 植入代码片段
        ctClass.toClass();

        return true;
    }

    /**
     * 只有在 fromMessage 执行完成后才能拿到 targetMethod, 然后需要从 channel 里面拿到 Connection 的 virtualHost 信息，
     * 所有拦截 invokeHandlerAndProcessResult 这个方法
     *
     *
     * @return
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    public static boolean receiveEnhance() throws NotFoundException, CannotCompileException {

        ClassPool classPool = ClassPool.getDefault();

        //拦截receive方法
        CtClass ctClass = classPool.getCtClass(ENHANCE_RABBIT_RECEIVE_CLASS);
        if (ctClass == null) {
            System.out.println("RabbitMQ Listener not found");
            return false;
        }
        CtClass messageClass = classPool.get(Message.class.getName());
        CtClass channelClass = classPool.get(Channel.class.getName());
        CtClass messagingClass = classPool.get(org.springframework.messaging.Message.class.getName());
        CtClass[] params = new CtClass[]{messageClass,channelClass, messagingClass};

        CtMethod doExecuteMethod = ctClass.getDeclaredMethod(ENHANCE_RECEIVE_METHOD, params);
        String sb = "{" + ENHANCE_RABBITMQ_RECEIVE_INTERCEPTOR_PATH + "($1, $2);" + "}"; // 调用封装的方法
        doExecuteMethod.insertBefore(sb); // 植入代码片段
        ctClass.toClass();

        return true;
    }
}



```

##### 3.3.3  创建拦截解析类

```java

package cn.rivamed.log.rabbitmq.interceptor;

import cn.rivamed.log.core.entity.RabbitLogMessage;
import cn.rivamed.log.core.factory.MessageAppenderFactory;
import cn.rivamed.log.core.util.JsonUtil;
import cn.rivamed.log.rabbitmq.util.RabbitLogMessageUtils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 描述: RabbitMQ方法拦截解析类
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/10/24 10:34
 */
public class RabbitMQInterceptor {

    /**
     * 处理RabbitMQ发送消息事件
     *
     * @param rabbitTemplate
     * @param args
     */
    public static void sendInterceptor(RabbitTemplate rabbitTemplate, Object[] args) {
        if (rabbitTemplate.getClass().isAssignableFrom(RabbitTemplate.class)) {
            RabbitLogMessage rabbitLogMessage = RabbitLogMessageUtils.collectFromSend(rabbitTemplate, args);
            MessageAppenderFactory.pushRabbitLogMessage(rabbitLogMessage);
        }
    }

    /**
     * 处理RabbitMQ接收消息事件
     *
     * @param message
     * @param channel
     */
    public static void receiveInterceptor(Message message, Channel channel) {
        RabbitLogMessage rabbitLogMessage = RabbitLogMessageUtils.collectFromReceive(message, channel);
        System.out.println("收到消息" + JsonUtil.toJSONString(rabbitLogMessage));
        MessageAppenderFactory.pushRabbitLogMessage(rabbitLogMessage);
    }
}


```
##### 3.3.4  在Spring初始化之前增强

```java
package cn.rivamed.log.rabbitmq.initializer;

import cn.rivamed.log.rabbitmq.instrument.RabbitMQInstrumentation;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 描述: RabbitMQ初始执行
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/10/26 10:14
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RabbitMQEnhanceContextInitializer implements ApplicationContextInitializer {

    private static boolean sendEnhanceFlag = false;
    private static boolean receiveEnhanceFlag = false;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (!sendEnhanceFlag) {
            try {
                sendEnhanceFlag = RabbitMQInstrumentation.sendEnhance();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }

        if (!receiveEnhanceFlag) {
            try {
                receiveEnhanceFlag = RabbitMQInstrumentation.receiveEnhance();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }

    }
}

```
##### 3.3.5 创建spring.factories

```properties
# Initializers
org.springframework.context.ApplicationContextInitializer=\
cn.rivamed.log.rabbitmq.initializer.RabbitMQEnhanceContextInitializer

```



