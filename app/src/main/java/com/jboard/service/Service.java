package com.jboard.service;

import com.jboard.exception.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class Service {
    protected static boolean isDevelopmentMode = true;

    protected static String getAPIEndpointBaseURL(){
        return Service.isDevelopmentMode ? "http://192.168.2.1:7777" : "http://jboard.enricosola.com";
    }

    protected int[] ignoredErrorCodes;

    private byte[] prepareRequestBody(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        for ( Map.Entry<String,String> param : params.entrySet() ){
            if ( stringBuilder.length() > 0 ){
                stringBuilder.append("&");
            }
            stringBuilder.append(URLEncoder.encode(param.getKey(), "UTF-8")).append("=");
            stringBuilder.append(URLEncoder.encode(param.getValue(), "UTF-8"));
        }
        return stringBuilder.toString().getBytes("UTF-8");
    }

    private void checkResponse(JSONObject response) throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        try{
            int code = response.has("code") ? response.getInt("code") : 0;
            boolean errorShouldBeHandled = true;
            int j = 0;
            if ( this.ignoredErrorCodes != null && this.ignoredErrorCodes.length > 0 ){
                while ( errorShouldBeHandled && j < this.ignoredErrorCodes.length ){
                    if ( this.ignoredErrorCodes[j] == code ){
                        errorShouldBeHandled = false;
                    }
                    j++;
                }
            }
            if ( errorShouldBeHandled ){
                if ( code == 403 ){
                    throw new UnauthorizedException("Unauthorized.");
                }else if ( code == 400 ){
                    HashMap<String, String> validationMessages = new HashMap<>();
                    if ( response.has("message") ){
                        validationMessages.put("_global", response.getString("message"));
                    }else{
                        JSONObject messages = response.getJSONObject("messages");
                        JSONArray keys = messages.names();
                        if ( keys != null ){
                            for ( int i = 0 ; i < keys.length() ; i++ ){
                                String key = keys.get(i).toString();
                                validationMessages.put(key, messages.getString(key));
                            }
                        }
                    }
                    throw new InvalidInputException("Invalid sent data.", validationMessages);
                }else if ( code != 200 ){
                    throw new OperationalException("Server error.");
                }
            }
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }
    }

    protected JSONObject sendRequest(String path, String token, HashMap<String, String> params) throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        try{
            String completeURL = Service.getAPIEndpointBaseURL() + path;
            if ( token != null && !token.isEmpty() ){
                completeURL += ( completeURL.contains("?") ? "&" : "?" ) + "token=" + token;
            }
            URL url = new URL(completeURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setInstanceFollowRedirects(true);
            if ( params != null ){
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                byte[] body = this.prepareRequestBody(params);
                httpURLConnection.setFixedLengthStreamingMode(body.length);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                httpURLConnection.connect();
                try ( OutputStream outputStream = httpURLConnection.getOutputStream() ){
                    outputStream.write(body);
                }
            }
            int status = httpURLConnection.getResponseCode();
            if ( status != 200 ){
                throw new NetworkException("HTTP request error.");
            }
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ( ( line = bufferedReader.readLine() ) != null ) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            httpURLConnection.disconnect();System.out.println(stringBuilder.toString());
            JSONObject response = new JSONObject(stringBuilder.toString());
            this.checkResponse(response);
            return response;
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }catch(IOException ex){
            throw new NetworkException("HTTP request error.", ex);
        }
    }
}
