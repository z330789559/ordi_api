package cn.iocoder.yudao.module.admin.dal.dataobject.web3;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.admin.enums.social.SocialTypeEnum;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 社交客户端 DO
 *
 * @author 芋道源码
 */
@TableName(value = "system_social_client", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Web3ClientDO extends TenantBaseDO {

    /**
     * 编号，自增
     */
    @TableId
    private Long id;
    /**
     * 应用名
     */
    private String name;
    /**
     * 社交类型
     *
     * 枚举 {@link SocialTypeEnum}
     */
    private Integer socialType;
    /**
     * 用户类型
     *
     * 目的：不同用户类型，对应不同的小程序，需要自己的配置
     *
     * 枚举 {@link UserTypeEnum}
     */
    private Integer userType;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 客户端 id
     */
    private String clientId;
    /**
     * 客户端 Secret
     */
    private String clientSecret;

    /**
     * 代理编号
     *
     * 目前只有部分“社交类型”在使用：
     * 1. 企业微信：对应授权方的网页应用 ID
     */
    private String agentId;

}
