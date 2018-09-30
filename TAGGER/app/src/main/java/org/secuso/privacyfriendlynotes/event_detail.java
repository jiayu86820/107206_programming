package org.secuso.privacyfriendlynotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.secuso.privacyfriendlynotes.code_old.DbOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class event_detail extends AppCompatActivity implements View.OnClickListener {
    TextView txtDate;
    EditText edtTitle, edtTag1, edtTag2, edtTime, edtNotice;
    Button btnGo, btnSet;
    public static final String BROADCAST_ACTION = "net.macdidi.broadcast01.action.MYBROADCAST02";
    int flag = 1;
    public static DbOpenHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("新增事件");

        init();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        String str = formatter.format(curDate);

        sqLiteHelper = new DbOpenHelper(this);
        sqLiteHelper.queryData2("CREATE TABLE IF NOT EXISTS Calender(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title VARCHAR, tag1 VARCHAR, tag2 VARCHAR, time VARCHAR, notice VARCHAR)");

        btnGo.setOnClickListener(this);
        btnSet.setOnClickListener(this);
        txtDate.setText(str);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGo:
                sqLiteHelper.insertData2(
                        edtTitle.getText().toString(),
                        edtTag1.getText().toString(),
                        edtTag2.getText().toString(),
                        edtTime.getText().toString(),
                        edtNotice.getText().toString()
                );
                edtTitle.setText("");
                edtTag1.setText("");
                edtTag2.setText("");
                edtTime.setText("");
                edtNotice.setText("");

                startActivity(new Intent(getApplication(), insert_event.class));

                break;

            case R.id.button3:
                Intent intent = new Intent();
                intent.setAction(BROADCAST_ACTION);
                                            //如果flag等於1
                   // intent.putExtra("flag", 1);              //傳送名為flag的數值1
                    Calendar c = Calendar.getInstance();       //宣告Calendar,並命名c
                    // flag = 0;                                //設定flag等於0
                    new TimePickerDialog(event_detail.this, t, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();  //產生一個TimePickerDialog

                    break;

        }
    }

        TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
            //建立TimePickerDialog監聽,並命名t
            Intent intent3 = new Intent(BROADCAST_ACTION);                             //宣告Intent,並命名intent

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {           //可在此底下做你想做的事

                edtNotice.setText( hourOfDay + ":" + minute );   //設定txtTime顯示出內容
                Toast.makeText(getApplicationContext(), hourOfDay + "點" + minute + "分" + "將推播訊息給您",
                        Toast.LENGTH_LONG).show();//顯示出訊息Toast
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication().getApplicationContext(),
                        0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);//宣告 取得 PendingIntent
                AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);//宣告 取得AlarmManager

                Calendar calendar = Calendar.getInstance();         //宣告 Calendar 並命名calendar
                calendar.setTimeInMillis(System.currentTimeMillis());//取得現在時間
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);//取得小時格式
                calendar.set(Calendar.MINUTE, minute);          //取得分鐘格式

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        0, pendingIntent);//設定定時鬧鐘以RTC_WAKEUP方式呈現

            }

        };

    public void init () {
        edtTitle = (EditText) findViewById(R.id.editTitle);
        edtTag1 = (EditText) findViewById(R.id.editTag1);
        edtTag2 = (EditText) findViewById(R.id.editTag2);
        edtTime = (EditText) findViewById(R.id.editTime);
        edtNotice = (EditText) findViewById(R.id.editNotice);
        txtDate = (TextView) findViewById(R.id.t_date);
        btnGo = (Button) findViewById(R.id.btnGo);
        btnSet = (Button) findViewById(R.id.button3);
    }
}