package com.dustess.oniondemo.service.context;

import lombok.Data;
import lombok.NoArgsConstructor;

public class ContextHolder {

    private static final ThreadLocal<ContextInfo> CONTEXT_INFO = ThreadLocal.withInitial(ContextInfo::new);

    public static void setAccount(String account) {
        CONTEXT_INFO.get().setAccount(account);
    }

    public static String getAccount() {
        return CONTEXT_INFO.get().getAccount();
    }

    public static String getMemberId() {
        return CONTEXT_INFO.get().getAccount();
    }

    public static String getCustomerId() {
        return CONTEXT_INFO.get().getAccount();
    }

    @Data
    @NoArgsConstructor
    static class ContextInfo {
        private String account;
        private String memberId;
        private String customerId;
    }
}
