package cn.iocoder.yudao.module.project.dal.dataobject.member;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 会员用户 DO
 *
 * @author 芋道源码
 */
@TableName("member_user")
@KeySequence("member_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 父ID
     */
    private Long parentId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 父钱包地址
     */
    private String parentAddress;
    /**
     * 钱包地址
     */
    private String address;
    /**
     * 密码
     */
    private String password;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 注册 IP
     */
    private String registerIp;
    /**
     * 注册终端
     */
    private Integer registerTerminal;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 真实名字
     */
    private String name;
    /**
     * 用户性别
     */
    private Integer sex;
    /**
     * 所在地
     */
    private Long areaId;
    /**
     * 出生日期
     */
    private LocalDateTime birthday;
    /**
     * 会员备注
     */
    private String mark;
    /**
     * 积分
     */
    private Integer point;
    /**
     * 用户标签编号列表，以逗号分隔
     */
    private String tagIds;
    /**
     * 等级编号
     */
    private Long levelId;
    /**
     * 经验
     */
    private Integer experience;
    /**
     * 用户分组编号
     */
    private Long groupId;
    /**
     * 邀请码
     */
    private String invitationCode;
    /**
     * 邀请时间
     */
    private LocalDateTime invitationTime;
    /**
     * 上下级关系
     */
    private String tree;
    /**
     * 等级水平
     */
    private Integer level;

}