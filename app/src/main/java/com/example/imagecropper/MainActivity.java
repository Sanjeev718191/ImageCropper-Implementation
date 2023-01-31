package com.example.imagecropper;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

//Video tutorial link - https://www.youtube.com/watch?v=DM8vorNKIFg

public class MainActivity extends AppCompatActivity {
    ImageView imageView;

    ActivityResultLauncher<String> mGetContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        imageView .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                String dest_uri = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();

                UCrop.Options options = new UCrop.Options();
                options.setCircleDimmedLayer(true);
                UCrop.of(result, Uri.fromFile(new File(getCacheDir(),dest_uri)))
                        .withOptions(options)
                        .useSourceImageAspectRatio()
                        .withMaxResultSize(2000, 2000)
                        .start(MainActivity.this);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode==UCrop.REQUEST_CROP) {
            imageView.setImageURI(UCrop.getOutput(data));
        } else if(resultCode == UCrop.RESULT_ERROR){
            final Throwable cropError = UCrop.getError(data);
        }

    }

}