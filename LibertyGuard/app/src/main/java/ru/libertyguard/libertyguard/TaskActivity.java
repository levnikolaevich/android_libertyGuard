package ru.libertyguard.libertyguard;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TaskActivity extends ActionBarActivity {

    String idQuest, idUEQ, nameQuest, idTask;
    TextView tvTaskName, tvTaskDescription;
    EditText etTaskData;
    Button btTaskPhoto, btTaskSentData;
    double latitude, longitude;
    GPSTracker gps;

    //Эксперименты
    ImageView ivTaskPhoto;
    String mCurrentPhotoPath;

    private static final int ACTION_TAKE_PHOTO_B = 1;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Set the Action Bar to use tabs for navigation
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4190AB")));
        ab.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#3A8098")));
        ab.setTitle("Задача");

        gps = new GPSTracker(this);

        idQuest = getIntent().getStringExtra("idQuest");
        nameQuest = getIntent().getStringExtra("nameQuest");
        idUEQ = getIntent().getStringExtra("idUEQ");
        //Log.d("Квест часть 5-на ЗАДАЧЕ", "" + idUEQ);

        TaskListActivity.TaskInfo taskInfo = getIntent().getParcelableExtra("TaskInfo");

        tvTaskName = (TextView) findViewById(R.id.tvTaskName);
        tvTaskDescription = (TextView) findViewById(R.id.tvTaskDescription);
        etTaskData = (EditText) findViewById(R.id.etTaskData);

        btTaskPhoto = (Button) findViewById(R.id.btTaskPhoto);
        btTaskSentData = (Button) findViewById(R.id.btTaskSentData);
        ivTaskPhoto = (ImageView) findViewById(R.id.ivTaskPhoto);

        tvTaskName.setText(taskInfo.getTaskName());
        tvTaskDescription.setText(taskInfo.getTaskDescription());
        idTask = String.valueOf(taskInfo.getTaskId());

        if(taskInfo.getTaskNeedData() == 1){
            etTaskData.setVisibility(View.VISIBLE);
        }
        else{
            etTaskData.setVisibility(View.INVISIBLE);
        }

        if(taskInfo.getTaskNeedFoto() == 1){
            btTaskPhoto.setVisibility(View.VISIBLE);
        }
        else{
            btTaskPhoto.setVisibility(View.INVISIBLE);
        }


        setBtnListenerOrDisable(
                btTaskPhoto,
                mTakePicOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );


        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();


        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        btTaskSentData.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                sentTasks sTask = new sentTasks();
                sTask.execute();

                btTaskSentData.setText("Ждите");
                btTaskSentData.setEnabled(false);
                btTaskPhoto.setEnabled(false);
                etTaskData.setEnabled(false);

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }


    }


    Button.OnClickListener mTakePicOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
                }
            };


    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch(actionCode) {
            case ACTION_TAKE_PHOTO_B:
                File f = null;

                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }
                break;

            default:
                break;
        } // switch

        startActivityForResult(takePictureIntent, actionCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B: {
                if (resultCode == RESULT_OK) {
                    handleBigCameraPhoto();
                }
                break;
            } // ACTION_TAKE_PHOTO_B
        } // switch
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("LibertyAlbum", "ошибка при создании директории");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
           // mCurrentPhotoPath = null;
        }

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = ivTaskPhoto.getWidth();
        int targetH = ivTaskPhoto.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        ivTaskPhoto.setImageBitmap(bitmap);
        ivTaskPhoto.setVisibility(View.VISIBLE);

        btTaskPhoto.setText("Фото готово. Переснять?");
        btTaskPhoto.setTextColor(Color.parseColor("#4190AB"));

    }


    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    //Передача данных на сервер
    class sentTasks extends AsyncTask<Void, Void, Boolean> {
        ConnectSupport cs = new ConnectSupport();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            }

        @Override
        protected Boolean doInBackground(Void... params) {

             return cs.sentTaskImg(mCurrentPhotoPath, idTask, etTaskData.getText().toString(), idUEQ, String.valueOf(longitude), String.valueOf(latitude));

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result)
            {
                Intent taskListIntent = new Intent(TaskActivity.this, TaskListActivity.class);

                taskListIntent.putExtra("idQuest", idQuest);
                taskListIntent.putExtra("nameQuest", nameQuest);
                taskListIntent.putExtra("idUEQ", idUEQ);

                startActivity(taskListIntent);

                Toast.makeText(getApplicationContext(), "Данные переданы успешно", Toast.LENGTH_LONG).show();
              //  onDestroy();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Данные не дошли", Toast.LENGTH_LONG).show();
            }

            cs = null;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();


        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent taskListIntent = new Intent(this, TaskListActivity.class);

                taskListIntent.putExtra("idQuest", idQuest);
                taskListIntent.putExtra("nameQuest", nameQuest);
                taskListIntent.putExtra("idUEQ", idUEQ);

                startActivity(taskListIntent);

               // NavUtils.navigateUpFromSameTask(this);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
