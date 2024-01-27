package cn.iocoder.yudao.module.product.api.item;

import cn.iocoder.yudao.module.product.api.item.dto.*;

import java.util.Collection;
import java.util.List;

/**
 * 商品 Item API 接口
 */
public interface ItemApi {

    ItemRespDTO getItem(Long id);

    ItemRespDTO shelveItem(ItemCreateReqDTO copyReqDTO);

    void sellItem(Long id);

    void disableItem(Long id);

    void updateOrderItem(Long id, Long orderItemId);
}
