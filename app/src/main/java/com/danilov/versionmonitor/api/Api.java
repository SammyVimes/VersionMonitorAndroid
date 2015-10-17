package com.danilov.versionmonitor.api;

import com.danilov.versionmonitor.model.LoginResponse;
import com.danilov.versionmonitor.model.ProjectDetails;
import com.danilov.versionmonitor.model.Project;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Semyon on 14.10.2015.
 */
public interface Api {

    @GET("api/project/")
    Call<List<Project>> getAllProjects(@Query("key") final String token);

    @GET("api/project/{projectId}")
    Call<ProjectDetails> getProjectDetails(@Path("projectId") final String projectId, @Query("key") final String token);

    @POST("api/login")
    @FormUrlEncoded
    Call<LoginResponse> login(@Field("login") final String login, @Field("password") final String password);


}
