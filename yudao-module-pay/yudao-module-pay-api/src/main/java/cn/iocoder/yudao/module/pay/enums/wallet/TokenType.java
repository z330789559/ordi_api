package cn.iocoder.yudao.module.pay.enums.wallet;

import java.util.Arrays;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TokenType
 *
 * @author libaozhong
 * @version 2024-01-29
 **/

@AllArgsConstructor
@Getter
public enum TokenType  implements IntArrayValuable {
	NU2T(1, "NU2T"),
	BTC(2, "BTC"),
	;
	/**
	 * 业务分类
	 */
	private final Integer type;
	/**
	 * 说明
	 */
	private final String description;

	public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(TokenType::getType).toArray();
	@Override
	public int[] array() {
		return ARRAYS;
	}
}
