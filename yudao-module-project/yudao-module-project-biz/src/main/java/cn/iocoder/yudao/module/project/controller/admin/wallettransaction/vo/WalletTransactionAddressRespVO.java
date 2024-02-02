package cn.iocoder.yudao.module.project.controller.admin.wallettransaction.vo;

import lombok.Data;

/**
 * WalletTransactionAddressRespVO
 *
 * @author libaozhong
 * @version 2024-02-02
 **/

@Data
public class WalletTransactionAddressRespVO extends WalletTransactionRespVO{
  private String address;
}
