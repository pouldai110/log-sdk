package cn.rivamed.log.core.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 系统日志
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2020<br/>
 *
 * @author fujiali
 * @version V1.0
 * @date 2022/9/27 15:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
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

}
