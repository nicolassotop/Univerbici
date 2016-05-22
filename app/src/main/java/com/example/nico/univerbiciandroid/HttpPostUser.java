package com.example.nico.univerbiciandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by nico on 22-05-16.
 */
public class HttpPostUser extends AsyncTask <String, Void, String>{

    private Context context;
    private JSONObject json;
    ProgressDialog progressDialog;
    /**
     * Constructor
     */
    public HttpPostUser(Context context, JSONObject json, RegistrarseActivity activity) {
        this.context = context;
        this.json = json;
        this.progressDialog = new ProgressDialog(activity);
    }// HttpGet(Context context)

    @Override
    protected String doInBackground(String... urls) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Host", "http://192.168.0.15:8080/sakila-backend-master/usuarios");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            //JSONObject here
            connection.setUseCaches(false);
            connection.connect();

            OutputStreamWriter out = new   OutputStreamWriter(connection.getOutputStream());
            out.write(json.toString());
            out.close();
            //OutputStream outputStream = connection.getOutputStream();
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            //writer.write(json.toString());
            //writer.close();

            //Read
            int HttpResult =connection.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(),"utf-8"));
                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                Log.e("Salida","Respuesta positiva");
                return "OK";
            }else{
                Log.e("Respuesta del servidor",connection.getResponseMessage());

                Handler handler =  new Handler(context.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "Los servidores estan en mantenimiento, intenta más tarde.", Toast.LENGTH_SHORT).show();
                    }
                });
                handler.postDelayed(new Runnable() {
                    public void run() {
                        System.exit(0);
                    }
                }, 5000);

                return "FAIL";
            }
        } catch (MalformedURLException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        } catch (ProtocolException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        } catch (IOException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result)
    {

        progressDialog.cancel();

        //Call your method that checks if the pictures were downloaded

    }

    @Override
    protected void onPreExecute() {

        progressDialog.setMessage("Ingresando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


}
