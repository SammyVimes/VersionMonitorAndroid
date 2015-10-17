package com.danilov.versionmonitor.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.danilov.versionmonitor.App;
import com.danilov.versionmonitor.model.LoginResponse;
import com.danilov.versionmonitor.model.ProjectDetails;
import com.danilov.versionmonitor.model.Project;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Semyon on 14.10.2015.
 */
public class ApiService {

    private Api api = null;

    public List<Project> getAllProjects() {

        final Context context = App.getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = sharedPreferences.getString("TOKEN", "");

        List<Project> projects = new ArrayList<>();
        try {
            Response<List<Project>> execute = api.getAllProjects(token).execute();
            projects.addAll(execute.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public ProjectDetails getProjectDetails(final long projectId) {

        final Context context = App.getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = sharedPreferences.getString("TOKEN", "");

        try {
            Response<ProjectDetails> execute = api.getProjectDetails("" + projectId, token).execute();
            return execute.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LoginResponse login(final String login, final String password) {
        try {
            Response<LoginResponse> execute = api.login(login, password).execute();
            return execute.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAppIconUrl(final long projectId, final int versionId) {
        return url + "api/project/" + projectId + "/" + versionId + "/icon";
    }

    public String getAppApk(final long projectId, final int versionId) {

        final Context context = App.getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = sharedPreferences.getString("TOKEN", "");

        return url + "api/project/" + projectId + "/" + versionId + "/apk?token=" + token;
    }

    private static ApiService ourInstance = new ApiService();

    public static ApiService getInstance() {
        return ourInstance;
    }

    private Retrofit retrofit = null;
    private String url = Config.URL;

    private ApiService() {
        OkHttpClient client = new OkHttpClient();
        client.setProtocols(Collections.singletonList(Protocol.HTTP_1_1));

        final Context context = App.getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String baseUrl = sharedPreferences.getString("BASE_URL", "");
        if ("".equals(baseUrl)) {
            baseUrl = Config.URL;
        }

        url = baseUrl;
        retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        api = retrofit.create(Api.class);
    }

    public void setBaseUrl(final String url) {
        final Context context = App.getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString("BASE_URL", url).apply();

        OkHttpClient client = new OkHttpClient();
        client.setProtocols(Collections.singletonList(Protocol.HTTP_1_1));

        this.url = url;
        retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        api = retrofit.create(Api.class);
    }

}
