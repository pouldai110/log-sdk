package cn.rivamed.log.springboot.runner;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.entity.LogClientInfo;
import cn.rivamed.log.core.rabbitmq.RabbitMQClient;
import cn.rivamed.log.core.spring.RivamedLogPropertyInit;
import cn.rivamed.log.core.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 描述: 启动完成推送注册事件
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/12/28 10:28
 */
@Component
public class RivamedLogApplicationRunnerImpl implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(RivamedLogApplicationRunnerImpl.class);

    @Resource
    private RivamedLogPropertyInit rivamedLogPropertyInit;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("发送客户端启动注册事件");
        //发送客户端启动注册事件
        LogClientInfo logClientInfo = new LogClientInfo(rivamedLogPropertyInit.getSysName(), IpUtil.getLocalHostIp(), rivamedLogPropertyInit.getClientPort());
        RabbitMQClient.getClient().pushSimpleMessage(LogMessageConstant.RIVAMED_REG_LOG_QUEUE_NAME, logClientInfo);
    }
}