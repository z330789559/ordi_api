package cn.iocoder.yudao.module.pay.service.wallet;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * RewardRule
 *
 * @author libaozhong
 * @version 2024-01-31
 **/

public class RewardRule {

  private static final Map<Integer, BigDecimal> REWARD_RATE_MAP = new HashMap<>();

    static {
        REWARD_RATE_MAP.put(0, new BigDecimal("0.1"));
        REWARD_RATE_MAP.put(1, new BigDecimal("0.15"));
        REWARD_RATE_MAP.put(2, new BigDecimal("0.2"));
        REWARD_RATE_MAP.put(3, new BigDecimal("0.25"));
        REWARD_RATE_MAP.put(4, new BigDecimal("0.3"));
    }

  public static BigDecimal getRewardRate(Integer level){
    if(level < 5){
      return REWARD_RATE_MAP.get(level);
    }else{
        return REWARD_RATE_MAP.get(4);
    }

  }
}
