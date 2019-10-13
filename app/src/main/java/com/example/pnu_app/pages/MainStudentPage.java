package com.example.pnu_app.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.pnu_app.R;
import com.example.pnu_app.database.AppDatabase;
import com.example.pnu_app.entity.Student;
import com.example.pnu_app.signin.SignIn;

public class MainStudentPage extends AppCompatActivity {

    TextView tvPage;
    String yourName;
    String yourLastNameP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student_page);
        BuildStudentPage(AppDatabase.getAppDatabase(MainStudentPage.this));
    }

    public void BuildStudentPage(final AppDatabase db){


        tvPage = findViewById(R.id.tvPage);

        tvPage.setText("Hello");
    }
}
