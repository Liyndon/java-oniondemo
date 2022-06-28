package com.dustess.oniondemo.feign;

import com.dustess.oniondemo.feign.input.QueryUserInfoInput;
import com.dustess.oniondemo.feign.output.QueryUserInfoOutput;
import com.dustess.oniondemo.feign.output.RemoteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "deptUser", url = "${feign.url.qw-dept-user}")
public interface QwDeptUserFeign {

    @RequestMapping(value = "/internal/qw_account/v1/address-book/user/list_by_page", method = RequestMethod.POST)
    RemoteResponse<QueryUserInfoOutput> queryUserInfoByUserIds(@RequestBody QueryUserInfoInput input);
}
