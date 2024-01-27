package cn.iocoder.yudao.module.pay.api.transfer.dto;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.pay.enums.transfer.PayTransferTypeEnum;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 转账单创建 Request DTO
 *
 * @author jason
 */
@Data
public class PayTransferCreateReqDTO {

}
