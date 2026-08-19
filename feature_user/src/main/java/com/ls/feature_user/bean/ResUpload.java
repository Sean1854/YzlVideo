package com.ls.feature_user.bean;

/**
 * 上传文件结果
 */
public class ResUpload {


    /**
     * url : /uploads/20250429/b4ee80207d67072b2227034c496f7fce.jpg
     * fullurl : https://titok.fzqq.fun/uploads/20250429/b4ee80207d67072b2227034c496f7fce.jpg
     */

    private String url;
    private String fullurl;//完整路径

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullurl() {
        return fullurl;
    }

    public void setFullurl(String fullurl) {
        this.fullurl = fullurl;
    }
}
