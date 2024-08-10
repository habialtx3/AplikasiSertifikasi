package com.example.seritifikasilogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class Insert extends AppCompatActivity {

    DatabaseHelper db;
    EditText formNama, formNomor, formAlamat, formJenis, formTanggal_lahir;
    FloatingActionButton bSimpan;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }

        db = new DatabaseHelper(this);

        formNomor = findViewById(R.id.formNomor);
        formNama = findViewById(R.id.formNama);
        formTanggal_lahir = findViewById(R.id.formTanggal);
        formJenis = findViewById(R.id.formJenis);
        formAlamat = findViewById(R.id.formAlamat);
        bSimpan = findViewById(R.id.bSimpan);

        formTanggal_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        bSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void showDatePickerDialog() {
        // Get current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Display the selected date in the EditText
                formTanggal_lahir.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void insertData() {
        String nomor = formNomor.getText().toString();
        String nama = formNama.getText().toString();
        String tanggal_lahir = formTanggal_lahir.getText().toString();
        String jenis_kelamin = formJenis.getText().toString();
        String alamat = formAlamat.getText().toString();

        if (nomor.isEmpty() || nama.isEmpty() || tanggal_lahir.isEmpty() || jenis_kelamin.isEmpty() || alamat.isEmpty()) {
            Toast.makeText(this, "Semua File Harus Diisi", Toast.LENGTH_SHORT).show();
        } else {
            boolean isInserted = db.insertData(
                    Integer.parseInt(nomor),
                    nama,
                    tanggal_lahir,
                    jenis_kelamin,
                    alamat
            );

            if (isInserted) {
                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                clearField();
            } else {
                Toast.makeText(this, "Data gagal disimpan", Toast.LENGTH_SHORT).show();
            }
        }
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
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void clearField() {
        formNomor.setText("");
        formNama.setText("");
        formTanggal_lahir.setText("");
        formJenis.setText("");
        formAlamat.setText("");
    }
}