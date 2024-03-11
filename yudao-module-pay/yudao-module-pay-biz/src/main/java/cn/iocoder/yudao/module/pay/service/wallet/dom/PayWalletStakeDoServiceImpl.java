package cn.iocoder.yudao.module.pay.service.wallet.dom;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakePageReqVo;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake.AppPayWalletStakeVo;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletStakeDO;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletStakeMapper;
import cn.iocoder.yudao.module.pay.enums.wallet.StakeStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import org.apache.ibatis.session.SqlSessionFactory;

import org.springframework.stereotype.Service;

/**
 * PayWalletStakeDoServiceImpl
 *
 * @author libaozhong
 * @version 2024-02-27
 **/

@Service
public class PayWalletStakeDoServiceImpl  implements PayWalletStakeDoService{

  @Resource
  private PayWalletStakeMapper payWalletStakeMapper;

  @Resource
  private SqlSessionFactory sqlSessionFactory;
  @Override
  public int createStake(PayWalletStakeDO stake) {
    return payWalletStakeMapper.insert(stake);
  }

  @Override
  public PayWalletStakeDO getStake(Long id) {
    return payWalletStakeMapper.selectById(id);
  }

  @Override
  public void updateStake(PayWalletStakeDO stake) {
    stake.setRefundTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss" ));
    payWalletStakeMapper.updateById(stake);
  }


  @TenantIgnore
  @Override
  public PageResult<PayWalletStakeDO> getStakesByPage(Long loginUserId, Integer userType,PageParam pageReqVo) {
    LambdaQueryWrapper<PayWalletStakeDO> queryWrapper = new LambdaQueryWrapperX<PayWalletStakeDO>()
        .eq(PayWalletStakeDO::getUserId, loginUserId)
            .eqIfPresent(PayWalletStakeDO::getUserType, userType)
            .orderByAsc(PayWalletStakeDO::getStakeStatus)
            ;

    return payWalletStakeMapper.selectPage(pageReqVo, queryWrapper);

  }

  @Override
  public PayWalletStakeDO getStakes(Long loginUserId) {

    return payWalletStakeMapper.sumStake(loginUserId);
  }

  @Override
  public Optional<List<PayWalletStakeDO>> getCanRewardStakes() {
   return  Optional.of(payWalletStakeMapper.selectList(new LambdaQueryWrapper<PayWalletStakeDO>()
        .eq(PayWalletStakeDO::getStakeStatus, StakeStatusEnum.Pay.getCode())
    ));
  }

  @Override
  public void updateStakes(List<PayWalletStakeDO> stakes) {

     payWalletStakeMapper.batchUpdateStakes(sqlSessionFactory,stakes);

  }

}
