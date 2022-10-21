package cn.rivamed.log.rabbitmq.aspect;

import cn.rivamed.log.core.entity.RabbitLogMessage;
import cn.rivamed.log.core.factory.MessageAppenderFactory;
import cn.rivamed.log.rabbitmq.util.RabbitLogMessageUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Method;

@Aspect
public class RabbitAspect {

    protected static final Logger log = LoggerFactory.getLogger(RabbitAspect.class);

    /**
     * 拦截RabbitTemplate的convertAndSend与send方法
     *
     * @param joinPoint joinPoint
     * @return java.lang.Object
     * @author pujian
     * @date 2022/10/13 11:07
     */
    @Around("execution(public void org.springframework.amqp.rabbit.core.RabbitTemplate.convertAndSend(..)) || " +
            "execution(public void org.springframework.amqp.rabbit.core.RabbitTemplate.send(..))")
    public Object sendInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {

        RabbitTemplate rabbitTemplate = (RabbitTemplate) joinPoint.getTarget();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();

        RabbitLogMessage rabbitLogMessage = RabbitLogMessageUtils.collectFromSend(rabbitTemplate, method, args);
        if (rabbitLogMessage != null) {
            log.info("向MQ发送消息，exchange={}，routingKey={}，message={}", rabbitLogMessage.getExchange(), rabbitLogMessage.getRoutingKey(), rabbitLogMessage.getMessage());
            MessageAppenderFactory.pushRabbitLogMessage(rabbitLogMessage, joinPoint);
        }
        return joinPoint.proceed();
    }


    /**
     * 拦截{@link org.springframework.amqp.rabbit.annotation.RabbitListener}注解标注的方法
     *
     * @param joinPoint joinPoint
     * @return java.lang.Object
     * @author pujian
     * @date 2022/10/13 11:07
     */
    @Around("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    public Object listenerInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        RabbitLogMessage rabbitLogMessage = RabbitLogMessageUtils.collectFromListener(args);
        log.info("rabbit listener接收到消息，exchange={}，routingKey={}，queue={}，message={}", rabbitLogMessage.getExchange(), rabbitLogMessage.getRoutingKey(), rabbitLogMessage.getQueueName(), rabbitLogMessage.getMessage());
        MessageAppenderFactory.pushRabbitLogMessage(rabbitLogMessage, joinPoint);
        return joinPoint.proceed();
    }


}
