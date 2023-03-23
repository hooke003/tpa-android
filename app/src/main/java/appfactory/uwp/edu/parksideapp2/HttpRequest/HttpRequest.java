package appfactory.uwp.edu.parksideapp2.HttpRequest;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import timber.log.Timber;

/**
 * Created by kyluong09 on 3/18/18.
 */

public class HttpRequest {
    private final String routeApi = "58d904a497c67e00015b45fc8e14b676f5ad4f9d8bf19bf842c812fa";

    /**
     * Open Http Connection
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String doHttpConnection(String url) throws IOException {
        URL tempUrl = new URL(url);
        HttpURLConnection connect = (HttpURLConnection) tempUrl.openConnection();
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;
        Timber.d("Url: %s", tempUrl.toString());
        Timber.d("Response Code: %s", String.valueOf(connect.getResponseCode()));
        // Check connection result
        if (connect.getResponseCode() == HttpURLConnection.HTTP_OK) {
            Timber.d("Success");

        } else {
            Timber.d("Failed");
        }
        try {
            reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            Timber.d("XML = " + stringBuilder.toString());
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            connect.disconnect();
        }
    }

    /**
     * Get String XMl With Two Parameters
     * e.g. https://www.uwp.edu/customcf/xml/getevents.cfm?startdate=2018-01-01&enddate=2018-02-19
     *
     * @param url
     * @param startDate
     * @param endDate
     * @return
     * @throws IOException
     */
    public String getUrlEvent(String url, String startDate, String endDate) throws IOException {
        String tempUrl = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("startdate", startDate)
                .appendQueryParameter("enddate", endDate)
                .build().toString();
        Timber.d("URL = " + tempUrl);
        return doHttpConnection(tempUrl);
    }


    /**
     * Get Google Direction API With Two Parameter
     * @param origin
     * @param destination
     * @return
     * @throws IOException
     */
    @SuppressLint("TimberArgCount")
    public String getUrlDirection(String origin, String destination) throws IOException {
        String url = "https://api.openrouteservice.org/v2/directions/foot-walking?api_key=" + String.format("%s&start=%s&end=%s", routeApi, origin, destination);
        Timber.d(url);
//        String url = "https://api.openrouteservice.org/directions?" + "coordinates=" + origin + "|" + destination + "&" + "profile=foot-walking" + "&" + "units=mi" + "&" + "profile=foot-walking" + "&" + "api_key=" + routeApi;
        return doHttpConnection(url);
    }

    /**
     * Get String XML Without Query Parameters
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String getUrlWithoutParameter(String url) throws IOException {
        return doHttpConnection(url);
    }
}
