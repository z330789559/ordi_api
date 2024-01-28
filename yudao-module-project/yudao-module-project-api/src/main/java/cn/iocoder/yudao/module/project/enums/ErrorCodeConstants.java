package cn.iocoder.yudao.module.project.enums;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
public  interface  ErrorCodeConstants {

	ErrorCode MEMBER_USERNAME_EXISTS = new ErrorCode(1, "会员用户的用户名已经存在");
	ErrorCode MEMBER_NOT_EXISTS = new ErrorCode(2, "会员用户不存在");

}