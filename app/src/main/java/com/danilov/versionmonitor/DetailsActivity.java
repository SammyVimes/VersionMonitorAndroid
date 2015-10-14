package com.danilov.versionmonitor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Callback;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

    private boolean loaded = false;

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

        view(R.id.download_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!loaded) {
                    return;
                }
                downloadVersion(project.getId(), project.getLastVersionInt());
            }
        });

        Intent intent = getIntent();
        long projectId = intent.getLongExtra(PROJECT_ID_EXTRA, -1);
        if (projectId != -1) {
            DetailsTask detailsTask = new DetailsTask(projectId);
            detailsTask.execute();
        }
    }

    @Override
    public void onClick(final View v) {
        if (!loaded) {
            return;
        }
    }

    private void downloadVersion(final long projectId, final int versionInt) {
        DownloadTask downloadTask = new DownloadTask(projectId, versionInt);
        downloadTask.execute();
    }

    class DownloadTask extends AsyncTask<Void, Integer, File> {

        private long projectId;
        private int versionInt;


        private ProgressDialog progressDialog;

        public DownloadTask(final long projectId, final int versionInt) {
            this.projectId = projectId;
            this.versionInt = versionInt;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DetailsActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Загрузка...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected File doInBackground(final Void... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(ApiService.getAppApk(projectId, versionInt))
                    .addHeader("Content-Type", "application/json").build();
            Response response = null;
            try {
                response = client.newCall(request).execute();

                InputStream in = response.body().byteStream();
                long lenghtOfFile = response.body().contentLength();

                File externalFilesDir = context.getExternalFilesDir(null);
                if (externalFilesDir == null) {
                    externalFilesDir = context.getFilesDir();
                }
                File outputFile = new File(externalFilesDir.getPath() + "/app.apk");

                BufferedInputStream input = new BufferedInputStream(in);
                FileOutputStream output = new FileOutputStream(outputFile, false);

                byte data[] = new byte[1024];
                int count;
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int)(total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                return outputFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final File file) {
            if (file != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            progressDialog.dismiss();
            progressDialog = null;
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            progressDialog.setProgress(values[0]);
        }

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
            if (projectDetails != null) {
                project = projectDetails.getProject();
                appName.setText(project.getName());
                appPackageName.setText(project.getPackageName());
                appLastVersion.setText(project.getLastVersionString());
                appDescription.setText(project.getDefinition());
                picasso.load(ApiService.getAppIconUrl(project.getId(), project.getLastVersionInt())).into(appIcon);


                List<Version> versions = projectDetails.getVersions();
                VersionsAdapter versionsAdapter = new VersionsAdapter(versions);
                versionsList.setAdapter(versionsAdapter);
                loaded = true;
            }
        }

    }

    private class VersionsAdapter extends RecyclerView.Adapter<VersionViewHolder> {

        private List<Version> versions;

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
