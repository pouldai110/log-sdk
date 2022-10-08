package cn.rivamed.log.core.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述: 接口、定时任务日志信息
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2020<br/>
 *
 * @author fujiali
 * @version V1.0
 * @date 2022/9/27 15:34
 */
@Data
public class LogRecordMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 语义日志id
     */
    private String id;

    /**
     * 日志发生时间
     */
    private Date bizTime = new Date();

    /**
     * 操作人账号
     */
    private String userId;

    /**
     * 操作人姓名
     */
    private String userName = "";

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备sn
     */
    private String sn;

    /**
     * 访问URL
     */
    private String url;

    /**
     * 系统方法名
     */
    private String method;

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
     * 系统名称
     */
    private String sysName;

    /**
     * 执行机器ip
     */
    private String bizIP;

    /**
     * 日志详情
     */
    private String bizDetail;

    /**
     * 接口执行时长
     */
    private Long costTime;

    /**
     * 操作状态码
     */
    private String responseCode;

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
     * LogRecode(语义化日志字符串)
     */
    private String logRecord;

    /**
     * 堆栈信息
     */
    private String stackTrace;

    /**
     * record日志类型 timeTaskLog：定时任务 userLog：接口
     */
    private String logRecordType;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 日志类型 logRecord/rabbitMQ/systemLog
     */
    private String logType;

}
