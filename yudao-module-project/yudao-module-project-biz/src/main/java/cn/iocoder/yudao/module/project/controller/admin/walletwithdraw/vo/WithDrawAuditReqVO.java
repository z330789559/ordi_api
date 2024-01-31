package cn.iocoder.yudao.module.project.controller.admin.walletwithdraw.vo;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import cn.iocoder.yudao.module.pay.enums.wallet.WithdrawStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * withDrawAuditReqVO
 *
 * @author libaozhong
 * @version 2024-01-29
 **/

@Schema(description = "管理后台 - 提现新增/修改 Request VO")
@Data
public class WithDrawAuditReqVO {
	@Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "18582")
	@Positive(message = "id必须为正数")
	private Long id;

	private WithdrawStatusEnum status;

	private String remark;
}
