/*
 * Copyright 2013-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.rivamed.log.springboot.configuration;

import brave.Tracer;
import cn.rivamed.log.task.spring.SpringScheduledTaskAop;
import cn.rivamed.log.task.xxlJob.XxlJobTaskAop;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.aspectj.lang.ProceedingJoinPoint")
@ConditionalOnProperty(value = "spring.sleuth.scheduled.enabled", matchIfMissing = true)
@AutoConfigureAfter(TraceAutoConfiguration.class)
public class RivamedLogTraceSchedulingAutoConfiguration {

    @Bean
    public SpringScheduledTaskAop springScheduledTaskAop(Tracer tracer) {
        return new SpringScheduledTaskAop(tracer);
    }


    @Bean
    @ConditionalOnClass(name = "com.xxl.job.core.handler.annotation.XxlJob")
    public XxlJobTaskAop xxlJobTaskAop(Tracer tracer) {
        return new XxlJobTaskAop(tracer);
    }

}
