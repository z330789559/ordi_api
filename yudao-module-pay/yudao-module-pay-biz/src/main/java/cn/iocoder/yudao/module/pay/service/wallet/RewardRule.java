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

  public static BigDecimal getRewardRate(Integer level){
     switch (level){
         case 0:
             return new BigDecimal("0.1");
         case 1:
             return new BigDecimal("0.15");
         case 2:
             return new BigDecimal("0.2");
         case 3:
             return new BigDecimal("0.25");
         case 4:
             return new BigDecimal("0.3");
         default:
             return new BigDecimal("0.1");
     }
  }
}
