package fi.jamk.hunteam.explorejyvaskylaandroid.serverconnection;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostPlaceToServer extends AsyncTask<String, Void, String> {

    public interface PostPlaceCallBack{
        public void onRemoteCallComplete(String json);
    }

    PostPlaceCallBack postPlaceCallBack;

    public PostPlaceToServer(PostPlaceCallBack postPlaceCallBack){
        this.postPlaceCallBack = postPlaceCallBack;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        try {
            URL url = new URL("http://192.168.1.34:3000/locations");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setChunkedStreamingMode(0);
            httpURLConnection.setRequestProperty("Content-Type","application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("GoogleID", params[0]);
            jsonObject.put("Name", params[1]);
            jsonObject.put("Address", params[2]);
            jsonObject.put("Latitude", params[3]);
            jsonObject.put("Longitude", params[4]);
            jsonObject.put("Type", params[5]);
            jsonObject.put("Phone", params[6]);
            jsonObject.put("Web", params[7]);

            String post_urlcoded = jsonObject.toString();
            System.out.println(post_urlcoded);

            DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            outputStream.writeBytes(post_urlcoded);
            outputStream.flush();
            outputStream.close();

            StringBuffer buffer = new StringBuffer();
            InputStreamReader in = new InputStreamReader((InputStream) httpURLConnection.getContent());
            BufferedReader buff = new BufferedReader(in);
            String line = buff.readLine();
            while (line != null){
                buffer.append(line).append("\n");
                line = buff.readLine();
            }
            result = buffer.toString();

            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        postPlaceCallBack.onRemoteCallComplete(s);
    }
}
