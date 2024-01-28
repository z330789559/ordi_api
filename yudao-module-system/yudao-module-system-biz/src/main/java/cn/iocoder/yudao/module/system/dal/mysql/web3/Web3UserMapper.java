package cn.iocoder.yudao.module.system.dal.mysql.web3;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.web3.Web3UserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Web3UserMapper extends BaseMapperX<Web3UserDO> {

    default Web3UserDO selectByAddress(String address) {
        return selectOne(new LambdaQueryWrapper<Web3UserDO>().eq(Web3UserDO::getAddress, address));
    }

}
