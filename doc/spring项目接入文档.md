# spring项目接入

* 使用前请耐心的按照步骤把文档看完，需要对logback,log4j两大日志框架基本配置有一定了解


## 一、支持spring版本

* 1.只支持spring-boot项目，基于spring-boot(2.3.12.RELEASE)版本开发，请尽量使用高版本的spring-boot,低版本里面的RabbitMQ可能会不兼容。

  

## 二、业务系统配置

#### 1. 引入日志sdk及日志配置文件
	根据项目需求使用情况导入对应的日志包并修改日志配置文件
##### log4j项目
* 引入

```xml

<dependency>
    <groupId>cn.rivamed</groupId>
    <artifactId>rivamed-log-log4j-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
  <!-- 需要语义化日志时添加 -->
<dependency>
     <groupId>io.github.mouzt</groupId>
     <artifactId>bizlog-sdk</artifactId>
     <version>3.0.4</version>
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
##### logback项目

* 引入

```xml

<dependency>
    <groupId>cn.rivamed</groupId>
    <artifactId>rivamed-log-logback-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
  <!-- 需要语义化日志时添加 -->
<dependency>
     <groupId>io.github.mouzt</groupId>
     <artifactId>bizlog-sdk</artifactId>
     <version>3.0.4</version>
</dependency>

```

* logback-spring.xml配置

```xml


<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration>
<configuration>
	<include resource="org/springframework/boot/logging/logback/defaults.xml"/>
	<include resource="org/springframework/boot/logging/logback/console-appender.xml"/>
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

##### log4j2项目

* 引入

```xml

<dependency>
    <groupId>cn.rivamed</groupId>
    <artifactId>rivamed-log-log4j2-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
  <!-- 需要语义化日志时添加 -->
<dependency>
     <groupId>io.github.mouzt</groupId>
     <artifactId>bizlog-sdk</artifactId>
     <version>3.0.4</version>
</dependency>
```

* 配置,示例如下：

```xml

<?xml version="1.0" encoding="UTF-8"?>
<configuration monitorInterval="30">
    <Properties>
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
    <loggers>
        <root level="info">
            <appender-ref ref="RabbitMQAppender"/>
        </root>
    </loggers>
</configuration>

```
注意：如果使用了配置中心，则需要把日志路径配置移到配置中心下统一配置，不在每个项目下单独配置。配置示例:

```yml
# 日志配置
logging:
  config: classpath:log4j2-spring.xml
```

#### 2. 日志队列配置

* 配置文件中添加rabbitmq日志队列配置

```yml

rivamed:
  log:
    rabbitmq:
      host: 192.168.111.222
      port: 5672
      virtualHost: jishou
      username: rivamed
      password: rivamed

```

|  字段值   | 用途  |
|  ----  | ----  |
| host  | RabbitMQ 主机 |
| port  | RabbitMQ 端口 |
| virtualHost  | RabbitMQ 虚拟主机名 |
| username  | RabbitMQ 账号 |
| password  | RabbitMQ 密码 |







#### 3.日志基础数据添加

##### 3.1 接口日志

* 如果不需要语义化日志解析，那么只需要在业务系统中创建一个AOP继承AbstractLogRecordAspect.java，然后设置用户登录信息，系统信息。


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
                    .setTenantId("tenantId")
                    .setTokenId("tokenId");
          
            processProviderSide(rivamedLogLabel);

            return aroundExecute(point);
    }
}

```

* 如果需要语义化日志，那么需要在业务系统中创建一个AOP继承继承 AbstractMztBizLogRecordAspect.java,然后设置用户登录信息，系统信息。

```java

@Aspect
@Component
public class RestAop extends AbstractMztBizLogRecordAspect {
    
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
                    .setTenantId("tenantId")
                    .setTokenId("tokenId");
          
            processProviderSide(rivamedLogLabel);

            return aroundExecute(point);
    }
}

```

##### 3.2 语义化日志

* rest层添加logRecord注解，日志框架会解析注解形成语义化日志
启动类上需要添加注解，语义日志才能生效
```java

@EnableLogRecord(tenant = "")


