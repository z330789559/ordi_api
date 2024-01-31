package cn.iocoder.yudao.module.member.controller.app.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AppMemberUserBindBrcAddressReqVO
 *
 * @author libaozhong
 * @version 2024-01-29
 **/

@Schema(description = "用户 APP - 校验验证码 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppMemberUserBindBrcAddressReqVO {

	private String brcAddress;
}
