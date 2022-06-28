package com.dustess.oniondemo.interfaces;

public interface ContextAccessor {
    /**
     * 租户ID
     * @return String
     */
    String getAccount();

    /**
     * 员工ID
     * @return String
     */
    String getMemberId();

    /**
     * 客户ID
     * @return String
     */
    String getCustomerId();
}
