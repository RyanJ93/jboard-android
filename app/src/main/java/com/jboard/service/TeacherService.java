package com.jboard.service;

import com.jboard.exception.*;
import com.jboard.model.Teacher;
import com.jboard.model.TeacherList;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class TeacherService extends Service {
    private static final String TEACHER_LIST_ENDPOINT_URL = "/api/teacher/list";
    private static final String TEACHER_CREATE_ENDPOINT_URL = "/api/teacher/create";
    private static final String TEACHER_DELETE_ENDPOINT_URL = "/api/teacher/delete?id={ID}";

    private Teacher teacher;

    public TeacherService(){}

    public TeacherService(Teacher teacher){
        this.teacher = teacher;
    }

    public TeacherService setTeacher(Teacher teacher){
        this.teacher = teacher;
        return this;
    }

    public Teacher getTeacher(){
        return this.teacher;
    }

    public TeacherList list() throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        try{
            String token = UserService.getUserToken();
            JSONObject response = this.sendRequest(TeacherService.TEACHER_LIST_ENDPOINT_URL, token, null);
            return new TeacherList(response.getJSONArray("data"));
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }
    }

    public Teacher create(String name, String surname) throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        try{
            HashMap<String, String> params = new HashMap<>();
            String token = UserService.getUserToken();
            params.put("surname", surname);
            params.put("name", name);
            JSONObject response = this.sendRequest(TeacherService.TEACHER_CREATE_ENDPOINT_URL, token, params);
            return this.teacher = new Teacher(response.getJSONObject("data"));
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }
    }

    public TeacherService delete() throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        if ( this.teacher == null ){
            throw new RuntimeException("No teacher defined.");
        }
        String url = TeacherService.TEACHER_DELETE_ENDPOINT_URL;
        String token = UserService.getUserToken();
        url = url.replace("{ID}", Integer.toString(this.teacher.getID()));
        this.sendRequest(url, token, null);
        return this;
    }
}
