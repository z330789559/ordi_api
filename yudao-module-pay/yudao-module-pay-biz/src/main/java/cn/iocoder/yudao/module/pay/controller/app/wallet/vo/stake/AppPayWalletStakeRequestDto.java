package cn.iocoder.yudao.module.pay.controller.app.wallet.vo.stake;

import java.math.BigDecimal;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Positive;

import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletUserTypeEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.TokenType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * AppPayWalletStakeRequestDto
 *
 * @author libaozhong
 * @version 2024-02-27
 **/

@Schema(description = "用户 App - 创建质押 Request VO")
@Data
public class AppPayWalletStakeRequestDto {

  @Schema(description = "token catalog", example = "2048")
  private PayWalletUserTypeEnum userType;

  @Schema(description = "金额", example = "1")
  @Positive(message = "金额必须大于0")
  private BigDecimal price;

  @AssertTrue(message = "参数不正确")
  @JsonIgnore
  public boolean isValid() {
    return userType != null && price != null;
  }


}
