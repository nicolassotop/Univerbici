package com.example.nico.univerbiciandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class amigosCercaActivity extends AppCompatActivity {
    private Handler handler;
    private Thread t;
    private Context mContext;

    private TableLayout stk;
    private int num;

    private String nombre;
    private String apellido;
    private String email;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos_cerca);

        //tabla = (TableLayout)findViewById(R.id.tableLayoutId);


        handler = new Handler();

        t = new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() { // This thread runs in the UI
                    @Override
                    public void run() {
                        final ArrayList<Estacionamiento> estacionamientos = new ArrayList<Estacionamiento>();//creamos el objeto lista;
                        String rest = null;

                        try {
                            //obtengo el id para buscar solo los datos del usuario logeado
                            String idUser = Login.getIdUserLogged();
                            //Hago la peticion al servicio REST
                            rest = new HttpGet(mContext, amigosCercaActivity.this).execute("http://192.168.0.15:9090/sakila-backend-master/usuarios/"+idUser+"/cercanos").get();

                            //Se guarda el resultado en un array
                            JSONArray jRestAmigosCerca = new JSONArray(rest);


                            init();
                            //Se deben obtener varios amigos
                            for (int i = 0; i < jRestAmigosCerca.length(); i++) {
                                //Para cada amigo vuelvo a crear un jsonObject
                                JSONObject jObjectAmigosCerca = null;
                                try {
                                    jObjectAmigosCerca = jRestAmigosCerca.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //Si quedan amigos cerca y si el amigo no soy yo mismo
                                if (jObjectAmigosCerca != null && !idUser.equals(Integer.toString(i))) {

                                    //Se obtienen los campos para llenar la tabla
                                    nombre = jObjectAmigosCerca.getString("nombre");
                                    apellido = jObjectAmigosCerca.getString("apellido");
                                    email = jObjectAmigosCerca.getString("email");
                                    nickname = jObjectAmigosCerca.getString("nickname");

                                    //se llena la tabla
                                    llenarTabla(nombre,apellido,email,nickname);

                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                            ///
                        };
                });




        }
        });


        try {
        t.join();
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        t.start();
    }

    public void init() {
        stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" Nombre ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Apellido ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Email ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText(" Nickname ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);

    }

    public void llenarTabla(String name, String last, String correo, String nickk){

            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText(name);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);

            TextView t2v = new TextView(this);
            t2v.setText(last);
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);

            TextView t3v = new TextView(this);
            t3v.setText(correo);
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);

            TextView t4v = new TextView(this);
            t4v.setText(nickk);
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            stk.addView(tbrow);

    }

    public void onBackPressed() {
        //onNavigateUp();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
