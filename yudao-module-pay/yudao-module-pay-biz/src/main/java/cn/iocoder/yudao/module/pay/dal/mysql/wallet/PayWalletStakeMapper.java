package cn.iocoder.yudao.module.pay.dal.mysql.wallet;

import java.math.BigDecimal;
import java.util.List;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletStakeDO;
import com.baomidou.mybatisplus.core.batch.BatchMethod;
import com.baomidou.mybatisplus.core.toolkit.MybatisBatchUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * PayWalletStakeMapper
 *
 * @author libaozhong
 * @version 2024-02-27
 **/

@Mapper
public interface PayWalletStakeMapper extends BaseMapperX<PayWalletStakeDO> {

	@Select("SELECT SUM(total_price) as totalPrice, SUM(bonus_price) as bonusPrice,SUM(pay_price) as payPrice, SUM(refund_bonus_price) as refundBonusPrice FROM pay_wallet_stake WHERE user_id = #{loginUserId} and stake_status=2"  )
	PayWalletStakeDO sumStake(Long loginUserId);



	default void batchUpdateStakes(SqlSessionFactory sqlSessionFactory,List<PayWalletStakeDO> stakes){
		BatchMethod<PayWalletStakeDO> bm = new BatchMethod<PayWalletStakeDO>("updateStakeReward");
	     MybatisBatchUtils.execute(sqlSessionFactory, stakes, bm );
	};



	@Update("UPDATE pay_wallet_stake SET  refund_bonus_price = refund_bonus_price + #{refundBonusPrice} where id = #{id}")
	void updateStakeReward(@Param("refundBonusPrice") BigDecimal refundBonusPrice, @Param("id") Long id);


}
