package cn.iocoder.yudao.module.system.dal.dataobject.web3;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 社交用户的绑定
 * 即 {@link Web3UserDO} 与 UserDO 的关联表
 *
 * @author 芋道源码
 */
@TableName(value = "system_web3_user_bind", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Web3UserBindDO extends BaseDO {
    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 关联的用户编号
     *
     * 关联 UserDO 的编号
     */
    private Long userId;
    /**
     * 用户类型
     *
     * 枚举 {@link UserTypeEnum}
     */
    private Integer userType;

    /**
     * 社交平台的用户编号
     *
     * 关联 {@link Web3UserDO#getId()}
     */
    private Long web3UserId;

}
