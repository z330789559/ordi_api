package cn.iocoder.yudao.module.infra.api.logger;

import cn.iocoder.yudao.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;

import javax.validation.Valid;

/**
 * API 错误日志的 API 接口
 *
 * @author 芋道源码
 */
public interface ApiErrorLogApi {
	void createApiErrorLog(@Valid ApiErrorLogCreateReqDTO createDTO);

}
