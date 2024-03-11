package cn.iocoder.yudao.module.project.dal.dataobject.whitelist;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 白名单 DO
 *
 * @author BT2N
 */
@TableName("pay_white_list")
@KeySequence("pay_white_list_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhiteListDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Integer id;
    /**
     * 地址
     */
    private String address;
    /**
     * 是否开启
     *
     * 枚举 {@link TODO StartStatus 对应的类}
     */
    private Integer status;

}