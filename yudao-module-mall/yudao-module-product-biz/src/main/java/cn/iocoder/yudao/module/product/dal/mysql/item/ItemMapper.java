package cn.iocoder.yudao.module.product.dal.mysql.item;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.product.controller.app.item.vo.ItemPageReqVO;
import cn.iocoder.yudao.module.product.controller.app.item.vo.AppItemPageReqVO;
import cn.iocoder.yudao.module.product.dal.dataobject.item.ItemDO;
import cn.iocoder.yudao.module.product.enums.item.ItemStatusEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mapper
public interface ItemMapper extends BaseMapperX<ItemDO> {

    /**
     * 获取商品分页列表数据
     *
     * @param reqVO 分页请求参数
     * @return 商品分页列表数据
     */
    default PageResult<ItemDO> selectPage(ItemPageReqVO reqVO) {
        LambdaQueryWrapperX<ItemDO> queryWrapper = new LambdaQueryWrapperX<ItemDO>()
                .likeIfPresent(ItemDO::getName, reqVO.getName())
                .eqIfPresent(ItemDO::getTokenId, reqVO.getTokenId())
                .betweenIfPresent(ItemDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ItemDO::getSort);
        return selectPage(reqVO, queryWrapper);
    }

    /**
     * 查询触发警戒库存的 数量
     *
     * @return 触发警戒库存的 数量
     */
    default Long selectCount() {
        LambdaQueryWrapperX<ItemDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.notIn(ItemDO::getStatus, ItemStatusEnum.RECYCLE.getStatus());
        return selectCount(queryWrapper);
    }

    /**
     * 获得商品 分页，提供给用户 App 使用
     */
    default PageResult<ItemDO> selectPage(AppItemPageReqVO pageReqVO, Set<Long> categoryIds) {
        LambdaQueryWrapperX<ItemDO> query = new LambdaQueryWrapperX<ItemDO>()
                .eqIfPresent(ItemDO::getUserId, pageReqVO.getUserId())
                .eqIfPresent(ItemDO::getStatus, pageReqVO.getStatus())
                // 关键字匹配，目前只匹配商品名
                .likeIfPresent(ItemDO::getName, pageReqVO.getKeyword())
                // 分类
                .inIfPresent(ItemDO::getTokenId, categoryIds);
        // 上架状态 且有库存
        query.gt(ItemDO::getStock, 0);
        // 状态在控制器层处理
//        query.eq(ItemDO::getStatus, ItemStatusEnum.ENABLE.getStatus());
        // 推荐类型的过滤条件
        if (ObjUtil.equal(pageReqVO.getRecommendType(), AppItemPageReqVO.RECOMMEND_TYPE_HOT)) {
            query.eq(ItemDO::getRecommendHot, true);
        }

        // 排序逻辑
        if (Objects.equals(pageReqVO.getSortField(), AppItemPageReqVO.SORT_FIELD_SALES_COUNT)) {
            query.last(String.format(" ORDER BY (sales_count + virtual_sales_count) %s, sort DESC, id DESC",
                    pageReqVO.getSortAsc() ? "ASC" : "DESC"));
        } else if (Objects.equals(pageReqVO.getSortField(), AppItemPageReqVO.SORT_FIELD_PRICE)) {
            query.orderBy(true, pageReqVO.getSortAsc(), ItemDO::getPrice)
                    .orderByDesc(ItemDO::getSort).orderByDesc(ItemDO::getId);
        } else {
            query.orderByAsc(ItemDO::getPrice);
//            query.orderByDesc(ItemDO::getSort).orderByDesc(ItemDO::getId);
        }
        return selectPage(pageReqVO, query);
    }

    default List<ItemDO> selectListByRecommendType(String recommendType, Integer count) {
        QueryWrapperX<ItemDO> query = new QueryWrapperX<>();
        // 上架状态 且有库存
        query.eq("status", ItemStatusEnum.ENABLE.getStatus()).gt("stock", 0);
        // 推荐类型的过滤条件
        if (ObjUtil.equal(recommendType, AppItemPageReqVO.RECOMMEND_TYPE_HOT)) {
            query.eq("recommend_hot", true);
        }
        // 设置最大长度
        query.limitN(count);
        return selectList(query);
    }

    /**
     * 更新 SKU 库存（增加）
     *
     * @param id        编号
     * @param incrCount 增加库存（正数）
     */
    default void updateStockIncr(Long id, Integer incrCount) {
        Assert.isTrue(incrCount > 0);
        LambdaUpdateWrapper<ItemDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<ItemDO>()
                .setSql(" stock = stock + " + incrCount)
                .eq(ItemDO::getId, id);
        update(null, lambdaUpdateWrapper);
    }

    /**
     * 更新 SKU 库存（减少）
     *
     * @param id        编号
     * @param incrCount 减少库存（负数）
     * @return 更新条数
     */
    default int updateStockDecr(Long id, Integer incrCount) {
        Assert.isTrue(incrCount < 0);
        LambdaUpdateWrapper<ItemDO> updateWrapper = new LambdaUpdateWrapper<ItemDO>()
                .setSql(" stock = stock + " + incrCount) // 负数，所以使用 + 号
                .eq(ItemDO::getId, id)
                .ge(ItemDO::getStock, -incrCount); // cas 逻辑
        return update(null, updateWrapper);
    }

}
