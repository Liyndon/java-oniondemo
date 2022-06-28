package com.dustess.oniondemo.service;


import com.dustess.oniondemo.entity.user.UserInfo;
import com.dustess.oniondemo.exception.BizException;
import com.dustess.oniondemo.exception.ExceptionEnum;
import com.dustess.oniondemo.feign.QwDeptUserFeign;
import com.dustess.oniondemo.feign.input.QueryUserInfoInput;
import com.dustess.oniondemo.feign.output.QueryUserInfoOutput;
import com.dustess.oniondemo.feign.output.RemoteResponse;
import com.dustess.oniondemo.interfaces.ContextAccessor;
import com.dustess.oniondemo.interfaces.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeignUserService implements UserService {

    @Resource
    private QwDeptUserFeign qwDeptUserFeign;

    @Autowired
    private ContextAccessor contextAccessor;

    @Override
    public UserInfo get(String id) {
        List<UserInfo> infos = getSome(List.of(id));
        return infos.size() > 0 ? infos.get(0) : null;
    }

    @Override
    public List<UserInfo> getSome(List<String> ids) {
        QueryUserInfoInput input = new QueryUserInfoInput();
        input.setUserIds(ids);
        input.setPage(1);
        input.setPageSize(ids.size());
        input.setAccount(contextAccessor.getAccount());
        input.setType(0);

        try {
            RemoteResponse<QueryUserInfoOutput> output = qwDeptUserFeign.queryUserInfoByUserIds(input);
            List<QueryUserInfoOutput.UserInfo> infos = output.getData().getList();
            return infos.stream().map(info -> {
                UserInfo userInfo = new UserInfo();
                BeanUtils.copyProperties(info, UserInfo.class);
                return userInfo;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
    }
}
