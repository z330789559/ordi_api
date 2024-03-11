package cn.iocoder.yudao.module.pay.enums.wallet;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum PayWalletUserTypeEnum  implements IntArrayValuable {
    FINANCE(0, "金融货币"),
    CIRCULATION(1, "流通货币");

    /**
     * 业务分类
     */
    @JsonValue
    private final Integer type;

    @JsonCreator
    public static PayWalletUserTypeEnum fromType(Integer type) {
        for (PayWalletUserTypeEnum value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
    /**
     * 说明
     */
    private final String description;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(PayWalletUserTypeEnum::getType).toArray();
    @Override
    public int[] array() {
        return ARRAYS;
    }
}
