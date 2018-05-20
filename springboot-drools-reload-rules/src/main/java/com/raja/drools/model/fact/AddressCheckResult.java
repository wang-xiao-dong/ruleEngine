package com.raja.drools.model.fact;

/**
 * Created by WXD on 2018/5/18.
 */
public class AddressCheckResult {

    /**
     * true:通过校验；false：未通过校验
     */
    private boolean postCodeResult = false;

    public boolean isPostCodeResult() {
        return postCodeResult;
    }

    public void setPostCodeResult(boolean postCodeResult) {
        this.postCodeResult = postCodeResult;
    }
}
