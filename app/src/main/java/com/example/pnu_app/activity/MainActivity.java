package com.example.pnu_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pnu_app.R;
import com.example.pnu_app.database.AppDatabase;
import com.example.pnu_app.utils.DatabaseInitializer;

public class MainActivity extends AppCompatActivity {

    private Button btnMainSignIn;
    private Button btnRegistration;

    private static final String TAG = MainActivity.class.getName();
    private View clickHereBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenerOnButton();
        DatabaseInitializer.populateAsync(AppDatabase.getAppDatabase(MainActivity.this));

    }

  /*  @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    } Changes */
    
    


    public void listenerOnButton(){
        btnMainSignIn = findViewById(R.id.btnMainSignIn);
        btnRegistration = findViewById(R.id.btnRegister);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent;
                switch(view.getId()){
                    case R.id.btnMainSignIn:
                        intent = new Intent( "com.example.pnu_app.signin.SignIn");
                        startActivity(intent);
                        break;
                    case R.id.btnRegister:
                        intent = new Intent( "com.example.pnu_app.registration.Registration");
                        startActivity(intent);
                        break;



                }
            }

        };

        btnMainSignIn.setOnClickListener(listener);
        btnRegistration.setOnClickListener(listener);




    }
}
