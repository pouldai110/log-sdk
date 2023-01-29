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
    private boolean logEnabled;

    @Value("${rivamed.log.collect.sqlEnabled}")
    private boolean sqlEnabled;

    @Value("${rivamed.log.collect.rabbitmqEnabled}")
    private boolean rabbitmqEnabled;

    @Value("${rivamed.log.collect.taskEnabled}")
    private boolean taskEnabled;

    @Value("${rivamed.log.collect.requestEnabled}")
    private boolean requestEnabled;

    @Value("${rivamed.log.collect.responseEnabled}")
    private boolean responseEnabled;

    private String host;
    private int port;
    private String virtualHost;
    private String username;
    private String password;

    public boolean isLogEnabled() {
        return logEnabled;
    }

    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }

    public boolean isSqlEnabled() {
        return sqlEnabled;
    }

    public void setSqlEnabled(boolean sqlEnabled) {
        this.sqlEnabled = sqlEnabled;
    }

    public boolean isRabbitmqEnabled() {
        return rabbitmqEnabled;
    }

    public void setRabbitmqEnabled(boolean rabbitmqEnabled) {
        this.rabbitmqEnabled = rabbitmqEnabled;
    }

    public boolean isTaskEnabled() {
        return taskEnabled;
    }

    public void setTaskEnabled(boolean taskEnabled) {
        this.taskEnabled = taskEnabled;
    }

    public boolean isRequestEnabled() {
        return requestEnabled;
    }

    public void setRequestEnabled(boolean requestEnabled) {
        this.requestEnabled = requestEnabled;
    }

    public boolean isResponseEnabled() {
        return responseEnabled;
    }

    public void setResponseEnabled(boolean responseEnabled) {
        this.responseEnabled = responseEnabled;
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
