package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.Student;
import com.social_network.pnu_app.firebase.QueriesFirebase;
import com.social_network.pnu_app.localdatabase.AppDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;


public class MainStudentPage extends AppCompatActivity {

    TextView tvPIBvalue;
    TextView tvFacultyValue;
    TextView tvGroupValue;
    TextView tvDateOfEntryValue;
    TextView tvFormStudyingValue;

    Button btnLoadPhotoStudent;
    private FirebaseAnalytics mFirebaseAnalytics;

    public static HashMap<Object, Object> studentData = new HashMap();

    private CircleImageView imStudentMainPhoto;

    private ImageView imSendPhotoWall;

    private Button btnAddPicture;

    private File mTempPhoto;

    private String mImageUri = "";

    private String mRereference = "";

    private EmojiconEditText emojiconEditText;

    String SeriesIDCard;
    String nameFileFirebase = "MainStudentPhoto";


    private static final int REQUEST_CODE_PERMISSION_RECEIVE_CAMERA = 102;
    private static final int REQUEST_CODE_TAKE_PHOTO = 103;


   static File finalLocalFile;

    private StorageReference mStorageRef;

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
        btnLoadPhotoStudent.setOnClickListener(listenerBtnLoapPhotoStudent);
        //    emojiconEditText = findViewById(R.id.editTextWall);

