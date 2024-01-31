package cn.iocoder.yudao.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DirectInvetedRespVo
 *
 * @author libaozhong
 * @version 2024-01-29
 **/


@Schema(description = "用户 APP -用户邀请相关 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectInvitedRespVo {

	@Schema(description = "直推数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
	private Integer directNum;

	@Schema(description = "父地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
	private String address;
}
