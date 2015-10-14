package com.danilov.versionmonitor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danilov.versionmonitor.api.ApiService;
import com.danilov.versionmonitor.model.ProjectDetails;
import com.danilov.versionmonitor.model.Project;
import com.danilov.versionmonitor.model.Version;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Semyon on 14.10.2015.
 */
public class DetailsActivity extends BaseActivity implements View.OnClickListener {

    public static final String PROJECT_ID_EXTRA = "PROJECT_ID_EXTRA";

    private TextView appName;
    private TextView appPackageName;
    private TextView appLastVersion;
    private TextView appDescription;
    private ImageView appIcon;
    private RecyclerView versionsList;

    private Project project = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);

        appName = view(R.id.project_name);
        appIcon = view(R.id.project_icon);
        appPackageName = view(R.id.package_name);
        appLastVersion = view(R.id.project_version);
        appDescription = view(R.id.project_description);
        versionsList = view(R.id.versions_list);
        versionsList.setLayoutManager(new LinearLayoutManager(context));

        Intent intent = getIntent();
        long projectId = intent.getLongExtra(PROJECT_ID_EXTRA, -1);
        if (projectId != -1) {
            DetailsTask detailsTask = new DetailsTask(projectId);
            detailsTask.execute();
        }
    }

    @Override
    public void onClick(final View v) {

    }


    class DetailsTask extends AsyncTask<Void, Void, ProjectDetails> {

        private long projectId;

        public DetailsTask(final long projectId) {
            this.projectId = projectId;
        }

        @Override
        protected ProjectDetails doInBackground(final Void... params) {
            return ApiService.getInstance().getProjectDetails(projectId);
        }

        @Override
        protected void onPostExecute(final ProjectDetails projectDetails) {
            project = projectDetails.getProject();
            appName.setText(project.getName());
            appPackageName.setText(project.getPackageName());
            appLastVersion.setText(project.getLastVersionString());
            appDescription.setText(project.getDefinition());
            picasso.load(ApiService.getAppIconUrl(project.getId(), project.getLastVersionInt())).into(appIcon);


            List<Version> versions = projectDetails.getVersions();
            VersionsAdapter versionsAdapter = new VersionsAdapter(versions);
            versionsList.setAdapter(versionsAdapter);
        }

    }

    private class VersionsAdapter extends RecyclerView.Adapter<VersionViewHolder> {

        private List<Version> versions;

        private Context context = getApplicationContext();

        public VersionsAdapter(final List<Version> versions) {
            this.versions = versions;
        }

        @Override
        public VersionViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.version_list_item, parent, false);
            v.setOnClickListener(DetailsActivity.this);
            return new VersionViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final VersionViewHolder holder, final int position) {
            Version version = versions.get(position);
            holder.versionInt.setText(version.getVersionInteger());
            holder.versionString.setText(version.getVersionString());
            holder.versionChanges.setText(version.getChanges());
            picasso.load(ApiService.getAppIconUrl(project.getId(), Integer.parseInt(version.getVersionInteger())))
                    .into(holder.versionIcon, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("a", "success");
                        }

                        @Override
                        public void onError() {
                            Log.d("a", "error");
                        }
                    });
        }

        public List<Version> getVersions() {
            return versions;
        }

        @Override
        public int getItemCount() {
            return versions.size();
        }

    }

    private static class VersionViewHolder extends RecyclerView.ViewHolder {

        private ImageView versionIcon;
        private TextView versionInt;
        private TextView versionString;
        private TextView versionChanges;

        public VersionViewHolder(final View itemView) {
            super(itemView);
            versionIcon = (ImageView) itemView.findViewById(R.id.version_icon);
            versionInt = (TextView) itemView.findViewById(R.id.version_int);
            versionString = (TextView) itemView.findViewById(R.id.version_string);
            versionChanges = (TextView) itemView.findViewById(R.id.version_changes);
        }
    }

}
