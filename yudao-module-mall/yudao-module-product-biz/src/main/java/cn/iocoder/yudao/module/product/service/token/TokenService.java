package cn.iocoder.yudao.module.product.service.token;

import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenCreateReqVO;
import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenListReqVO;
import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenUpdateReqVO;
import cn.iocoder.yudao.module.product.controller.app.token.vo.AppTokenRespVO;
import cn.iocoder.yudao.module.product.dal.dataobject.token.TokenDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 商品分类 Service 接口
 *
 * @author 芋道源码
 */
public interface TokenService {

    /**
     * 创建商品分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createToken(@Valid TokenCreateReqVO createReqVO);

    /**
     * 更新商品分类
     *
     * @param updateReqVO 更新信息
     */
    void updateToken(@Valid TokenUpdateReqVO updateReqVO);

    /**
     * 删除商品分类
     *
     * @param id 编号
     */
    void deleteToken(Long id);

    /**
     * 获得商品分类
     *
     * @param id 编号
     * @return 商品分类
     */
    TokenDO getToken(Long id);

    /**
     * 校验商品分类
     *
     * @param id 分类编号
     */
    TokenDO validateToken(Long id);

    /**
     * 获得商品分类的层级
     *
     * @param id 编号
     * @return 商品分类的层级
     */
    Integer getTokenLevel(Long id);

    /**
     * 获得商品分类列表
     *
     * @param listReqVO 查询条件
     * @return 商品分类列表
     */
    List<TokenDO> getEnableTokenList(TokenListReqVO listReqVO);

    /**
     * 获得开启状态的商品分类列表
     *
     * @return 商品分类列表
     */
    List<TokenDO> getEnableTokenList();

    TokenDO getEnableToken(String name);

    /**
     * 校验商品分类是否有效。如下情况，视为无效：
     * 1. 商品分类编号不存在
     * 2. 商品分类被禁用
     *
     * @param ids 商品分类编号数组
     */
    void validateTokenList(Collection<Long> ids);

    void scanTokenPrice();

    void scanChainTokenPrice();

    AppTokenRespVO selectVolume();
}
