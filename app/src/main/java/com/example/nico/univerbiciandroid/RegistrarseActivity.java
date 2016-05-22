package com.example.nico.univerbiciandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;

public class RegistrarseActivity extends AppCompatActivity implements View.OnClickListener{

    Button botonGuardar;
    Button botonCancelar;

    EditText nombreIngresa;
    EditText apellidoIngresa;

    RadioButton sexoIngresaHombre;
    RadioButton sexoIngresaMujer;
    RadioGroup grupoSexo;

    EditText nickIngresa;
    EditText correoIngresa;
    EditText passIngresa;
    EditText dirIngresa;
    EditText telIngresa;
    EditText nacimientoIngresa;


    String nombre;
    String apellido;
    String nick;
    String pass;
    int sexoInt;
    String fecha;
    String correo;
    String telefono;
    String dire;

    int sexo;

    Usuario user;

    Thread t;
    Handler handler;
    Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        botonGuardar = (Button)findViewById(R.id.aceptaIn);
        //Registrando la escucha sobre la actividad Main
        botonGuardar.setOnClickListener(this);

        botonCancelar = (Button)findViewById(R.id.cancelaIn);
        //Registrando la escucha sobre la actividad Main
        botonCancelar.setOnClickListener(this);

        grupoSexo = (RadioGroup)findViewById(R.id.grupoSex);
        grupoSexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.hombre){
                    //SEXO HOMBRE
                    sexo=1;

                }
                else if (checkedId == R.id.mujer){
                    //SEXO MUJER
                    sexo=0;
                }
            }
        });


        nombreIngresa = (EditText)findViewById(R.id.nombreIn);
        apellidoIngresa = (EditText)findViewById(R.id.apellidoIn);
        sexoIngresaHombre = (RadioButton) findViewById(R.id.hombre);
        sexoIngresaMujer = (RadioButton) findViewById(R.id.mujer);
        nickIngresa = (EditText)findViewById(R.id.nickIn);
        correoIngresa = (EditText)findViewById(R.id.correoIn);
        passIngresa = (EditText)findViewById(R.id.passIn);
        dirIngresa = (EditText)findViewById(R.id.dirIn);
        telIngresa= (EditText)findViewById(R.id.telIn);
        nacimientoIngresa =(EditText)findViewById(R.id.nacimientoIn);

    }

    public static String POST(String url, Usuario userNuevo){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("email", userNuevo.getEmailUser());
            jsonObject.accumulate("password", userNuevo.getPassUser());
            jsonObject.accumulate("nombre", userNuevo.getNombreUser());
            jsonObject.accumulate("apellido", userNuevo.getApellidoUser());
            jsonObject.accumulate("nickname", userNuevo.getNicknameUser());
            jsonObject.accumulate("direccion", userNuevo.getDireccionUser());
            jsonObject.accumulate("sexo", userNuevo.getSexoUser());
            jsonObject.accumulate("fechaNacimiento", userNuevo.getFechaNacUser());
            jsonObject.accumulate("Telefono", userNuevo.getTelefonoUser());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("usuarios", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
/*
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            user = new Usuario();
            user.setNombreUser(nombre);
            user.setApellidoUser(apellido);
            user.setNicknameUser(nick);
            user.setEmailUser(correo);
            user.setPassUser(pass);
            user.setDireccionUser(dire);
            user.setTelefonoUser(telefono);
            user.setFechaNacUser(fecha);
            user.setSexoUser(sexoInt);
            Log.e("ESPACIO","\n\n\n\n\n\n\n\n\n\n\n\n\n");
            Log.e("Registro", "Nombre: "+nombre+"\n"+"Apellido: "+apellido);
            Log.e("ESPACIO","\n\n\n\n\n\n\n\n\n\n\n\n\n");
            return POST(urls[0],user);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Se ha registrado exitosamente", Toast.LENGTH_LONG).show();

        }
    }*/


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.aceptaIn) {

            nombre=nombreIngresa.getText().toString();
            apellido=apellidoIngresa.getText().toString();
            nick=nickIngresa.getText().toString();
            correo=correoIngresa.getText().toString();
            pass=passIngresa.getText().toString();
            dire=dirIngresa.getText().toString();
            telefono=telIngresa.getText().toString();
            fecha=nacimientoIngresa.getText().toString();
            sexoInt=sexo;

            Log.e("JSONOBJ","Nombre: "+nombre);

            JSONObject jsonUser = new JSONObject();

            try {
                jsonUser.put("nombre",nombre);
                jsonUser.put("apellido",apellido);
                jsonUser.put("nickname",nick);
                jsonUser.put("email",correo);
                jsonUser.put("password",pass);
                jsonUser.put("direccion",dire);
                jsonUser.put("Telefono",telefono);
                jsonUser.put("fechaNacimiento",fecha);
                jsonUser.put("sexo",sexoInt);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            new HttpPostUser(mcontext, jsonUser,RegistrarseActivity.this).execute("http://192.168.0.15:8080/sakila-backend-master/usuarios/");


            /*Intent intent = new Intent(this, mapa.class);
            startActivity(intent);
            Pantalla bienvenida, mostrar mensaje




            t = new Thread(new Runnable() {
                public void run() {
                    handler.post(new Runnable() { // This thread runs in the UI
                        @Override
                        public void run() {

                            JSONObject jsonParam = new JSONObject();

                            nombreIngresa = (EditText)findViewById(R.id.nombreIn);
                            apellidoIngresa = (EditText)findViewById(R.id.apellidoIn);
                            sexoIngresaHombre = (RadioButton) findViewById(R.id.hombre);
                            sexoIngresaMujer = (RadioButton) findViewById(R.id.mujer);
                            nickIngresa = (EditText)findViewById(R.id.nickIn);
                            correoIngresa = (EditText)findViewById(R.id.correoIn);
                            passIngresa = (EditText)findViewById(R.id.passIn);
                            dirIngresa = (EditText)findViewById(R.id.dirIn);
                            telIngresa= (EditText)findViewById(R.id.telIn);
                            nacimientoIngresa =(EditText)findViewById(R.id.nacimientoIn);


                            String nombre = nombreIngresa.getText().toString();
                            String apellido = apellidoIngresa.getText().toString();
                            String nick = nickIngresa.getText().toString();
                            String correo = correoIngresa.getText().toString();
                            String pass = passIngresa.getText().toString();
                            String dir = dirIngresa.getText().toString();
                            int tel = Integer.parseInt(telIngresa.getText().toString());
                            String nac= nacimientoIngresa.getText().toString();



                            Usuario nuevoUser = new Usuario(nombre, apellido, nick, pass, correo,
                                    dir, sexo, tel, nac);


                            try {
                                jsonParam.put("nombre", nombre);
                                jsonParam.put("apellido", apellido);
                                jsonParam.put("nickname", nick);
                                jsonParam.put("email", correo);
                                jsonParam.put("password", pass);
                                jsonParam.put("direccion", dir);
                                jsonParam.put("Telefono", tel);
                                jsonParam.put("fechaNacimiento", nac);
                                jsonParam.put("sexo",sexo);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            new HttpPostUser(mcontext,jsonParam, RegistrarseActivity.this).execute("http://192.168.0.15:8080/sakila-backend-master/usuarios/");

                        }
                    });
                }
            });
            t.start();




*/

        }
        if(v.getId() == R.id.cancelaIn){

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

        }

    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
