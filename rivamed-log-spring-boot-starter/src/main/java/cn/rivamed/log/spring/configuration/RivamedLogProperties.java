package cn.rivamed.log.spring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述: TODO
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @version V2.10.0
 * @author: Zuo Yang
 * @date: 2022/10/8 10:21
 */
@Component
@ConfigurationProperties(prefix = "rivamed.log")
public class RivamedLogProperties {

    private boolean enable=false;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
