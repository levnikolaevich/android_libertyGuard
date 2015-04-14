package ru.libertyguard.libertyguard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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

        // Log.d("Login: ", username);
        // Log.d("Pass: ", password);
        //  Log.d("Строка: ", username);

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

            maininfo = response.toString();

            Log.d("Response: ", response.toString());

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

            Log.d("Response: ", response.toString());

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

}
