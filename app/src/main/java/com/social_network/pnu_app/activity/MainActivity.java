package com.social_network.pnu_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.localdatabase.AppDatabase;
import com.social_network.pnu_app.localdatabase.DatabaseInitializer;


public class MainActivity extends AppCompatActivity {

    private Button btnMainSignIn;
    private Button btnRegistration;

    Button btnGetCurrentUser;
    TextView bottomText;
    private RelativeLayout rlActivityMain;

    private static final String TAG = MainActivity.class.getName();
    private View clickHereBtn;

    private static int buttonCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rlActivityMain = findViewById(R.id.rlActivityMain);
        Intent intentFromMainActivity;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            intentFromMainActivity = new Intent("com.social_network.pnu_app.pages.MainStudentPage");
            startActivity(intentFromMainActivity);
        }
        else {
          //  Snackbar.make(rlActivityMain , "Ви не автризовані", Snackbar.LENGTH_LONG).show();

            //    mess_current_user.setText("current_user(displayName) = " + FirebaseAuth.getInstance().getCurrentUser().getUid() );
        }

        setContentView(R.layout.activity_main);


      //  Intent intentFromSignIn = new Intent("com.social_network.pnu_app.pages.MainStudentPage");
      //  startActivity(intentFromSignIn);

        DatabaseInitializer.populateAsync(AppDatabase.getAppDatabase(MainActivity.this));
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

        bottomText = findViewById(R.id.text_bottom);
        btnGetCurrentUser = findViewById(R.id.btnGetCurrentUser);

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
                    case R.id.btnGetCurrentUser:

                        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

                            bottomText.append(FirebaseAuth.getInstance().getCurrentUser().toString());
                            bottomText.append(" " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                            FirebaseAuth.getInstance().signOut();
                            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                                bottomText.append("null");
                            }
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                bottomText.append(FirebaseAuth.getInstance().getCurrentUser().toString());
                            }
                        }
                        else{
                            bottomText.append("null");
                        }




                }
            }


        };

        btnMainSignIn.setOnClickListener(listener);
        btnRegistration.setOnClickListener(listener);
        btnGetCurrentUser.setOnClickListener(listener);



    }
}
