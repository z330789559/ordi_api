package cn.iocoder.yudao.module.project.service.whitelist;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.project.controller.admin.whitelist.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.whitelist.StatisticsDo;
import cn.iocoder.yudao.module.project.dal.dataobject.whitelist.WhiteListDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.project.dal.mysql.whitelist.WhiteListMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.project.enums.ErrorCodeConstants.*;

/**
 * 白名单 Service 实现类
 *
 * @author BT2N
 */
@Service
@Validated
public class WhiteListServiceImpl implements WhiteListService {

    @Resource
    private WhiteListMapper whiteListMapper;

    @Override
    public Integer createWhiteList(WhiteListSaveReqVO createReqVO) {
        // 插入
        WhiteListDO whiteList = BeanUtils.toBean(createReqVO, WhiteListDO.class);
        whiteListMapper.insert(whiteList);
        // 返回
        return whiteList.getId();
    }

    @Override
    public void updateWhiteList(WhiteListSaveReqVO updateReqVO) {
        // 校验存在
        validateWhiteListExists(updateReqVO.getId());
        // 更新
        WhiteListDO updateObj = BeanUtils.toBean(updateReqVO, WhiteListDO.class);
        whiteListMapper.updateById(updateObj);
    }

    @Override
    public void deleteWhiteList(Integer id) {
        // 校验存在
        validateWhiteListExists(id);
        // 删除
        whiteListMapper.deleteById(id);
    }

    private void validateWhiteListExists(Integer id) {
        if (whiteListMapper.selectById(id) == null) {
            throw exception(WHITE_LIST_NOT_EXISTS);
        }
    }

    @Override
    public WhiteListDO getWhiteList(Integer id) {
        return whiteListMapper.selectById(id);
    }

    @Override
    public PageResult<WhiteListDO> getWhiteListPage(WhiteListPageReqVO pageReqVO) {
        return whiteListMapper.selectPage(pageReqVO);
    }

    @Override
    public StatisticsDo getStatisticData() {

       List<Long> ids= whiteListMapper.selectUserIdIds();
       BigDecimal shelveCount= whiteListMapper.getShelveProductCount(ids);
       BigDecimal totalBtcQuantity= whiteListMapper.getTotalBtcQuantity(ids);
       BigDecimal totalOrdinalsCount= whiteListMapper.getTotalOrdinalsCount(ids);
        StatisticsDo statisticsDo = new StatisticsDo();
        statisticsDo.setShelveOrdinasNum(shelveCount);
        statisticsDo.setBtcTotalTvl(totalBtcQuantity);
        statisticsDo.setOrdinasTvl(totalOrdinalsCount);
        return statisticsDo;
    }

}