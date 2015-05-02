package ru.libertyguard.libertyguard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.preference.PreferenceActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Лев on 06.04.2015.
 */
public class ConnectSupport {


    public ConnectSupport() {

    }

    public boolean getAuth (String username, String password, String token) throws UnsupportedEncodingException {
        HttpURLConnection connection = null;

        URL url;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String urlParameters = "email=" + URLEncoder.encode(username, "UTF-8")+
                "&pass="+URLEncoder.encode(password,"UTF-8")
                +"&token="+URLEncoder.encode(token,"UTF-8");



        try {
            //Create connection
            url = new URL("http://libertyguard.ru/android/Auth?");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);

            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            if(response.toString().contains("1")){

                return true;
            }
            else{
                return false;
            }

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }

    }

    public String getMainInfo (String username) throws UnsupportedEncodingException {

        String maininfo;

        URL url;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String urlParameters = "name=" + URLEncoder.encode(username, "UTF-8");

        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL("http://libertyguard.ru/android/GetMainInfo");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Language", "ru-RU");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

           //

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);



            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            maininfo = response.toString();



            return maininfo;


        } catch (Exception e) {

            e.printStackTrace();
            return "no";

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }

    }

    public Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("Картинка ", "Error getting bitmap", e);
        }
        return bm;
    }

    public String getQuests (String username) throws UnsupportedEncodingException {

        String quest;

        URL url;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String urlParameters = "name=" + URLEncoder.encode(username, "UTF-8");

        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL("http://libertyguard.ru/android/getquests");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Language", "ru-RU");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);

            // Log.d("Строка: ", wr.toString());

            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            quest = response.toString();



            return quest;


        } catch (Exception e) {

            e.printStackTrace();
            return "no";

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }

    }

    public String getTasks (String idQuest, String ueqId) throws UnsupportedEncodingException {

        String tasks;

        URL url;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String urlParameters = "id=" + URLEncoder.encode(idQuest, "UTF-8")
                +"&ueqId="+URLEncoder.encode(ueqId,"UTF-8");

        Log.d("id", ""+idQuest);
        Log.d("ueqId", ""+ueqId);

        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL("http://libertyguard.ru/android/gettasks");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Language", "ru-RU");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);



            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            tasks = response.toString();

            return tasks;


        } catch (Exception e) {

            e.printStackTrace();
            return "no";

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }

    }


    @SuppressWarnings("deprecation")
    public static boolean sentTaskImg (String sourceFileUri, String taskId, String value, String ueqId, String longitude, String latitude){
        try {
            //Log.d("Отправка", ""+sourceFileUri);
           HttpClient client = new DefaultHttpClient();
           HttpPost poster = new HttpPost("http://libertyguard.ru/android/sendtaskvalue");
            MultipartEntityBuilder  multipartEntity = MultipartEntityBuilder.create();


            if(sourceFileUri!=null) {

                File imageFile = new File(sourceFileUri);

                Bitmap resized = decodeSampledBitmapFromFile(sourceFileUri,380,672);

                FileOutputStream fileStream = new FileOutputStream(imageFile);
                resized.compress(Bitmap.CompressFormat.JPEG, 100, fileStream);
                fileStream.close();

                File imageFile2 = new File(sourceFileUri);
                //Bitmap resized = Bitmap.createScaledBitmap(yourBitmap,(int)(yourBitmap.getWidth()*0.8), (int)(yourBitmap.getHeight()*0.8), true);


                FileBody fileBody = new FileBody(imageFile2);


                multipartEntity.addPart("file",fileBody);
            }

            if(value != null)
            {
                multipartEntity.addPart("value", new StringBody(URLEncoder.encode(value,"UTF-8")));
            }

            multipartEntity.addPart("taskId", new StringBody(URLEncoder.encode(taskId,"UTF-8")));

            multipartEntity.addPart("ueqId", new StringBody(URLEncoder.encode(ueqId,"UTF-8")));
            multipartEntity.addPart("longitude", new StringBody(URLEncoder.encode(longitude,"UTF-8")));
            multipartEntity.addPart("latitude", new StringBody(URLEncoder.encode(latitude,"UTF-8")));

            poster.setEntity(multipartEntity.build());

            HttpResponse response1 = client.execute(poster);
            //Get the response from the server
            HttpEntity resEntity = response1.getEntity();
            String Response = EntityUtils.toString(resEntity);


            if(Response.contains("1")){
                return true;
            }
            else{
                return false;
            }

        } catch (Exception e){
            Log.d("Отправка:", "ОШИБКА" + e.getMessage());
            return false;
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }


        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }


    public String getRatings () throws UnsupportedEncodingException {

       // Log.d("Рейтинг","СОЕДИНЕНИЕ");

        String rating;

        URL url;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection connection = null;

        try {
            //Create connection
            url = new URL("http://libertyguard.ru/android/getuserrating");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Language", "ru-RU");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());

            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            rating = response.toString();

            return rating;

        } catch (Exception e) {

            e.printStackTrace();
            return "no";

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }

    }

}
