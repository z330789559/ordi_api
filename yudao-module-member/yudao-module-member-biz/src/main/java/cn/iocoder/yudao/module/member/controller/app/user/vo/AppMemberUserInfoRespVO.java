package cn.iocoder.yudao.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "用户 APP - 用户个人信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppMemberUserInfoRespVO {

    @Schema(description = "邀请码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String invitationCode;

    @Schema(description = "邀请人ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long parentId;

    @Schema(description = "地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String address;

    @Schema(description = "邀请人地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String parentAddress;

    @Schema(description = "邀请时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private LocalDateTime invitationTime;

    private BigDecimal balance;

    private Integer level;


    private BigDecimal teamBalance;

    private BigDecimal personalBalance;

    private Integer teamMemberNum;

    private String brcAddress;
}
