package cn.iocoder.yudao.module.project.service.whitelist;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.project.controller.admin.whitelist.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.whitelist.StatisticsDo;
import cn.iocoder.yudao.module.project.dal.dataobject.whitelist.WhiteListDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 白名单 Service 接口
 *
 * @author BT2N
 */
public interface WhiteListService {

    /**
     * 创建白名单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createWhiteList(@Valid WhiteListSaveReqVO createReqVO);

    /**
     * 更新白名单
     *
     * @param updateReqVO 更新信息
     */
    void updateWhiteList(@Valid WhiteListSaveReqVO updateReqVO);

    /**
     * 删除白名单
     *
     * @param id 编号
     */
    void deleteWhiteList(Integer id);

    /**
     * 获得白名单
     *
     * @param id 编号
     * @return 白名单
     */
    WhiteListDO getWhiteList(Integer id);

    /**
     * 获得白名单分页
     *
     * @param pageReqVO 分页查询
     * @return 白名单分页
     */
    PageResult<WhiteListDO> getWhiteListPage(WhiteListPageReqVO pageReqVO);

	StatisticsDo getStatisticData();
}