        BuildStudentPage(AppDatabase.getAppDatabase(MainStudentPage.this));
        imStudentMainPhoto = (CircleImageView) findViewById(R.id.imStudentMainPhoto);
        imSendPhotoWall = (ImageView) findViewById(R.id.imSendPhotoWall);
        btnAddPicture = (Button) findViewById(R.id.btnLoadPhotoStudent);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_profile);
        bottomNavigationView.setSelectedItemId((R.id.action_main_student_page));

        menuChanges(bottomNavigationView);

        // btnAddPicture.setOnClickListener((View.OnClickListener) this);

        File localFile = null;

        mRereference = getIntent().getStringExtra("Reference");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        nameFileFirebase+=SeriesIDCard;

        try {

        if (finalLocalFile == null) {


                localFile = createTempImageFile(getExternalCacheDir());
                finalLocalFile = localFile;
                mStorageRef.child(SeriesIDCard += "/" + nameFileFirebase).getFile(finalLocalFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        Picasso.with(getBaseContext())
                                .load(finalLocalFile)
                                .placeholder(R.drawable.logo_pnu)
                                .error(R.mipmap.ic_error2)
                                .centerCrop()
                                .fit()
                                //.resize(1920,2560)
                                .into(imSendPhotoWall);

                        Toast.makeText(MainStudentPage.this, "Succes get photo from FirebaseStorage onCreate " + finalLocalFile,
                                Toast.LENGTH_LONG).show();

                        Picasso.with(getBaseContext())
                                .load(finalLocalFile)
                                .placeholder(R.drawable.logo_pnu)
                                .error(R.mipmap.ic_error2)
                                .centerCrop()
                                .fit()
                                // .resize(1920,2560)
                                .into(imStudentMainPhoto);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Load", "" + e);

                        Toast.makeText(MainStudentPage.this, "Not Succes get photo from FirebaseStorage onCreate",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        else{
            Picasso.with(getBaseContext())
                    .load(finalLocalFile)
                    .placeholder(R.drawable.logo_pnu)
                    .error(R.mipmap.ic_error2)
                    .centerCrop()
                    .fit()
                    //.resize(1920,2560)
                    .into(imSendPhotoWall);

            Toast.makeText(MainStudentPage.this, "Succes get photo from FirebaseStorage onCreate  " + finalLocalFile,
                    Toast.LENGTH_LONG).show();

            Picasso.with(getBaseContext())
                    .load(finalLocalFile)
                    .placeholder(R.drawable.logo_pnu)
                    .error(R.mipmap.ic_error2)
                    .centerCrop()
                    .fit()
                    // .resize(1920,2560)
                    .into(imStudentMainPhoto);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    QueriesFirebase qfd = new QueriesFirebase();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void BuildStudentPage(final AppDatabase db){

        int currentStudent = Integer.parseInt(db.studentDao().getCurrentStudent());
        SeriesIDCard = db.studentDao().getSeriesBYId(currentStudent);
        Bundle bundle = new Bundle();
        //  bundle.putString(FirebaseAnalytics.Param.ITEM_ID,  studentData.get("id"));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, (String) db.studentDao().getFirstNameById(currentStudent));
        // bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        studentData = Student.student;

        tvPIBvalue.setText(db.studentDao().getFirstNameById(currentStudent) + " " +
                db.studentDao().getLastNameById(currentStudent) + " " +
                db.studentDao().getPatronymById(currentStudent));

        tvFacultyValue.setText(" " + db.studentDao().getFacultyById(currentStudent));
        tvGroupValue.setText(" " + db.studentDao().getGroupById(currentStudent));
        tvDateOfEntryValue.setText(" " + db.studentDao().getDateOfEntryById(currentStudent));
        tvFormStudyingValue.setText(" " + db.studentDao().getFormStudyingById(currentStudent));


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

    //Метод для добавления фото
    public void addPhoto() {
        //Проверяем разрешение на работу с камеройMainStudentPage
        boolean isCameraPermissionGranted = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        //Проверяем разрешение на работу с внешнем хранилещем телефона
        boolean isWritePermissionGranted = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        //Если разрешения != true
        if(!isCameraPermissionGranted || !isWritePermissionGranted) {

            String[] permissions;//Разрешения которые хотим запросить у пользователя

            if (!isCameraPermissionGranted && !isWritePermissionGranted) {
                permissions = new String[] {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            } else if (!isCameraPermissionGranted) {
                permissions = new String[] {android.Manifest.permission.CAMERA};
            } else {
                permissions = new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            }
            //Запрашиваем разрешения у пользователя
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION_RECEIVE_CAMERA);
        } else {
            //Если все разрешения получены
            try {
                mTempPhoto = createTempImageFile(getExternalCacheDir());
                mImageUri = mTempPhoto.getAbsolutePath();

                //Создаём лист с интентами для работы с изображениями
                List<Intent> intentList = new ArrayList<>();
                Intent chooserIntent = null;


                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                takePhotoIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempPhoto));

                intentList = addIntentsToList(this, intentList, pickIntent);
                intentList = addIntentsToList(this, intentList, takePhotoIntent);

                if (!intentList.isEmpty()) {
                    chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),"Choose your image source");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
                }

                /*После того как пользователь закончит работу с приложеним(которое работает с изображениями)
                 будет вызван метод onActivityResult
                */
                startActivityForResult(chooserIntent, REQUEST_CODE_TAKE_PHOTO);
            } catch (IOException e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
    }

    //Получаем абсолютный путь файла из Uri
    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int columnIndex = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    /*
      File storageDir -  абсолютный путь к каталогу конкретного приложения на
      основном общем /внешнем устройстве хранения, где приложение может размещать
      файлы кеша, которыми он владеет.
     */
    public static File createTempImageFile(File storageDir) throws IOException {

        // Генерируем имя файла
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());//получаем время
        String imageFileName = "photo_" + timeStamp;//состовляем имя файла

        //Создаём файл
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    /*
    Метод для добавления интента в лист интентов
    */
    public static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }


    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode , resultCode , data);
        switch (requestCode){
            case REQUEST_CODE_TAKE_PHOTO:
                if(resultCode == RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        mImageUri = getRealPathFromURI(data.getData());
                        Toast.makeText(MainStudentPage.this, "onActivityResult " + Uri.fromFile(finalLocalFile).getUserInfo(),
                                Toast.LENGTH_LONG).show();
                        Picasso.with(getBaseContext())
                                .load(data.getData())
                                .placeholder(R.drawable.logo_pnu)
                                .error(R.drawable.com_facebook_close)
                                .centerCrop()
                                .fit()
                              //  .resize(100,100)
                                .into(imSendPhotoWall);

                        Picasso.with(getBaseContext())
                                .load(data.getData())
                                .placeholder(R.drawable.logo_pnu)
                                .error(R.drawable.com_facebook_close)
                                .centerCrop()
                                .fit()
                           //     .resize(1920,2560)
                                .into(imStudentMainPhoto);

                        uploadFileInFireBaseStorage(data.getData());
                    } else if (mImageUri != null) {
                        mImageUri = Uri.fromFile(mTempPhoto).toString();
                        Toast.makeText(MainStudentPage.this, "onActivityResult 2" + Uri.fromFile(finalLocalFile).getUserInfo(),
                                Toast.LENGTH_LONG).show();
                        uploadFileInFireBaseStorage(Uri.fromFile((mTempPhoto)));
                        Picasso.with(this)
                                .load(mImageUri)
                                .placeholder(R.drawable.logo_pnu)
                                .error(R.drawable.com_facebook_tooltip_black_xout)
                                .centerCrop()
                                .fit()
                           //     .resize(100,100)
                                .into(imSendPhotoWall);

                        Picasso.with(getBaseContext())
                                .load(data.getData())
                                .error(R.drawable.com_facebook_tooltip_black_xout)
                                .placeholder(R.drawable.logo_pnu)
                                .centerCrop()
                                .fit()
                             //   .resize(100,100)
                                .into(imStudentMainPhoto);


                    }
                }
                break;
        }
    }

    public void uploadFileInFireBaseStorage (Uri uri){
        UploadTask uploadTask = mStorageRef.child(SeriesIDCard+="/").putFile(uri);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred());
                Log.i("Load","Upload is " + progress + "% done");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //    Task<Uri> donwoldUri = mStorageRef.getDownloadUrl();
                //       Log.i("Load" , "Uri donwlod" + donwoldUri);
            }
        });
    }

    View.OnClickListener listenerBtnLoapPhotoStudent = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btnLoadPhotoStudent){
                addPhoto();
            }
        }
    };

}