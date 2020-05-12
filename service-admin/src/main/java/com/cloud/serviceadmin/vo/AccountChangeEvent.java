package com.cloud.serviceadmin.vo;

import java.io.Serializable;


public class AccountChangeEvent implements Serializable {
    /**
     * 账号
     */
    private String accountNo;
    /**
     * 变动金额
     */
    private double amount;
    /**
     * 事务号
     */
    private String txNo;

    public AccountChangeEvent(String accountNo, double amount, String txNo) {
        this.accountNo = accountNo;
        this.amount = amount;
        this.txNo = txNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTxNo() {
        return txNo;
    }

    public void setTxNo(String txNo) {
        this.txNo = txNo;
    }
}
