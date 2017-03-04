package com.example.shichaonie.eduassist.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Shichao Nie on 2016/12/22.
 */

public final class HttpUtil {
    public static String myConnectionPOST(String info, String Url){
        HttpURLConnection conn = null;
        String is = null;
        URL url = createUrl(Url);
        try {
            assert url != null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("ser-Agent", "Fiddler");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(info.getBytes());
            os.close();
            if(conn.getResponseCode() == 200){
                is = readFromStream(conn.getInputStream());
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            assert conn != null;
            conn.disconnect();
        }
        return is;
    }
    public static String myConnectionGET(String Url){
        URL url = createUrl(Url);
        HttpURLConnection conn = null;
        String is = null;
        try {
            assert url != null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(10 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.connect();
            if(conn.getResponseCode() == 200){
                is = readFromStream(conn.getInputStream());
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            assert conn != null;
            conn.disconnect();
        }
        return is;
    }
    public static URL createUrl(String StringUrl) {
        URL url = null;
        try {
            url = new URL(StringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return url;
    }
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
