package fi.jamk.hunteam.explorejyvaskylaandroid.serverconnection;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by DoubleD on 2016. 12. 05..
 */

public class Login extends AsyncTask<String, Void, String> {

    private LoginCallBack loginCallBack;

    public interface LoginCallBack{
        public void onRemoteCallComplete(String id, String name, String picture);
    }

    public Login(LoginCallBack loginCallBack){
        this.loginCallBack = loginCallBack;
    }

    @Override
    protected String doInBackground(String... params) {

        if (params[0] == null || params[0].length() == 0){
            return null;
        }

        try {
            StringBuffer buffer = new StringBuffer();
            URL url = new URL("http://130.234.200.164:3000/login/" + params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStreamReader in = new InputStreamReader((InputStream) httpURLConnection.getContent());
            BufferedReader buff = new BufferedReader(in);
            String line = buff.readLine();
            while (line != null){
                buffer.append(line).append("\n");
                line = buff.readLine();
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String json) {
        System.out.println("____ from the server: " + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String picture = jsonObject.getString("picture");
            loginCallBack.onRemoteCallComplete(id, name, picture);
        } catch (JSONException e) {
            loginCallBack.onRemoteCallComplete(null, null, null);
            e.printStackTrace();
        }
    }
}
