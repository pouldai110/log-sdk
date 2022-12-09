package cn.rivamed.log.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * className：BaseLogMessage
 * description：
 * time：2022-10-01.15:28
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LogClientInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 子系统名称
     */
    private String systemName;

    /**
     * 客户端IP
     */
    private String systemIp;

    /**
     * 客户端端口
     */
    private String systemPort;

}
