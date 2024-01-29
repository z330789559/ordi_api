package cn.iocoder.yudao.module.product.controller.app.token;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.product.controller.app.token.vo.AppTokenRespVO;
import cn.iocoder.yudao.module.product.convert.token.TokenConvert;
import cn.iocoder.yudao.module.product.dal.dataobject.token.TokenDO;
import cn.iocoder.yudao.module.product.service.token.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.Comparator;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 商品分类")
@RestController
@RequestMapping("/product/token")
@Validated
public class AppTokenController {

    @Resource
    private TokenService tokenService;

    @GetMapping("/list")
    @Operation(summary = "获得币种列表")
    public CommonResult<List<AppTokenRespVO>> getTokenList() {
        List<TokenDO> list = tokenService.getEnableTokenList();
        list.sort(Comparator.comparing(TokenDO::getSort));
        AppTokenRespVO appTokenRespVO = tokenService.selectVolume();
        TokenDO btc = tokenService.getToken(2L);
        for (TokenDO token : list) {
            if (token.getId() == 1L){
                token.setVolume(appTokenRespVO.getVolume().multiply(btc.getPrice()).add(token.getVolume()));
                token.setVolume24h(appTokenRespVO.getVolume24h().multiply(btc.getPrice()).add(token.getVolume24h()));
                token.setMarketCap(tokenService.getPastDayLowestShelvesPrice());
            }
        }
        return success(TokenConvert.INSTANCE.convertList03(list));
    }

    @GetMapping("/get")
    @Operation(summary = "获得币种")
    @PermitAll
    public CommonResult<AppTokenRespVO> getToken() {
        TokenDO data = tokenService.getEnableToken("btc");
        return success(TokenConvert.INSTANCE.convert02(data));
    }
}
