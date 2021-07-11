package com.jboard.service;

import com.jboard.exception.*;
import com.jboard.model.Course;
import com.jboard.model.Repetition;
import com.jboard.model.RepetitionList;
import com.jboard.model.Teacher;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class RepetitionService extends Service {
    private static final String REPETITION_LIST_ENDPOINT_URL = "/api/repetition/list";
    private static final String REPETITION_CREATE_ENDPOINT_URL = "/api/repetition/create";
    private static final String REPETITION_DELETE_ENDPOINT_URL = "/api/repetition/delete?id={ID}";

    private Repetition repetition;

    public RepetitionService(){}

    public RepetitionService(Repetition repetition){
        this.repetition = repetition;
    }

    public RepetitionService setRepetition(Repetition repetition){
        this.repetition = repetition;
        return this;
    }

    public Repetition getRepetition(){
        return this.repetition;
    }

    public RepetitionList list() throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        try{
            String token = UserService.getUserToken();
            JSONObject response = this.sendRequest(RepetitionService.REPETITION_LIST_ENDPOINT_URL, token, null);
            return new RepetitionList(response.getJSONArray("data"));
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }
    }

    public Repetition create(Teacher teacher, Course course) throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        try{
            HashMap<String, String> params = new HashMap<>();
            String token = UserService.getUserToken();
            params.put("teacherID", Integer.toString(teacher.getID()));
            params.put("courseID", Integer.toString(course.getID()));
            JSONObject response = this.sendRequest(RepetitionService.REPETITION_CREATE_ENDPOINT_URL, token, params);
            return this.repetition = new Repetition(response.getJSONObject("data"));
        }catch(JSONException ex){
            throw new NetworkException("Malformed response.", ex);
        }
    }

    public RepetitionService delete() throws UnauthorizedException, InvalidInputException, OperationalException, NetworkException {
        if ( this.repetition == null ){
            throw new RuntimeException("No repetition defined.");
        }
        String url = RepetitionService.REPETITION_DELETE_ENDPOINT_URL;
        String token = UserService.getUserToken();
        url = url.replace("{ID}", Integer.toString(this.repetition.getID()));
        this.sendRequest(url, token, null);
        return this;
    }
}
