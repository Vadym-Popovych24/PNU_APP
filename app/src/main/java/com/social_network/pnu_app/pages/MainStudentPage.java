package com.social_network.pnu_app.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.social_network.pnu_app.R;
import com.social_network.pnu_app.database.AppDatabase;
import com.social_network.pnu_app.signin.SignIn;

public class MainStudentPage extends AppCompatActivity {

    TextView tvPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student_page);
        BuildStudentPage(AppDatabase.getAppDatabase(MainStudentPage.this));
    }

    public void BuildStudentPage(final AppDatabase db){
        SignIn student = new SignIn();
        String NameStudent = student.getYourName(AppDatabase.getAppDatabase(MainStudentPage.this));
        String LastNameStudent = student.getYourLastName(AppDatabase.getAppDatabase(MainStudentPage.this));

        tvPage = findViewById(R.id.tvPage);
        tvPage.setText("Hello " +  NameStudent + " " + LastNameStudent);
    }
}
