package com.dustess.oniondemo.feign.input;

import lombok.Data;

import javax.validation.constraints.Min;
import java.util.List;

@Data
public class QueryUserInfoInput {

    private List<String> userIds;

    private Integer type;

    private Boolean containLeave;

    private String account;

    @Min(value = 1, message = "页码需大于0")
    private Integer page = 1;

    @Min(value = 1, message = "分页数量需大于0")
    private Integer pageSize;

    public Integer getOffset() {
        return (page - 1) * pageSize;
    }
}
