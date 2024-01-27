package cn.iocoder.yudao.module.trade.enums.order;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 交易订单项 - 售后状态
 *
 * @author 芋道源码
 */
@RequiredArgsConstructor
@Getter
public enum TradeOrderItemStatusEnum implements IntArrayValuable {

    ON_SALE(1, "出售中"),
    SOLD(2, "售出"),
    PURCHASE(3, "购买"),
    OFF_SALE(4, "下架");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(TradeOrderItemStatusEnum::getStatus).toArray();

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
