/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package cn.rivamed.log.log4j.appender;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.factory.MessageAppenderFactory;
import cn.rivamed.log.core.rabbitmq.RabbitMQClient;
import cn.rivamed.log.log4j.util.LogMessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * An appender that writes to random access files and can roll over at
 * intervals.
 */
public class RabbitMQAppender extends AppenderSkeleton {

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
    protected void append(LoggingEvent loggingEvent) {
        if (rabbitMQClient == null) {
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
            if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(virtualHost) &&StringUtils.isNotBlank(exchange) &&StringUtils.isNotBlank(routingKey)) {
                RivamedLogContext.putSysName(sysName);
                RivamedLogContext.putEnv(env);
                rabbitMQClient = RabbitMQClient.getInstance(host, port, virtualHost, username, password, exchange, routingKey);
                AbstractClient.setClient(rabbitMQClient);
            }
        } else {
            final BaseLogMessage logMessage = LogMessageUtil.getLogMessage(sysName, env, loggingEvent);
            MessageAppenderFactory.push(logMessage);
        }

    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
