package com.danilov.versionmonitor.api;

import com.danilov.versionmonitor.model.ProjectDetails;
import com.danilov.versionmonitor.model.Project;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Semyon on 14.10.2015.
 */
public interface Api {

    @GET("api/project/")
    Call<List<Project>> getAllProjects();

    @GET("api/project/{projectId}")
    Call<ProjectDetails> getProjectDetails(@Path("projectId") final String projectId);


}
