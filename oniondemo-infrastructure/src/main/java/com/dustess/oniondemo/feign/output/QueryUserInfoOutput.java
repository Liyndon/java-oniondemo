package com.dustess.oniondemo.feign.output;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class QueryUserInfoOutput {
    private Integer total;
    private List<UserInfo> list;

    @Data
    public static class UserInfo {
        @JSONField(name = "_id")
        private String id;

        private String name;

        private String qwUserId;

        private String qr_code;
    }
}
