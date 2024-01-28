package cn.iocoder.yudao.module.system.dal.mysql.web3;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.web3.Web3UserBindDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Web3UserBindMapper extends BaseMapperX<Web3UserBindDO> {

    default Web3UserBindDO selectByUserTypeAndWeb3UserId(Integer userType, Long web3UserId) {
        return selectOne(Web3UserBindDO::getUserType, userType,
                Web3UserBindDO::getWeb3UserId, web3UserId);
    }

    default List<Web3UserBindDO> selectListByUserIdAndUserType(Long userId, Integer userType) {
        return selectList(new LambdaQueryWrapperX<Web3UserBindDO>()
                .eq(Web3UserBindDO::getUserId, userId)
                .eq(Web3UserBindDO::getUserType, userType));
    }

}
