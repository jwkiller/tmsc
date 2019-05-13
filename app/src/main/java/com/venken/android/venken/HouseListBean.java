package com.venken.android.venken;

import com.fota.android.commonlib.utils.Pub;

import java.io.Serializable;
import java.util.List;

public class HouseListBean implements Serializable {

    @Override
    public String toString() {
        if (Pub.isListExists(list)) {
            StringBuilder sb = new StringBuilder();
            for (HouseList item : list) {
                sb.append(item.getBuildingname());
                sb.append("---");
                sb.append(item.getUnitname());
                sb.append("单元");
                sb.append("---");
                sb.append(item.getHouseno());
                sb.append("---");
                sb.append("总价：");
                sb.append(item.getTotalprice());
                sb.append("---");
                sb.append("均价：");
                sb.append(item.getSignalprice());
                sb.append("---");
                sb.append("面积：");
                sb.append(item.getBuiltuparea());
                sb.append("m2");
                sb.append("---");
                sb.append("\n");
            }
            return sb.toString();
        }

        return "没有一房一价数据";
    }

    List<HouseList> list;

    public static class HouseList implements Serializable {
        private String houseid;
        private String unitname;
        private String houseno;
        private String builtuparea;
        private String totalprice;
        private String buildingname;
        private String signalprice;
        private String dfl;

        public String getHouseid() {
            return houseid;
        }

        public void setHouseid(String houseid) {
            this.houseid = houseid;
        }

        public String getUnitname() {
            return unitname;
        }

        public void setUnitname(String unitname) {
            this.unitname = unitname;
        }

        public String getHouseno() {
            return houseno;
        }

        public void setHouseno(String houseno) {
            this.houseno = houseno;
        }

        public String getBuiltuparea() {
            return builtuparea;
        }

        public void setBuiltuparea(String builtuparea) {
            this.builtuparea = builtuparea;
        }

        public String getTotalprice() {
            return totalprice;
        }

        public void setTotalprice(String totalprice) {
            this.totalprice = totalprice;
        }

        public String getBuildingname() {
            return buildingname;
        }

        public void setBuildingname(String buildingname) {
            this.buildingname = buildingname;
        }

        public String getSignalprice() {
            return signalprice;
        }

        public void setSignalprice(String signalprice) {
            this.signalprice = signalprice;
        }

        public String getDfl() {
            return dfl;
        }

        public void setDfl(String dfl) {
            this.dfl = dfl;
        }
    }

    public List<HouseList> getList() {
        return list;
    }

    public void setList(List<HouseList> list) {
        this.list = list;
    }
}
