package com.pbids.sanqin.model.entity;

import java.util.List;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:39
 * @desscribe 类描述:流水列表实体类的集合
 * @remark 备注:包含了其列表heared（heared为时间）
 * @see
 */
public class AccountsGroup {
    private String header;
    private List<Accounts> accountses;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Accounts> getAccountses() {
        return accountses;
    }

    public void setAccountses(List<Accounts> accountses) {
        this.accountses = accountses;
    }
}
