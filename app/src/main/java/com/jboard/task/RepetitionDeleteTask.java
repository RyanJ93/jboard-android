package com.jboard.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import com.jboard.R;
import com.jboard.adapter.RepetitionsManagementRepetitionAdapter;
import com.jboard.exception.*;
import com.jboard.model.Repetition;
import com.jboard.service.RepetitionService;

public class RepetitionDeleteTask extends AsyncTask<Void, Void, Void> {
    private final Repetition repetition;
    private final Context context;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            RepetitionService repetitionService = new RepetitionService(this.repetition);
            repetitionService.delete();
            Activity activity = (Activity)this.context;
            activity.runOnUiThread(() -> {
                ListView repetitionsManagementRepetitionList = activity.findViewById(R.id.repetitionsManagementRepetitionList);
                ((RepetitionsManagementRepetitionAdapter)repetitionsManagementRepetitionList.getAdapter()).removeFromList(this.repetition.getID());
            });
        }catch(OperationalException | InvalidInputException ex){
            ex.showAlert(this.context);
        }catch(NetworkException ex){
            ex.setTask(this).showAlert(this.context);
        }catch(UnauthorizedException ex){
            ex.showLoginActivity(this.context);
        }
        return null;
    }

    public RepetitionDeleteTask(Repetition repetition, Context context){
        this.repetition = repetition;
        this.context = context;
    }
}
