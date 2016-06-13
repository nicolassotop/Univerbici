package com.example.nico.univerbiciandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.StringTokenizer;

//import java.class.Login;

/**
 * Created by nico on 13-06-16.
 */
public class HttpPostLogin extends AsyncTask<String, Void, String> {

    private Context context;
    private JSONObject json;
    ProgressDialog progressDialog;
    static int ready;

    public static int getReady() {
        return ready;
    }

    public static void setReady(int ready) {
        HttpPostLogin.ready = ready;
    }

    /**
     * Constructor
     */
    public HttpPostLogin(Context context, JSONObject json, Login activity) {
        this.context = context;
        this.json = json;
        this.progressDialog = new ProgressDialog(activity);
        ready=0;
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
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Host", "192.168.0.15");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setUseCaches(false);
            connection.connect();




            OutputStreamWriter out = new   OutputStreamWriter(connection.getOutputStream());
            out.write(json.toString());
            out.flush();
            out.close();



            //Read
            int HttpResult =connection.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(),"utf-8"));
                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    Log.e("WHILE","sb="+sb);
                    //Imprime {"INFO":"Loggeado","usuarioId":1,"nickname":"nick1","email":"email1"}
                    String retorno = sb.toString();
                    StringTokenizer tokens = new StringTokenizer(retorno,"{}:,\"");

//                    while(tokens.hasMoreTokens()){

                    tokens.nextToken();
                    Login.setEstadoUserLogged(tokens.nextToken());
                    //String estadoUserLogin = tokens.nextToken();

                    Log.e("ESTADO USER","estado: "+Login.getEstadoUserLogged());

                    if (Login.getEstadoUserLogged().equals("Loggeado")){
                        tokens.nextToken();

                        Login.setIdUserLogged(tokens.nextToken());
                        Log.e("ID LOGGED","id= "+Login.getIdUserLogged());

                        tokens.nextToken();

                        Login.setNickUserLogged(tokens.nextToken());
                        Log.e("NICK LOGGED","nick= "+Login.getNickUserLogged());

                        tokens.nextToken();

                        Login.setEmailUserLogged(tokens.nextToken());
                        Log.e("CORREO LOGGED","correo= "+Login.getEmailUserLogged());



                    }
                    ready = 1;


                }
                br.close();

                Log.e("Salida","Respuesta positiva");
                return "OK";
            }else{
                Log.e("Respuesta del servidor",connection.getResponseMessage());
                return "OK";

                // CON ESTO SE CAEEEE
                //DEJARLO ASI HASTA SUBIR LA APP A UN SERVIDOR

                /*Handler handler =  new Handler(context.getMainLooper());
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

                return "FAIL";*/
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
        ready = 1;
        Log.e("TERMINA","ready= "+ready);

        //Call your method that checks if the pictures were downloaded

    }

    @Override
    protected void onPreExecute() {

        progressDialog.setMessage("Ingresando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


}
