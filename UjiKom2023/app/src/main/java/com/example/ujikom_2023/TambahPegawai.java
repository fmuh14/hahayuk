package com.example.ujikom_2023;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.Nullable;

import java.util.Calendar;


public class TambahPegawai extends AppCompatActivity {
    private Button inputPegawai, pilihTanggalLahir, pilihFoto;
    private EditText txtNama, txtNIP, txtNoHP;
    private TextView txtTanggalLahir;
    private String jenisKelamin;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    private final int GALLERY_REQ_CODE = 1000;
    String cameraPermission[];
    String storagePermission[];
    ProgressDialog pd;
    Uri imageuri;
    String profileOrCoverPhoto;
    private ImageView FotoProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_pegawai);
        Intent toUploadPhoto = new Intent(TambahPegawai.this, HomeScreen.class);

        //Button
        inputPegawai = findViewById(R.id.inputPegawai);
        pilihTanggalLahir = findViewById(R.id.pilihTanggalLahir);
        pilihFoto = findViewById(R.id.pilihFoto);

        //EditText
        txtNama = findViewById(R.id.inputNama);
        txtNIP = findViewById(R.id.inputNIP);
        txtNoHP = findViewById(R.id.inputPhoneNumber);

        //TextView
        txtTanggalLahir = findViewById(R.id.resultTanggalLahir);

        //UploadFoto
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);


        pilihTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year, month, day;
                // Mendapatkan tanggal hari ini
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        TambahPegawai.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                if(monthOfYear < 9) {
                                    txtTanggalLahir.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                } else {
                                    txtTanggalLahir.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        FotoProfil = findViewById(R.id.profileImage);
        pilihFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent iGallery = new Intent(Intent.ACTION_PICK);
//                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(iGallery, GALLERY_REQ_CODE);
                pd.setMessage("Updating Profile Picture");
                profileOrCoverPhoto = "image";
                showImagePicDialog();
            }
        });

        inputPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama, nip, nomorHandphone, tanggalLahir;

                nama = txtNama.getText().toString();
                nip = txtNIP.getText().toString();
                nomorHandphone = txtNoHP.getText().toString();
                tanggalLahir = txtTanggalLahir.getText().toString();

                if(nama.equals("")) {
                    Toast.makeText(TambahPegawai.this, "Input nama anda.", Toast.LENGTH_SHORT).show();
                }
                else if(nip.equals("")){
                    Toast.makeText(TambahPegawai.this, "Input NIP anda.", Toast.LENGTH_SHORT).show();
                }
                else if(nomorHandphone.equals("")){
                    Toast.makeText(TambahPegawai.this, "Input nomor handphone anda.", Toast.LENGTH_SHORT).show();
                }
                else if(jenisKelamin.equals("")){
                    Toast.makeText(TambahPegawai.this, "Pilih jenis kelamin anda.", Toast.LENGTH_SHORT).show();
                }
                else if(tanggalLahir.equals("DD-MM-YYYY")){
                    Toast.makeText(TambahPegawai.this, "Pilih tanggal lahir anda.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(TambahPegawai.this, String.format("%s %s %s %s %s",nama,nip,nomorHandphone,jenisKelamin, tanggalLahir), Toast.LENGTH_SHORT).show();
                }
//                startActivity(toUploadPhoto);
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_male:
                if (checked)
                    jenisKelamin = "Laki - laki";
                    break;
            case R.id.radio_female:
                if (checked)
                    jenisKelamin = "Perempuan";
                    break;
        }
    }

    // Here we are showing image pic dialog where we will select
    // and image either from camera or gallery
    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // if access is not given then we will request for permission
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    // checking storage permission ,if given then we can add something in our storage
    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // requesting for storage permission
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    // checking camera permission ,if given then we can click image using our camera
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // requesting for camera permission if not given
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    // Here we will click a photo and then go to startactivityforresult for updating data
    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageuri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(camerIntent, IMAGE_PICKCAMERA_REQUEST);
    }

    // We will select an image from gallery
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        Log.d("TEST TAG","Udah sampe sini ni gan");
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGEPICK_GALLERY_REQUEST) {
                assert data != null;
                imageuri = data.getData();
                FotoProfil.setImageURI(imageuri);
            }
            if (requestCode == IMAGE_PICKCAMERA_REQUEST) {
                FotoProfil.setImageURI(imageuri);
            }
        }
//        if (resultCode == RESULT_OK) {
//            if (requestCode == GALLERY_REQ_CODE) {
//                Log.d("TEST TAG","Udah sampe sini ni gan");
//                assert data != null;
//                FotoProfil.setImageURI(data.getData());
//            }
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromCamera();
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case STORAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                } else {
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}

