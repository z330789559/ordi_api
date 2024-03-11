package cn.iocoder.yudao.module.pay.enums.wallet;

import java.util.Arrays;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * StakeStatusEnum
 *
 * @author libaozhong
 * @version 2024-02-27
 **/
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StakeStatusEnum implements IntArrayValuable {
	/**
	 * 未开始
	 */
	UnPay(1, "未支付"),
	/**
	 * 已支付
	 */
	Pay(2, "已支付"),
	/**
	 * unstake
	 *
	 */
	UnStake(3, "解质押"),
	;
	private int code;

	private String name;

	StakeStatusEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public StakeStatusEnum getValue() {
		return UnPay;
	}

	@JsonValue
	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}


	public StakeStatusEnum getSelf(int code) {
		for (StakeStatusEnum value : StakeStatusEnum.values()) {
			if (value.getCode() == code) {
				return value;
			}
		}
		return null;

	}

	@JsonCreator
	public static StakeStatusEnum getEnum(int code) {
		for (StakeStatusEnum value : StakeStatusEnum.values()) {
			if (value.getCode() == code) {
				return value;
			}
		}
		return null;
	}

	public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(StakeStatusEnum::getCode).toArray();

	@Override
	public int[] array() {
		return ARRAYS;
	}
}
