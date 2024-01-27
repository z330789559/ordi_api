package cn.iocoder.yudao.module.product.dal.dataobject.item;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Item DO
 */
@TableName(value = "ordi_product_item", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDO extends BaseDO {
    /**
     * 编号，自增
     */
    @TableId
    private Long id;
    /**
     * 上架关联ID
     */
    private Long orderItemId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * Btc Nft Type
     */
    private Integer btcNftType;
    /**
     * Chain
     */
    private Integer chain;
    /**
     * 合约地址
     */
    private String contractAddress;
    /**
     * 金额
     */
    private BigDecimal floorPrice;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * Meta
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> meta;
    /**
     * Nft Id
     */
    private String nftId;
    /**
     * 所属者地址
     */
    private String ownerAddress;
    /**
     * Project
     */
    private String project;
    /**
     * Token Id
     */
    private Long tokenId;
    /**
     * token name
     */
    private String ticker;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 是否热卖推荐
     */
    private Boolean recommendHot;
    /**
     * 总销量
     */
    private Integer salesCount;
    /**
     * 虚拟销量
     */
    private Integer virtualSales;
    /**
     * 单位
     */
    private String unit;
    /**
     * VIP价格
     */
    private BigDecimal vipPrice;
}
