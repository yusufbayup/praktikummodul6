package com.example.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;

import java.util.HashMap;


public class  MainActivity extends AppCompatActivity implements View.OnClickListener{

    //dibawah ini meruapakan perintah untuk mendefinisikan view
    private EditText editTextNama;
    private EditText editTextJurusan;
    private EditText editTextEmail;
    private Button buttonAdd;
    private Button buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inisialisasi dari view
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextJurusan = (EditText) findViewById (R.id.editTextJurusan);
        editTextEmail = (EditText) findViewById (R.id.editTextEmail);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView = (Button) findViewById(R.id.buttonView);

        //setting listence to button
        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);
    }

    //dibawah ini meruapakan peritah untuk menambhakan mahasiswa (CREATE)
    private void addMahasiswa(){
        final String nama = editTextNama.getText().toString().trim();
        final String jurusan = editTextJurusan.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        class AddMahasiswa extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

         @Override
            protected void onPreExecute(){
             super.onPreExecute();
             loading = ProgressDialog.show(MainActivity.this,"menambahkan...","tunggu...",false,false);
        }


        @Override
            protected void onPostExecute (String s){
             super.onPostExecute(s);
             loading.dismiss();

             Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();

         }

         @Override
            protected String doInBackground(Void... v){
             HashMap<String,String> params = new HashMap<>();
             params.put(konfigurasi.KEY_MHS_NAMA,nama);
             params.put(konfigurasi.KEY_MHS_JURUSAN,jurusan);
             params.put(konfigurasi.KEY_MHS_EMAIL,email);

             RequestHandler rh = new RequestHandler();
             String res = rh.sendPostRequest(konfigurasi.URL_ADD, params);
             return res;
         }

        }

        AddMahasiswa ae = new AddMahasiswa();
        ae.execute();

    }

    @Override
    public void onClick(View v){
        if (v == buttonAdd){
            addMahasiswa();
        }

        if (v == buttonView){
            startActivity(new Intent(MainActivity.this, RequiresPermission.Read.class));


        }
    }



}
