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
     * 登录tokenId
     */
    private String tokenId;

    /**
     * 租户id
     */
    private String tenantId;

}
