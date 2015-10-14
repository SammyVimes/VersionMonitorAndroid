package com.danilov.versionmonitor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danilov.versionmonitor.api.ApiService;
import com.danilov.versionmonitor.model.Project;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AppsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView appsList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        appsList = (RecyclerView) findViewById(R.id.apps_list);
        appsList.setLayoutManager(new LinearLayoutManager(this));
        ProjectsTask projectsTask = new ProjectsTask();
        projectsTask.execute();
    }

    @Override
    public void onClick(final View v) {
        int position = appsList.getChildLayoutPosition(v);
        AppsAdapter adapter = (AppsAdapter) appsList.getAdapter();
        Project project = adapter.getProjects().get(position);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.PROJECT_ID_EXTRA, project.getId());
        startActivity(intent);
    }

    class ProjectsTask extends AsyncTask<Void, Void, List<Project>> {

        @Override
        protected List<Project> doInBackground(final Void... params) {
            return ApiService.getInstance().getAllProjects();
        }

        @Override
        protected void onPostExecute(final List<Project> projects) {
            appsList.setAdapter(new AppsAdapter(projects));
        }

    }

    private class AppsAdapter extends RecyclerView.Adapter<AppViewHolder> {

        private List<Project> projects;

        private Context context = getApplicationContext();
        private Picasso picasso = null;

        {
            OkHttpClient client = new OkHttpClient();
            client.setProtocols(Collections.singletonList(Protocol.HTTP_1_1));
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttpDownloader(client));
            picasso = builder.build();

        }

        public AppsAdapter(final List<Project> projects) {
            this.projects = projects;
        }

        @Override
        public AppViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_list_item, parent, false);
            v.setOnClickListener(AppsActivity.this);
            return new AppViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final AppViewHolder holder, final int position) {
            Project project = projects.get(position);
            holder.appName.setText(project.getName());
            holder.appVersion.setText(project.getLastVersionString());
            picasso.load(ApiService.getAppIconUrl(project.getId(), project.getLastVersionInt()))
                    .into(holder.appIcon, new Callback() {
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

        public List<Project> getProjects() {
            return projects;
        }

        @Override
        public int getItemCount() {
            return projects.size();
        }

    }

    private class AppViewHolder extends RecyclerView.ViewHolder {

        ImageView appIcon;
        private TextView appName;
        private TextView appVersion;

        public AppViewHolder(final View itemView) {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.project_icon);
            appName = (TextView) itemView.findViewById(R.id.project_name);
            appVersion = (TextView) itemView.findViewById(R.id.project_version);
        }
    }

}
