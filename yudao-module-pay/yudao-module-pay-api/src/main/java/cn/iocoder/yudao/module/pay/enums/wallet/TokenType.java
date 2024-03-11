package cn.iocoder.yudao.module.pay.enums.wallet;

import java.util.Arrays;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
	@JsonValue
	private final Integer type;
	/**
	 * 说明
	 */
	private final String description;


	@JsonCreator
	public static TokenType fromType(Integer type) {
		for (TokenType value : values()) {
			if (value.getType().equals(type)) {
				return value;
			}
		}
		return null;
	}

	public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(TokenType::getType).toArray();
	@Override
	public int[] array() {
		return ARRAYS;
	}
}
