package cn.iocoder.yudao.module.admin.dal.mysql.errorcode;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.admin.controller.admin.errorcode.vo.ErrorCodePageReqVO;
import cn.iocoder.yudao.module.admin.dal.dataobject.errorcode.ErrorCodeDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Mapper
public interface ErrorCodeMapper extends BaseMapperX<ErrorCodeDO> {

    default PageResult<ErrorCodeDO> selectPage(ErrorCodePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErrorCodeDO>()
                .eqIfPresent(ErrorCodeDO::getType, reqVO.getType())
                .likeIfPresent(ErrorCodeDO::getApplicationName, reqVO.getApplicationName())
                .eqIfPresent(ErrorCodeDO::getCode, reqVO.getCode())
                .likeIfPresent(ErrorCodeDO::getMessage, reqVO.getMessage())
                .betweenIfPresent(ErrorCodeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ErrorCodeDO::getCode));
    }

    default List<ErrorCodeDO> selectListByCodes(Collection<Integer> codes) {
        return selectList(ErrorCodeDO::getCode, codes);
    }

    default ErrorCodeDO selectByCode(Integer code) {
        return selectOne(ErrorCodeDO::getCode, code);
    }

    default List<ErrorCodeDO> selectListByApplicationNameAndUpdateTimeGt(String applicationName, LocalDateTime minUpdateTime) {
        return selectList(new LambdaQueryWrapperX<ErrorCodeDO>().eq(ErrorCodeDO::getApplicationName, applicationName)
                .gtIfPresent(ErrorCodeDO::getUpdateTime, minUpdateTime));
    }

}
