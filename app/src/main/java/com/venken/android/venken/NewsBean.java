package com.venken.android.venken;

import java.util.List;

public class NewsBean {

    private int count;
    private boolean isover;
    private List<NewsItemBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isIsover() {
        return isover;
    }

    public void setIsover(boolean isover) {
        this.isover = isover;
    }

    public List<NewsItemBean> getList() {
        return list;
    }

    public void setList(List<NewsItemBean> list) {
        this.list = list;
    }

    public static class NewsItemBean {

        private String title;
        private String contentid;
        private String categoryid;
        private String date;
        private String clicknum;
        private String source;
        private String jizhename;
        private String isadtype;
        private String logo;
        private String descr;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContentid() {
            return contentid;
        }

        public void setContentid(String contentid) {
            this.contentid = contentid;
        }

        public String getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(String categoryid) {
            this.categoryid = categoryid;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getClicknum() {
            return clicknum;
        }

        public void setClicknum(String clicknum) {
            this.clicknum = clicknum;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getJizhename() {
            return jizhename;
        }

        public void setJizhename(String jizhename) {
            this.jizhename = jizhename;
        }

        public String getIsadtype() {
            return isadtype;
        }

        public void setIsadtype(String isadtype) {
            this.isadtype = isadtype;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getDescr() {
            return descr;
        }

        public void setDescr(String descr) {
            this.descr = descr;
        }
    }
}
