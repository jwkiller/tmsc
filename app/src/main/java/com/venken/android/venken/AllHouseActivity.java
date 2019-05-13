package com.venken.android.venken;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.fota.android.commonlib.recyclerview.EasyAdapter;
import com.fota.android.commonlib.recyclerview.RecyclerViewUtils;
import com.fota.android.commonlib.recyclerview.ViewHolder;
import com.fota.android.commonlib.utils.Pub;
import com.fota.android.commonlib.utils.ToastUitl;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AllHouseActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EasyAdapter<String, ViewHolder> adapter;
    PropertyBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (PropertyBean) getIntent().getExtras().get(BundleKey.LIST);
        List<String> list = data.getNewNameList();
        setContentView(R.layout.activity_over);
        mRecyclerView = findViewById(R.id.recyclerView);
        init();
        if (list == null) {
            ToastUitl.showLong("没有一房一价信息");
            return;
        }
        adapter.putList(list);
    }


    /**
     * 密码登录
     */
    private void init() {
        initAdapter();
        RecyclerViewUtils.initRecyclerView(mRecyclerView, this);
        mRecyclerView.setAdapter(adapter);
    }


    public void initAdapter() {
        adapter = new EasyAdapter<String, ViewHolder>(this, R.layout.item_build) {

            @Override
            public void convert(final ViewHolder holder, final String model, final int position) {
                holder.setText(R.id.name, model);
                //http://jia3.tmsf.com/tmj3/property_detail.jspx?json=true&propertyid=427253360&siteid=33
                //area=&buildingid=1180180938&housetype=1&page=1&propertyid=427253360&siteid=33


                //http://jia3.tmsf.com/tmj3/property_control.jspx?area=&siteid=33&buildingid=12527035&propertyid=30405756&siteid=330184&page=1
                //http://jia3.tmsf.com/tmj3/property_control.jspx?area=&buildingid=12527035&housetype=&page=1&propertyid=30405756&siteid=330184

                BtbMap map = new BtbMap();
                map.put("area", "");
                map.put("buildingid", getBuidingId(model));
                map.put("housetype", "");
                map.put("page", "1");
                map.put("propertyid", data.getPropertyid());
                map.put("siteid", data.getSiteid());
                RetrofitHelper.api().getHouses(map)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<HouseListBean>() {

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(HouseListBean detailBean) {
                                holder.setText(R.id.info, detailBean.toString());
                            }
                        });


            }
        };
    }

    private String getBuidingId(String model) {
        if (data == null) {
            return "";
        }
        int position = data.getBNameList().indexOf(model);
        String id = data.getBidList().get(position);
        return id;
    }

}
