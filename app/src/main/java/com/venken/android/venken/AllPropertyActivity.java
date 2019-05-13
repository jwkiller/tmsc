package com.venken.android.venken;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fota.android.commonlib.recyclerview.EasyAdapter;
import com.fota.android.commonlib.recyclerview.RecyclerViewUtils;
import com.fota.android.commonlib.recyclerview.ViewHolder;
import com.fota.android.commonlib.utils.Pub;
import com.fota.android.commonlib.utils.SharedPreferencesUtil;
import com.fota.android.commonlib.utils.ToastUitl;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AllPropertyActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EasyAdapter<PropertyBean, ViewHolder> adapter;

    private HashMap<String, String> mapNews = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);
        mRecyclerView = findViewById(R.id.recyclerView);
        init();
        getData();
        getNews();
        Intent intent = new Intent(AllPropertyActivity.this, AllPropertyService.class);
        startService(intent);
    }

    private void getNews() {
        BtbMap map = new BtbMap();
        map.put("contentid", "1180735482");
        map.put("page", "1");
        RetrofitHelper.api().getNews(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewsBean>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NewsBean news) {
                        if (news == null) {
                            return;
                        }

                        if (!Pub.isListExists(news.getList())) {
                            return;
                        }

                        for (NewsBean.NewsItemBean item : news.getList()) {
                            String info = "";
                            if (item.getDescr().contains("推广名：")) {
                                int first = item.getDescr().indexOf("（");
                                int last = item.getDescr().indexOf("）");
                                info = item.getDescr().substring(first + 5, last);
                            } else {
                                if (item.getTitle().startsWith("[方案公示]")) {
                                    int last = item.getTitle().indexOf(" ");
                                    info = item.getTitle().substring(6, last);
                                }
                            }
                            mapNews.put(info, item.getDescr());
                        }

                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void getData() {
        String lastJson = SharedPreferencesUtil.getInstance().get("info", "");
        PropertyContainerBean containerBean = new Gson().fromJson(lastJson, PropertyContainerBean.class);
        if (containerBean != null) {
            setData(containerBean);
        }
    }

    private void setData(PropertyContainerBean container) {
        List<PropertyBean> list = container.getList();
        if (Pub.isListExists(list)) {
            adapter.putList(list);
        } else {
            ToastUitl.showLong("暂时没有新盘，请晚点再来");
        }
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
        adapter = new EasyAdapter<PropertyBean, ViewHolder>(this, R.layout.item_customer) {
            @Override
            public void convert(final ViewHolder holder, final PropertyBean model, final int position) {
                holder.setText(R.id.name, model.getPropertyname());
                holder.setText(R.id.price, "均价：" + model.getPresel180());
                holder.setText(R.id.time, "登记时间：" + model.getDjtime());
                holder.setText(R.id.no, model.getNewInfo());
                holder.setTextColor(R.id.no, model.isHasInfo() ? 0xFF31AC5D : 0xFFE15A55);
                holder.setText(R.id.news, getNewsInfo(model));
                //http://jia3.tmsf.com/tmj3/property_detail.jspx?json=true&propertyid=427253360&siteid=33
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getFirstId() == null) {
                            ToastUitl.showLong("没有一房一价");
                            return;
                        }
                        Intent intent = new Intent(getContext(), AllHouseActivity.class);
                        intent.putExtra(BundleKey.LIST, model);
                        startActivity(intent);
                    }
                });

                if (model.isReq()) {
                    return;
                }
                BtbMap map = new BtbMap();
                map.put("json", "true");
                map.put("propertyid", String.valueOf(model.getPropertyid()));
                map.put("siteid", "33");
                RetrofitHelper.api().getBuilding(map)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<PropertyDetailBean>() {

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(PropertyDetailBean detailBean) {
                                model.setPropertyDetailBean(detailBean);
                                String id = model.getFirstId();
                                if (id == null) {
                                    return;
                                }
                                BtbMap map = new BtbMap();
                                map.put("area", "");
                                map.put("buildingid", model.getFirstId());
                                map.put("housetype", "");
                                map.put("page", "1");
                                map.put("propertyid", model.getPropertyid());
                                map.put("siteid", model.getSiteid());
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
                                                if (detailBean == null) {
                                                    model.setHasInfo(false);
                                                    adapter.notifyItemChanged(position);
                                                    return;
                                                }
                                                if (Pub.isListExists(detailBean.getList())) {
                                                    model.setHasInfo(true);
                                                } else {
                                                    model.setHasInfo(false);
                                                }
                                                adapter.notifyItemChanged(position);
                                            }
                                        });
                            }
                        });

            }
        };
    }

    /**
     *
     * @param model
     * @return
     */
    private String getNewsInfo(PropertyBean model) {
        if (model.getPropertyname() == null) {
            return "";
        }
        if (mapNews.containsKey(model.getPropertyname())) {
            return mapNews.get(model.getPropertyname());
        }
        return "";
    }

    private Context getContext() {
        return AllPropertyActivity.this;
    }

}
