package com.example.seritifikasilogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    ListView listView;
    ArrayList<String> listItem;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }


        myDb = new DatabaseHelper(this);
        listItem = new ArrayList<>();
        listView = findViewById(R.id.listView);

        // Load data from database
        viewAllData();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = listItem.get(position);
                showOptionDialog(selectedItem, position);
                return true;
            }
        });
    }

    private void viewAllData() {
        // Cek apakah tabel mahasiswa ada
        if (!myDb.isTableExists(DatabaseHelper.TABLE_NAME)) {
            Toast.makeText(MainActivity.this, "Tabel mahasiswa tidak ditemukan. Tidak dapat mengambil data.", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = myDb.getAllData();
        listItem.clear(); // Bersihkan item sebelumnya

        if (cursor == null || cursor.getCount() == 0) {
            // Tampilkan pesan jika data tidak ditemukan
            listItem.add("No data found.");
        } else {
            while (cursor.moveToNext()) {
                // Gabungkan semua data dari setiap baris ke dalam satu string
                String data = "Nomor: " + cursor.getString(0) + "\n" +
                        "Nama: " + cursor.getString(1);
                listItem.add(data);
            }
        }

        // Set adapter ke ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        listView.setAdapter(adapter);
    }


    private void showOptionDialog(final String selectedItem, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilihan");

        String[] pilihan = {"Lihat Data", "Update Data", "Delete Data"};
        builder.setItems(pilihan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Lihat Data
                        Intent detailIntent = new Intent(MainActivity.this, Profile.class);
                        detailIntent.putExtra("selectedItem", selectedItem);
                        startActivity(detailIntent);
                        break;
                    case 1: // Update Data
                        Intent updateIntent = new Intent(MainActivity.this, Update.class);
                        updateIntent.putExtra("selectedItem", selectedItem);
                        startActivity(updateIntent);
                        break;
                    case 2: // Delete Data
                        showDeleteConfirmationDialog(position);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Hapus");
        builder.setMessage("Apakah Anda yakin ingin menghapus data ini?");

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ambil nomor dari data yang dipilih
                String nomor = listItem.get(position).split("\n")[0].split(": ")[1];

                // Hapus data dari database
                int deletedRows = myDb.deleteData(nomor);

                if (deletedRows > 0) {
                    Toast.makeText(MainActivity.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                    listItem.remove(position); // Hapus item dari listItem
                    adapter.notifyDataSetChanged(); // Update ListView
                } else {
                    Toast.makeText(MainActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
