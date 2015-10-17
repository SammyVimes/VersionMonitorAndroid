package com.danilov.versionmonitor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.danilov.versionmonitor.api.ApiService;
import com.danilov.versionmonitor.model.LoginResponse;

public class LoginActivity extends BaseActivity {

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = sharedPreferences.getString("TOKEN", "");
        if (!"".equals(token)) {
            proceed();
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
        coordinatorLayout = view(R.id.coordinator);
        final EditText loginET = view(R.id.login);
        final EditText passwordET = view(R.id.password);
        final EditText hostET = view(R.id.host);
        String host = sharedPreferences.getString("BASE_URL", "");
        if (!"".equals(host)) {
            hostET.setText(host);
        }
        final Button loginBtn = view(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String login = loginET.getText().toString();
                String password = passwordET.getText().toString();
                String host = hostET.getText().toString();
                ApiService.getInstance().setBaseUrl(host);
                LoginTask loginTask = new LoginTask();
                loginTask.execute(login, password);
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, LoginResponse> {

        @Override
        protected LoginResponse doInBackground(final String... params) {
            return ApiService.getInstance().login(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(final LoginResponse loginResponse) {
            if (loginResponse == null) {
                return;
            }
            if (loginResponse.getSuccess()) {
                final Context context = getApplicationContext();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                sharedPreferences.edit().putString("TOKEN", loginResponse.getKey()).apply();
                proceed();
            } else {
                SpannableStringBuilder snackbarText = new SpannableStringBuilder();
                snackbarText.append("Ошибка: ");
                int boldStart = snackbarText.length();
                snackbarText.append(loginResponse.getReason().getDef());
                snackbarText.setSpan(new ForegroundColorSpan(0xFFFF0000), boldStart, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), boldStart, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                snackbarText.append(", попробуйте ещё раз.");

                Snackbar.make(coordinatorLayout, snackbarText, Snackbar.LENGTH_LONG).show();
            }
        }

    }

    private void proceed() {
        startActivity(new Intent(this, AppsActivity.class));
    }

}
