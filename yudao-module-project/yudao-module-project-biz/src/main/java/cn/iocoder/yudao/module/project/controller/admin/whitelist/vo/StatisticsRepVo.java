package cn.iocoder.yudao.module.project.controller.admin.whitelist.vo;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * StatisticsRepVo
 *
 * @author libaozhong
 * @version 2024-02-04
 **/

@Data
@ToString(callSuper = true)
public class StatisticsRepVo {


  private BigDecimal shelveOrdinasNum;

  private BigDecimal btcTotalTvl;

  private BigDecimal ordinasTvl;
}
