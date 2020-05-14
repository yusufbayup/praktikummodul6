package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class select extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextId;
    private EditText editTextNama;
    private EditText editTextJurusan;
    private EditText editTextEmail;

    private Button buttonUpdate;
    private Button buttonDelete;

    private String id;
    @Override
        protected void onCreate (Bundle saveInstanceState){
            super.onCreate(saveInstanceState);
            setContentView(R.layout.activity_select);
            Intent intent = getIntent();

            id = intent.getStringExtra(konfigurasi.MHS_ID);
            editTextId = (EditText) findViewById(R.id.editTextId);
            editTextNama = (EditText) findViewById(R.id.editTextNama);
            editTextJurusan = (EditText) findViewById(R.id.editTextJurusan);
            editTextEmail = (EditText) findViewById(R.id.editTextEmail);

            buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
            buttonDelete = (Button) findViewById(R.id.buttonDelete);
            buttonUpdate.setOnClickListener(this);
            buttonDelete.setOnClickListener(this);

            editTextId.setText(id);

            getMahasiswa();
    }
    private void getMahasiswa(){
        class GetMahasiswa extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(select.this,"Fetching...","Wait...",false,false);

            }
            @Override
            protected void onPostExecute (String s){
                super.onPostExecute(s);
                loading.dismiss();
                showMahasiswa(s);


            }
            @Override
            protected String doInBackground(Void... params){
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_MHS,id);
                return  s;
            }
        }
        GetMahasiswa ge = new GetMahasiswa();
        ge.execute();
    }

        private void showMahasiswa(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject C = result.getJSONObject(0);
            String name = C.getString(konfigurasi.TAG_NAMA);
            String desg = C.getString(konfigurasi.KEY_MHS_JURUSAN);
            String sal = C.getString(konfigurasi.TAG_EMAIL);

            editTextNama.setText(name);
            editTextJurusan.setText(desg);
            editTextEmail.setText(sal);

        }catch (JSONException e){
            e.printStackTrace();
        }
        }
        private void updateMahasiswa(){
        final String nama = editTextNama.getText().toString().trim();
        final  String jurusan = editTextJurusan.getText().toString().trim();
        final  String email = editTextEmail.getText().toString().trim();

        class UpdateMahasiswa extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(select.this,"Mengupdate...","Silahkan Tunggu...",false,false);

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();

                Toast.makeText(select.this,s,Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(konfigurasi.KEY_MHS_ID,id);
                hashMap.put(konfigurasi.KEY_MHS_NAMA,nama);
                hashMap.put(konfigurasi.KEY_MHS_JURUSAN,jurusan);
                hashMap.put(konfigurasi.KEY_MHS_EMAIL,email);
                RequestHandler rh = new RequestHandler();

                String s =
                        rh.sendPostRequest(konfigurasi.URL_UPDATE_MHS,hashMap);
                return s;
            }
        }
            UpdateMahasiswa ue = new UpdateMahasiswa();
            ue.execute();
        }
    private void deleteMahasiswa(){
        class DeleteMahasiswa extends AsyncTask<Void,Void,String>
        {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(select.this,
                        "Mengupdate...", "Silahkan Tunggu...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(select.this, s,
                        Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s =
                        rh.sendGetRequestParam(konfigurasi.URL_DELETE_MHS, id);
                return s;
            }
        }
        DeleteMahasiswa de = new DeleteMahasiswa();
        de.execute();
    }
    private void confirmDeleteEmployee(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda Yakin Ingin Menghapus Pegawai ini?");
                alertDialogBuilder.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int
                                    arg1) {
                                deleteMahasiswa();
                                startActivity(new
                                        Intent(select.this, RequiresPermission.Read.class));
                            }
                        });
        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int
                            arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateMahasiswa();
        }
        if(v == buttonDelete){

            confirmDeleteEmployee();
        }
    }
}

