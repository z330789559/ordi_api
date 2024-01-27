package cn.iocoder.yudao.module.infra.api.logger;

import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;

import javax.validation.Valid;

/**
 * API 访问日志的 API 接口
 *
 * @author 芋道源码
 */
public interface ApiAccessLogApi {
	void createApiAccessLog(@Valid ApiAccessLogCreateReqDTO createDTO);

}
