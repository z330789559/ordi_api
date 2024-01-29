package cn.iocoder.yudao.module.project.service.walletwithdraw;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.project.controller.admin.walletwithdraw.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.walletwithdraw.WalletWithdrawDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.project.dal.mysql.walletwithdraw.WalletWithdrawMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.project.enums.ErrorCodeConstants.*;

/**
 * 提现 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class WalletWithdrawServiceImpl implements WalletWithdrawService {

    @Resource
    private WalletWithdrawMapper walletWithdrawMapper;

    @Override
    public Long createWalletWithdraw(WalletWithdrawSaveReqVO createReqVO) {
        // 插入
        WalletWithdrawDO walletWithdraw = BeanUtils.toBean(createReqVO, WalletWithdrawDO.class);
        walletWithdrawMapper.insert(walletWithdraw);
        // 返回
        return walletWithdraw.getId();
    }

    @Override
    public void updateWalletWithdraw(WalletWithdrawSaveReqVO updateReqVO) {
        // 校验存在
        validateWalletWithdrawExists(updateReqVO.getId());
        // 更新
        WalletWithdrawDO updateObj = BeanUtils.toBean(updateReqVO, WalletWithdrawDO.class);
        walletWithdrawMapper.updateById(updateObj);
    }

    @Override
    public void deleteWalletWithdraw(Long id) {
        // 校验存在
        validateWalletWithdrawExists(id);
        // 删除
        walletWithdrawMapper.deleteById(id);
    }

    private void validateWalletWithdrawExists(Long id) {
        if (walletWithdrawMapper.selectById(id) == null) {
            throw exception(WALLET_WITHDRAW_NOT_EXISTS);
        }
    }

    @Override
    public WalletWithdrawDO getWalletWithdraw(Long id) {
        return walletWithdrawMapper.selectById(id);
    }

    @Override
    public PageResult<WalletWithdrawDO> getWalletWithdrawPage(WalletWithdrawPageReqVO pageReqVO) {
        return walletWithdrawMapper.selectPage(pageReqVO);
    }

}