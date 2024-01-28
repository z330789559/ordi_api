package cn.iocoder.yudao.module.system.dal.dataobject.web3;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 社交（三方）用户
 *
 * @author weir
 */
@TableName(value = "system_web3_user", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Web3UserDO extends BaseDO {
    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * UUID
     */
    private String uuid;
    /**
     * 社交 address
     */
    private String address;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;

}


