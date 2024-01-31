package cn.iocoder.yudao.module.product.dal.mysql.token;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenListReqVO;
import cn.iocoder.yudao.module.product.controller.app.token.vo.AppTokenRespVO;
import cn.iocoder.yudao.module.product.dal.dataobject.token.TokenDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 币种 Mapper
 */
@Mapper
public interface TokenMapper extends BaseMapperX<TokenDO> {

    default List<TokenDO> selectList(TokenListReqVO listReqVO) {
        return selectList(new LambdaQueryWrapperX<TokenDO>()
                .likeIfPresent(TokenDO::getName, listReqVO.getName())
                .eqIfPresent(TokenDO::getStatus, listReqVO.getStatus())
                .orderByDesc(TokenDO::getId));
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(TokenDO::getParentId, parentId);
    }

    default List<TokenDO> selectListByStatus(Integer status) {
        return selectList(TokenDO::getStatus, status);
    }

    default TokenDO selectByStatusAndName(Integer status,String name) {
       return selectOne(new LambdaQueryWrapper<TokenDO>().eq(TokenDO::getStatus,status).eq(TokenDO::getName,name));
    }




    @Select("select " +
            "ifnull(min(price),0) as lowestPrice " +
            " from ordi_product_item where create_time > date_sub(now(), interval 24 hour)  and status=1 "
    )
    BigDecimal queryPastDayLowestPrice();


    @Select("select ifnull(sum(otoi.price * otoi.quantity),0) as volume," +
            "ifnull(sum(case when pwt.create_time >= date_sub(now(), interval 24 hour) then otoi.price * otoi.quantity else 0 end),0) as volume24h " +
            "from (select biz_id, min(create_time) create_time, biz_type from pay_wallet_transaction where biz_type = 5 group by biz_id,biz_type) pwt " +
            "join ordi_trade_order_item otoi on otoi.order_id = pwt.biz_id ")
    AppTokenRespVO selectVolume();

}
