package com.example.seritifikasilogin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class Update extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText formNomor, formNama, formTanggalLahir, formAlamat;
    RadioGroup radioGroupJenis;
    RadioButton radioMale, radioFemale;
    FloatingActionButton bUpdate;
    String nomor;
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }

        myDb = new DatabaseHelper(this);

        // Initialize the UI components
        formNomor = findViewById(R.id.formNomor);
        formNama = findViewById(R.id.formNama);
        formTanggalLahir = findViewById(R.id.formTanggal);
        formAlamat = findViewById(R.id.formAlamat);
        radioGroupJenis = findViewById(R.id.radioGroupJenis);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        bUpdate = findViewById(R.id.bSimpan);

        // Get the selected item data from the Intent
        Intent intent = getIntent();
        String selectedItem = intent.getStringExtra("selectedItem");
        nomor = selectedItem.split("\n")[0].split(": ")[1];

        formTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Populate fields with existing data
        populateFields();

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
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
                formTanggalLahir.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void populateFields() {
        Cursor cursor = myDb.getAllData();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (cursor.getString(0).equals(nomor)) {
                    formNomor.setText(cursor.getString(0));
                    formNama.setText(cursor.getString(1));
                    formTanggalLahir.setText(cursor.getString(2));

                    String gender = cursor.getString(3);
                    if (gender.equalsIgnoreCase("Laki-laki")) {
                        radioMale.setChecked(true);
                    } else if (gender.equalsIgnoreCase("Perempuan")) {
                        radioFemale.setChecked(true);
                    }

                    formAlamat.setText(cursor.getString(4));
                    setTitle("Update "+cursor.getString(1));
                    break;
                }
            } while (cursor.moveToNext());
        }
        if(cursor != null){
            cursor.close();
        }
    }

    private void updateData() {
        String nomor = formNomor.getText().toString();
        String nama = formNama.getText().toString();
        String tanggalLahir = formTanggalLahir.getText().toString();

        int selectedId = radioGroupJenis.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(selectedId);
        String jenisKelamin = selectedGender != null ? selectedGender.getText().toString() : "";

        String alamat = formAlamat.getText().toString();

        boolean isUpdated = myDb.updateData(nomor, nama, tanggalLahir, jenisKelamin, alamat);
        if (isUpdated) {
            Toast.makeText(Update.this, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(Update.this, "Gagal memperbarui data!", Toast.LENGTH_SHORT).show();
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
}
