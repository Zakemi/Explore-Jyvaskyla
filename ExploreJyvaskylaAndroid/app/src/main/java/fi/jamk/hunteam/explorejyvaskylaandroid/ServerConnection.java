package fi.jamk.hunteam.explorejyvaskylaandroid;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DoubleD on 2016. 10. 21..
 */

public class ServerConnection extends AsyncTask<Void, Void, String>{

    private GetJSONData getJSONData;

    public ServerConnection(GetJSONData getJSONData){
        this.getJSONData = getJSONData;
    };

    public interface GetJSONData {
        public void onRemoteCallComplete(String json);
    }

    @Override
    protected String doInBackground(Void... params) {
        URL url;
        HttpURLConnection httpURLConnection = null;
        try {
            StringBuffer buffer = new StringBuffer();
            url = new URL("http://192.168.1.34:3000/locations");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStreamReader in = new InputStreamReader((InputStream) httpURLConnection.getContent());
            BufferedReader buff = new BufferedReader(in);
            String line = buff.readLine();
            while (line != null){
                buffer.append(line).append("\n");
                line = buff.readLine();
            }
            String response = buffer.toString();
            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        getJSONData.onRemoteCallComplete(s);
    }
}
