package cn.iocoder.yudao.module.product.controller.app.token.vo;

import lombok.Data;

import java.util.List;

@Data
public class TokenPriceScanRespVO {

    private int code;
    private String msg;
    private List<DataItem> data;

    // Getters and Setters

    @Data
    public static class DataItem {
        private String instId;
        private String idxPx;
        private String high24h;
        private String sodUtc0;
        private String open24h;
        private String low24h;
        private String sodUtc8;
        private String ts;

        @Override
        public String toString() {
            return "DataItem{" +
                    "instId='" + instId + '\'' +
                    ", idxPx='" + idxPx + '\'' +
                    ", high24h='" + high24h + '\'' +
                    ", sodUtc0='" + sodUtc0 + '\'' +
                    ", open24h='" + open24h + '\'' +
                    ", low24h='" + low24h + '\'' +
                    ", sodUtc8='" + sodUtc8 + '\'' +
                    ", ts='" + ts + '\'' +
                    '}';
        }
    }
}
