package cn.rivamed.log.core.entity;

import java.io.Serializable;

/**
 * 描述: 系统日志
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2020<br/>
 *
 * @author fujiali
 * @version V1.0
 * @date 2022/9/27 15:30
 */
public class SystemLogMessage extends BaseLogMessage {

    private static final long serialVersionUID = 1L;

    /**
     * 接口执行时长
     */
    private Long costTime;

    /**
     * 登录tokenId
     */
    private String tokenId;

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
