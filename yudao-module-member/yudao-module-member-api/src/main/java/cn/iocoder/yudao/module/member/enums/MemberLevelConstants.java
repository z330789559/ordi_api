package cn.iocoder.yudao.module.member.enums;

import java.math.BigDecimal;

/**
 * MemberLevelConstants
 *
 * @author libaozhong
 * @version 2024-02-02
 **/
public interface MemberLevelConstants {

    public static final Integer LEVEL_1 = 1;
    public static final Integer LEVEL_2 = 2;
    public static final Integer LEVEL_3 = 3;
    public static final Integer LEVEL_4 = 4;
    public static final Integer LEVEL_5 = 5;

    public static final BigDecimal LEVEL_1_REWARD_RATE = new BigDecimal("0.01");

    public static final BigDecimal LEVEL_2_REWARD_RATE = new BigDecimal("0.15");

    public static final BigDecimal LEVEL_3_REWARD_RATE = new BigDecimal("0.20");

    public static final BigDecimal LEVEL_4_REWARD_RATE = new BigDecimal("0.35");

    public static final BigDecimal LEVEL_5_REWARD_RATE = new BigDecimal("0.30");
}
