package com.venken.android.venken;

import com.fota.android.commonlib.http.BaseHttpResult;
import com.fota.android.commonlib.utils.Pub;

import java.util.List;

public class PropertyContainerBean extends BaseHttpResult {

    private int count;
    private boolean isover;
    private List<PropertyBean> list;

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

    public List<PropertyBean> getList() {
        return list;
    }

    public void setList(List<PropertyBean> list) {
        this.list = list;
    }
}
