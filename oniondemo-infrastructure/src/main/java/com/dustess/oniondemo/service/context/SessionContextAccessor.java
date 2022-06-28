package com.dustess.oniondemo.service.context;


import com.dustess.oniondemo.exception.BizException;
import com.dustess.oniondemo.exception.ExceptionEnum;
import com.dustess.oniondemo.interfaces.ContextAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SessionContextAccessor implements ContextAccessor {

    public String getAccount() {
        String account = ContextHolder.getAccount();
        if (StringUtils.isEmpty(account)) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        return account;
    }

    public String getMemberId() {
        String uid = ContextHolder.getMemberId();
        if (StringUtils.isEmpty(uid)) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        return uid;
    }

    public String getCustomerId() {
        String uid = ContextHolder.getCustomerId();
        if (StringUtils.isEmpty(uid)) {
            throw BizException.with(ExceptionEnum.LOGIN_REQUIRED);
        }
        return uid;
    }
}
