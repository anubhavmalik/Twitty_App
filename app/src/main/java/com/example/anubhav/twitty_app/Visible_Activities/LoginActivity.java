package com.example.anubhav.twitty_app.Visible_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.example.anubhav.twitty_app.R;
import com.example.anubhav.twitty_app.Utilities.TwitterUtils;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginActivity extends AppCompatActivity {
    Twitter twitter;
    TwitterSession session;
    TwitterLoginButton twitterLoginButton;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Twitter.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Intent i = new Intent(LoginActivity.this, MainActivity.class);
        if (TwitterUtils.getSession() != null) {
            startActivity(i);
        }
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                startActivity(i);
            }

            @Override
            public void failure(TwitterException exception) {

                Snackbar.make(twitterLoginButton, "Login Unsuccessful", Snackbar.LENGTH_SHORT).show();

            }
        });

    }
}
