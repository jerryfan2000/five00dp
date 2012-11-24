package com.nyuen.five00dp;

import com.fivehundredpx.api.FiveHundredException;
import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.api.tasks.XAuth500pxTask;
import com.fivehundredpx.api.tasks.XAuth500pxTask.Delegate;
import com.fivehundredpx.api.auth.AccessToken;
import com.nyuen.five00dp.api.FiveHundred;
import com.nyuen.five00dp.util.AccountUtils;
import com.nyuen.five00dp.util.Toaster;
import com.nyuen.five00dp.util.UIUtils;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AccountAuthenticatorActivity implements XAuth500pxTask.Delegate{
    private static final String TAG = "LoginActivity";
    
    AccessToken accessToken;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);

        if (AccountUtils.hasAccount(this)) {
            Toaster.get().showShortText("Can only have 1 account");
            finish();
        }

        initUi();
    }

    private void initUi() {
        View loginPanel = findViewById(R.id.loginPanel);
        TextView tvUsername = (TextView) findViewById(R.id.inputEmail);
        TextView tvPassword = (TextView) findViewById(R.id.inputPassword);
        Button btnLogin = (Button) findViewById(R.id.buttonLogin);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);

        tvUsername.setError(null);
        tvPassword.setError(null);

        loginPanel.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
    }

    private void onLogin() {
        View loginPanel = findViewById(R.id.loginPanel);
        TextView tvUsername = (TextView) findViewById(R.id.inputEmail);
        TextView tvPassword = (TextView) findViewById(R.id.inputPassword);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);

        String username = tvUsername.getText().toString();
        String password = tvPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            tvUsername.setError(getString(R.string.no_name));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            tvUsername.setError(getString(R.string.no_password));
            return;
        }

        loginPanel.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        UIUtils.hideKeyboard(this, progressBar);

        XAuth500pxTask loginTask = new XAuth500pxTask(this);
        loginTask.execute(FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET, username, password);
    }

    @Override
    public void onSuccess(AccessToken result) {
       Log.w(TAG, "success "+result);
       
    }

    @Override
    public void onFail(FiveHundredException e) {
        //initUi();
        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
    }

    
    //    private class LoginTask extends AsyncTask<String, Void, LoginResponse> {
    //        
    //        Context mContext;
    //        String mUsername;
    //        String mPassword;
    //        
    //        LoginTask(Context context) {
    //            mContext = context;
    //        }
    //
    //        @Override
    //        protected LoginResponse doInBackground(String... params) {
    //            
    //            try {
    //                mUsername = params[0];
    //                mPassword = params[1];
    //                
    //                String json = Http.get().httpGet(MaizeApi.BASE_URL + "/api",
    //                        new BasicNameValuePair("a", "l"),
    //                        new BasicNameValuePair("u", mUsername),
    //                        new BasicNameValuePair("p", mPassword)
    //                );
    //                
    //                return new Gson().fromJson(json, LoginResponse.class);
    //                
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //            }
    //            
    //            return null;
    //        }
    //        
    //        @Override
    //        protected void onPostExecute(LoginResponse result) {
    //            if (result == null) {
    //                initUi();
    //                Toast.makeText(mContext, R.string.login_failed, Toast.LENGTH_SHORT).show();
    //                return;
    //            }
    //            
    //            if (result.success) {
    //                Bundle userData = new Bundle();
    //                userData.putString("nickname", result._userinfo.nickname);
    //                userData.putString("userid", String.valueOf(result._userinfo.id_key));
    //                
    //                Account account = new Account(mUsername, getString(R.string.ACCOUNT_TYPE));
    //                AccountManager am = AccountManager.get(mContext);
    //                
    //                if (am.addAccountExplicitly(account, mPassword, userData)) {
    //                    Bundle bundle = new Bundle();
    //                    bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
    //                    bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
    //                    setAccountAuthenticatorResult(bundle);
    //                    Toast.makeText(mContext, getString(R.string.welcome, result._userinfo.nickname), Toast.LENGTH_SHORT).show();
    //                    
    //                    // Set syncable
    //                    ContentResolver.setIsSyncable(account, FavoriteProvider.AUTHORITY, 1);
    //                    // Start periodic sync
    //                    ContentResolver.addPeriodicSync(account, FavoriteProvider.AUTHORITY, new Bundle(), 14400);
    //                    // Enable sync
    //                    ContentResolver.setSyncAutomatically(account, FavoriteProvider.AUTHORITY, true);
    //                    // Make it start right now
    //                    ContentResolver.requestSync(account, FavoriteProvider.AUTHORITY, new Bundle());
    //                    
    //                    setResult(RESULT_OK);
    //                    
    //                    finish();
    //                } else {
    //                    initUi();
    //                    Toast.makeText(mContext, R.string.login_failed, Toast.LENGTH_SHORT).show();
    //                }
    //            } else {
    //                initUi();
    //                Toast.makeText(mContext, result.error_msg, Toast.LENGTH_SHORT).show();
    //            }
    //        }
    //    }
}
