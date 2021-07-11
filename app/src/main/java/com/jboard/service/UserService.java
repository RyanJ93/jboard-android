package com.jboard.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.jboard.exception.*;
import com.jboard.model.User;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class UserService extends Service {
    private static final String USER_GET_ENDPOINT_URL = "/api/user/get";
    private static final String USER_LOGIN_ENDPOINT_URL = "/api/user/login";
    private static final String USER_LOGOUT_ENDPOINT_URL = "/api/user/logout";

    private static String userToken = null;
    private static User authenticatedUser;

    public static boolean isUserTokenDefined(){
        return UserService.userToken != null && !UserService.userToken.isEmpty();
    }

    public static String getUserToken(){
        return UserService.userToken;
    }

    public static User getAuthenticatedUser(){
        return UserService.authenticatedUser;
    }

    private Context context;

    private void storeUserToken(String userToken){
            if ( this.context != null ){
            SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.jboard.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userToken", userToken);
            editor.apply();
            UserService.userToken = userToken;
        }
    }

    private void destroyUserToken(){
        if ( this.context != null ){
            SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.jboard.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            UserService.userToken = null;
            editor.remove("userToken");
            editor.apply();
        }
    }

    public UserService(){
        super();
        this.ignoredErrorCodes = new int[]{404};
    }

    public UserService setContext(Context context){
        this.context = context;
        return this;
    }

    public Context getContext(){
        return this.context;
    }

    public UserService loadUserToken(){
        if ( this.context != null ){
            SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.jboard.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
            UserService.userToken = sharedPreferences.getString("userToken", null);
        }
        return this;
    }

    public User fetchAuthenticatedUser() throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        UserService.authenticatedUser = null;
        if ( UserService.userToken != null && !UserService.userToken.isEmpty() ){
            try{
                JSONObject response = this.sendRequest(UserService.USER_GET_ENDPOINT_URL, UserService.userToken, null);
                JSONObject data = response.getJSONObject("data");
                UserService.authenticatedUser = new User();
                UserService.authenticatedUser.setPropertiesFromJSONObject(data.getJSONObject("user"));
            }catch(UnauthorizedException ex){
                this.destroyUserToken();
                throw ex;
            }catch(JSONException ex){
                throw new NetworkException("Malformed response.", ex);
            }
        }
        return UserService.authenticatedUser;
    }

    public User login(String account, String password) throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException, UserNotFoundException {
        try{
            HashMap<String, String> params = new HashMap<>();
            params.put("account", account);
            params.put("password", password);
            params.put("useAuthToken", "1");
            UserService.authenticatedUser = null;
            this.destroyUserToken();
            JSONObject response = this.sendRequest(UserService.USER_LOGIN_ENDPOINT_URL, null, params);
            int code = response.getInt("code");
            if ( code == 404 ){
                throw new UserNotFoundException("No such user found.");
            }
            JSONObject data = response.getJSONObject("data");
            UserService.authenticatedUser = new User();
            UserService.authenticatedUser.setPropertiesFromJSONObject(data.getJSONObject("user"));
            this.storeUserToken(data.getString("token"));
            return UserService.authenticatedUser;
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }
    }

    public UserService logout() throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        String token = UserService.getUserToken();
        this.sendRequest(UserService.USER_LOGOUT_ENDPOINT_URL, token, null);
        UserService.authenticatedUser = null;
        this.destroyUserToken();
        return this;
    }
}
