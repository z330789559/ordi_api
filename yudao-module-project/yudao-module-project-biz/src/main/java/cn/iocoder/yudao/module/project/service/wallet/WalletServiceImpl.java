package cn.iocoder.yudao.module.project.service.wallet;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.project.controller.admin.wallet.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.wallet.WalletDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.project.dal.mysql.wallet.AdminWalletMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.project.enums.ErrorCodeConstants.*;

/**
 * 钱包 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class WalletServiceImpl implements WalletService {

    @Resource
    private AdminWalletMapper walletMapper;

    @Override
    public Long createWallet(WalletSaveReqVO createReqVO) {
        // 插入
        WalletDO wallet = BeanUtils.toBean(createReqVO, WalletDO.class);
        walletMapper.insert(wallet);
        // 返回
        return wallet.getId();
    }

    @Override
    public void updateWallet(WalletSaveReqVO updateReqVO) {
        // 校验存在
        validateWalletExists(updateReqVO.getId());
        // 更新
        WalletDO updateObj = BeanUtils.toBean(updateReqVO, WalletDO.class);
        walletMapper.updateById(updateObj);
    }

    @Override
    public void deleteWallet(Long id) {
        // 校验存在
        validateWalletExists(id);
        // 删除
        walletMapper.deleteById(id);
    }

    private void validateWalletExists(Long id) {
        if (walletMapper.selectById(id) == null) {
            throw exception(WALLET_NOT_EXISTS);
        }
    }

    @Override
    public WalletDO getWallet(Long id) {
        return walletMapper.selectById(id);
    }

    @Override
    public PageResult<WalletDO> getWalletPage(WalletPageReqVO pageReqVO) {
        return walletMapper.selectPage(pageReqVO);
    }

}