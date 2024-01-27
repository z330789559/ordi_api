package cn.iocoder.yudao.module.product.api.item;

import cn.iocoder.yudao.module.product.api.item.dto.*;
import cn.iocoder.yudao.module.product.controller.app.item.vo.AppItemUpdateStatusReqVO;
import cn.iocoder.yudao.module.product.convert.item.ItemConvert;
import cn.iocoder.yudao.module.product.enums.item.ItemStatusEnum;
import cn.iocoder.yudao.module.product.service.item.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 商品 SPU API 接口实现类
 *
 * @author LeeYan9
 * @since 2022-09-06
 */
@Service
@Validated
public class ItemApiImpl implements ItemApi {

    @Resource
    private ItemService itemService;

    @Override
    public ItemRespDTO shelveItem(ItemCreateReqDTO copyReqDTO) {
        return ItemConvert.INSTANCE.convert02(itemService.shelveItem(copyReqDTO));
    }

    @Override
    public void sellItem(Long id) {
        itemService.updateItem(new AppItemUpdateStatusReqVO().setId(id)
                .setStatus(ItemStatusEnum.RECYCLE.getStatus())
                .setStock(0));
    }

    @Override
    public void disableItem(Long id) {
        itemService.updateItem(new AppItemUpdateStatusReqVO().setId(id)
                .setStatus(ItemStatusEnum.DISABLE.getStatus())
                .setStock(0));
    }

    @Override
    public void updateOrderItem(Long id, Long orderItemId) {
        itemService.updateItem(new AppItemUpdateStatusReqVO().setId(id).setOrderItemId(orderItemId));
    }

    @Override
    public ItemRespDTO getItem(Long id) {
        return ItemConvert.INSTANCE.convert02(itemService.getItem(id));
    }

}
