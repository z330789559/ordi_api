package cn.iocoder.yudao.module.project.dal.dataobject.tradeorder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * AdminTradeOrderPageDo
 *
 * @author libaozhong
 * @version 2024-02-02
 **/

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AdminTradeOrderPageDO extends AdminTradeOrderDO{

    private String address;
}
