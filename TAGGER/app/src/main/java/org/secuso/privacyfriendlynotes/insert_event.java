package org.secuso.privacyfriendlynotes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlynotes.code_old.DbOpenHelper;

public class insert_event extends AppCompatActivity implements View.OnClickListener{
    private TextView txtTitle,txtTag1,txtTag2,txtTime,txtNotice;
    public static DbOpenHelper sqLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_event);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("事件");
        init();

        txtTag1.setOnClickListener(this);
        txtTag2.setOnClickListener(this);

        DbOpenHelper sqLiteHelper = new DbOpenHelper(this);
         Cursor cursor = sqLiteHelper.getData2("SELECT * FROM Calender ");

        while (cursor.moveToNext()) {

            String title = cursor.getString(1);
            String tag1 = cursor.getString(2);
            String tag2 = cursor.getString(3);
            String time = cursor.getString(4);
            String notice = cursor.getString(5);

            txtTitle.setText(title);
            txtTag1.setText(tag1);
            txtTag2.setText(tag2);
            txtTime.setText(time);
            txtNotice.setText(notice);

        }
    }
    public void init(){
        Toast.makeText(this,"測試",Toast.LENGTH_SHORT).show();
        txtTitle=(TextView)findViewById(R.id.txtTitle);
        txtTag1=(TextView)findViewById(R.id.txtTag1);
        txtTag2=(TextView)findViewById(R.id.txtTag2);
        txtTime=(TextView)findViewById(R.id.txtTime);
        txtNotice=(TextView)findViewById(R.id.txtNotice);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtTag1:
            Intent intent = new Intent(insert_event.this, FindTagFragment.class);
            String CText = txtTag1.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("detectedText", CText);

            //將Bundle物件assign給intent
            intent.putExtras(bundle);
            startActivity(intent);
            break;
            case R.id.txtTag2:
                Intent intent2 = new Intent(insert_event.this, FindTagFragment.class);
                String CText2 = txtTag2.getText().toString();
                Bundle bundle2 = new Bundle();
                bundle2.putString("detectedText", CText2);

                //將Bundle物件assign給intent
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
        }
    }
}
