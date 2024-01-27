package cn.iocoder.yudao.module.product.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Product 错误码枚举类
 * <p>
 * product 系统，使用 1-008-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 商品分类相关 1-008-001-000 ============
    ErrorCode TOKEN_NOT_EXISTS = new ErrorCode(1_008_001_000, "商品分类不存在");
    ErrorCode TOKEN_PARENT_NOT_EXISTS = new ErrorCode(1_008_001_001, "父分类不存在");
    ErrorCode TOKEN_PARENT_NOT_FIRST_LEVEL = new ErrorCode(1_008_001_002, "父分类不能是二级分类");
    ErrorCode TOKEN_EXISTS_CHILDREN = new ErrorCode(1_008_001_003, "存在子分类，无法删除");
    ErrorCode TOKEN_DISABLED = new ErrorCode(1_008_001_004, "商品分类({})已禁用，无法使用");
    ErrorCode TOKEN_HAVE_BIND_ITEM = new ErrorCode(1_008_001_005, "类别下存在商品，无法删除");

    // ========== 商品品牌相关编号 1-008-002-000 ==========
    ErrorCode BRAND_NOT_EXISTS = new ErrorCode(1_008_002_000, "品牌不存在");
    ErrorCode BRAND_DISABLED = new ErrorCode(1_008_002_001, "品牌已禁用");
    ErrorCode BRAND_NAME_EXISTS = new ErrorCode(1_008_002_002, "品牌名称已存在");

    // ========== 商品属性项 1-008-003-000 ==========
    ErrorCode PROPERTY_NOT_EXISTS = new ErrorCode(1_008_003_000, "属性项不存在");
    ErrorCode PROPERTY_EXISTS = new ErrorCode(1_008_003_001, "属性项的名称已存在");
    ErrorCode PROPERTY_DELETE_FAIL_VALUE_EXISTS = new ErrorCode(1_008_003_002, "属性项下存在属性值，无法删除");

    // ========== 商品属性值 1-008-004-000 ==========
    ErrorCode PROPERTY_VALUE_NOT_EXISTS = new ErrorCode(1_008_004_000, "属性值不存在");
    ErrorCode PROPERTY_VALUE_EXISTS = new ErrorCode(1_008_004_001, "属性值的名称已存在");

    // ========== 商品 ITEM 1-008-005-000 ==========
    ErrorCode ITEM_SAVE_FAIL_TOKEN_LEVEL_ERROR = new ErrorCode(1_008_005_001, "商品分类不正确，原因：必须使用第二级的商品分类及以下");
    ErrorCode ITEM_SAVE_FAIL_COUPON_TEMPLATE_NOT_EXISTS = new ErrorCode(1_008_005_002, "商品 ITEM 保存失败，原因：优惠卷不存在");
    ErrorCode ITEM_NOT_ENABLE = new ErrorCode(1_008_005_003, "商品 ITEM【{}】不处于上架状态");
    ErrorCode ITEM_NOT_RECYCLE = new ErrorCode(1_008_005_004, "商品 ITEM 不处于回收站状态");

    // ========== 商品 SKU 1-008-006-000 ==========
    ErrorCode ITEM_NOT_EXISTS = new ErrorCode(1_008_006_000, "商品 不存在");
    ErrorCode ITEM_NOT_BELONG_YOUR = new ErrorCode(1_008_006_000, "商品不属于你");
    ErrorCode ITEM_BELONG_YOUR_SELF = new ErrorCode(1_008_006_000, "不允许购买自己的商品");
    ErrorCode ITEM_STATUS_ERROR = new ErrorCode(1_008_006_000, "商品状态错误");
    ErrorCode SKU_PROPERTIES_DUPLICATED = new ErrorCode(1_008_006_001, "商品 SKU 的属性组合存在重复");
    ErrorCode ITEM_ATTR_NUMBERS_MUST_BE_EQUALS = new ErrorCode(1_008_006_002, "一个 ITEM 下的每个 SKU，其属性项必须一致");
    ErrorCode ITEM_SKU_NOT_DUPLICATE = new ErrorCode(1_008_006_003, "一个 ITEM 下的每个 SKU，必须不重复");
    ErrorCode ITEM_STOCK_NOT_ENOUGH = new ErrorCode(1_008_006_004, "商品库存不足");

    // ========== 商品 评价 1-008-007-000 ==========
    ErrorCode COMMENT_NOT_EXISTS = new ErrorCode(1_008_007_000, "商品评价不存在");
    ErrorCode COMMENT_ORDER_EXISTS = new ErrorCode(1_008_007_001, "订单的商品评价已存在");

    // ========== 商品 收藏 1-008-008-000 ==========
    ErrorCode FAVORITE_EXISTS = new ErrorCode(1_008_008_000, "该商品已经被收藏");
    ErrorCode FAVORITE_NOT_EXISTS = new ErrorCode(1_008_008_001, "商品收藏不存在");

}
