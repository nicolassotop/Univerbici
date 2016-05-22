package com.example.nico.univerbiciandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nico on 21-05-16.
 */
public class getEstacionamientos extends AsyncTask<String, Void, String> {

    private Context context;
    private ProgressDialog progressDialog;
    /**
     * Constructor
     */
    public getEstacionamientos(Context context, Activity activity) {
        this.context = context;
        this.progressDialog = new ProgressDialog(activity);
    }
    public getEstacionamientos(Context context) {
        this.context = context;
        this.progressDialog = null;
    }
    /**
     * Método que realiza la petición al servidor
     */
    @Override
    protected String doInBackground(String... urls) {

        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }


        return result.toString();
    }// doInBackground(String... urls)

    /**
     * Método que manipula la respuesta del servidor
     */
    @Override
    protected void onPreExecute() {
        if(progressDialog != null) {
            progressDialog.setMessage("Cargando datos de estacionamientos...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }

    @Override
    protected void onPostExecute(String result) {
        if(progressDialog != null)
            progressDialog.cancel();
        //Intent intent = new Intent("httpData").putExtra("data", result);
        //context.sendBroadcast(intent);

        /*
        //parse JSON data
        try {
            JSONArray jArray = new JSONArray(result);
            for(int i=0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                String name = jObject.getString("nombreEstacionamiento");
                int lat = jObject.getInt("ubi_x");
                int  lon = jObject.getInt("ubi_y");
                int cap = jObject.getInt("capacidad");
                int ocupados = jObject.getInt("ocupados");

            } // End Loop
            this.progressDialog.dismiss();
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        } // catch (JSONException e)*/
    } // protected void onPostExecute(Void v)

    //}// onPostExecute(String result)

}
