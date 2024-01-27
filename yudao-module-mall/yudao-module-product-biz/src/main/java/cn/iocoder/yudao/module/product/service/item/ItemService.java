package cn.iocoder.yudao.module.product.service.item;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.product.api.item.dto.*;
import cn.iocoder.yudao.module.product.controller.app.item.vo.*;
import cn.iocoder.yudao.module.product.dal.dataobject.item.ItemDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 商品 SPU Service 接口
 *
 * @author 芋道源码
 */
public interface ItemService {

    void updateItem(AppItemUpdateStatusReqVO updateReqVO);

    /**
     * 获得商品 SPU
     *
     * @param id 编号
     * @return 商品 SPU
     */
    ItemDO getItem(Long id);

    /**
     * 获得商品 SPU 列表
     *
     * @param ids 编号数组
     * @return 商品 SPU 列表
     */
    List<ItemDO> getItemList(Collection<Long> ids);

    /**
     * 获得商品 SPU 映射
     *
     * @param ids 编号数组
     * @return 商品 SPU 映射
     */
    default Map<Long, ItemDO> getItemMap(Collection<Long> ids) {
        return convertMap(getItemList(ids), ItemDO::getId);
    }

    /**
     * 获得指定状态的商品 SPU 列表
     *
     * @param status 状态
     * @return 商品 SPU 列表
     */
    List<ItemDO> getItemListByStatus(Integer status);

    /**
     * 获得商品 SPU 分页，提供给挂你兰后台使用
     *
     * @param pageReqVO 分页查询
     * @return 商品item分页
     */
    PageResult<ItemDO> getItemPage(ItemPageReqVO pageReqVO);

    /**
     * 获得商品 SPU 分页，提供给用户 App 使用
     *
     * @param pageReqVO 分页查询
     * @return 商品 SPU 分页
     */
    PageResult<ItemDO> getItemPage(AppItemPageReqVO pageReqVO);

    /**
     * 获得商品 SPU 列表，提供给用户 App 使用
     *
     * @param recommendType 推荐类型
     * @param count 数量
     * @return 商品 SPU 列表
     */
    List<ItemDO> getItemList(String recommendType, Integer count);

    ItemDO shelveItem(@Valid ItemCreateReqDTO createReqVO);

    /**
     * 更新商品 库存（增量）
     */
    void updateItemStock(ItemUpdateStockReqDTO updateStockReqDTO);

    /**
     * 更新商品 库存（增量）
     */
    void updateItemStatusList(ItemUpdateStatusReqDTO updateStatusReqDTO);

    /**
     * 复制商品
     * @param copyReqDTO
     * @return Long
     */
    Long copyItem(ItemCopyReqDTO copyReqDTO);

    /**
     * 更新 SPU 状态
     *
     * @param updateReqVO 更新请求
     */
    void updateItemStatus(ItemUpdateStatusReqVO updateReqVO);

    /**
     * 获取 SPU 列表标签对应的 Count 数量
     *
     * @return Count 数量
     */
    Map<Integer, Long> getTabsCount();

    /**
     * 通过分类 tokenId 查询 个数
     *
     * @param tokenId tokenId
     * @return SPU 数量
     */
    Long getItemCountByTokenId(Long tokenId);


    /**
     * 校验商品是否有效。如下情况，视为无效：
     * 1. 商品编号不存在
     * 2. 商品被禁用
     *
     * @param ids 商品编号数组
     * @return 商品 SPU 列表
     */
    List<ItemDO> validateItemList(Collection<Long> ids);

    ItemDO getLowerPriceEnableItem();
}
