package cn.iocoder.yudao.module.project.dal.dataobject.wallettransaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * WalletTransactionAddressDO
 *
 * @author libaozhong
 * @version 2024-02-02
 **/

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionAddressDO extends WalletTransactionDO{
  private String address;
}
