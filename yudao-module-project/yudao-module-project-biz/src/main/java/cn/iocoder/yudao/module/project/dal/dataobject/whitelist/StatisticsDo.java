package cn.iocoder.yudao.module.project.dal.dataobject.whitelist;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * StatisticsDo
 *
 * @author libaozhong
 * @version 2024-02-04
 **/

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class StatisticsDo {


	private BigDecimal shelveOrdinasNum;

	private BigDecimal btcTotalTvl;

	private BigDecimal ordinasTvl;
}
