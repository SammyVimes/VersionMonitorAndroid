package com.danilov.versionmonitor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danilov.versionmonitor.api.ApiService;
import com.danilov.versionmonitor.model.IndexResponse;
import com.danilov.versionmonitor.model.Project;
import com.danilov.acentrifugo.PushService;
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

    class ProjectsTask extends AsyncTask<Void, Void, IndexResponse> {

        @Override
        protected IndexResponse doInBackground(final Void... params) {
            return ApiService.getInstance().getAllProjects();
        }

        @Override
        protected void onPostExecute(final IndexResponse projects) {
            if (projects != null) {
                if (projects.getIndexResult() != null) {
                    if (projects.getIndexResult() == IndexResponse.IndexResult.NOT_AUTHORIZED) {
                        PreferenceManager.getDefaultSharedPreferences(AppsActivity.this).edit().putString("TOKEN", "").apply();
                        startActivity(new Intent(AppsActivity.this, LoginActivity.class));
                        finish();
                    }
                    return;
                }
                appsList.setAdapter(new AppsAdapter(projects.getProjects()));
            }
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
            picasso.load(ApiService.getInstance().getAppIconUrl(project.getId(), project.getLastVersionInt()))
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("TOKEN", "").apply();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
//        PushService.start(this,
//                "ws://192.168.0.73:8001/connection/websocket",
//                "my-client-id",
//                "1446134472610",
//                "f6ead68483cc078cde1df20fde625a265030983d297a9ca0a9cb4e6347bcb18a",
//                "python");
        super.onResume();
    }

}
