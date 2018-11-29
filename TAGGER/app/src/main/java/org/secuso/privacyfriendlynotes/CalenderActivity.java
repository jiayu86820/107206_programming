package org.secuso.privacyfriendlynotes;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.secuso.privacyfriendlynotes.code_old.DbAccess;
import org.secuso.privacyfriendlynotes.code_old.DbContract;
import org.secuso.privacyfriendlynotes.code_old.ManageCategoriesActivity;
import org.secuso.privacyfriendlynotes.code_old.NotificationService;
import org.secuso.privacyfriendlynotes.code_old.Preferences;
import org.secuso.privacyfriendlynotes.code_old.SettingsActivity;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalenderActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, PopupMenu.OnMenuItemClickListener {
    public static final String EXTRA_ID = "org.secuso.privacyfriendlynotes.ID";

    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 1;
    int y,m,d,h,m2;
    EditText etName;
    EditText etContent;
    EditText etTag;
    EditText etTag1,etTag2,etTag3,etNotice;
    Spinner spinner;
    Button btnSettime,btn_search1,btn_search2,btn_search3;

    private ShareActionProvider mShareActionProvider = null;

    private int dayOfMonth, monthOfYear, year;
    Calendar date;
    private boolean edit = false;
    private boolean hasAlarm = false;
    private boolean shouldSave = true;
    private int id = -1;
    private int notification_id = -1;
    private int currentCat;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public static final String BROADCAST_ACTION = "net.macdidi.broadcast01.action.MYBROADCAST02";
    Cursor noteCursor = null;
    Cursor notificationCursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        final android.support.v7.app.ActionBar bar_c =getSupportActionBar();
        bar_c.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#07567F")));
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);

        init();



        loadActivity(true);

    }

    private void loadActivity(boolean initial){
        //Look for a note ID in the intent. If we got one, then we will edit that note. Otherwise we create a new one.
        if (id == -1) {
            Intent intent = getIntent();
            id = intent.getIntExtra(EXTRA_ID, -1);
        }
        edit = (id != -1);

        SimpleCursorAdapter adapter = null;
        // Should we set a custom font size?
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(SettingsActivity.PREF_CUSTOM_FONT, false)) {
            etContent.setTextSize(Float.parseFloat(sp.getString(SettingsActivity.PREF_CUTSOM_FONT_SIZE, "15")));
            etName.setTextSize(Float.parseFloat(sp.getString(SettingsActivity.PREF_CUTSOM_FONT_SIZE, "15")));
        }

        //CategorySpinner
        Cursor c = DbAccess.getCategories(getBaseContext());
        if (c.getCount() == 0) {
            displayCategoryDialog();
        } else {


        }
        //fill in values if update
        if (edit) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            noteCursor = DbAccess.getNote(getBaseContext(), id);
            noteCursor.moveToFirst();
            etName.setText(noteCursor.getString(noteCursor.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_NAME)));
            etContent.setText(noteCursor.getString(noteCursor.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_CONTENT)));
            etTag1.setText(noteCursor.getString(noteCursor.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_TAG1)));
            etTag2.setText(noteCursor.getString(noteCursor.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_TAG2)));
            etTag3.setText(noteCursor.getString(noteCursor.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_TAG3)));
            etNotice.setText(noteCursor.getString(noteCursor.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_NOTICE)));
            //find the current category and set spinner to that

            //fill the notificationCursor
            notificationCursor = DbAccess.getNotificationByNoteId(getBaseContext(), id);
            hasAlarm = notificationCursor.moveToFirst();
            if (hasAlarm) {
                notification_id = notificationCursor.getInt(notificationCursor.getColumnIndexOrThrow(DbContract.NotificationEntry.COLUMN_ID));
            }
            findViewById(R.id.btn_delete).setEnabled(true);
            ((Button) findViewById(R.id.btn_save)).setText(getString(R.string.action_update));
        } else {
            findViewById(R.id.btn_delete).setEnabled(false);
        }
        if(!initial) {
            invalidateOptionsMenu();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //The Activity is not visible anymore. Save the work!
        if (shouldSave) {
            if (edit) {
                updateNote();
            } else {
                saveNote();
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadActivity(false);
    }

    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                Toast.makeText(getBaseContext(), R.string.toast_canceled, Toast.LENGTH_SHORT).show();
                shouldSave = false;
                finish();
                break;
            case R.id.btn_delete:
                if (edit) { //note only exists in edit mode
                    displayTrashDialog();
                }
                break;
            case R.id.btn_save:
                shouldSave = true; //safe on exit
                finish();
                break;
            case R.id.btnSettime:
                shouldSave = false; //safe on exit
                showDateTimePicker();
                break;
            case R.id.btn_search1:
                Intent intent = new Intent(CalenderActivity.this, FindTagFragment.class);
                String CText = etTag3.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("detectedText", CText);

                //將Bundle物件assign給intent
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_search2:
                Intent intent2 = new Intent(CalenderActivity.this, FindTagFragment.class);
                String CText2 = etTag1.getText().toString();
                Bundle bundle2 = new Bundle();
                bundle2.putString("detectedText", CText2);

                //將Bundle物件assign給intent
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
            case R.id.btn_search3:
                Intent intent3 = new Intent(CalenderActivity.this, FindTagFragment.class);
                String CText3 = etTag2.getText().toString();
                Bundle bundle3 = new Bundle();
                bundle3.putString("detectedText", CText3);

                //將Bundle物件assign給intent
                intent3.putExtras(bundle3);
                startActivity(intent3);
                break;
            default:
        }
    }
    public void showDateTimePicker() {

        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();

        new DatePickerDialog(CalenderActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(CalenderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Intent intent3 = new Intent(BROADCAST_ACTION);
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        etNotice.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日"+hourOfDay+"點"+minute+"分");
                        y=year;
                        m=monthOfYear;
                        d=dayOfMonth;
                        h=hourOfDay;
                        m2=minute;
                        String noticeName =etName.getText().toString();
                        String noticeContent =etContent.getText().toString();

                        intent3.putExtra("InfoName",noticeName);
                        intent3.putExtra("InfoContent",noticeContent);


                        //將Bundle物件assign給intent


                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication().getApplicationContext(),
                                0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);//宣告 取得 PendingIntent
                        AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);//宣告 取得AlarmManager

                        Calendar calendar = Calendar.getInstance();         //宣告 Calendar 並命名calendar
                        calendar.setTimeInMillis(System.currentTimeMillis());//取得現在時間
                        calendar.set(calendar.YEAR,year);
                        calendar.set(calendar.MONTH,monthOfYear);
                        calendar.set(calendar.DATE,dayOfMonth);
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);//取得小時格式
                        calendar.set(Calendar.MINUTE, minute);
                        //取得分鐘格式

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                0, pendingIntent);//設定定時鬧鐘以RTC_WAKEUP方式呈現
                        Toast.makeText(getApplicationContext(), year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日"+hourOfDay+"點"+minute+"分" + "將推播訊息給您",
                                Toast.LENGTH_LONG).show();//顯示出訊息Toast


                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();

    }

    private void updateNote(){
        fillNameIfEmpty();
        DbAccess.updateNote3(getBaseContext(), id, etName.getText().toString(), etContent.getText().toString(),etTag1.getText().toString(),etTag2.getText().toString(),
                etTag3.getText().toString(),etNotice.getText().toString());
        Toast.makeText(getApplicationContext(), R.string.toast_updated, Toast.LENGTH_SHORT).show();
    }

    private void saveNote(){
        Intent intent44 = new Intent(Intent.ACTION_EDIT);
        intent44.setType("vnd.android.cursor.item/event");

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(y, m, d, h, m2);
        Calendar endTime = Calendar.getInstance();
        endTime.set(y, m, d, h, m2);


        intent44.putExtra(CalendarContract.Events.TITLE, etName.getText().toString());
        intent44.putExtra(CalendarContract.Events.DESCRIPTION, etContent.getText().toString());
        intent44.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        intent44.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,  endTime.getTimeInMillis());
        intent44.putExtra(CalendarContract.Events.ALL_DAY,false);


