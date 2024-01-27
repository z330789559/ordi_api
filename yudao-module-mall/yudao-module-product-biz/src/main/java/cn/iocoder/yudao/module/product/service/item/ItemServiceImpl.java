package cn.iocoder.yudao.module.product.service.item;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.product.api.item.dto.*;
import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenListReqVO;
import cn.iocoder.yudao.module.product.controller.app.item.vo.*;
import cn.iocoder.yudao.module.product.convert.item.ItemConvert;
import cn.iocoder.yudao.module.product.dal.dataobject.item.ItemDO;
import cn.iocoder.yudao.module.product.dal.dataobject.token.TokenDO;
import cn.iocoder.yudao.module.product.dal.mysql.item.ItemMapper;
import cn.iocoder.yudao.module.product.enums.item.ItemStatusEnum;
import cn.iocoder.yudao.module.product.service.token.TokenService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.*;

/**
 * 商品 ITEM Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemMapper itemMapper;

    @Resource
    private TokenService tokenService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateItem(AppItemUpdateStatusReqVO updateReqVO) {
        // 校验 ITEM 是否存在
        validateItemExists(updateReqVO.getId());

        // 更新 ITEM
        ItemDO updateObj = ItemConvert.INSTANCE.convert(updateReqVO);
        itemMapper.updateById(updateObj);
    }

    /**
     * 校验商品币种是否合法
     *
     * @param id 商品币种编号
     */
    private TokenDO validateToken(Long id) {
        TokenDO tokenDO = tokenService.validateToken(id);

        return tokenDO;
    }

    @Override
    public List<ItemDO> validateItemList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        // 获得商品信息
        List<ItemDO> list = itemMapper.selectBatchIds(ids);
        Map<Long, ItemDO> itemMap = CollectionUtils.convertMap(list, ItemDO::getId);
        // 校验
        ids.forEach(id -> {
            ItemDO item = itemMap.get(id);
            if (item == null) {
                throw exception(ITEM_NOT_EXISTS);
            }
            if (!ItemStatusEnum.isEnable(item.getStatus())) {
                throw exception(ITEM_NOT_ENABLE, item.getName());
            }
        });
        return list;
    }

    @Override
    public ItemDO getLowerPriceEnableItem() {
        return itemMapper.selectOne(new LambdaQueryWrapper<ItemDO>()
                .eq(ItemDO::getStatus, ItemStatusEnum.ENABLE.getStatus())
                .orderByAsc(ItemDO::getPrice)
                .last("limit 1"));
    }

    private void validateItemExists(Long id) {
        if (itemMapper.selectById(id) == null) {
            throw exception(ITEM_NOT_EXISTS);
        }
    }

    @Override
    public ItemDO getItem(Long id) {
        return itemMapper.selectById(id);
    }

    @Override
    public List<ItemDO> getItemList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return itemMapper.selectBatchIds(ids);
    }

    @Override
    public List<ItemDO> getItemListByStatus(Integer status) {
        return itemMapper.selectList(ItemDO::getStatus, status);
    }

    @Override
    public PageResult<ItemDO> getItemPage(ItemPageReqVO pageReqVO) {
        return itemMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<ItemDO> getItemPage(AppItemPageReqVO pageReqVO) {
        // 查找时，如果查找某个币种编号，则包含它的子币种。因为顶级币种不包含商品
        Set<Long> tokenIds = new HashSet<>();
        if (pageReqVO.getTokenId() != null && pageReqVO.getTokenId() > 0) {
            tokenIds.add(pageReqVO.getTokenId());
            List<TokenDO> tokenChildren = tokenService.getEnableTokenList(new TokenListReqVO()
                    .setParentId(pageReqVO.getTokenId()).setStatus(CommonStatusEnum.ENABLE.getStatus()));
            tokenIds.addAll(CollectionUtils.convertList(tokenChildren, TokenDO::getId));
        }
        // 分页查询
        return itemMapper.selectPage(pageReqVO, tokenIds);
    }

    @Override
    public List<ItemDO> getItemList(String recommendType, Integer count) {
        return itemMapper.selectListByRecommendType(recommendType, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItemDO shelveItem(ItemCreateReqDTO createReqDTO) {
        // 校验币种、品牌
        TokenDO tokenDO = validateToken(createReqDTO.getTokenId());

        ItemDO item = new ItemDO().setStatus(ItemStatusEnum.ENABLE.getStatus())
                .setPrice(createReqDTO.getPrice()).setStock(createReqDTO.getQuantity())
                .setName(tokenDO.getItemName()).setTicker(tokenDO.getName())
                .setTokenId(tokenDO.getId()).setUserId(createReqDTO.getUserId());
        itemMapper.insert(item);
        // 返回
        return item;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateItemStock(ItemUpdateStockReqDTO updateStockReqDTO) {
        // 更新 库存
        updateStockReqDTO.getItems().forEach(item -> {
            if (item.getIncrCount() > 0) {
                itemMapper.updateStockIncr(item.getId(), item.getIncrCount());
            } else if (item.getIncrCount() < 0) {
                int updateStockIncr = itemMapper.updateStockDecr(item.getId(), item.getIncrCount());
                if (updateStockIncr == 0) {
                    throw exception(ITEM_STOCK_NOT_ENOUGH);
                }
            }
        });
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateItemStatusList(ItemUpdateStatusReqDTO updateStockReqDTO) {
        // 更新 库存
        updateStockReqDTO.getItems().forEach(item -> {
            // 更新状态
            ItemDO itemDO = itemMapper.selectById(item.getId()).setStatus(updateStockReqDTO.getStatus());
            itemMapper.updateById(itemDO);
        });
    }

    /**
     * 复制
     *
     * @param copyReqDTO
     * @return Long
     */
    @Override
    public Long copyItem(ItemCopyReqDTO copyReqDTO) {
        ItemDO item = itemMapper.selectById(copyReqDTO.getItemId());

        // 更新原商品状态为已售出
        LambdaUpdateWrapper<ItemDO> updateWrapper = new LambdaUpdateWrapper<>();
        // 商品是回收中状态
        updateWrapper.eq(ItemDO::getStatus, ItemStatusEnum.RECYCLE.getStatus());
        // 改为出售
        updateWrapper.set(ItemDO::getStatus, ItemStatusEnum.SELL.getStatus());
        itemMapper.update(item, updateWrapper);

        item.setUserId(copyReqDTO.getUserId());
        item.setStatus(ItemStatusEnum.BUY.getStatus());
        item.setStock(copyReqDTO.getQuantity());
        item.setId(null);

        // 创建新商品
        itemMapper.insert(item);
        // 返回
        return item.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateItemStatus(ItemUpdateStatusReqVO updateReqVO) {
        // 校验存在
        validateItemExists(updateReqVO.getId());

        // 更新状态
        ItemDO itemDO = itemMapper.selectById(updateReqVO.getId()).setStatus(updateReqVO.getStatus());
        itemMapper.updateById(itemDO);
    }

    @Override
    public Map<Integer, Long> getTabsCount() {
        Map<Integer, Long> counts = Maps.newLinkedHashMapWithExpectedSize(5);
        // 查询销售中的商品数量
        counts.put(ItemPageReqVO.FOR_SALE,
                itemMapper.selectCount(ItemDO::getStatus, ItemStatusEnum.ENABLE.getStatus()));
        // 查询仓库中的商品数量
        counts.put(ItemPageReqVO.IN_WAREHOUSE,
                itemMapper.selectCount(ItemDO::getStatus, ItemStatusEnum.DISABLE.getStatus()));
        // 查询售空的商品数量
        counts.put(ItemPageReqVO.SOLD_OUT,
                itemMapper.selectCount(ItemDO::getStock, 0));
        // 查询触发警戒库存的商品数量
        counts.put(ItemPageReqVO.ALERT_STOCK,
                itemMapper.selectCount());
        // 查询回收站中的商品数量
        counts.put(ItemPageReqVO.RECYCLE_BIN,
                itemMapper.selectCount(ItemDO::getStatus, ItemStatusEnum.RECYCLE.getStatus()));
        return counts;
    }

    @Override
    public Long getItemCountByTokenId(Long tokenId) {
        return itemMapper.selectCount(ItemDO::getTokenId, tokenId);
    }

}
