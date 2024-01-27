package cn.iocoder.yudao.module.product.convert.token;

import cn.iocoder.yudao.module.product.api.item.dto.TokenRespDTO;
import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenCreateReqVO;
import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenRespVO;
import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenUpdateReqVO;
import cn.iocoder.yudao.module.product.controller.app.token.vo.AppTokenRespVO;
import cn.iocoder.yudao.module.product.dal.dataobject.token.TokenDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 商品分类 Convert
 */
@Mapper
public interface TokenConvert {

    TokenConvert INSTANCE = Mappers.getMapper(TokenConvert.class);

    TokenDO convert(TokenCreateReqVO bean);

    TokenDO convert(TokenUpdateReqVO bean);

    TokenRespVO convert(TokenDO bean);

    AppTokenRespVO convert02(TokenDO bean);

    TokenRespDTO convertDTO(TokenDO bean);

    List<TokenRespVO> convertList(List<TokenDO> list);

    List<AppTokenRespVO> convertList03(List<TokenDO> list);
}
