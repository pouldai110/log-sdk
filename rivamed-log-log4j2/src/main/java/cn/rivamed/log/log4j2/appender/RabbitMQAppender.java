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
package cn.rivamed.log.log4j2.appender;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogRecordContext;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.factory.MessageAppenderFactory;
import cn.rivamed.log.core.rabbitmq.RabbitMQClient;
import cn.rivamed.log.log4j2.util.LogMessageUtil;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;

/**
 * An appender that writes to random access files and can roll over at
 * intervals.
 */
@Plugin(name = "RabbitMQAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public final class RabbitMQAppender extends AbstractAppender {

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

    protected RabbitMQAppender(String name, String sysName, String env, String host, int port, String virtualHost, String username, String password, String exchange, String routingKey, Filter filter, Layout<? extends Serializable> layout,
                               final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        this.sysName = sysName;
        this.env = env;
        this.host = host;
        this.port = port;
        this.virtualHost = virtualHost;
        this.username = username;
        this.password = password;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @PluginFactory
    public static RabbitMQAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginAttribute("sysName") String sysName,
            @PluginAttribute("env") String env,
            @PluginAttribute("host") String host,
            @PluginAttribute("port") int port,
            @PluginAttribute("virtualHost") String virtualHost,
            @PluginAttribute("username") String username,
            @PluginAttribute("password") String password,
            @PluginAttribute("exchange") String exchange,
            @PluginAttribute("routingKey") String routingKey,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter) {
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
        if (!host.contains(LogMessageConstant.EXCEPTION_CONFIG) && !virtualHost.contains(LogMessageConstant.EXCEPTION_CONFIG)) {
            RivamedLogRecordContext.putSysName(sysName);
            RivamedLogRecordContext.putEnv(env);
            rabbitMQClient = RabbitMQClient.getInstance(host, port, virtualHost, username, password, exchange, routingKey);
            AbstractClient.setClient(rabbitMQClient);
            return new RabbitMQAppender(name, sysName, env, host, port, virtualHost, username, password, exchange, routingKey, filter, layout, true);
        }
        return new RabbitMQAppender(name, sysName, env, host, port, virtualHost, username, password, exchange, routingKey, filter, layout, true);
    }

    @Override
    public void append(LogEvent logEvent) {
        final BaseLogMessage logMessage = LogMessageUtil.getLogMessage(sysName, env, logEvent);
        if (rabbitMQClient != null) {
            MessageAppenderFactory.push(logMessage);
        }
    }

    public String getSysName() {
        return sysName;
    }


    public String getEnv() {
        return env;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getExchange() {
        return exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

}
