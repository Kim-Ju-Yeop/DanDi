package org.techtown.schooler.network.retrofit.interfaces;

import org.techtown.schooler.network.Data;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ChannelEvent {

    @GET("/channel-event")
    Call<Response<Data>> getChannelEvent(@Header("Token")String token,
                                         @Query("channel_id")String channel_id);

    @POST("/channel-event/add")
    Call<Response<Data>> AddChannelEvent(@Header("Token")String token,
                                         @Query("channel_id")String channel_id,
                                         @Body String title,
                                         @Body String start_date,
                                         @Body String end_date);

    @DELETE("/channel-event/delete")
    Call<Response<Data>> DeleteChannelEvent(@Header("Token")String token,
                                            @Query("event_id")String event_id);

    @PUT("/channel-event/update")
    Call<Response<Data>> UpdateChannelEvent(@Header("Token")String token,
                                            @Query("event_id")String event_id,
                                            @Body String title,
                                            @Body String start_date,
                                            @Body String end_date);

    @GET("/channel-event/search")
    Call<org.techtown.schooler.network.response.Response<Data>> SearchChannelEvent(@Header("Token") String Token,
                                                                                   @Query("channel_id") String channel_id,
                                                                                   @Query("keyword") String keyword);
}
