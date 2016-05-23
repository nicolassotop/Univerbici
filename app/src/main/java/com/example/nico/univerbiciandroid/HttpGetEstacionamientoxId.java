package com.example.nico.univerbiciandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nico on 23-05-16.
 */
public class HttpGetEstacionamientoxId extends AsyncTask<String, Void, String> {
    private Context context;
    private ProgressDialog progressDialog;
    /**
     * Constructor
     */
    public HttpGetEstacionamientoxId(Context context, editarEstacionamActivity activity) {
        this.context = context;
        this.progressDialog = new ProgressDialog(activity);
    }// HttpGet(Context context)
    public HttpGetEstacionamientoxId(Context context) {
        this.context = context;
        this.progressDialog = null;
    }// HttpGet(Context context)

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
            progressDialog.setMessage("Cargando datos de estacionamiento...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }

    @Override
    protected void onPostExecute(String result) {
        if(progressDialog != null)
            progressDialog.cancel();/*
        Intent intent = new Intent("httpData").putExtra("data", result);
        context.sendBroadcast(intent);*/
    }// onPostExecute(String result)

}//