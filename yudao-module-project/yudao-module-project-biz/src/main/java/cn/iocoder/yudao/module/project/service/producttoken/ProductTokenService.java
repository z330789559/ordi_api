package cn.iocoder.yudao.module.project.service.producttoken;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.project.controller.admin.producttoken.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.producttoken.ProductTokenDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 产品列 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductTokenService {

    /**
     * 创建产品列
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductToken(@Valid ProductTokenSaveReqVO createReqVO);

    /**
     * 更新产品列
     *
     * @param updateReqVO 更新信息
     */
    void updateProductToken(@Valid ProductTokenSaveReqVO updateReqVO);

    /**
     * 删除产品列
     *
     * @param id 编号
     */
    void deleteProductToken(Long id);

    /**
     * 获得产品列
     *
     * @param id 编号
     * @return 产品列
     */
    ProductTokenDO getProductToken(Long id);

    /**
     * 获得产品列分页
     *
     * @param pageReqVO 分页查询
     * @return 产品列分页
     */
    PageResult<ProductTokenDO> getProductTokenPage(ProductTokenPageReqVO pageReqVO);

}