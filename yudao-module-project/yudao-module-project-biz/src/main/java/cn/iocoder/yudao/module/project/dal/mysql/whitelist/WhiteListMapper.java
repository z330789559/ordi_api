package cn.iocoder.yudao.module.project.dal.mysql.whitelist;

import java.math.BigDecimal;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.project.dal.dataobject.whitelist.WhiteListDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.project.controller.admin.whitelist.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 白名单 Mapper
 *
 * @author BT2N
 */
@Mapper
public interface WhiteListMapper extends BaseMapperX<WhiteListDO> {

    @TenantIgnore
    default PageResult<WhiteListDO> selectPage(WhiteListPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WhiteListDO>()
                .eqIfPresent(WhiteListDO::getAddress, reqVO.getAddress())
                .eqIfPresent(WhiteListDO::getStatus, reqVO.getStatus())
                .orderByDesc(WhiteListDO::getId));
    }


	@Select("select u.id  from member_user u where u.address in (select address from pay_white_list where `status`=1)")
	  List<Long> selectUserIdIds();


	//使用mybatis plus foreach 把in 填充
	@TenantIgnore
	@Select("<script>" +"select sum(stock) from ordi_product_item where `status` =1 and user_id not in  <foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>" + "</script>")
	BigDecimal getShelveProductCount(@Param("ids") List<Long> ids);

	@TenantIgnore
	@Select("<script>" +"select sum(balance+frozen+recharge) from pay_wallet where `user_type` =0 and user_id not in  <foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>" + "</script>")
	BigDecimal getTotalBtcQuantity(@Param("ids") List<Long> ids);

	@TenantIgnore
    @Select("<script>"+ "select sum(balance+frozen+recharge) from pay_wallet where `user_type` =1 and user_id not in  <foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>" + "</script>")
	BigDecimal getTotalOrdinalsCount(@Param("ids")  List<Long> ids);
}