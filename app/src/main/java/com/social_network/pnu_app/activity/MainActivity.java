package com.social_network.pnu_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.social_network.pnu_app.R;


public class MainActivity extends AppCompatActivity {

    private Button btnMainSignIn;
    private Button btnRegistration;

    private static final String TAG = MainActivity.class.getName();
    private View clickHereBtn;

    private static int buttonCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenerOnButton();
    }


  /*  @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    } */

    public static void plusCounter(){
        buttonCounter++;
    }


    public void listenerOnButton(){
        btnMainSignIn = findViewById(R.id.btnMainSignIn);
        btnRegistration = findViewById(R.id.btnRegister);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent;
                switch(view.getId()){
                    case R.id.btnMainSignIn:
                        intent = new Intent( "com.social_network.pnu_app.signin.SignIn");
                        startActivity(intent);
                        break;
                    case R.id.btnRegister:
                        if (buttonCounter == 0){
                            plusCounter();
                     //    MigrationToSQLITE.addDatatoSqliteFromFirebase(AppDatabase.getAppDatabase(MainActivity.this));
                        }
                        intent = new Intent( "com.social_network.pnu_app.registration.Registration");
                        startActivity(intent);
                        break;


                }
            }

        };

        btnMainSignIn.setOnClickListener(listener);
        btnRegistration.setOnClickListener(listener);




    }
}
