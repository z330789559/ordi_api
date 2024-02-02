package cn.iocoder.yudao.module.project.dal.dataobject.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * WalletMemberDO
 *
 * @author libaozhong
 * @version 2024-02-01
 **/

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class WalletMemberDO extends WalletDO{

  private String address;
}
