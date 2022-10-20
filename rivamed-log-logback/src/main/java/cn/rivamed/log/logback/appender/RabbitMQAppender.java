package cn.rivamed.log.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogRecordContext;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.factory.MessageAppenderFactory;
import cn.rivamed.log.core.rabbitmq.RabbitMQClient;
import cn.rivamed.log.logback.util.LogMessageUtil;

/**
 * className：RabbitMQAppender
 * description：
 * time：2022-10-09.15:26
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class RabbitMQAppender extends AppenderBase<ILoggingEvent> {

    private static RabbitMQClient rabbitMQClient;
    private String sysName;
    private String env;
    private String host;
    private int port;
    private String virtualHost;
    private String username;
    private String password;
    private String exchange;
    private String routingKey;

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    @Override
    protected void append(ILoggingEvent event) {
        final BaseLogMessage logMessage = LogMessageUtil.getLogMessage(sysName, env, event);
        if (rabbitMQClient != null) {
            MessageAppenderFactory.push(logMessage);
        }
    }

    @Override
    public void start() {
        if (env == null) {
            env = "dev";
        }
        if (host == null) {
            host = "127.0.0.1";
        }
        if (port == 0) {
            port = 5672;
        }

        if (virtualHost == null) {
            virtualHost = "/";
        }

        if (exchange == null) {
            exchange = "rivamed-log";
        }

        if (routingKey == null) {
            routingKey = "rivamed-log";
        }
        //项目刚启动的时候拿不到配置信息，不去初始化客户端
        if (!host.contains(LogMessageConstant.IS_UNDEFINED) && !virtualHost.contains(LogMessageConstant.IS_UNDEFINED)) {
            RivamedLogRecordContext.putSysName(sysName);
            RivamedLogRecordContext.putEnv(env);
            rabbitMQClient = RabbitMQClient.getInstance(host, port, virtualHost, username, password, exchange, routingKey);
            AbstractClient.setClient(rabbitMQClient);
            super.start();
        }

    }
}
