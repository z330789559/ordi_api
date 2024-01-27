package cn.iocoder.yudao.module.product.api.token;

import cn.iocoder.yudao.module.product.api.item.dto.ItemRespDTO;
import cn.iocoder.yudao.module.product.api.item.dto.TokenRespDTO;
import cn.iocoder.yudao.module.product.convert.item.ItemConvert;
import cn.iocoder.yudao.module.product.convert.token.TokenConvert;
import cn.iocoder.yudao.module.product.service.token.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 商品分类 API 接口实现类
 *
 * @author owen
 */
@Service
@Validated
public class TokenApiImpl implements TokenApi {

    @Resource
    private TokenService tokenService;

    @Override
    public TokenRespDTO getToken(Long id) {
        return TokenConvert.INSTANCE.convertDTO(tokenService.getToken(id));
    }
}
