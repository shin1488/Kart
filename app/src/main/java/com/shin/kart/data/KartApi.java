package com.shin.kart.data;

import android.content.Context;
import android.util.Log;

import com.shin.kart.R;


import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class KartApi {

    public Context context;
    public String stringData;
    public String responseBody;

    public KartApi (String stringData, Context context) {
        this.stringData = stringData;
        this.context = context;
    }

    public void main() {

        String apiKey = context.getResources().getString(R.string.api_key);
        String apiURL = "https://api.nexon.co.kr/kart/v1.0/users/nickname/" + stringData;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", apiKey);
        responseBody = get(apiURL,requestHeaders);
        Log.d("Rp", responseBody);
        parseJsonData(responseBody);
    }

    private String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }

        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    private static void parseJsonData(String responseBody) {
        String accessId;
        String userName;
        Long level;
        try {
            Object object = null;
            JSONParser jsonParser = new JSONParser();
            object = jsonParser.parse(responseBody);

            accessId = (String) ((JSONObject)object).get("accessId");
            userName = (String) ((JSONObject)object).get("name");
            level = Long.parseLong(((JSONObject)object).get("level").toString());

            System.out.println("id : " + accessId);
            System.out.println("name : " + userName);
            System.out.println("level : " + level);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}