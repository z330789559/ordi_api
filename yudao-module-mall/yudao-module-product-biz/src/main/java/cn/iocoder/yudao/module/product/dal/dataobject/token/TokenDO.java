package cn.iocoder.yudao.module.product.dal.dataobject.token;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

/**
 * 币种 DO
 */
@TableName("ordi_product_token")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDO extends BaseDO {
    /**
     * 限定分类层级
     */
    public static final int CATEGORY_LEVEL = 2;

    /**
     * 父编号
     */
    public static final Long PARENT_ID_NULL = 0L;

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 父编号
     */
    private Long parentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 开启状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 持有人数
     */
    private Integer holders;
    /**
     * 涨跌幅
     */
    private BigDecimal changeRate;
    /**
     * 图标
     *
     * 建议 180*180 分辨率
     */
    private String image;
    /**
     * 对标美元价格
     */
    private BigDecimal usdPrice;
    /**
     * 排序
     */
    private Integer sort;

    private BigDecimal marketCap;

    private BigDecimal volume;

    private BigDecimal volume24h;

    private Integer isBuy;

    private String itemName;

    private BigDecimal buyGasFee;

    private BigDecimal sellGasFee;

    private BigDecimal withdrawGasFee;

    private BigDecimal buyGasLimit;

    private BigDecimal sellGasLimit;

    private Integer tradeCount;
}
