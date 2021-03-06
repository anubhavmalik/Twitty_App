package com.example.anubhav.twitty_app.Visible_Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anubhav.twitty_app.Adapter.CustomAdapter;
import com.example.anubhav.twitty_app.Networking.ApiInterface;
import com.example.anubhav.twitty_app.Networking.MyTwitterApiClient;
import com.example.anubhav.twitty_app.R;
import com.example.anubhav.twitty_app.Utilities.AboutActivity;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements CustomAdapter.OnTweetClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    CustomAdapter customAdapter;
    ListView profileListView;
    ProgressBar progressBar;
    Button logoutButton;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 500) {
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getHomeTimeline();
                }
            }, 2000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Twitter.initialize(this);
        profileListView = (ListView) findViewById(R.id.profile_list);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        showProgress(true);

        logoutButton = (Button) findViewById(R.id.nav_logout);
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(R.string.com_twitter_sdk_android_CONSUMER_KEY + "", R.string.com_twitter_sdk_android_CONSUMER_SECRET + "");
        TwitterAuthToken authToken = new TwitterAuthToken("851119038168051713-oSCvGHS8nMkOElBxC8q5PjZ6IfwVvq5", "7CfGlq2J5cp4zV77PQCuk2tL8jNZIUhwVdmEeDws6gyun");
        OAuthSigning oAuthSigning = new OAuthSigning(authConfig, authToken);

        getHomeTimeline();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new TweetComposer.Builder(MainActivity.this)
                        .createIntent();

                startActivityForResult(intent, 500);

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_trending) {
            // Handle the trending action
        } else if (id == R.id.nav_share) {
            //shares app link
        } else if (id == R.id.nav_home) {
            getHomeTimeline();
        } else if (id == R.id.nav_manage) {
            //tools
        } else if (id == R.id.nav_send) {
            //screenshot and send using app you want
            takeScreenshot();
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        } else if (id == R.id.nav_logout) {
            logout(logoutButton);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getHomeTimeline() {
        ApiInterface apiInterface = MyTwitterApiClient.getApiInterface();
        Call<ArrayList<Tweet>> call = apiInterface.getHomeTimeline();
        call.enqueue(new Callback<ArrayList<Tweet>>() {
            @Override
            public void onResponse(Call<ArrayList<Tweet>> call, Response<ArrayList<Tweet>> response) {

                FixedTweetTimeline homeTimeline = new FixedTweetTimeline.Builder()
                        .setTweets(response.body())
                        .build();

                customAdapter = new CustomAdapter(MainActivity.this, homeTimeline, MainActivity.this);
                profileListView.setAdapter(customAdapter);
                TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(true, false, false);
                showProgress(false);
            }

            @Override
            public void onFailure(Call<ArrayList<Tweet>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onTweetClicked(int position, Tweet tweet) {
        Toast.makeText(this, tweet.text, Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void takeScreenshot() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Handler h = new Handler();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String mPath = Environment.getExternalStorageDirectory().toString() + "/twitty" + ".jpg";
                    View v1 = getWindow().getDecorView().getRootView();
                    v1.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                    v1.setDrawingCacheEnabled(false);

                    File imageFile = new File(mPath);

                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    openScreenshot(imageFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Couldn't take screenshot", Toast.LENGTH_SHORT).show();
                }
            }

            private void openScreenshot(File imageFile) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.fromFile(imageFile);
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);
            }

        }, 500);

    }

}