package com.venken.android.venken;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fota.android.commonlib.utils.Pub;
import com.fota.android.commonlib.utils.SharedPreferencesUtil;
import com.google.gson.Gson;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AllPropertyService extends Service {


    private Gson gson;
    Handler handler;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        postNotification(AllPropertyService.this, "运行中", true);
        return START_REDELIVER_INTENT;
    }


    public void postNotification(Context context, String msg, boolean isFore) {
        int channelId = 0x22222;
        Notification.Builder builder;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0系统之上
            NotificationChannel channel = new NotificationChannel(String.valueOf(channelId), "chanel_name", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder = new Notification.Builder(context, String.valueOf(channelId));
            //或者
            //builder = new Notification.Builder(context);
            //builder.setChannelId(String.valueOf(channelId)); //创建通知时指定channelId
        } else {
            builder = new Notification.Builder(context);
        }
        //需要跳转指定的页面
        Intent intent = new Intent(context, AllPropertyActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setTicker("new message")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("透明家追踪")
                .setContentText(msg)
                .setContentIntent(pendingIntent);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
            //   //设置手机震动
            //第一个，0表示手机静止的时长，第二个，1000表示手机震动的时长
            //第三个，1000表示手机震动的时长，第四个，1000表示手机震动的时长
            //此处表示手机先震动1秒，然后静止1秒，然后再震动1秒
            long[] vibrates = {0, 1000, 1000, 1000};
            notification.vibrate = vibrates;

            //设置LED指示灯的闪烁
            //ledARGB设置颜色
            //ledOnMS指定LED灯亮起的时间
            //ledOffMS指定LED灯暗去的时间
            //flags用于指定通知的行为
            notification.ledARGB = Color.GREEN;
            notification.ledOnMS = 1000;
            notification.ledOffMS = 1000;
            notification.flags = Notification.FLAG_SHOW_LIGHTS;
        }
        if (isFore) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                manager.notify(channelId, notification);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startForeground(channelId, notification);// 开始前台服务
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AllPropertyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        getData();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getData();
        }
    };


    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    private void getData() {
        RetrofitHelper.api().getAllBuilding()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PropertyContainerBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PropertyContainerBean container) {
                        if (container == null) {
                            return;
                        }
                        if (!Pub.isListExists(container.getList())) {
                            return;
                        }
                        String json = getGson().toJson(container);
                        String lastJson = SharedPreferencesUtil.getInstance().get("info", "");
                        if (lastJson.equals(json)) {
                            //postNotification(AllPropertyService.this, "相同信息", false);
                        } else {
                            SharedPreferencesUtil.getInstance().put("info", json);
                            postNotification(AllPropertyService.this, "有新楼盘啦", false);
                        }
                        handler.postDelayed(runnable, 200000);

                        for (PropertyBean model : container.getList()) {
                            putModelInfo(model);
                        }


                    }
                });
    }

    private void putModelInfo(final PropertyBean model) {
        BtbMap map = new BtbMap();
        map.put("json", "true");
        map.put("propertyid", String.valueOf(model.getPropertyid()));
        map.put("siteid", "33");
        RetrofitHelper.api().getBuilding(map)
                .subscribeOn(Schedulers.io())
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
                        map.put("buildingid", id);
                        map.put("siteid", "33");
                        RetrofitHelper.api().getHouses(map)
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<HouseListBean>() {

                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(HouseListBean detailBean) {
                                        if (detailBean != null && Pub.isListExists(detailBean.getList())) {
                                            model.setHasInfo(true);
                                        } else {
                                            model.setHasInfo(false);
                                        }
                                        String newInfo = getGson().toJson(model);
                                        if (jsons.containsKey(model.getPropertyid())) {
                                            if (!jsons.get(model.getPropertyid()).equals(newInfo)) {
                                                postNotification(AllPropertyService.this,
                                                        model.getPropertyname() + "出预售证了", false
                                                );
                                            }
                                        }
                                        jsons.put(model.getPropertyid(), newInfo);

                                    }
                                });
                    }
                });

    }

    private HashMap<String, String> jsons = new HashMap<>();

    private Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public class AllPropertyBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public AllPropertyService getService() {
            return AllPropertyService.this;
        }
    }


}
