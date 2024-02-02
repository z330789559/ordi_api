package cn.iocoder.yudao.module.pay.dal.redis.no;

import cn.hutool.core.date.DatePattern;import cn.hutool.core.date.DateUtil;
import cn.iocoder.yudao.module.pay.dal.redis.RedisKeyConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 支付序号的 Redis DAO
 *
 * @author 芋道源码
 */
@Repository
public class PayNoRedisDAO {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成序号
     *
     * @param prefix 前缀
     * @return 序号
     */
    public String generate(String prefix) {
        // 递增序号
        String noPrefix = prefix + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN);
        String key = RedisKeyConstants.PAY_NO + noPrefix;
        Long no = stringRedisTemplate.opsForValue().increment(key);
        // 设置过期时间
        stringRedisTemplate.expire(key, Duration.ofMinutes(1L));
        return noPrefix + no;
    }

    public String getRewardLimit(){
        String rate = stringRedisTemplate.opsForValue().get(RedisKeyConstants.REWARD_LIMIT);
        if(rate == null){
            stringRedisTemplate.opsForValue().set(RedisKeyConstants.REWARD_LIMIT,"20000");
            return "20000";
        }
        return rate;
    }



}
