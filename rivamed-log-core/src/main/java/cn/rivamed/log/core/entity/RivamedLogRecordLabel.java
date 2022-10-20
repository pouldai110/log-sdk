package cn.rivamed.log.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Rivamed Log Label上下文
 *
 * @author Zuo Yang
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RivamedLogRecordLabel implements Serializable {

    private static final long serialVersionUID = 5235299539593154309L;

    /**
     * 操作人账号
     */
    private String userId;

    /**
     * 操作人姓名
     */
    private String userName;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备sn
     */
    private String sn;

    /**
     * 业务号
     */
    private String bizId;

    /**
     * 业务产品名
     */
    private String bizProd;

    /**
     * 业务动作
     */
    private String bizAction;

    /**
     * 操作状态码
     */
    private String responseCode;

    /**
     * LogRecode(语义化日志字符串)
     */
    private String logRecord;

    /**
     * 登录tokenId
     */
    private String tokenId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 系统名称
     */
    private String sysName;

}
