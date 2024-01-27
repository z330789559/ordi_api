package cn.iocoder.yudao.module.product.service.token;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenCreateReqVO;
import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenListReqVO;
import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenUpdateReqVO;
import cn.iocoder.yudao.module.product.controller.app.token.vo.AppTokenRespVO;
import cn.iocoder.yudao.module.product.controller.app.token.vo.TokenPriceScanRespVO;
import cn.iocoder.yudao.module.product.convert.token.TokenConvert;
import cn.iocoder.yudao.module.product.dal.dataobject.item.ItemDO;
import cn.iocoder.yudao.module.product.dal.dataobject.token.TokenDO;
import cn.iocoder.yudao.module.product.dal.mysql.token.TokenMapper;
import cn.iocoder.yudao.module.product.service.item.ItemService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.product.dal.dataobject.token.TokenDO.PARENT_ID_NULL;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.*;

/**
 * 商品分类 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class TokenServiceImpl implements TokenService {

    @Resource
    private TokenMapper tokenMapper;
    @Resource
    @Lazy // 循环依赖，避免报错
    private ItemService itemService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Long createToken(TokenCreateReqVO createReqVO) {
        // 校验父分类存在
        validateParentProductToken(createReqVO.getParentId());

        // 插入
        TokenDO token = TokenConvert.INSTANCE.convert(createReqVO);
        tokenMapper.insert(token);
        // 返回
        return token.getId();
    }

    @Override
    public void updateToken(TokenUpdateReqVO updateReqVO) {
        // 校验分类是否存在
        validateProductTokenExists(updateReqVO.getId());
        // 校验父分类存在
        validateParentProductToken(updateReqVO.getParentId());

        // 更新
        TokenDO updateObj = TokenConvert.INSTANCE.convert(updateReqVO);
        tokenMapper.updateById(updateObj);
    }

    @Override
    public void deleteToken(Long id) {
        // 校验分类是否存在
        validateProductTokenExists(id);
        // 校验是否还有子分类
        if (tokenMapper.selectCountByParentId(id) > 0) {
            throw exception(TOKEN_EXISTS_CHILDREN);
        }
        // 校验分类是否绑定了 SPU
        Long spuCount = itemService.getItemCountByTokenId(id);
        if (spuCount > 0) {
            throw exception(TOKEN_HAVE_BIND_ITEM);
        }
        // 删除
        tokenMapper.deleteById(id);
    }

    private void validateParentProductToken(Long id) {
        // 如果是根分类，无需验证
        if (Objects.equals(id, PARENT_ID_NULL)) {
            return;
        }
        // 父分类不存在
        TokenDO token = tokenMapper.selectById(id);
        if (token == null) {
            throw exception(TOKEN_PARENT_NOT_EXISTS);
        }
        // 父分类不能是二级分类
        if (!Objects.equals(token.getParentId(), PARENT_ID_NULL)) {
            throw exception(TOKEN_PARENT_NOT_FIRST_LEVEL);
        }
    }

    private void validateProductTokenExists(Long id) {
        TokenDO token = tokenMapper.selectById(id);
        if (token == null) {
            throw exception(TOKEN_NOT_EXISTS);
        }
    }

    @Override
    public void validateTokenList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得商品分类信息
        List<TokenDO> list = tokenMapper.selectBatchIds(ids);
        Map<Long, TokenDO> tokenMap = CollectionUtils.convertMap(list, TokenDO::getId);
        // 校验
        ids.forEach(id -> {
            TokenDO token = tokenMap.get(id);
            if (token == null) {
                throw exception(TOKEN_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(token.getStatus())) {
                throw exception(TOKEN_DISABLED, token.getName());
            }
        });
    }

    @Override
    public TokenDO getToken(Long id) {
        return tokenMapper.selectById(id);
    }

    @Override
    public TokenDO validateToken(Long id) {
        TokenDO token = tokenMapper.selectById(id);
        if (token == null) {
            throw exception(TOKEN_NOT_EXISTS);
        }
        if (Objects.equals(token.getStatus(), CommonStatusEnum.DISABLE.getStatus())) {
            throw exception(TOKEN_DISABLED, token.getName());
        }
        return token;
    }

    @Override
    public Integer getTokenLevel(Long id) {
        if (Objects.equals(id, PARENT_ID_NULL)) {
            return 0;
        }
        int level = 1;
        // for 的原因，是因为避免脏数据，导致可能的死循环。一般不会超过 100 层哈
        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            // 如果没有父节点，break 结束
            TokenDO token = tokenMapper.selectById(id);
            if (token == null
                    || Objects.equals(token.getParentId(), PARENT_ID_NULL)) {
                break;
            }
            // 继续递归父节点
            level++;
            id = token.getParentId();
        }
        return level;
    }

    @Override
    public List<TokenDO> getEnableTokenList(TokenListReqVO listReqVO) {
        return tokenMapper.selectList(listReqVO);
    }

    @Override
    public List<TokenDO> getEnableTokenList() {
        return tokenMapper.selectListByStatus(CommonStatusEnum.ENABLE.getStatus());
    }

    @Override
    @TenantIgnore // 暂时忽略tenant_id
    public TokenDO getEnableToken(String name) {
        return tokenMapper.selectByStatusAndName(CommonStatusEnum.ENABLE.getStatus(), name);
    }

    @Override
    @TenantIgnore
    public void scanChainTokenPrice() {
        try {
            String url = "https://www.okx.com/api/v5/market/index-tickers?instId=BTC-USDT";
            String body = HttpRequest.get(url).execute().body();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            TokenPriceScanRespVO result = objectMapper.readValue(body, TokenPriceScanRespVO.class);
            if (result != null && result.getData().size() > 0) {
                TokenPriceScanRespVO.DataItem item = result.getData().get(0);
                // 涨跌幅。。。 暂时为 24小时指数开盘价格和当前价对比
                BigDecimal idPx = new BigDecimal(item.getIdxPx());
                BigDecimal open24h = new BigDecimal(item.getOpen24h());

                BigDecimal changeRate = (idPx.subtract(open24h)).divide(open24h, 6, RoundingMode.UP);

                BigDecimal rate = BigDecimal.ONE.divide(idPx, 10, RoundingMode.UP);

                tokenMapper.update(new LambdaUpdateWrapper<TokenDO>()
                        .set(TokenDO::getPrice, rate)
                        .set(TokenDO::getUsdPrice, rate)
                        .set(TokenDO::getMarketCap, idPx)
                        .set(TokenDO::getChangeRate, changeRate)
                        .eq(TokenDO::getName, "BTC"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AppTokenRespVO selectVolume() {
        return tokenMapper.selectVolume();
    }

    @Override
    @TenantIgnore
    public void scanTokenPrice() {

        String key = "product_token:price:NUBT";
        LocalTime currentTime = LocalTime.now();

        ItemDO item = itemService.getLowerPriceEnableItem();
        if (item == null) {
            return;
        }

        // 定时任务10秒一次，每天第一次保存redis
        boolean isFirstTime = isBeforeSeconds(currentTime, 20);
        if (isFirstTime) {
            stringRedisTemplate.opsForValue().set(key, item.getPrice().toString());
        }

        String CachePrice = stringRedisTemplate.opsForValue().get(key);
        if (CachePrice == null) {
            CachePrice = item.getPrice().toString();
            stringRedisTemplate.opsForValue().set(key, CachePrice);
        }


        BigDecimal openPrice = new BigDecimal(CachePrice);

        BigDecimal changeRate = (item.getPrice().subtract(openPrice)).divide(openPrice, 6, RoundingMode.UP);

        BigDecimal rate = BigDecimal.ONE.divide(item.getPrice(), 10, RoundingMode.UP);

        tokenMapper.update(new LambdaUpdateWrapper<TokenDO>()
                .set(TokenDO::getPrice, rate)
                .set(TokenDO::getUsdPrice, rate)
                .set(TokenDO::getChangeRate, changeRate)
                .set(TokenDO::getMarketCap, item.getPrice())
                .eq(TokenDO::getId,1));
    }

    private static boolean isBeforeSeconds(LocalTime currentTime, int second) {
        // 设置目标时间为每天的前20秒
        LocalTime targetTime = LocalTime.of(0, 0, second);
        // 判断当前时分秒是否在目标时间之前
        return currentTime.isBefore(targetTime);
    }

}
