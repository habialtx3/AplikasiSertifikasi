package com.example.seritifikasilogin;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.seritifikasilogin.R;

public class Profile extends AppCompatActivity {

    DatabaseHelper myDb;
    TextView formNomor, formNama, formTanggalLahir, formJenisKelamin, formAlamat;
    String nomor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }


        myDb = new DatabaseHelper(this);

        // Initialize the UI components
        formNomor = findViewById(R.id.formNomor);
        formNama = findViewById(R.id.formNama);
        formTanggalLahir = findViewById(R.id.formTanggal);
        formJenisKelamin = findViewById(R.id.formJenis);
        formAlamat = findViewById(R.id.formAlamat);

        // Get the selected item data from the Intent
        Intent intent = getIntent();
        String selectedItem = intent.getStringExtra("selectedItem");
        nomor = selectedItem.split("\n")[0].split(": ")[1]; // Extract nomor from selected item

        // Populate fields with existing data
        populateFields();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void populateFields() {
        Cursor cursor = myDb.getAllData();
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0).equals(nomor)) {
                    formNomor.setText(cursor.getString(0));
                    formNama.setText(cursor.getString(1));
                    formTanggalLahir.setText(cursor.getString(2));
                    formJenisKelamin.setText(cursor.getString(3));
                    formAlamat.setText(cursor.getString(4));
                    setTitle(cursor.getString(1));
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}