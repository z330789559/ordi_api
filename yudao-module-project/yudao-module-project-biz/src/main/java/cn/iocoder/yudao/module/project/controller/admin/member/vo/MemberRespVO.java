package cn.iocoder.yudao.module.project.controller.admin.member.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 会员用户 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MemberRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1796")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "父ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30922")
    @ExcelProperty("父ID")
    private Long parentId;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("手机号")
    private String mobile;

    @Schema(description = "父钱包地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("父钱包地址")
    private String parentAddress;

    @Schema(description = "钱包地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("钱包地址")
    private String address;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("密码")
    private String password;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "注册 IP", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("注册 IP")
    private String registerIp;

    @Schema(description = "注册终端")
    @ExcelProperty("注册终端")
    private Integer registerTerminal;

    @Schema(description = "最后登录IP")
    @ExcelProperty("最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    @ExcelProperty("最后登录时间")
    private LocalDateTime loginDate;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("用户昵称")
    private String nickname;

    @Schema(description = "头像", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("头像")
    private String avatar;

    @Schema(description = "真实名字", example = "李四")
    @ExcelProperty("真实名字")
    private String name;

    @Schema(description = "用户性别")
    @ExcelProperty("用户性别")
    private Integer sex;

    @Schema(description = "所在地", example = "5956")
    @ExcelProperty("所在地")
    private Long areaId;

    @Schema(description = "出生日期")
    @ExcelProperty("出生日期")
    private LocalDateTime birthday;

    @Schema(description = "会员备注")
    @ExcelProperty("会员备注")
    private String mark;

    @Schema(description = "积分", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("积分")
    private Integer point;

    @Schema(description = "用户标签编号列表，以逗号分隔")
    @ExcelProperty("用户标签编号列表，以逗号分隔")
    private String tagIds;

    @Schema(description = "等级编号", example = "28514")
    @ExcelProperty("等级编号")
    private Long levelId;

    @Schema(description = "经验", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("经验")
    private Integer experience;

    @Schema(description = "用户分组编号", example = "5181")
    @ExcelProperty("用户分组编号")
    private Long groupId;

    @Schema(description = "邀请码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("邀请码")
    private String invitationCode;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "邀请时间")
    @ExcelProperty("邀请时间")
    private LocalDateTime invitationTime;

    @Schema(description = "上下级关系", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("上下级关系")
    private String tree;

    @Schema(description = "等级水平", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("等级水平")
    private Integer level;

}