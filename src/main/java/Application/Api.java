package Application;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Api {
    private String urlString = "";

    public Api(String urlString) throws MalformedURLException {
        this.urlString = urlString;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream is = connection.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder JSONStringBuilder = new StringBuilder();
            while ((line = bf.readLine()) != null) {
                JSONStringBuilder.append(line).append("\n");
            }
            is.close();
            connection.disconnect();

            String jsonString = JSONStringBuilder.toString();
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

