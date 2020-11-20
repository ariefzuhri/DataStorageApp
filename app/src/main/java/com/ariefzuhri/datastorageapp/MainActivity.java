package com.ariefzuhri.datastorageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final String FILENAME = "filename.txt";
    public static final String PREFNAME = "com.ariefzuhri.datastorageapp.PREF";

    private TextView textView;
    private EditText editText;

    // Untuk external storage
    private static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final int REQUEST_CODE = 100;

    private static boolean hasPermission(Context context, String... permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null){
            for (String permission : permissions){
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);
        editText = findViewById(R.id.edit_text);
    }

    /*
    * Fungsi button onClick*/
    public void saveFileSP(View view) {
        saveFileSP();
    }

    public void deleteFileSP(View view) {
        deleteFileSP();
    }

    public void readFileSP(View view) {
        readFileSP();
    }

    public void saveFileIS(View view) {
        saveFileIS();
    }

    public void deleteFileIS(View view) {
        deleteFileIS();
    }

    public void readFileIS(View view) {
        readFileIS();
    }

    public void saveFileES(View view) {
        saveFileES();
    }

    public void deleteFileES(View view) {
        deleteFileES();
    }

    public void readFileES(View view) {
        readFileES();
    }

    /*
    * Shared preference*/
    public void saveFileSP(){
        String content = editText.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FILENAME, content);
        editor.apply();
    }

    public void readFileSP(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME, MODE_PRIVATE);
        if (sharedPreferences.contains(FILENAME)){
            String myText = sharedPreferences.getString(FILENAME, "");
            textView.setText(myText);
        } else textView.setText("");
    }

    public void deleteFileSP() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /*
    * Internal storage*/
    public void saveFileIS(){
        String content = editText.getText().toString();
        File path = getDir("NEWFOLDER", MODE_PRIVATE); // Buat direktori baru
        File file = new File(path.toString(), FILENAME);

        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file, false);
            /*
             * Append false -> timpa file yang ada
             * Append true -> isi baru digabung dengan yang sudah ada*/
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFileIS(){
        File path = getDir("NEWFOLDER", MODE_PRIVATE);
        File file = new File(path.toString(), FILENAME);

        if (file.exists()){
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null){
                    text.append(line);
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            textView.setText(text.toString());
        } else {
            textView.setText("");
        }
    }

    public void deleteFileIS(){
        File path = getDir("NEWFOLDER", MODE_PRIVATE);
        File file = new File(path.toString(), FILENAME);

        if (file.exists()) file.delete();
    }

    /*
    * External storage*/

    public void saveFileES(){
        if (hasPermission(this, PERMISSIONS)){
            String content = editText.getText().toString();
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path.toString(), FILENAME);

            try {
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file, false);
                outputStream.write(content.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE);
        }
    }

    public void readFileES(){
        File path = Environment.getExternalStorageDirectory();
        File file = new File(path.toString(), FILENAME);

        if (file.exists()){
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null){
                    text.append(line);
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            textView.setText(text.toString());
        } else {
            textView.setText("");
        }
    }

    public void deleteFileES(){
        File path = Environment.getExternalStorageDirectory();
        File file = new File(path.toString(), FILENAME);

        if (file.exists()) file.delete();
    }
}