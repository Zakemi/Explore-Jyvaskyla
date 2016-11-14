package fi.jamk.hunteam.explorejyvaskylaandroid.serverconnection;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetPlacesFromServer extends AsyncTask<Void, Void, String> {

    public interface GetPlacesCallBack{
        public void onRemoteCallComplete(String json);
    }

    private GetPlacesCallBack getPlacesCallBack;

    public GetPlacesFromServer(GetPlacesCallBack getPlacesCallBack){
        this.getPlacesCallBack = getPlacesCallBack;
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
            return buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        getPlacesCallBack.onRemoteCallComplete(s);
    }
}
