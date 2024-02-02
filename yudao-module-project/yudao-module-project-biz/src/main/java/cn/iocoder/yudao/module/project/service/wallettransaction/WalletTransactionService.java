package cn.iocoder.yudao.module.project.service.wallettransaction;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.project.controller.admin.wallettransaction.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.wallettransaction.WalletTransactionAddressDO;
import cn.iocoder.yudao.module.project.dal.dataobject.wallettransaction.WalletTransactionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 钱包流水 Service 接口
 *
 * @author 芋道源码
 */
public interface WalletTransactionService {

    /**
     * 创建钱包流水
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWalletTransaction(@Valid WalletTransactionSaveReqVO createReqVO);

    /**
     * 更新钱包流水
     *
     * @param updateReqVO 更新信息
     */
    void updateWalletTransaction(@Valid WalletTransactionSaveReqVO updateReqVO);

    /**
     * 删除钱包流水
     *
     * @param id 编号
     */
    void deleteWalletTransaction(Long id);

    /**
     * 获得钱包流水
     *
     * @param id 编号
     * @return 钱包流水
     */
    WalletTransactionDO getWalletTransaction(Long id);

    /**
     * 获得钱包流水分页
     *
     * @param pageReqVO 分页查询
     * @return 钱包流水分页
     */
    PageResult<WalletTransactionAddressDO> getWalletTransactionPage(WalletTransactionPageReqVO pageReqVO);

}