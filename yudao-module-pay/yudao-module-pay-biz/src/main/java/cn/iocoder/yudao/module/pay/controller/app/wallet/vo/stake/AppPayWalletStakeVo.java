package cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake;

import java.math.BigDecimal;

import cn.iocoder.yudao.module.pay.enums.wallet.StakeStatusEnum;
import lombok.Data;

/**
 * PayWalletStakeVo
 *
 * @author libaozhong
 * @version 2024-02-27
 **/

@Data
public class AppPayWalletStakeVo {

	private Long id;
	private Long walletId;
	private String address;
	private BigDecimal totalPrice;
	private BigDecimal payPrice;
	private BigDecimal bonusPrice;
	private Long packageId;

	/**
	 * @link {{@link StakeStatusEnum}}
	 */
	private Integer stakeStatus;
	private Long payOrderId;
	private String payTime;
	private Long payRefundId;
	private BigDecimal refundTotalPrice;
	private BigDecimal refundPayPrice;
	private BigDecimal refundBonusPrice;
	private String refundTime;
}
