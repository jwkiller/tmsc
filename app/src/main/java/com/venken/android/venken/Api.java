package com.venken.android.venken;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface Api {
    @GET("tmj3/search_property.jspx?_d=5559&areaid=33&areazoom=&distance=&hxzoom=&keyword=&lat=30.288041287731804&lng=120.00990146432434&lpztzoom=&mjzoom=&nosoldout=true&nostate=true&orderby=1&page=1&pricezoom=&subid=&subidzoom=&tesezoom=&wylxzoom=&yhztzoom=&zxzoom=")
    Observable<PropertyContainerBean> getAllBuilding();

//    @GET("tmj3/search_property.jspx?_d=6909&areaid=33&areazoom=all&distance=&hxzoom=all&keyword=&lat=30.287419226273038&lng=120.00914223313839&lpztzoom=all&mjzoom=all&nosoldout=true&nostate=true&orderby=0&page=1&pricezoom=0_999999&subid=&subidzoom=all&tesezoom=all&wylxzoom=all&yhztzoom=,0&zxzoom=all")
//    Observable<PropertyContainerBean> getAllBuilding();

    @GET("tmj3/property_detail.jspx")
    Observable<PropertyDetailBean> getBuilding(@QueryMap BtbMap btbMap);

    //http://jia3.tmsf.com/tmj3/property_control.jspx?area=&buildingid=1180180938&housetype=1&page=1&propertyid=427253360&siteid=33

    @GET("tmj3/property_control.jspx")
    Observable<HouseListBean> getHouses(@QueryMap BtbMap btbMap);

    @GET("tmj3/news_list.jspx")
    Observable<NewsBean> getNews(@QueryMap BtbMap btbMap);


}
