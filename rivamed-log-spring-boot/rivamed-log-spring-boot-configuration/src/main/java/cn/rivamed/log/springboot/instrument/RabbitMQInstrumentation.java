package cn.rivamed.log.springboot.instrument;

import com.rabbitmq.client.Channel;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;

/**
 * 描述: RabbitMQ增强类
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/10/24 10:34
 */
@Slf4j
public class RabbitMQInstrumentation {

    public static final String ENHANCE_RABBITMQ_SEND_INTERCEPTOR_PATH = "cn.rivamed.log.springboot.interceptor.RabbitMQInterceptor.sendInterceptor"; // 发送拦截器

    public static final String ENHANCE_RABBITMQ_RECEIVE_INTERCEPTOR_PATH = "cn.rivamed.log.springboot.interceptor.RabbitMQInterceptor.receiveInterceptor"; // 接收拦截器

    public static final String ENHANCE_RABBIT_TEMPLATE_CLASS = "org.springframework.amqp.rabbit.core.RabbitTemplate"; // 增强的类
    public static final String ENHANCE_RABBIT_RECEIVE_CLASS = "org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter"; // 增强的类
    public static final String ENHANCE_SEND_METHOD = "send"; // 增强的方法
    public static final String ENHANCE_RECEIVE_METHOD = "invokeHandler"; // 增强的方法

    public static boolean sendEnhance() throws NotFoundException, CannotCompileException {

        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));

        //拦截send方法
        CtClass ctClass = classPool.getOrNull(ENHANCE_RABBIT_TEMPLATE_CLASS);
        if (ctClass == null) {
            log.error("RabbitMQ client not found");
            return false;
        }
        CtClass strClass = classPool.get(String.class.getName());
        CtClass messageClass = classPool.get(Message.class.getName());
        CtClass correlationDataClass = classPool.get(CorrelationData.class.getName());
        CtClass[] params = new CtClass[]{strClass, strClass, messageClass, correlationDataClass};
        CtMethod doExecuteMethod = ctClass.getDeclaredMethod(ENHANCE_SEND_METHOD, params);
        String sb = "{" + ENHANCE_RABBITMQ_SEND_INTERCEPTOR_PATH + "($0, $args);" + "}"; // 调用封装的方法
        doExecuteMethod.insertBefore(sb); // 植入代码片段
        ctClass.toClass();

        return true;
    }

    /**
     * 只有在 fromMessage 执行完成后才能拿到 targetMethod, 然后需要从 channel 里面拿到 Connection 的 virtualHost 信息，
     * 所有拦截 invokeHandlerAndProcessResult 这个方法
     *
     *
     * @return
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    public static boolean receiveEnhance() throws NotFoundException, CannotCompileException {

        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));

        //拦截receive方法
        CtClass ctClass = classPool.getOrNull(ENHANCE_RABBIT_RECEIVE_CLASS);
        if (ctClass == null) {
            log.error("RabbitMQ Listener not found");
            return false;
        }
        CtClass messageClass = classPool.get(Message.class.getName());
        CtClass channelClass = classPool.get(Channel.class.getName());
        CtClass messagingClass = classPool.get(org.springframework.messaging.Message.class.getName());
        CtClass[] params = new CtClass[]{messageClass,channelClass, messagingClass};

        CtMethod doExecuteMethod = ctClass.getDeclaredMethod(ENHANCE_RECEIVE_METHOD, params);
        String sb = "{" + ENHANCE_RABBITMQ_RECEIVE_INTERCEPTOR_PATH + "($1, $2);" + "}"; // 调用封装的方法
        doExecuteMethod.insertBefore(sb); // 植入代码片段
        ctClass.toClass();

        return true;
    }
}
