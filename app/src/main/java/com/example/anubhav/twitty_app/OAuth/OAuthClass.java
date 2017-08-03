package com.example.anubhav.twitty_app.OAuth;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Anubhav on 02-08-2017.
 */

public class OAuthClass {

    String signature;
    String method;
    String baseurl;

    public String getAuthheader() throws UnsupportedEncodingException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        Authheader="OAuth ";
        getSignature();
        Authheader=Authheader+ URLEncoder.encode("oauth_consumer_key","UTF-8")+"=\""+URLEncoder.encode(oauth_consumer_key,"UTF-8")+"\", "
                +URLEncoder.encode("oauth_nonce","UTF-8")+"=\""+URLEncoder.encode(oauth_nonce,"UTF-8")+"\", "
                +URLEncoder.encode("oauth_signature","UTF-8")+"=\""+URLEncoder.encode(signature,"UTF-8")+"\", "
                +URLEncoder.encode("oauth_signature_method","UTF-8")+"=\""+URLEncoder.encode(oauth_signature_method,"UTF-8")+"\", "
                +URLEncoder.encode("oauth_timestamp","UTF-8")+"=\""+URLEncoder.encode(oauth_timestamp,"UTF-8")+"\", "
                +URLEncoder.encode("oauth_token","UTF-8")+"=\""+URLEncoder.encode(oauth_token,"UTF-8")+"\", "
                +URLEncoder.encode("oauth_version","UTF-8")+"=\""+URLEncoder.encode(oauth_version,"UTF-8")+"\"";
        return Authheader;

    }

    public String displaySignature()
    {
        return signature;
    }

    String Authheader;
    HashMap<String,String> body;

    public OAuthClass setQuery(HashMap<String, String> query) {
        this.query = query;
        return this;
    }

    HashMap<String,String> query;

    String oauth_consumer_key;
    String oauth_nonce;
    String oauth_signature_method;
    String oauth_timestamp;
    String oauth_token;
    String oauth_version;
    String consumersecret;

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }

    public static String calculateRFC2104HMAC(String data, String key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
    {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return Base64.encodeToString(mac.doFinal(data.getBytes()),Base64.DEFAULT);
    }

    public OAuthClass setConsumersecret(String consumersecret) {
        this.consumersecret = consumersecret;
        return this;
    }

    public OAuthClass setTokensecret(String tokensecret) {
        this.tokensecret = tokensecret;
        return this;
    }

    String tokensecret;

    String signaturebase;
    String signaturekey;

    public OAuthClass setMethod(String method) {
        this.method = method;
        return this;
    }

    public OAuthClass setBaseurl(String baseurl) {
        this.baseurl = baseurl;
        return this;
    }

    public OAuthClass setBody(HashMap<String, String> body) {
        this.body = body;
        return this;
    }


    public OAuthClass setOauth_consumer_key(String oauth_consumer_key) {
        this.oauth_consumer_key = oauth_consumer_key;
        return this;
    }

    public OAuthClass setOauth_nonce(String oauth_nonce) {
        this.oauth_nonce = oauth_nonce;
        return this;
    }

    public OAuthClass setOauth_signature_method(String oauth_signature_method) {
        this.oauth_signature_method = oauth_signature_method;
        return this;
    }

    public OAuthClass setOauth_timestamp(String oauth_timestamp) {
        this.oauth_timestamp = oauth_timestamp;
        return this;
    }

    public OAuthClass setOauth_token(String oauth_token) {
        this.oauth_token = oauth_token;
        return this;
    }

    public OAuthClass setOauth_version(String oauth_version) {
        this.oauth_version = oauth_version;
        return this;
    }

    public void getSignature() throws UnsupportedEncodingException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        signaturebase=this.getSignatureBase();
        signaturekey=this.getSignatureKey();
        signature=null;
        this.signature=sign(signaturebase,signaturekey);
        String[] temp= signature.split("\n");
        signature=temp[0]; //Because signature is somehow ending with /n

    }

    public String sign(String signatuase, String signtukey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return calculateRFC2104HMAC(signatuase,signtukey);
    }

    private String getSignatureKey() throws UnsupportedEncodingException {
        String answer=URLEncoder.encode(consumersecret,"UTF-8")+"&"+URLEncoder.encode(tokensecret,"UTF-8");
        return answer;
    }

    private String getSignatureBase() throws UnsupportedEncodingException {
        HashMap<String,String>parameters = new HashMap<String, String>();

        if(oauth_consumer_key!=null)
            parameters.put(URLEncoder.encode("oauth_consumer_key","UTF-8"), URLEncoder.encode(oauth_consumer_key,"UTF-8"));
        if(oauth_nonce!=null)
            parameters.put(URLEncoder.encode("oauth_nonce","UTF-8"), URLEncoder.encode(oauth_nonce,"UTF-8"));
        if(oauth_signature_method!=null)
            parameters.put(URLEncoder.encode("oauth_signature_method","UTF-8"), URLEncoder.encode(oauth_signature_method,"UTF-8"));
        if(oauth_timestamp!=null)
            parameters.put(URLEncoder.encode("oauth_timestamp","UTF-8"), URLEncoder.encode(oauth_timestamp,"UTF-8"));
        if(oauth_token!=null)
            parameters.put(URLEncoder.encode("oauth_token","UTF-8"), URLEncoder.encode(oauth_token,"UTF-8"));
        if(oauth_version!=null)
            parameters.put(URLEncoder.encode("oauth_version","UTF-8"), URLEncoder.encode(oauth_version,"UTF-8"));
        if(body!=null) {
            Iterator it = body.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                parameters.put(URLEncoder.encode((String) pair.getKey(), "UTF-8").replace("+", "%20"), URLEncoder.encode((String) pair.getValue(), "UTF-8").replace("+", "%20"));

            }
        }

        if(query!=null) {
            Iterator it = query.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                parameters.put(URLEncoder.encode((String) pair.getKey(), "UTF-8").replace("+", "%20"), URLEncoder.encode((String) pair.getValue(), "UTF-8").replace("+", "%20"));

            }
        }
        String answer="";
        int i=1;
        SortedSet<String> keys = new TreeSet<String>(parameters.keySet());
        for (String key : keys) {
            String value = parameters.get(key);
            answer=answer+key+"="+value;
            if(i!=parameters.size())
                answer=answer+"&";
            i=i+1;
        }

        Log.d("parameter",answer);
        String finalans=method;
        finalans=finalans.toUpperCase();
        finalans=finalans+"&";
        finalans=finalans+URLEncoder.encode(baseurl,"UTF-8");
        finalans=finalans+"&";
        finalans=finalans+URLEncoder.encode(answer,"UTF-8");
        return  finalans;


    }
}
