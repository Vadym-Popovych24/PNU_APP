package com.social_network.pnu_app.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.Student;
import com.social_network.pnu_app.registration.Registration;
import com.social_network.pnu_app.signin.SignIn;

import java.util.HashMap;


public class MainStudentPage extends AppCompatActivity {

    TextView tvPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student_page);
        BuildStudentPage();
    }

    public void BuildStudentPage(){
        HashMap studentData;
        SignIn student = new SignIn();
        studentData = student.getStudentFB();

        tvPage = findViewById(R.id.tvPage);
        tvPage.setText("Hello " +  studentData.get("name") + " " + studentData.get("lastName"));
    }
}
