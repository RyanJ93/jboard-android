package com.jboard.service;

import com.jboard.exception.*;
import com.jboard.model.AvailableCourseList;
import com.jboard.model.Course;
import com.jboard.model.Lesson;
import com.jboard.model.LessonList;
import com.jboard.model.LessonSlot;
import com.jboard.model.Teacher;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class LessonService extends Service {
    private static final String LESSON_MARK_ENDPOINT_URL = "/api/lesson/mark?id={ID}&completed={COMPLETED}";
    private static final String LESSON_CANCEL_ENDPOINT_URL = "/api/lesson/delete?id={ID}";
    private static final String COURSE_AVAILABLE_ENDPOINT_URL = "/api/course/available";
    private static final String LESSON_LIST_ALL_ENDPOINT_URL = "/api/lesson/list-all";
    private static final String LESSON_CREATE_ENDPOINT_URL = "/api/lesson/create";
    private static final String LESSON_LIST_ENDPOINT_URL = "/api/lesson/list";

    private Lesson lesson;

    public LessonService(){}

    public LessonService(Lesson lesson){
        this.lesson = lesson;
    }

    public LessonService setLesson(Lesson lesson){
        this.lesson = lesson;
        return this;
    }

    public Lesson getLesson(){
        return this.lesson;
    }

    public AvailableCourseList getAvailableCourses() throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        try{
            String token = UserService.getUserToken();
            JSONObject response = this.sendRequest(LessonService.COURSE_AVAILABLE_ENDPOINT_URL, token, null);
            return new AvailableCourseList(response.getJSONArray("data"));
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }
    }

    public LessonList listAll() throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        try{
            String token = UserService.getUserToken();
            JSONObject response = this.sendRequest(LessonService.LESSON_LIST_ALL_ENDPOINT_URL, token, null);
            return new LessonList(response.getJSONArray("data"));
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }
    }

    public Lesson create(LessonSlot lessonSlot, Teacher teacher, Course course) throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        try{
            String token = UserService.getUserToken();
            HashMap<String, String> params = new HashMap<>();
            params.put("teacherID", Integer.toString(teacher.getID()));
            params.put("hour", Integer.toString(lessonSlot.getHour()));
            params.put("day", Integer.toString(lessonSlot.getDay()));
            params.put("courseID", Integer.toString(course.getID()));
            JSONObject response = this.sendRequest(LessonService.LESSON_CREATE_ENDPOINT_URL, token, params);
            return new Lesson(response.getJSONObject("data"));
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }
    }

    public LessonList list() throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        try{
            String token = UserService.getUserToken();
            JSONObject response = this.sendRequest(LessonService.LESSON_LIST_ENDPOINT_URL, token, null);
            return new LessonList(response.getJSONArray("data"));
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }
    }

    public LessonService mark(boolean completed) throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        if ( this.lesson == null ){
            throw new RuntimeException("No lesson defined.");
        }
        String url = LessonService.LESSON_MARK_ENDPOINT_URL;
        url = url.replace("{ID}", Integer.toString(this.lesson.getID()));
        url = url.replace("{COMPLETED}", ( completed ? "true" : "false" ));
        String token = UserService.getUserToken();
        this.sendRequest(url, token, null);
        return this;
    }

    public LessonService cancel() throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        if ( this.lesson == null ){
            throw new RuntimeException("No lesson defined.");
        }
        String url = LessonService.LESSON_CANCEL_ENDPOINT_URL;
        url = url.replace("{ID}", Integer.toString(this.lesson.getID()));
        String token = UserService.getUserToken();
        this.sendRequest(url, token, null);
        return this;
    }
}