//送出意圖
        startActivity(intent44);



        fillNameIfEmpty();
        id = DbAccess.addNote3(getBaseContext(), etName.getText().toString(),etContent.getText().toString(), etTag1.getText().toString(), etTag2.getText().toString(),
                etTag3.getText().toString(), etNotice.getText().toString(), DbContract.NoteEntry.TYPE_CALENDER);
        Toast.makeText(getApplicationContext(), R.string.toast_saved, Toast.LENGTH_SHORT).show();
    }

    private void fillNameIfEmpty(){
        if (etName.getText().toString().isEmpty()) {
            SharedPreferences sp = getSharedPreferences(Preferences.SP_VALUES, Context.MODE_PRIVATE);
            int counter = sp.getInt(Preferences.SP_VALUES_NAMECOUNTER, 1);
            etName.setText(String.format(getString(R.string.note_standardname), counter));
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(Preferences.SP_VALUES_NAMECOUNTER, counter+1);
            editor.commit();
        }
    }

    private void displayCategoryDialog() {
        new AlertDialog.Builder(CalenderActivity.this)
                .setTitle(getString(R.string.dialog_need_category_title))
                .setMessage(getString(R.string.dialog_need_category_message))
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(CalenderActivity.this, ManageCategoriesActivity.class));
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void displayTrashDialog() {
        SharedPreferences sp = getSharedPreferences(Preferences.SP_DATA, Context.MODE_PRIVATE);
        if (sp.getBoolean(Preferences.SP_DATA_DISPLAY_TRASH_MESSAGE, true)){
            //we never displayed the message before, so show it now
            new AlertDialog.Builder(CalenderActivity.this)
                    .setTitle(getString(R.string.dialog_trash_title))
                    .setMessage(getString(R.string.dialog_trash_message))
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            shouldSave = false;
                            DbAccess.trashNote(getBaseContext(), id);
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(Preferences.SP_DATA_DISPLAY_TRASH_MESSAGE, false);
            editor.commit();
        } else {
            shouldSave = false;
            DbAccess.trashNote(getBaseContext(), id);
            finish();
        }

    }

    private void displayDeleteDialog() {
        new AlertDialog.Builder(CalenderActivity.this)
                .setTitle(String.format(getString(R.string.dialog_delete_title), etName.getText().toString()))
                .setMessage(String.format(getString(R.string.dialog_delete_message), etName.getText().toString()))
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shouldSave = false;
                        DbAccess.trashNote(getBaseContext(), id);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        this.monthOfYear = monthOfYear;
        this.year = year;
        final Calendar c = Calendar.getInstance();
        if (hasAlarm) {
            c.setTimeInMillis(notificationCursor.getLong(notificationCursor.getColumnIndexOrThrow(DbContract.NotificationEntry.COLUMN_TIME)));
        }
        TimePickerDialog tpd = new TimePickerDialog(CalenderActivity.this, this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        tpd.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar alarmtime = Calendar.getInstance();
        alarmtime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute);

        if (hasAlarm) {
            //Update the current alarm
            DbAccess.updateNotificationTime(getBaseContext(), notification_id, alarmtime.getTimeInMillis());
        } else {
            //create new alarm
            notification_id = (int) (long) DbAccess.addNotification(getBaseContext(), id, alarmtime.getTimeInMillis());
        }
        //Store a reference for the notification in the database. This is later used by the service.

        //Create the intent that is fired by AlarmManager
        Intent i = new Intent(this, NotificationService.class);
        i.putExtra(NotificationService.NOTIFICATION_ID, notification_id);

        PendingIntent pi = PendingIntent.getService(this, notification_id, i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmtime.getTimeInMillis(), pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmtime.getTimeInMillis(), pi);
        }
        Toast.makeText(getApplicationContext(), String.format(getString(R.string.toast_alarm_scheduled), dayOfMonth + "." + (monthOfYear+1) + "." + year + " " + hourOfDay + ":" + String.format("%02d",minute)), Toast.LENGTH_SHORT).show();
        loadActivity(false);
    }

    private void cancelNotification(){
        //Create the intent that would be fired by AlarmManager
        Intent i = new Intent(this, NotificationService.class);
        i.putExtra(NotificationService.NOTIFICATION_ID, notification_id);

        PendingIntent pi = PendingIntent.getService(this, notification_id, i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pi);
        DbAccess.deleteNotification(getBaseContext(), notification_id);
        loadActivity(false);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reminder_edit) {
            final Calendar c = Calendar.getInstance();
            c.setTimeInMillis(notificationCursor.getLong(notificationCursor.getColumnIndexOrThrow(DbContract.NotificationEntry.COLUMN_TIME)));
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(CalenderActivity.this, this, year, month, day);
            dpd.getDatePicker().setMinDate(new Date().getTime());
            dpd.show();
            return true;
        } else if (id == R.id.action_reminder_delete) {
            cancelNotification();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Save the file
                    saveToExternalStorage();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.toast_need_permission_write_external, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void saveToExternalStorage(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File path;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                path = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "/TAGGER_Backup");
            } else{
                path = new File(Environment.getExternalStorageDirectory(), "/TAGGER_Backup");
            }

            File file = new File(path, "/text_" + etName.getText().toString() + ".txt");
            try {
                // Make sure the directory exists.
                boolean path_exists = path.exists() || path.mkdirs();
                if (path_exists) {

                    PrintWriter out = new PrintWriter(file);
                    out.println(etName.getText().toString());
                    out.println();
                    out.println(etContent.getText().toString());
                    out.close();
                    // Tell the media scanner about the new file so that it is
                    // immediately available to the user.
                    MediaScannerConnection.scanFile(this,
                            new String[] { file.toString() }, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                }
                            });

                    Toast.makeText(getApplicationContext(), String.format(getString(R.string.toast_file_exported_to), file.getAbsolutePath()), Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                // Unable to create file, likely because external storage is
                // not currently mounted.
                Log.w("ExternalStorage", "Error writing " + file, e);
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.toast_external_storage_not_mounted, Toast.LENGTH_LONG).show();
        }
    }

    private void setShareIntent(){
        if (mShareActionProvider != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, etName.getText().toString() + "\n\n" + etContent.getText().toString());
            mShareActionProvider.setShareIntent(sendIntent);
        }
    }
    // Showing google speech input dialog

    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hi speak something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    // Receiving speech input

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etTag.setText(result.get(0));
                }
                break;
            }

        }
    }
    public void init(){
        etTag1=(EditText)findViewById(R.id.etTag1);
        etTag2=(EditText)findViewById(R.id.etTag2);
        etTag3=(EditText)findViewById(R.id.etTag3);
        etNotice=(EditText)findViewById(R.id.etNotice);

        btnSettime=(Button)findViewById(R.id.btnSettime);
        btn_search1=(Button)findViewById(R.id.btn_search1);
        btn_search2=(Button)findViewById(R.id.btn_search2);
        btn_search3=(Button)findViewById(R.id.btn_search3);

        btnSettime.setOnClickListener(this);
        btn_search1.setOnClickListener(this);
        btn_search2.setOnClickListener(this);
        btn_search3.setOnClickListener(this);

        etTag1.setOnClickListener(this);
        etTag2.setOnClickListener(this);
        etTag=(EditText)findViewById(R.id.etTag);
        etName = (EditText) findViewById(R.id.etName);
        etContent = (EditText) findViewById(R.id.etContent);
    }
}
