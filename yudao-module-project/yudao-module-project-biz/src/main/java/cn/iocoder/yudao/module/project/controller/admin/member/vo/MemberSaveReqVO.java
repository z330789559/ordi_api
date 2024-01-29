package cn.iocoder.yudao.module.project.controller.admin.member.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会员用户新增/修改 Request VO")
@Data
public class MemberSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1796")
    private Long id;

    @Schema(description = "父ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30922")
    private Long parentId;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mobile;

    @Schema(description = "父钱包地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String parentAddress;

    @Schema(description = "钱包地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "钱包地址不能为空")
    private String address;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "注册 IP", requiredMode = Schema.RequiredMode.REQUIRED)
    private String registerIp;

    @Schema(description = "注册终端")
    private Integer registerTerminal;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    private LocalDateTime loginDate;

    private String nickname;

    @Schema(description = "头像", requiredMode = Schema.RequiredMode.REQUIRED)
    private String avatar;

    @Schema(description = "真实名字", example = "李四")
    private String name;

    @Schema(description = "用户性别")
    private Integer sex;

    @Schema(description = "所在地", example = "5956")
    private Long areaId;

    @Schema(description = "出生日期")
    private LocalDateTime birthday;

    @Schema(description = "会员备注")
    private String mark;

    @Schema(description = "积分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer point;

    @Schema(description = "用户标签编号列表，以逗号分隔")
    private String tagIds;

    @Schema(description = "等级编号", example = "28514")
    private Long levelId;

    @Schema(description = "经验", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer experience;

    @Schema(description = "用户分组编号", example = "5181")
    private Long groupId;

    @Schema(description = "邀请码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "邀请码不能为空")
    private String invitationCode;

    @Schema(description = "邀请时间")
    private LocalDateTime invitationTime;

    @Schema(description = "上下级关系", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "上下级关系不能为空")
    private String tree;

    @Schema(description = "等级水平", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer level;

}