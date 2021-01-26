package com.mrwhoami.qqservices.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;




public class TuringApiUtil {


    private static final String key = "0f287105b99643528167c25a302daead";

    public static String getResult( String content ) {

        String apiUrl = "http://www.tuling123.com/openapi/api?key="+key+"&info=";

        try {

            content = URLEncoder.encode(content, "utf-8");
            apiUrl = apiUrl + content;

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        HttpGet request = new HttpGet(apiUrl);

        String result = "";

        try {

            HttpResponse response = HttpClients.createDefault().execute(request);

            int code = response.getStatusLine().getStatusCode();

            if (code == 200) {

                result = EntityUtils.toString(response.getEntity());
            }else {
                System.out.println("code=" + code);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
