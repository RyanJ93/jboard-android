package com.jboard;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.jboard.model.User;
import com.jboard.service.UserService;
import com.jboard.task.AvailableLessonListTask;
import com.jboard.task.CourseListTask;
import com.jboard.task.LessonListAllTask;
import com.jboard.task.LessonListTask;
import com.jboard.task.RepetitionListTask;
import com.jboard.task.TeacherListTask;
import com.jboard.task.UserLogoutTask;
import java.util.ArrayList;

public abstract class AppCompatActivityWithNavigationDrawer extends AppCompatActivity {
    private ArrayList<String> menuEntries;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    protected DrawerLayout drawerLayout;

    private void loadMenuEntries(){
        this.menuEntries = new ArrayList<>();
        User authenticatedUser = UserService.getAuthenticatedUser();
        if ( authenticatedUser != null && authenticatedUser.isAdmin() ){
            this.menuEntries.add("Available lessons");
            this.menuEntries.add("All lessons");
            this.menuEntries.add("Courses management");
            this.menuEntries.add("Teachers management");
            this.menuEntries.add("Repetitions management");
            this.menuEntries.add("Logout");
        }else{
            this.menuEntries.add("Available lessons");
            this.menuEntries.add("My lessons");
            this.menuEntries.add("Logout");
        }
    }

    protected void handleMenuAction(String itemName){
        switch ( itemName ) {
            case "Available lessons": {
                AvailableLessonListTask availableLessonListTask = new AvailableLessonListTask(this);
                availableLessonListTask.execute();
            }break;
            case "All lessons": {
                LessonListAllTask lessonListAllTask = new LessonListAllTask(this);
                lessonListAllTask.execute();
            }break;
            case "My lessons": {
                LessonListTask lessonListTask = new LessonListTask(this);
                lessonListTask.execute();
            }break;
            case "Courses management": {
                CourseListTask courseListTask = new CourseListTask(this);
                courseListTask.execute();
            }break;
            case "Teachers management": {
                TeacherListTask teacherListTask = new TeacherListTask(this);
                teacherListTask.execute();
            }break;
            case "Repetitions management": {
                RepetitionListTask repetitionListTask = new RepetitionListTask(this);
                repetitionListTask.execute();
            }break;
            case "Logout": {
                UserLogoutTask userLogoutTask = new UserLogoutTask(this);
                userLogoutTask.execute();
            }break;
        }
    }

    protected void setupNavigationDrawer(){
        this.loadMenuEntries();
        this.drawerLayout = this.findViewById(R.id.drawer_layout);
        ActionBar actionBar = this.getSupportActionBar();
        if ( actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        this.actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, R.string.app_name, R.string.app_name){
            @Override
            public void onDrawerClosed(View drawerView){}

            @Override
            public void onDrawerOpened(View drawerView){}
        };
        this.drawerLayout.setDrawerListener(this.actionBarDrawerToggle);
        this.actionBarDrawerToggle.syncState();
        ListView listView = this.findViewById(R.id.menu);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.menu_item, this.menuEntries));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            handleMenuAction(this.menuEntries.get(position));
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if ( this.actionBarDrawerToggle.onOptionsItemSelected(item) ){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        this.actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        this.actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed(){}
}
