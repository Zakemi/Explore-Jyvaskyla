package fi.jamk.hunteam.explorejyvaskylaandroid;

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
import java.net.URLEncoder;

/**
 * Created by DoubleD on 2016. 11. 13..
 */

class ServerConnections {

    static class PostPlaceToServer extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            System.out.println("Hello from post");
            System.out.println(params[0]);
            System.out.println(params[7]);
            URL url;
            try {
                url = new URL("http://192.168.1.34:3000/locations");
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
                String response = buffer.toString();
                System.out.println(response);

                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
