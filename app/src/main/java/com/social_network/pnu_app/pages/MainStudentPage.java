package com.social_network.pnu_app.pages;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.Student;
import com.social_network.pnu_app.firebase.QueriesFirebase;
import com.social_network.pnu_app.registration.Registration;
import com.social_network.pnu_app.signin.SignIn;

import java.util.HashMap;
import java.util.Objects;


public class MainStudentPage extends AppCompatActivity {

    TextView tvPIBvalue;
    TextView tvFacultyValue;
    TextView tvGroupValue;
    TextView tvDateOfEntryValue;
    TextView tvFormStudyingValue;

    ImageView imageViewStudentMain;
    Button btnLoadPhotoStudent;
    private FirebaseAnalytics mFirebaseAnalytics;

    public static HashMap<Object, Object> studentData = new HashMap();

    //Проверяем разрешение на работу с камеройMainStudentPage
    boolean isCameraPermissionGranted = ActivityCompat.checkSelfPermission(MainStudentPage.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    //Проверяем разрешение на работу с внешнем хранилещем телефона
   static boolean isWritePermissionGranted = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student_page);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        btnLoadPhotoStudent = findViewById(R.id.btnLoadPhotoStudent);
        tvPIBvalue = findViewById(R.id.tvPIBvalue);
        tvFacultyValue = findViewById(R.id.tvFacultyValue);
        tvGroupValue = findViewById(R.id.tvGroupValue);
        tvDateOfEntryValue = findViewById(R.id.tvDateOfEntryValue);
        tvFormStudyingValue = findViewById(R.id.tvFormStudyingValue);


        BuildStudentPage();
    }
QueriesFirebase qfd = new QueriesFirebase();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void BuildStudentPage(){
        Bundle bundle = new Bundle();
      //  bundle.putString(FirebaseAnalytics.Param.ITEM_ID,  studentData.get("id"));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, (String) studentData.get("name"));
       // bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        studentData = Student.student;

        tvPIBvalue.setText(studentData.get("name") + " " + studentData.get("lastName") + studentData.get("patronym"));
        tvFacultyValue.setText(Objects.requireNonNull(studentData.get("faculty")).toString());
        tvGroupValue.setText(Objects.requireNonNull(studentData.get("group")).toString());
        tvDateOfEntryValue.setText(Objects.requireNonNull(studentData.get("dateOfEntry")).toString());
        tvFormStudyingValue.setText(Objects.requireNonNull(studentData.get("formStudying")).toString());

        btnLoadPhotoStudent.setOnClickListener(listenerBtnLoapPhotoStudent);
    }

    View.OnClickListener listenerBtnLoapPhotoStudent = new View.OnClickListener() {

        LoadPhoto loadPhoto = new LoadPhoto();
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btnLoadPhotoStudent){
                loadPhoto.addPhoto();
            }
        }
    };

}
