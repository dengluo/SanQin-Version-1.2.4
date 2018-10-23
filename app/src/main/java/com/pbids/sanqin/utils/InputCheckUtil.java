package com.pbids.sanqin.utils;

import com.pbids.sanqin.model.entity.CheckResult;

//表单验证检测工具
public class InputCheckUtil {

    //验证资金
    public static CheckResult testBalanceInput(String balanceInput) {
        float balance = 0.0f;
        try {
            balance = Float.parseFloat(balanceInput);
        } catch (Exception e) {
            return new CheckResult<Float>(false, "输入错误");
        }
        if (balance == 0) {
            return new CheckResult<Float>(false, "请输入的金额");
        }
        return new CheckResult<Float>(balance);
    }
}
