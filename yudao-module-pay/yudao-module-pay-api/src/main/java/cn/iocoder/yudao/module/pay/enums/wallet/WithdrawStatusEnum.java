package cn.iocoder.yudao.module.pay.enums.wallet;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum  WithdrawStatusEnum implements IntArrayValuable {
    AUDITING(0, "审核中"),
    AUDIT_SUCCESS(10, "审核通过"),
    WITHDRAW_SUCCESS(11, "提现成功"),
    AUDIT_FAIL(20, "审核不通过"),
    WITHDRAW_FAIL(21, "提现失败"),
    ;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(WithdrawStatusEnum::getStatus).toArray();

    /**
     * 状态
     */
    @JsonValue
    private final Integer status;
    /**
     * 名字
     */
    private final String name;


    @Override
    public int[] array() {
        return ARRAYS;
    }

    @JsonCreator
    public static WithdrawStatusEnum fromStatus(Integer status) {
        for (WithdrawStatusEnum value : values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }
}
