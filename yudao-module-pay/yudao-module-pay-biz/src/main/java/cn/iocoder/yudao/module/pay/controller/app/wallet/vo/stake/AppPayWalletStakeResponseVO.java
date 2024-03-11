package cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AppPayWalletStakeResponseVO
 *
 * @author libaozhong
 * @version 2024-02-27
 **/
@Schema(description = "用户 App - 创建质押 response VO")
@Data
@AllArgsConstructor
public class AppPayWalletStakeResponseVO {
	private Long id;
}
