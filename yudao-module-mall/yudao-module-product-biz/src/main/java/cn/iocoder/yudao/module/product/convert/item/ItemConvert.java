package cn.iocoder.yudao.module.product.convert.item;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.product.api.item.dto.ItemRespDTO;
import cn.iocoder.yudao.module.product.controller.app.item.vo.*;
import cn.iocoder.yudao.module.product.dal.dataobject.item.ItemDO;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static cn.hutool.core.util.ObjectUtil.defaultIfNull;

/**
 * 商品 Item Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ItemConvert {

    ItemConvert INSTANCE = Mappers.getMapper(ItemConvert.class);

    ItemDO convert(ItemUpdateReqVO bean);

    ItemDO convert(AppItemUpdateStatusReqVO bean);

    List<ItemDO> convertList(List<ItemDO> list);

    PageResult<ItemRespVO> convertPage(PageResult<ItemDO> page);

    ItemPageReqVO convert(AppItemPageReqVO bean);

    List<ItemRespDTO> convertList2(List<ItemDO> list);

    ItemRespDTO convert02(ItemDO bean);

    // ========== 用户 App 相关 ==========

    PageResult<AppItemPageRespVO> convertPageForGetItemPage(PageResult<ItemDO> page);

    default List<AppItemPageRespVO> convertListForGetItemList(List<ItemDO> list) {
        return convertListForGetItemList0(list);
    }

    @Named("convertListForGetItemList0")
    List<AppItemPageRespVO> convertListForGetItemList0(List<ItemDO> list);

    default AppItemDetailRespVO convertForGetItemDetail(ItemDO Item) {
        // 处理 Item
        return convertForGetItemDetail0(Item)
                .setSalesCount(Item.getSalesCount() + defaultIfNull(Item.getVirtualSales(), 0));
    }

    AppItemDetailRespVO convertForGetItemDetail0(ItemDO Item);

    ItemDetailRespVO convertForItemDetailRespVO(ItemDO Item);

    default List<ItemDetailRespVO> convertForItemDetailRespListVO(List<ItemDO> Items) {
        return CollectionUtils.convertList(Items, Item -> convertForItemDetailRespVO(Item));
    }
}
