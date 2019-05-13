package com.venken.android.venken;

import java.io.Serializable;
import java.util.List;

public class PropertyDetailBean implements Serializable {

    String sellhousestr;
    String buildids;
    List<Preselllist> preselllist;



    public static class Preselllist implements Serializable {

        String buildingname;
        String applydate;

        public String getBuildingname() {
            return buildingname;
        }

        public void setBuildingname(String buildingname) {
            this.buildingname = buildingname;
        }

        public String getApplydate() {
            return applydate;
        }

        public void setApplydate(String applydate) {
            this.applydate = applydate;
        }
    }


    public String getSellhousestr() {
        return sellhousestr;
    }

    public void setSellhousestr(String sellhousestr) {
        this.sellhousestr = sellhousestr;
    }

    public String getBuildids() {
        return buildids;
    }

    public void setBuildids(String buildids) {
        this.buildids = buildids;
    }



    public List<Preselllist> getPreselllist() {
        return preselllist;
    }

    public void setPreselllist(List<Preselllist> preselllist) {
        this.preselllist = preselllist;
    }


}
