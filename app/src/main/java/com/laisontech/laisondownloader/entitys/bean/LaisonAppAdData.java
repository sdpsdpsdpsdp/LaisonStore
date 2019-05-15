package com.laisontech.laisondownloader.entitys.bean;

import java.io.Serializable;

/**
 * Created by lu on 2018/1/4.
 * 广告地址链接实体类
 *
 */

public class LaisonAppAdData implements Serializable{
    private static final long serialVersionUID = 2201801041710L;
    private String imagedownloadlink;//图片下载地址
    private String linkedaddress;//图片关联的网络地址
    public String getImageDownloadLink() {
        return imagedownloadlink;
    }
    public String getLinkedAddress() {
        return linkedaddress;
    }

    @Override
    public String toString() {
        return "LaisonAppAdData{" +
                "imagedownloadlink='" + imagedownloadlink + '\'' +
                ", linkedaddress='" + linkedaddress + '\'' +
                '}';
    }
}
