package com.jboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.tabs.TabLayout;
import com.jboard.fragment.ActiveLessonListFragment;
import com.jboard.fragment.CanceledLessonListFragment;
import com.jboard.fragment.CompletedLessonListFragment;
import com.jboard.model.LessonList;
import com.jboard.model.User;
import com.jboard.service.UserService;

public class LessonsActivity extends AppCompatActivityWithNavigationDrawer {
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private LessonList lessonList;
    private Fragment fragment;

    private void loadLessons(){
        this.lessonList = null;
        Intent intent = this.getIntent();
        if ( intent != null ){
            Bundle extra = intent.getExtras();
            if ( extra != null && extra.containsKey("lessonList") ){
                this.lessonList = (LessonList)extra.get("lessonList");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadLessons();
        this.setContentView(R.layout.activity_lessons);
        this.setupNavigationDrawer();
        User authenticatedUser = UserService.getAuthenticatedUser();
        String title = authenticatedUser != null && authenticatedUser.isAdmin() ? "All lessons" : "My lessons";
        TextView activityTitle = this.findViewById(R.id.activityTitle);
        TabLayout lessonTabs = this.findViewById(R.id.lessonTabs);
        activityTitle.setText(title);
        fragment = new ActiveLessonListFragment(this.lessonList, this);
        this.fragmentManager = this.getSupportFragmentManager();
        this.fragmentTransaction = this.fragmentManager.beginTransaction();
        this.fragmentTransaction.replace(R.id.tabsContent, fragment);
        this.fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.fragmentTransaction.commit();
        final Context context = this;
        lessonTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                switch ( tab.getPosition() ){
                    case 0:{
                        fragment = new ActiveLessonListFragment(lessonList, context);
                    }break;
                    case 1:{
                        fragment = new CompletedLessonListFragment(lessonList);
                    }break;
                    case 2:{
                        fragment = new CanceledLessonListFragment(lessonList);
                    }break;
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.tabsContent, fragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){}

            @Override
            public void onTabReselected(TabLayout.Tab tab){}
        });
    }
}