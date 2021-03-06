/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:33 创建
 */
package org.antframework.configcenter.facade.info;

import org.antframework.common.util.facade.AbstractInfo;

/**
 * 环境info
 */
public class ProfileInfo extends AbstractInfo {
    // 环境编码
    private String profileCode;
    // 备注
    private String memo;

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
