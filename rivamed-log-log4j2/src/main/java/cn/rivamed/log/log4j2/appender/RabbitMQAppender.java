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


    protected RabbitMQAppender(String name, Filter filter, Layout<? extends Serializable> layout,
                               final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    @PluginFactory
    public static RabbitMQAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter) {
        return new RabbitMQAppender(name, filter, layout, true);
    }

    @Override
    public void append(LogEvent logEvent) {
        if (RabbitMQClient.getClient() != null) {
            final BaseLogMessage logMessage = LogMessageUtil.getLogMessage(logEvent);
            MessageAppenderFactory.push(logMessage);
        }
    }
}