```

* 业务代码示例

```java


    @GetMapping("findBusinessType")
    @LogRecord(
            fail = "{{#operator}} 查询出入库业务类型失败，失败原因：「{{#_errorMsg}}」",
            success = "{{#operator}} 查询出入库业务类型",
            type = LogMessageConstant.LOG_TYPE_RECORD, bizNo = "")
    public CommonResult<List<DictVO>> findBusinessType(@ApiParam("出入库类型：1-入库类型；-1-出库类型")
                                                           @RequestParam(value = "inOutType", required = false) String inOutType) {
        return CommonResult.success(businessOrderBlh.findBusinessType(inOutType));
    }


```
* logRecord使用详细说明参考：https://github.com/mouzt/mzt-biz-log.git
* type对应日志集合中的bizType,代表用户定义的业务类型，如耗材盘点、耗材领用等
* subType：对type的细分，如耗材 盘点中的盘点列表查询、命令下发，对应日志集合中的bizSubType
* bizNo：业务号：如订单号等

##### 3.3 登录日志

* 如果是推送登录日志，那么只需要在业务系统的登录接口下先封装对应的实体参数，然后打印一下带前缀的日志

* 登录成功示例

```java

public class LoginRest {
    
    public void login() {
            LoginLogMessage loginLogMessage = new LoginLogMessage()
                    .setSubSystemName("stemType") //必填
                    .setTenantId("tenantId")
                    .setTokenId("tokenId") //必填
                    .setAccountId("accountId") //必填
                    .setAccountName("accountName")
                    .setUserName("userName")
                    .setLoginType("loginType")
                    .setJobNo("jobNo")
                    .setLoginStatus(LogMessageConstant.SUCCESS)
                    .setLoginTime(new Date()) //必填
                    .setLoginDevice("device")
                    .setLoginDeviceSn("sn")
                    .setRemark("remark");
            LogMessageFactory.pushLoginLogMessage(loginLogMessage);
    }
}

```
* 登录失败示例

```java·

public class LoginRest {
    
    public void login() {
            LoginLogMessage loginLogMessage = new LoginLogMessage()
                    .setSubSystemName("stemType") //必填
                    .setTenantId("tenantId")
                    .setAccountId("accountId") //必填
                    .setAccountName("accountName")
                    .setLoginType("loginType")
                    .setLoginStatus(LogMessageConstant.FAIL)
                    .setLoginTime(new Date()) //必填
                    .setLoginDevice("device")
                    .setLoginDeviceSn("sn")
                    .setRemark("remark");
            LogMessageFactory.pushLoginLogMessage(loginLogMessage);
    }
}


```

* 退出登录示例

```java·

public class LoginRest {
    
    public void login() {
            LoginLogMessage loginLogMessage = new LoginLogMessage()
                    .setSubSystemName("stemType") //必填
                    .setTokenId("tokenId") //必填
                    .setLogoutTime(new Date()); //必填
            LogMessageFactory.pushLoginLogMessage(loginLogMessage);
    }
}


```

##### 3.4 RabbitMQ日志


* 系统默认支持rabbitmq消息监听日志，不需要用户配置


##### 3.5 定时任务日志


* 1、系统默认支持解析spring的@Scheduled注解方式的定时任务，如果使用这种定时任务，不需要单独配置

* 2、如果使用的quartz,那么需要将自定义的Job类继承RivamedLogQuartzJobBean，就可以实现定时任务日志~

* 示例

```java

@Component
public class BspDynamicJob extends RivamedLogQuartzJobBean {
    
    @Override
        public void executeTask(JobExecutionContext executorContext) {
            // TODO
        }
}

```

##### 3.6 日志脱敏

* 1、如果有些关键数据不需要完整展示在日志中，那么只需要在对应的字段上面加上 @Desensitize 注解，根据对应的业务类型设置相应的策略,即可实现脱敏。
* 2、暂时支持 用户名、身份证、座机号码、电话号码、邮箱、地址、车牌号、银行卡号、密码等类型的脱敏

* 示例

```java

@Data
@EqualsAndHashCode(callSuper = false)
public class BspLoginDto implements Serializable {
    
     private static final long serialVersionUID = 2291962030426081727L;
    
     @Desensitize(strategy = DesensitizeStrategy.PASSWORD)
     private String password;
     
}
 
 ``` 

* 脱敏后密码字段值变成*
```text

 /bsp/rmApi/login/bsplogin param: [{"isUpdatePart":"1","sourceFlag":"0","systemType":"jishou-oms","loginName":"cs01","password":"********","loginMode":"1","onlyToken":"0","continueToLogin":"1"}]
 
 ```
  
