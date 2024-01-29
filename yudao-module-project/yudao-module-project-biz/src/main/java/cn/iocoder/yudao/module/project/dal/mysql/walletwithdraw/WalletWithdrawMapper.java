package cn.iocoder.yudao.module.project.dal.mysql.walletwithdraw;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.project.dal.dataobject.walletwithdraw.WalletWithdrawDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.project.controller.admin.walletwithdraw.vo.*;

/**
 * 提现 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface WalletWithdrawMapper extends BaseMapperX<WalletWithdrawDO> {

    default PageResult<WalletWithdrawDO> selectPage(WalletWithdrawPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WalletWithdrawDO>()
                .eqIfPresent(WalletWithdrawDO::getWalletId, reqVO.getWalletId())
                .eqIfPresent(WalletWithdrawDO::getUserId, reqVO.getUserId())
                .eqIfPresent(WalletWithdrawDO::getType, reqVO.getType())
                .eqIfPresent(WalletWithdrawDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(WalletWithdrawDO::getAuditTime, reqVO.getAuditTime())
                .betweenIfPresent(WalletWithdrawDO::getExpireTime, reqVO.getExpireTime())
                .betweenIfPresent(WalletWithdrawDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WalletWithdrawDO::getId));
    }

}