package com.venken.android.venken;

import android.view.View;

import com.fota.android.commonlib.recyclerview.EasyAdapter;

public interface IRecyclerViewHelper {

    EasyAdapter initMainAdapter();

    boolean setRefreshEnable();

    boolean setLoadEnable();

    void onLoadMore();

    void onRefresh();

    View setHeadView();

    boolean setFootAndHeadTrans();
}
