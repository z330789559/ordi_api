package cn.iocoder.yudao.module.project.controller.admin.member.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 会员用户分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberPageReqVO extends PageParam {

    private  Long id;

    @Schema(description = "父ID", example = "30922")
    private Long parentId;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "父钱包地址")
    private String parentAddress;

    @Schema(description = "钱包地址")
    private String address;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "注册 IP")
    private String registerIp;

    @Schema(description = "注册终端")
    private Integer registerTerminal;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] loginDate;

    @Schema(description = "用户昵称", example = "芋艿")
    private String nickname;

    @Schema(description = "头像")
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

    @Schema(description = "积分")
    private Integer point;

    @Schema(description = "用户标签编号列表，以逗号分隔")
    private String tagIds;

    @Schema(description = "等级编号", example = "28514")
    private Long levelId;

    @Schema(description = "经验")
    private Integer experience;

    @Schema(description = "用户分组编号", example = "5181")
    private Long groupId;

    @Schema(description = "邀请码")
    private String invitationCode;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "邀请时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] invitationTime;

    @Schema(description = "上下级关系")
    private String tree;

    @Schema(description = "等级水平")
    private Integer level;

}