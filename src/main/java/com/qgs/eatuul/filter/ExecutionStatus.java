package com.qgs.eatuul.filter;
/**
 * @Description: TODO
 * @author: qianguisen
 * @Date: 2018/11/7 20:54
 **/
public enum ExecutionStatus {

    SUCCESS (1), SKIPPED(-1), DISABLED(-2), FAILED(-3);
    
    private int status;

    ExecutionStatus(int status) {
        this.status = status;
    }
}