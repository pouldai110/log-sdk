# Rivamed Log使用方法

* 使用前请耐心的按照步骤把文档看完，需要对logback,log4j两大日志框架基本配置有一定了解

## 使用前注意事项

* Rivamed Log使用的是RabbitMQ + Disruptor。
* 原理是增加一个新的 RabbitMQAppender,在配置完成后，启动项目时将日志推送到RabbitMQAppender,
RabbitMQAppender内部实例化了一个 BatchingRabbitTemplate客户端和一个Disruptor队列，日志数据先存到Disruptor,
然后通过BatchingRabbitTemplate将数据批量推送给RabbitMQ服务器端。

## 一、客户端使用

### （1）注意事项

* 1.客户端在项目使用，非maven项目下载依赖包（ https://gitee.com/James-ZY/rivamed-log-sdk/releases ）放在自己的lib下面直接使用，去除重复的包即可使用，然后配置log4j就可以搜集日志了

* 2.推荐使用logback,特别是SpringBoot，SpringCloud项目;

* 3.示例中仅仅是基本配置，更多配置请看文章下面配置详解
  

### （2）客户端配置

#### 1. 基础配置

* 客户项目先加上rabbitmq日志基础配置 


* application.yml中添加

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

#### 2.根据项目需求使用情况导入对应的日志包并加上配置

#### 2.1 log4j

* 引入

```xml

<dependency>
    <groupId>cn.rivamed</groupId>
    <artifactId>rivamed-log-log4j</artifactId>
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

#rabbitmq做为中间件 (自动从配置文件获取配置暂未实现，只有先写死)
#log4j.appender.rabbitmq=cn.rivamed.log.log4j.appender.RabbitMQAppender
#log4j.appender.rabbitmq.sysName=${spring.application.name}
#log4j.appender.rabbitmq.env=${spring.profiles.active}
#log4j.appender.rabbitmq.host=${rivamed.log.rabbitmq.host}
#log4j.appender.rabbitmq.port=${rivamed.log.rabbitmq.port}
#log4j.appender.rabbitmq.virtualHost=${rivamed.log.rabbitmq.virtualHost}
#log4j.appender.rabbitmq.username=${rivamed.log.rabbitmq.username}
#log4j.appender.rabbitmq.password=${rivamed.log.rabbitmq.password}
#log4j.appender.rabbitmq.exchange=${rivamed.log.rabbitmq.exchange}
#log4j.appender.rabbitmq.routingKey=${rivamed.log.rabbitmq.routingKey}

log4j.appender.rabbitmq=cn.rivamed.log.log4j.appender.RabbitMQAppender
log4j.appender.rabbitmq.sysName=rm-th
log4j.appender.rabbitmq.env=prod
log4j.appender.rabbitmq.host=192.168.111.222
log4j.appender.rabbitmq.port=5672
log4j.appender.rabbitmq.virtualHost=jishou
log4j.appender.rabbitmq.username=rivamed
log4j.appender.rabbitmq.password=rivamed
log4j.appender.rabbitmq.exchange=log.rm-th
log4j.appender.rabbitmq.routingKey=log.rm-th


```

同理如果使用logback,和log4j2配置如下,示例如下：

#### 2.2 logback

* 引入

```xml

<dependency>
    <groupId>cn.rivamed</groupId>
    <artifactId>rivamed-log-logback</artifactId>
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
	<!--应用名称-->
	<springProperty scope="context" name="APP_NAME" source="spring.application.name" defaultValue="th-sth"/>
	<springProperty scope="context" name="env" source="spring.profiles.active"/>
	<springProperty scope="context" name="host" source="rivamed.log.rabbitmq.host"/>
	<springProperty scope="context" name="port" source="rivamed.log.rabbitmq.port"/>
	<springProperty scope="context" name="virtualHost" source="rivamed.log.rabbitmq.virtualHost"/>
	<springProperty scope="context" name="username" source="rivamed.log.rabbitmq.username"/>
	<springProperty scope="context" name="password" source="rivamed.log.rabbitmq.password"/>
	<springProperty scope="context" name="exchange" source="rivamed.log.rabbitmq.exchange"/>
	<springProperty scope="context" name="routingKey" source="rivamed.log.rabbitmq.routingKey"/>

	<!--日志文件保存路径-->
	<property name="LOG_FILE_PATH" value="${LOG_FILE:-${LOG_PATH:-${LOG_TEMP:-${java.io.tmpdir:-/tmp}}}/logs}"/>

	<appender name="RabbitMQAppender" class="cn.rivamed.log.logback.appender.RabbitMQAppender">
		<sysName>${APP_NAME}</sysName><!-- 系统名称 -->
		<env>${env}</env><!-- 配置文件环境 -->
		<host>${host}</host><!-- RabbitMQ 主机 -->
		<port>${port}</port><!-- RabbitMQ 端口 -->
		<virtualHost>${virtualHost}</virtualHost><!-- RabbitMQ 虚拟主机名 -->
		<username>${username}</username><!-- RabbitMQ账户 -->
		<password>${password}</password><!-- RabbitMQ密码 -->
		<exchange>${exchange}</exchange><!-- 交换机名称 -->
		<routingKey>${routingKey}</routingKey><!-- 路由键 -->
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
    <artifactId>rivamed-log-log4j2</artifactId>
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
        <property name="env" value="${spring:spring.profiles.active}" />
        <property name="host" value="${spring:rivamed.log.rabbitmq.host}" />
        <property name="port" value="${spring:rivamed.log.rabbitmq.port}" />
        <property name="virtualHost" value="${spring:rivamed.log.rabbitmq.virtualHost}" />
        <property name="username" value="${spring:rivamed.log.rabbitmq.username}" />
        <property name="password" value="${spring:rivamed.log.rabbitmq.password}" />
        <property name="exchange" value="${spring:rivamed.log.rabbitmq.exchange}" />
        <property name="routingKey" value="${spring:rivamed.log.rabbitmq.routingKey}" />
    </Properties>

    <appenders>
        <!--使用RabbitMQ启用下面配置-->
        <!-- 字段说明 -->
        <!-- sysName:系统名称 -->
        <!-- env：配置文件环境 -->
        <!-- host：RabbitMQ 主机 不配置，默认使用127.0.0.1-->
        <!-- port：RabbitMQ 端口 不配置，默认使用5672-->
        <!-- virtualHost：RabbitMQ 虚拟主机名 不配置，默认使用/-->
        <!-- username：RabbitMQ 账号-->
        <!-- password：RabbitMQ 密码-->
        <!-- exchange：RabbitMQ 交换机名称 不配置，默认使用rivamed-log-->
        <!-- routingKey：RabbitMQ 路由键 不配置，默认使用rivamed-log-->
        <RabbitMQAppender name="RabbitMQAppender" sysName="${LOG_NAME}" env="${env}" host="${host}"
                          port="${port}" virtualHost="${virtualHost}" username="${username}" password="${password}"
                          exchange="${exchange}"  routingKey="${routingKey}"/>
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

### （3）客户端配置详解

RabbitMQAppender

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

### （4）自定义日志记录

#### 4.1 推送登录日志（logType: loginLog）



