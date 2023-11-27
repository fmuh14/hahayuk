package com.example.ujikom_2023;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        Intent toTambahPegawai = new Intent(HomeScreen.this, TambahPegawai.class);

        Button tambahPegawai = (Button) findViewById(R.id.buttonTambahPegawai);
        tambahPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toTambahPegawai);
            }
        });
    }
}
