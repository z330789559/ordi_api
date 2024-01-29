package cn.iocoder.yudao.module.project.service.producttoken;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.project.controller.admin.producttoken.vo.*;
import cn.iocoder.yudao.module.project.dal.dataobject.producttoken.ProductTokenDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.project.dal.mysql.producttoken.ProductTokenMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.project.enums.ErrorCodeConstants.*;

/**
 * 产品列 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductTokenServiceImpl implements ProductTokenService {

    @Resource
    private ProductTokenMapper productTokenMapper;

    @Override
    public Long createProductToken(ProductTokenSaveReqVO createReqVO) {
        // 插入
        ProductTokenDO productToken = BeanUtils.toBean(createReqVO, ProductTokenDO.class);
        productTokenMapper.insert(productToken);
        // 返回
        return productToken.getId();
    }

    @Override
    public void updateProductToken(ProductTokenSaveReqVO updateReqVO) {
        // 校验存在
        validateProductTokenExists(updateReqVO.getId());
        // 更新
        ProductTokenDO updateObj = BeanUtils.toBean(updateReqVO, ProductTokenDO.class);
        productTokenMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductToken(Long id) {
        // 校验存在
        validateProductTokenExists(id);
        // 删除
        productTokenMapper.deleteById(id);
    }

    private void validateProductTokenExists(Long id) {
        if (productTokenMapper.selectById(id) == null) {
            throw exception(PRODUCT_TOKEN_NOT_EXISTS);
        }
    }

    @Override
    public ProductTokenDO getProductToken(Long id) {
        return productTokenMapper.selectById(id);
    }

    @Override
    public PageResult<ProductTokenDO> getProductTokenPage(ProductTokenPageReqVO pageReqVO) {
        return productTokenMapper.selectPage(pageReqVO);
    }

}