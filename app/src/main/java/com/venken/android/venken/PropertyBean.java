package com.venken.android.venken;

import com.fota.android.commonlib.utils.Pub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PropertyBean implements Serializable {


    private String propertyid;
    private String djtime;
    private String areaname;
    private String propertyname;
    private String districtname;
    private String presel180;
    public String siteid;

    private boolean hasInfo;
    private boolean isReq;

    PropertyDetailBean.Preselllist detail;
    List<String> bNameList;
    List<String> bidList;
    List<String> newNameList;


    public void setPropertyDetailBean(PropertyDetailBean bean) {
        if (bean == null) {
            return;
        }
        if (Pub.isListExists(bean.getPreselllist())) {
            detail = bean.getPreselllist().get(0);
            String newNames[] = bean.getSellhousestr().split(",");
            newNameList = new ArrayList<>();
            for (String name : newNames) {
                newNameList.add(name);
            }
        }
        String bids[] = bean.getBuildids().split(",");
        String bNames[] = bean.getSellhousestr().split(",");

        bidList = new ArrayList<>();
        bNameList = new ArrayList<>();

        for (int i = 0; i < bids.length; i++) {
            bidList.add(bids[i]);
            bNameList.add(bNames[i]);
        }
    }


    public String getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(String propertyid) {
        this.propertyid = propertyid;
    }

    public String getDjtime() {
        return djtime;
    }

    public void setDjtime(String djtime) {
        this.djtime = djtime;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getPropertyname() {
        return propertyname;
    }

    public void setPropertyname(String propertyname) {
        this.propertyname = propertyname;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    public String getPresel180() {
        return presel180;
    }

    public void setPresel180(String presel180) {
        this.presel180 = presel180;
    }

    public String getNewInfo() {
        if (detail == null) {
            return "";
        }
        return "预售证" + detail.getApplydate() + ":" + detail.getBuildingname();
    }

    public boolean isHasInfo() {
        return hasInfo;
    }

    public void setHasInfo(boolean hasInfo) {
        isReq = true;
        this.hasInfo = hasInfo;
    }

    public List<String> getBNameList() {
        return bNameList;
    }

    public List<String> getBidList() {
        return bidList;
    }

    public List<String> getNewNameList() {
        return newNameList;
    }

    public String getFirstId() {
        if (Pub.isListExists(newNameList)) {
            String name = newNameList.get(0);
            int position = bNameList.indexOf(name);
            String id = bidList.get(position);
            return id;
        }
        return null;
    }

    public boolean isReq() {
        return isReq;
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }
}
