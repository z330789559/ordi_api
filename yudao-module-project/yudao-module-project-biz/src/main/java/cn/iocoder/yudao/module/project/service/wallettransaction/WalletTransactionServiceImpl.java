package cn.iocoder.yudao.module.project.service.wallettransaction;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.project.controller.admin.wallettransaction.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.wallettransaction.WalletTransactionAddressDO;
import cn.iocoder.yudao.module.project.dal.dataobject.wallettransaction.WalletTransactionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.project.dal.mysql.wallettransaction.WalletTransactionMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.project.enums.ErrorCodeConstants.*;

/**
 * 钱包流水 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class WalletTransactionServiceImpl implements WalletTransactionService {

    @Resource
    private WalletTransactionMapper walletTransactionMapper;

    @Override
    public Long createWalletTransaction(WalletTransactionSaveReqVO createReqVO) {
        // 插入
        WalletTransactionDO walletTransaction = BeanUtils.toBean(createReqVO, WalletTransactionDO.class);
        walletTransactionMapper.insert(walletTransaction);
        // 返回
        return walletTransaction.getId();
    }

    @Override
    public void updateWalletTransaction(WalletTransactionSaveReqVO updateReqVO) {
        // 校验存在
        validateWalletTransactionExists(updateReqVO.getId());
        // 更新
        WalletTransactionDO updateObj = BeanUtils.toBean(updateReqVO, WalletTransactionDO.class);
        walletTransactionMapper.updateById(updateObj);
    }

    @Override
    public void deleteWalletTransaction(Long id) {
        // 校验存在
        validateWalletTransactionExists(id);
        // 删除
        walletTransactionMapper.deleteById(id);
    }

    private void validateWalletTransactionExists(Long id) {
        if (walletTransactionMapper.selectById(id) == null) {
            throw exception(WALLET_TRANSACTION_NOT_EXISTS);
        }
    }

    @Override
    public WalletTransactionDO getWalletTransaction(Long id) {
        return walletTransactionMapper.selectById(id);
    }

    @Override
    public PageResult<WalletTransactionAddressDO> getWalletTransactionPage(WalletTransactionPageReqVO pageReqVO) {
        return walletTransactionMapper.selectPage(pageReqVO);
    }

}