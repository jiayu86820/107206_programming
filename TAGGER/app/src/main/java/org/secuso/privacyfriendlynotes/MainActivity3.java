package org.secuso.privacyfriendlynotes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.secuso.privacyfriendlynotes.code_old.DbAccess;
import org.secuso.privacyfriendlynotes.code_old.DbContract;
import org.secuso.privacyfriendlynotes.code_old.DbOpenHelper;
import org.secuso.privacyfriendlynotes.code_old.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener{
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            Intent intent = new Intent(MainActivity3.this, MainActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    EditText edtTag, edtContent;
    Button btnChoose, btnAdd, btnList, btnFind;
    ImageView imageView;

    final int REQUEST_CODE_GALLERY = 999;

    public static DbOpenHelper sqLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("新增相片筆記");
        }

        init();
        btnAdd.setOnClickListener(this);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        MainActivity3.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });




    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init(){
        edtContent = (EditText) findViewById(R.id.edtContent);
        edtTag = (EditText) findViewById(R.id.edtTag);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        imageView = (ImageView) findViewById(R.id.imageView);
    }


    @Override
    public void onClick(View v) {
        DbAccess.addNote2(
                getBaseContext(),
                edtTag.getText().toString(),
                edtContent.getText().toString(),
                imageViewToByte(imageView),
                DbContract.NoteEntry.TYPE_PHOTO
        );
        Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
        edtTag.setText("");
        edtContent.setText("");
        imageView.setImageResource(R.mipmap.ic_launcher);
    }

}
