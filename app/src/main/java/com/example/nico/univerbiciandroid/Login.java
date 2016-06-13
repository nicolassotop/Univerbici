package com.example.nico.univerbiciandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private Button botonRegistro;
    private Button botonLogea;
    private EditText nicknameIngresa;
    private EditText passIngresa;

    private String passIn;
    private String nickIn;
    private String nickDB;
    private String passDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        botonRegistro = (Button)findViewById(R.id.buttonIngresa);
        botonRegistro.setOnClickListener(this);

        botonLogea = (Button)findViewById(R.id.registraButton);
        botonLogea.setOnClickListener(this);


    }

    @Override
    public void onClick(View v){

        if(v.getId() == R.id.buttonIngresa){

            Intent intent = new Intent(this,RegistrarseActivity.class);
            startActivity(intent);

        }

        if (v.getId() == R.id.registraButton){

            nickIn = nicknameIngresa.getText().toString();
            passIn = passIngresa.getText().toString();


            //LLAMAR A REST PARA COMPROBAR
        }

    }
}
