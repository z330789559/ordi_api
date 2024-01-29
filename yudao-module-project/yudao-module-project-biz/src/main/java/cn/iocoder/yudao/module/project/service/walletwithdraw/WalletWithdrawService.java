package cn.iocoder.yudao.module.project.service.walletwithdraw;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.project.controller.admin.walletwithdraw.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.walletwithdraw.WalletWithdrawDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 提现 Service 接口
 *
 * @author 芋道源码
 */
public interface WalletWithdrawService {

    /**
     * 创建提现
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWalletWithdraw(@Valid WalletWithdrawSaveReqVO createReqVO);

    /**
     * 更新提现
     *
     * @param updateReqVO 更新信息
     */
    void updateWalletWithdraw(@Valid WalletWithdrawSaveReqVO updateReqVO);

    /**
     * 删除提现
     *
     * @param id 编号
     */
    void deleteWalletWithdraw(Long id);

    /**
     * 获得提现
     *
     * @param id 编号
     * @return 提现
     */
    WalletWithdrawDO getWalletWithdraw(Long id);

    /**
     * 获得提现分页
     *
     * @param pageReqVO 分页查询
     * @return 提现分页
     */
    PageResult<WalletWithdrawDO> getWalletWithdrawPage(WalletWithdrawPageReqVO pageReqVO);

}