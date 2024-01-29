package cn.iocoder.yudao.module.project.dal.mysql.producttoken;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.project.dal.dataobject.producttoken.ProductTokenDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.project.controller.admin.producttoken.vo.*;

/**
 * 产品列 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductTokenMapper extends BaseMapperX<ProductTokenDO> {

    default PageResult<ProductTokenDO> selectPage(ProductTokenPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductTokenDO>()
                .eqIfPresent(ProductTokenDO::getParentId, reqVO.getParentId())
                .likeIfPresent(ProductTokenDO::getName, reqVO.getName())
                .eqIfPresent(ProductTokenDO::getTicker, reqVO.getTicker())
                .eqIfPresent(ProductTokenDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ProductTokenDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductTokenDO::getId));
    }

}