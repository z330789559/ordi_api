package cn.iocoder.yudao.module.project.service.wallet;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.project.controller.admin.wallet.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.wallet.WalletDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 钱包 Service 接口
 *
 * @author 芋道源码
 */
public interface WalletService {

    /**
     * 创建钱包
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWallet(@Valid WalletSaveReqVO createReqVO);

    /**
     * 更新钱包
     *
     * @param updateReqVO 更新信息
     */
    void updateWallet(@Valid WalletSaveReqVO updateReqVO);

    /**
     * 删除钱包
     *
     * @param id 编号
     */
    void deleteWallet(Long id);

    /**
     * 获得钱包
     *
     * @param id 编号
     * @return 钱包
     */
    WalletDO getWallet(Long id);

    /**
     * 获得钱包分页
     *
     * @param pageReqVO 分页查询
     * @return 钱包分页
     */
    PageResult<WalletDO> getWalletPage(WalletPageReqVO pageReqVO);

}