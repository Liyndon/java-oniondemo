package com.dustess.oniondemo.service.context;

import com.dustess.oniondemo.interfaces.ContextAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class HybridContextAccessor implements ContextAccessor {

    @Autowired
    private SessionContextAccessor sessionContextAccessor;

    @Override
    public String getAccount() {
        if (ContextHolder.getAccount() != null) {
            return ContextHolder.getAccount();
        }
        return sessionContextAccessor.getAccount();
    }

    @Override
    public String getMemberId() {
        return sessionContextAccessor.getMemberId();
    }

    @Override
    public String getCustomerId() {
        return sessionContextAccessor.getCustomerId();
    }
}
