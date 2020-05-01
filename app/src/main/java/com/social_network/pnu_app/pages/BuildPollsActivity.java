package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.Friends;
import com.social_network.pnu_app.functional.QuestionAdapter;
import com.social_network.pnu_app.functional.VariantAdapter;
import com.social_network.pnu_app.localdatabase.AppDatabase;

public class BuildPollsActivity extends AppCompatActivity {

    Button btnCreateQuestion;
    Button btnBackCreatedPolls;

    Button btnAddQuestion;
    Button btnDeleteQuestion;
    Button btnFinishCreatePoll;
    DatabaseReference refPoll;
    MaterialEditText etNamePoll;
    MaterialEditText etDescriptionPoll;

    String namePoll;
    String descriptionName;
   public static RecyclerView questionPoll;

   String senderUserId;

  public static int countQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_polls);

        btnCreateQuestion = findViewById(R.id.btnCreateQuestion);
        btnBackCreatedPolls =findViewById(R.id.btnBackCreatedPolls);

        questionPoll = findViewById(R.id.recyclerViewCreatedPoll);
       //  questionPoll.setHasFixedSize(true);
        questionPoll.setLayoutManager(new LinearLayoutManager(this));


        btnCreateQuestion.setOnClickListener(btnlistener);
        btnBackCreatedPolls.setOnClickListener(btnlistener);


        btnAddQuestion = findViewById(R.id.btnAddQuestion);
        btnAddQuestion.setOnClickListener(btnlistener);

        btnDeleteQuestion = findViewById(R.id.btnDeleteQuestion);
        btnDeleteQuestion.setOnClickListener(btnlistener);

        btnFinishCreatePoll = findViewById(R.id.btnFinishCreatePoll);
        btnFinishCreatePoll.setOnClickListener(btnlistener);

        etNamePoll = findViewById(R.id.etNamePoll);
        etDescriptionPoll = findViewById(R.id.etDescriptionPoll);

        ProfileStudent profileStudent= new ProfileStudent();
        senderUserId = profileStudent.getKeyCurrentStudend(AppDatabase.getAppDatabase(BuildPollsActivity.this));

        refPoll = FirebaseDatabase.getInstance().getReference("students").child(senderUserId)
                .child("Polls").child("Created");

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_createdPoll);
        bottomNavigationView.setSelectedItemId((R.id.action_main_student_page));
        menuChanges(bottomNavigationView);
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

    View.OnClickListener btnlistener = new View.OnClickListener() {
        QuestionAdapter adapter = new QuestionAdapter();
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btnBackCreatedPolls:
                    Intent intentBackPolls;
                    intentBackPolls = new Intent( "com.social_network.pnu_app.pages.PollsActivity");
                    startActivity(intentBackPolls);
                    break;

                case R.id.btnCreateQuestion:

                    namePoll = etNamePoll.getText().toString();
                    refPoll.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(namePoll) && !namePoll.equals("")){
                                Toast.makeText(BuildPollsActivity.this, "Назва з таким опитуванням вже існує",
                                        Toast.LENGTH_LONG).show();
                            }
                            else{
                                if (!etNamePoll.getText().toString().equals("")){
                                    descriptionName = String.valueOf(etDescriptionPoll.getText());

                                    refPoll.child(namePoll).setValue(namePoll);

                                    btnCreateQuestion.setVisibility(View.GONE);
                                    questionPoll.setNestedScrollingEnabled(false);
                                    questionPoll.setAdapter(adapter);
                                    btnAddQuestion.setVisibility(View.VISIBLE);
                                    btnFinishCreatePoll.setVisibility(View.VISIBLE);
                                }
                                else {
                                    Toast.makeText(BuildPollsActivity.this, "Введіть назву опитування",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });






                    break;
                case R.id.btnAddQuestion:

                        questionPoll.setAdapter(adapter);
                        countQuestion= questionPoll.getChildCount()+2;
                        System.out.println("CLICKED btnAddQuestion");
                        BuildPollsActivity.questionPoll.setAdapter(new QuestionAdapter());
                       // btnFinishCreatePoll.setVisibility(View.GONE);
                      //  btnAddQuestion.setVisibility(View.GONE);
                     //   btnDeleteQuestion.setVisibility(View.GONE);

                    break;

            case R.id.btnDeleteQuestion:

                        System.out.println("CLICKED btnAddQuestion");
                        btnFinishCreatePoll.setVisibility(View.VISIBLE);
                        btnAddQuestion.setVisibility(View.VISIBLE);
                        btnDeleteQuestion.setVisibility(View.VISIBLE);
                        if (countQuestion == 0){
                            //  holder.btnCreateQuestion.setVisibility(View.VISIBLE);
                        }
                        break;

                case R.id.btnFinishCreatePoll:

                        for( int i =0; i<countQuestion; i++){
                            System.out.println("countQuestion = " + countQuestion);
                            System.out.println("countRecycler " + BuildPollsActivity.questionPoll.getChildCount());
                            View view = BuildPollsActivity.questionPoll.getChildAt(i);
                            MaterialEditText eTQuestions = view.findViewById(R.id.etQuestionPoll);
                            System.out.println("editText = " + eTQuestions.getText());
                        }

                        for( int i =0; i<countQuestion; i++){
                            View view = questionPoll.getChildAt(i);
                            MaterialEditText editTextVariant= view.findViewById(R.id.etQuestionVariantPoll1);
                            System.out.println("editText = " + editTextVariant.getText());
                        }

                        //   holder.editTextQuestions.ite.getItemId(); ..getText();
                        //    System.out.println("editText = " + holder.editTextQuestions.getText());
                        break;

            }
        }
    };
}


