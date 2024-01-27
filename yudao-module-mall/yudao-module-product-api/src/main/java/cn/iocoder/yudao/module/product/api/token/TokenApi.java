package cn.iocoder.yudao.module.product.api.token;

import cn.iocoder.yudao.module.product.api.item.dto.TokenRespDTO;

public interface TokenApi {

    TokenRespDTO getToken(Long id);
}
