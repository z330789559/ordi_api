package cn.iocoder.yudao.module.admin.api.logger.dto;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import lombok.Data;

/**
 * 操作日志分页 Request DTO
 *
 * @author HUIHUI
 */
@Data
public class OperateLogV2PageReqDTO extends PageParam {

    /**
     * 模块类型
     */
    private String bizType;
    /**
     * 模块数据编号
     */
    private Long bizId;

    /**
     * 用户编号
     */
    private Long userId;

}
