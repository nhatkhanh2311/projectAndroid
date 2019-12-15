package com.example.got_it;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private TextView textResult;
    private TessBaseAPI mTess1, mTess2;
    private ImageView imgInput;
    private Button btnVie, btnJpn;
    private Button btnCamera;
    private Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareTessDataCopy1();
        prepareTessDataCopy2();
        mTess1 = new TessBaseAPI();
        mTess2 = new TessBaseAPI();
        mTess1.init(getFilesDir() + "", "vie");
        mTess2.init(getFilesDir() + "", "jpn");
        textResult = findViewById(R.id.text);
        imgInput = findViewById(R.id.image);
        btnVie = findViewById(R.id.vie);
        btnJpn = findViewById(R.id.jpn);
        btnCamera = findViewById(R.id.camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        btnVie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTess1.setImage(bitmap);
                String result = mTess1.getUTF8Text();
                textResult.setText(result);
            }
        });
        btnJpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTess2.setImage(bitmap);
                String result = mTess2.getUTF8Text();
                textResult.setText(result);
            }
        });
    }
    private void prepareTessDataCopy1() {
        try {
            File dir = new File(getFilesDir() + "/tessdata");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File traineddata = new File(getFilesDir() + "/tessdata/vie.traineddata");
            if (!traineddata.exists()) {
                AssetManager asset = getAssets();
                InputStream in = asset.open("tessdata/vie.traineddata");
                OutputStream out = new FileOutputStream(getFilesDir() + "/tessdata/vie.traineddata");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {e.printStackTrace();}
    }
    private void prepareTessDataCopy2() {
        try {
            File dir = new File(getFilesDir() + "/tessdata");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File traineddata = new File(getFilesDir() + "/tessdata/jpn.traineddata");
            if (!traineddata.exists()) {
                AssetManager asset = getAssets();
                InputStream in = asset.open("tessdata/jpn.traineddata");
                OutputStream out = new FileOutputStream(getFilesDir() + "/tessdata/jpn.traineddata");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {e.printStackTrace();}
    }
    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity((getPackageManager())) != null) {
            startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            bitmap = imageBitmap;
            imgInput.setImageBitmap(imageBitmap);
        }
    }
}