package cn.iocoder.yudao.module.product.api.item.dto;

import cn.iocoder.yudao.module.product.enums.item.ItemStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品 Item 信息 Response DTO
 */
@Data
public class ItemRespDTO {
    /**
     * 编号，自增
     */
    private Long id;
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
     * VIP价格
     */
    private BigDecimal vipPrice;

    private BigDecimal usdPrice;

}
