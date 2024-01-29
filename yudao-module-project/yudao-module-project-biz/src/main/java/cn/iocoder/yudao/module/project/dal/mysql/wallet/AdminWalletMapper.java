package cn.iocoder.yudao.module.project.dal.mysql.wallet;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.project.dal.dataobject.wallet.WalletDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.project.controller.admin.wallet.vo.*;

/**
 * 钱包 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AdminWalletMapper extends BaseMapperX<WalletDO> {

    default PageResult<WalletDO> selectPage(WalletPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WalletDO>()
                .eqIfPresent(WalletDO::getUserType, reqVO.getUserType())
                .eqIfPresent(WalletDO::getUserId, reqVO.getUserId())
                .eqIfPresent(WalletDO::getTokenId, reqVO.getTokenId())
                .betweenIfPresent(WalletDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WalletDO::getId));
    }

}