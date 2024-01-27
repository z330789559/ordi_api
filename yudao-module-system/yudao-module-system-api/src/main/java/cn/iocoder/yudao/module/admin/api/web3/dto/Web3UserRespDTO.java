package cn.iocoder.yudao.module.admin.api.web3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 社交用户 Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Web3UserRespDTO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 社交用户 address
     */
    private String address;

}
