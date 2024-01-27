package cn.iocoder.yudao.module.member.convert.web3;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Web3UserConvert {

    Web3UserConvert INSTANCE = Mappers.getMapper(Web3UserConvert.class);

}
