package cn.iocoder.yudao.module.member.controller.app.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Schema(description = "用户 APP - 社交快捷登录 Request VO，使用 address 授权码")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppAuthWeb3LoginReqVO {

    @Schema(description = "地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "9b2ffbc1-7425-4155-9894-9d5c08541d62")
    @NotEmpty(message = "地址 不能为空")
    private String address;

    @Schema(description = "签名", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "签名不能为空")
    private String signature;

    @Schema(description = "邀请码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String invitationCode;

}