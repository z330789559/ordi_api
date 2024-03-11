package cn.iocoder.yudao.module.project.enums;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
public  interface  ErrorCodeConstants {

	ErrorCode MEMBER_USERNAME_EXISTS = new ErrorCode(1, "会员用户的用户名已经存在");
	ErrorCode MEMBER_NOT_EXISTS = new ErrorCode(2, "会员用户不存在");
	ErrorCode PRODUCT_TOKEN_NOT_EXISTS = new ErrorCode(3, "产品列不存在");
	ErrorCode WALLET_NOT_EXISTS = new ErrorCode(4, "钱包不存在");

	ErrorCode TRADE_ORDER_NOT_EXISTS = new ErrorCode(5, "交易订单不存在");
	ErrorCode WALLET_WITHDRAW_NOT_EXISTS = new ErrorCode(6, "提现不存在");

	ErrorCode WALLET_TRANSACTION_NOT_EXISTS = new ErrorCode(7, "钱包流水不存在");

	ErrorCode WHITE_LIST_NOT_EXISTS = new ErrorCode(8, "白名单不存在");

}