package de.b08.moodivation.utils;

import android.app.Activity;
import android.util.Pair;

import com.google.common.io.CharStreams;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ExportUtils {

    public static Pair<Boolean, Integer> exportDatabases(Activity activity, String id) throws IOException, JSONException {
        File locationDatabase = activity.getDatabasePath("LocationDatabase");
        File interventionDatabase = activity.getDatabasePath("InterventionDatabase");
        File questionnaireDatabase = activity.getDatabasePath("QuestionnaireDatabase");
        File sensorDatabase = activity.getDatabasePath("SensorDatabase");

        PostMethod postMethod = new PostMethod("http://127.0.0.1:8080/upload.php");

        Part locPart = new FilePart("locationdb", locationDatabase);
        Part sensorPart = new FilePart("sensordb", sensorDatabase);
        Part questionnairePart = new FilePart("questionnairedb", questionnaireDatabase);
        Part interventionPart = new FilePart("interventiondb", interventionDatabase);
        Part idPart = new StringPart("id", id);
        MultipartRequestEntity multipartRequestEntity = new MultipartRequestEntity(new Part[]{locPart, sensorPart, questionnairePart, interventionPart, idPart}, postMethod.getParams());
        postMethod.setRequestEntity(multipartRequestEntity);

        HttpClient client = new HttpClient();
        int statusCode = client.executeMethod(postMethod);

        InputStream inputStream = postMethod.getResponseBodyAsStream();
        String res = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        JSONObject jsonObject = new JSONObject(res);

        postMethod.releaseConnection();

        if (statusCode == HttpStatus.SC_OK) {
            if (jsonObject.getInt("result") != 1) {
                return new Pair<>(false, HttpStatus.SC_OK);
            }
            return new Pair<>(true, HttpStatus.SC_OK);
        } else {
            return new Pair<>(false, statusCode);
        }
    }

    public static String generateId() throws IOException, JSONException {
        URL url = new URL("http://127.0.0.1:8080/id.php");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        String res = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        JSONObject jsonObject = new JSONObject(res);
        if (jsonObject.getInt("result") != 1) {
            return null;
        } else {
            return jsonObject.getString("id");
        }
    }

}
