package cn.rivamed.log.core.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述: 队列日志
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2020<br/>
 *
 * @author fujiali
 * @version V1.0
 * @date 2022/9/27 15:30
 */
@Data
public class SystemLogMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    private String id;

    /**
     * 日志发生时间
     */
    private Date bizTime = new Date();

    /**
     * 日志详情
     */
    private String bizDetail;

    /**
     * 接口执行时长
     */
    private Long costTime;

    /**
     * 日志级别
     */
    private String level;

    /**
     * traceId
     */
    private String traceId;

    /**
     * 登录tokenId
     */
    private String tokenId;

    /**
     * 系统方法名
     */
    private String method;

    /**
     * 日志类型 logRecord/rabbitMQ/systemLog
     */
    private String logType;

    /**
     * 系统名称
     */
    private String sysName;

}
