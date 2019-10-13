package com.social_network.pnu_app.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.social_network.pnu_app.R;
import com.social_network.pnu_app.database.AppDatabase;

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
