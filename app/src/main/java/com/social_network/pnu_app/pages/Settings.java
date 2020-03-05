package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.activity.MainActivity;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_settings);
        bottomNavigationView.setSelectedItemId(R.id.action_settings);

        menuChanges(bottomNavigationView);

        Button btnSignOut;
        Button Exit;
        btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(listenerBtnSignOut);
        Exit = findViewById(R.id.btnExit);
        Exit.setOnClickListener(listenerBtnExit);
    }

    public void SignOut() {

        TextView tvExampleSignOut;
        TextView tvExampleSignOut2;

        tvExampleSignOut = findViewById(R.id.tvExampleSignOut);
        tvExampleSignOut2 = findViewById(R.id.tvExampleSignOut2);


        Context context;

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            tvExampleSignOut.append(FirebaseAuth.getInstance().getCurrentUser().toString());
            FirebaseAuth.getInstance().signOut();
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                tvExampleSignOut.append("null");

                tvExampleSignOut2.append("null");
            }
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                tvExampleSignOut.append(FirebaseAuth.getInstance().getCurrentUser().toString());
                tvExampleSignOut2.append(FirebaseAuth.getInstance().getCurrentUser().toString());
            }
            else {
                tvExampleSignOut.append("null");

                tvExampleSignOut2.append("null");
            }
        }
        else {
            tvExampleSignOut.append("null");
        }
    }

        View.OnClickListener listenerBtnSignOut = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnSignOut) {
                    SignOut();
                }
            }
        };

    View.OnClickListener listenerBtnExit = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnExit) {
                Exit();
            }
        }
    };

    private void Exit() {
        FirebaseAuth.getInstance().signOut();
        Intent intentSignOut = new Intent(this, MainActivity.class);
        intentSignOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentSignOut);
    }


    public void menuChanges(BottomNavigationView bottomNavigationView){

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Intent intentMenu;
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.Search");
                                startActivity(intentMenu);

                                break;
                            case R.id.action_message:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.Messenger");
                                startActivity(intentMenu);


                                break;
                            case R.id.action_main_student_page:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.MainStudentPage");
                                startActivity(intentMenu);

                                break;
                            case R.id.action_schedule:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.Schedule");
                                startActivity(intentMenu);

                                break;
                            case R.id.action_settings:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.Settings");
                                startActivity(intentMenu);

                                break;
                        }
                        return false;
                    }
                });
    }
}
