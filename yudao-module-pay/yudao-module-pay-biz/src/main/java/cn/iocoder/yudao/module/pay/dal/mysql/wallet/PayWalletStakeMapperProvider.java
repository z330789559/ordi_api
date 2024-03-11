package cn.iocoder.yudao.module.pay.dal.mysql.wallet;

import java.util.List;

import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletStakeDO;
import com.baomidou.mybatisplus.core.toolkit.MybatisBatchUtils;
import org.apache.ibatis.annotations.Param;

/**
 * PayWalletStakeMapperProvider
 *
 * @author libaozhong
 * @version 2024-03-02
 **/

public class PayWalletStakeMapperProvider {

    //手动开启session 批量更新 然后提交
    public void batchUpdateStakes(@Param("stakes") List<PayWalletStakeDO> stakes) {
        String sql="UPDATE pay_wallet_stake SET  refund_total_price =refund_total_price + #{refundTotalPrice}, refund_bonus_price = refund_bonus_price + #{refundBonusPrice} where id = #{id}";



    }
}
