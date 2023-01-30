package cn.rivamed.log.core.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Rivamed Log的 rabbitmq springboot配置参数
 *
 * @author Zuo Yang
 * @since 1.1.0
 */
@ConfigurationProperties(prefix = "rivamed.log.rabbitmq")
@RefreshScope
public class RivamedLogProperty {

    @Value("${rivamed.log.enabled}")
    private boolean logEnable;

    @Value("${rivamed.log.collect.sqlEnable}")
    private boolean sqlEnable;

    @Value("${rivamed.log.collect.rabbitmqEnable}")
    private boolean rabbitmqEnable;

    @Value("${rivamed.log.collect.taskEnable}")
    private boolean taskEnable;

    @Value("${rivamed.log.collect.requestEnable}")
    private boolean requestEnable;

    @Value("${rivamed.log.collect.responseEnable}")
    private boolean responseEnable;

    private String host;
    private int port;
    private String virtualHost;
    private String username;
    private String password;

    public boolean isLogEnable() {
        return logEnable;
    }

    public void setLogEnable(boolean logEnable) {
        this.logEnable = logEnable;
    }

    public boolean isSqlEnable() {
        return sqlEnable;
    }

    public void setSqlEnable(boolean sqlEnable) {
        this.sqlEnable = sqlEnable;
    }

    public boolean isRabbitmqEnable() {
        return rabbitmqEnable;
    }

    public void setRabbitmqEnable(boolean rabbitmqEnable) {
        this.rabbitmqEnable = rabbitmqEnable;
    }

    public boolean isTaskEnable() {
        return taskEnable;
    }

    public void setTaskEnable(boolean taskEnable) {
        this.taskEnable = taskEnable;
    }

    public boolean isRequestEnable() {
        return requestEnable;
    }

    public void setRequestEnable(boolean requestEnable) {
        this.requestEnable = requestEnable;
    }

    public boolean isResponseEnable() {
        return responseEnable;
    }

    public void setResponseEnable(boolean responseEnable) {
        this.responseEnable = responseEnable;
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

}
