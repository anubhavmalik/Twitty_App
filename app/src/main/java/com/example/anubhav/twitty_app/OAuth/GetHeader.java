package com.example.anubhav.twitty_app.OAuth;

import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.HashMap;

/**
 * Created by Anubhav on 02-08-2017.
 */

public class GetHeader {
    public String header;
    OAuthClass oAuthClass = new OAuthClass();
    Twitter twitter = Twitter.getInstance();

    public GetHeader(String URL, String method, HashMap<String, String> querymap, HashMap<String, String> bodymap) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        TwitterAuthToken authToken = twitterSession.getAuthToken();

        header = oAuthClass

                /**To be fetched from Shared Preference after the app passes testing phase.*/


                .setConsumersecret("4DuhkYTczyeeyEuOkhCywGSuwY1fl4jgYVPT8ynXW1wTR8VJq5")

                .setTokensecret("ycHX6jyuMjQvDtULWN8ksjxOnqM5fBHVKLM3sP5iCPCMI")

                .setOauth_consumer_key("jv2jKbjGdF6jRZ85Tv6RTscqC")

                .setOauth_token("851119038168051713-zGmSFxDmxfwA5WWalV9yNxF9wJd7c7K")

                .setOauth_signature_method("HMAC-SHA1")

                .setOauth_version("1.0")

                .setOauth_nonce(randomString())//should be a random string everytime
                .setOauth_timestamp(System.currentTimeMillis() / 1000 + "") //current epoch time
                .setBody(bodymap) //set to null if there is no request body
                .setQuery(querymap)
                .setBaseurl(URL)
                .setMethod(method)
                .getAuthheader();
    }

    public String randomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);

    }
}

