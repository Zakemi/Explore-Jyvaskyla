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
import java.net.ProtocolException;
import java.net.URL;

import fi.jamk.hunteam.explorejyvaskylaandroid.ManageSharedPreferences;

/**
 * Created by DoubleD on 2016. 12. 06..
 */

public class PostRating extends AsyncTask<Object, Void, Void>{


    @Override
    protected Void doInBackground(Object... params) {
        if (params.length != 3)
            return null;

        String userId = (String) params[0];
        Integer placeId = (Integer) params[1];
        Integer rating = ((Float) params[2]).intValue();

        URL url = null;
        try {
            url = new URL("http://130.234.200.164:3000/rate");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setChunkedStreamingMode(0);
            httpURLConnection.setRequestProperty("Content-Type","application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserId", userId);
            jsonObject.put("PlaceId", placeId);
            jsonObject.put("Rating", rating);
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
            System.out.println(buffer.toString());
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
