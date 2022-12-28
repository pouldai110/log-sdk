package cn.rivamed.log.core.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述: 登录日志
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2020<br/>
 *
 * @author zm
 * @version V1.0
 * @date 2022/10/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LoginLogMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务系统名称
     */
    private String subSystemName;


    /**
     * 租户Id
     */
    private String tenantId;

    /**
     * 账户Id
     */
    private String accountId;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 工号
     */
    private String jobNo;

    /**
     * 登录方式 1:密码 2:IC卡 3:指纹|指静脉 4:人脸 6:token 7:手机号 8:验证码 (必填)
     */
    private String loginType;

    /**
     * 登录设备
     */
    private String loginDevice;

    /**
     * 登录设备sn
     */
    private String loginDeviceSn;

    /**
     * 登录token
     */
    private String tokenId;

    /**
     * 登录时间
     */
    private Date loginTime = new Date();

    /**
     * 退出登录时间
     */
    private Date logoutTime;

    /**
     * 登录状态
     */
    private String loginStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * traceId
     */
    private String traceId;
}
