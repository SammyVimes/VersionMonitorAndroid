package com.danilov.versionmonitor.api;

import com.danilov.versionmonitor.model.ProjectDetails;
import com.danilov.versionmonitor.model.Project;

import java.io.IOException;
import java.util.ArrayList;
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
        List<Project> projects = new ArrayList<>();
        try {
            Response<List<Project>> execute = api.getAllProjects().execute();
            projects.addAll(execute.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public ProjectDetails getProjectDetails(final long projectId) {
        try {
            Response<ProjectDetails> execute = api.getProjectDetails("" + projectId).execute();
            return execute.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAppIconUrl(final long projectId, final int versionId) {
        return Config.URL + "project/" + projectId + "/" + versionId + "/icon";
    }

    private static ApiService ourInstance = new ApiService();

    public static ApiService getInstance() {
        return ourInstance;
    }

    private ApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }
}
