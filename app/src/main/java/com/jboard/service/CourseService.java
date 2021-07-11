package com.jboard.service;

import com.jboard.exception.*;
import com.jboard.model.Course;
import com.jboard.model.CourseList;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class CourseService extends Service {
    private static final String COURSE_LIST_ENDPOINT_URL = "/api/course/list";
    private static final String COURSE_CREATE_ENDPOINT_URL = "/api/course/create";
    private static final String COURSE_DELETE_ENDPOINT_URL = "/api/course/delete?id={ID}";

    private Course course;

    public CourseService(){}

    public CourseService(Course course){
        this.course = course;
    }

    public CourseService setCourse(Course course){
        this.course = course;
        return this;
    }

    public Course getCourse(){
        return this.course;
    }

    public CourseList list() throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        try{
            String token = UserService.getUserToken();
            JSONObject response = this.sendRequest(CourseService.COURSE_LIST_ENDPOINT_URL, token, null);
            return new CourseList(response.getJSONArray("data"));
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }
    }

    public Course create(String title) throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        try{
            HashMap<String, String> params = new HashMap<>();
            String token = UserService.getUserToken();
            params.put("title", title);
            JSONObject response = this.sendRequest(CourseService.COURSE_CREATE_ENDPOINT_URL, token, params);
            return this.course = new Course(response.getJSONObject("data"));
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }
    }

    public CourseService delete() throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        if ( this.course == null ){
            throw new RuntimeException("No course defined.");
        }
        String url = CourseService.COURSE_DELETE_ENDPOINT_URL;
        String token = UserService.getUserToken();
        url = url.replace("{ID}", Integer.toString(this.course.getID()));
        this.sendRequest(url, token, null);
        return this;
    }
}
