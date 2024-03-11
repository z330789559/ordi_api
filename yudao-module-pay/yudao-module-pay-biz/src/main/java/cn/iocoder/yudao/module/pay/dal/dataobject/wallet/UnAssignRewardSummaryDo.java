package cn.iocoder.yudao.module.pay.dal.dataobject.wallet;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * UnAssignRewardSummaryDo
 *
 * @author libaozhong
 * @version 2024-02-02
 **/
@Data
public class UnAssignRewardSummaryDo {

  @Schema(description = "总金额",  example = "100")
  private BigDecimal totalAmount = BigDecimal.ZERO;

  @Schema(description = "今日金额", example = "100")
  private BigDecimal todayAmount=BigDecimal.ZERO;

  private BigDecimal upLimit = BigDecimal.ZERO;
}
