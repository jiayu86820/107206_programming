package org.secuso.privacyfriendlynotes.code_old;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityOptions;

import org.secuso.privacyfriendlynotes.CalenderActivity;
import org.secuso.privacyfriendlynotes.KeepNoteBridge;
import org.secuso.privacyfriendlynotes.KeepNoteBridgeTag;
import org.secuso.privacyfriendlynotes.PhotoEditor;
import org.secuso.privacyfriendlynotes.PhotoNoteActivity;
import org.secuso.privacyfriendlynotes.PlatformFragment;
import org.secuso.privacyfriendlynotes.R;
import org.secuso.privacyfriendlynotes.calender_Show;
import org.secuso.privacyfriendlynotes.fragments.WelcomeDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int CAT_ALL = -1;
    private static final String TAG_WELCOME_DIALOG = "welcome_dialog";
    FloatingActionsMenu fabMenu;

    private int selectedCategory = CAT_ALL; //ID of the currently selected category. Defaults to "all"

    private static final String TAG = "Google Drive Activity";

    public static final int REQUEST_CODE_SIGN_IN = 0;
    public static final int REQUEST_CODE_OPENING = 1;
    public static final int REQUEST_CODE_CREATION = 2;
    public static final int REQUEST_CODE_PERMISSIONS = 2;
    private boolean isBackup = true;
    private MainActivity activity;
    private RemoteBackup remoteBackup;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        remoteBackup = new RemoteBackup(this);
        //set the OnClickListeners
        //set the OnClickListeners
        findViewById(R.id.fab_text).setOnClickListener(this);
//        findViewById(R.id.fab_checklist).setOnClickListener(this);
        //    findViewById(R.id.fab_audio).setOnClickListener(this);
        //  findViewById(R.id.fab_sketch).setOnClickListener(this);
        findViewById(R.id.fab_find).setOnClickListener(this);
        findViewById(R.id.fab_cal).setOnClickListener(this);
        findViewById(R.id.fab_platform).setOnClickListener(this);
        findViewById(R.id.fab_editImage).setOnClickListener(this);

        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Fill the list from database
        ListView notesList = (ListView) findViewById(R.id.notes_list);
        notesList.setAdapter(new CursorAdapter(getApplicationContext(), DbAccess.getCursorAllNotes(getBaseContext()), CursorAdapter.FLAG_AUTO_REQUERY) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.item_note, null);

                TextView text = (TextView) rowView.findViewById(R.id.item_name);
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_NAME));
                if (name.length() >= 30) {
                    text.setText(name.substring(0,27) + "...");
                } else {
                    text.setText(name);
                }

                ImageView iv = (ImageView) rowView.findViewById(R.id.item_icon);

                switch (cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_TYPE))) {
                    case DbContract.NoteEntry.TYPE_SKETCH:
                        iv.setImageResource(R.drawable.ic_photo_black_24dp);
                        break;
                    case DbContract.NoteEntry.TYPE_AUDIO:
                        iv.setImageResource(R.drawable.ic_mic_black_24dp);
                        break;
                    case DbContract.NoteEntry.TYPE_TEXT:
                        iv.setImageResource(R.drawable.ic_short_text_black_24dp);
                        break;
                    case DbContract.NoteEntry.TYPE_CHECKLIST:
                        iv.setImageResource(R.drawable.ic_format_list_bulleted_black_24dp);
                        break;
                    case DbContract.NoteEntry.TYPE_PHOTO:
                        iv.setImageResource(R.drawable.ic_photo_library_black_24dp);
                        break;
                    default:
                }
                return rowView;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView text = (TextView) view.findViewById(R.id.item_name);
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_NAME));
                if (name.length() >= 30) {
                    text.setText(name.substring(0,27) + "...");
                } else {
                    text.setText(name);
                }

                ImageView iv = (ImageView) view.findViewById(R.id.item_icon);
                switch (cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_TYPE))) {
                    case DbContract.NoteEntry.TYPE_SKETCH:
                        iv.setImageResource(R.drawable.ic_photo_black_24dp);
                        break;
                    case DbContract.NoteEntry.TYPE_AUDIO:
                        iv.setImageResource(R.drawable.ic_mic_black_24dp);
                        break;
                    case DbContract.NoteEntry.TYPE_TEXT:
                        iv.setImageResource(R.drawable.ic_short_text_black_24dp);
                        break;
                    case DbContract.NoteEntry.TYPE_CHECKLIST:
                        iv.setImageResource(R.drawable.ic_format_list_bulleted_black_24dp);
                        break;
                    default:
                }
            }
        });
        notesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get details about the clicked note
                CursorAdapter ca = (CursorAdapter) parent.getAdapter();
                Cursor c = ca.getCursor();
                c.moveToPosition(position);
                //start the appropriate activity
                switch (c.getInt(c.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_TYPE))) {
                    case DbContract.NoteEntry.TYPE_TEXT:
                        Intent i = new Intent(getApplication(), TextNoteActivity.class);
                        i.putExtra(TextNoteActivity.EXTRA_ID, c.getInt(c.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_ID)));
                        startActivity(i);
                        break;
                    case DbContract.NoteEntry.TYPE_PHOTO:
                        Intent i5 = new Intent(getApplication(), PhotoNoteActivity.class);
                        i5.putExtra(PhotoNoteActivity.EXTRA_ID, c.getInt(c.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_ID)));
                        startActivity(i5);
                        break;
                    case DbContract.NoteEntry.TYPE_AUDIO:
                        Intent i2 = new Intent(getApplication(), AudioNoteActivity.class);
                        i2.putExtra(AudioNoteActivity.EXTRA_ID, c.getInt(c.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_ID)));
                        startActivity(i2);
                        break;
                    case DbContract.NoteEntry.TYPE_SKETCH:
                        Intent i3 = new Intent(getApplication(), SketchActivity.class);
                        i3.putExtra(SketchActivity.EXTRA_ID, c.getInt(c.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_ID)));
                        startActivity(i3);
                        break;
                    case DbContract.NoteEntry.TYPE_CHECKLIST:
                        Intent i4 = new Intent(getApplication(), ChecklistNoteActivity.class);
                        i4.putExtra(ChecklistNoteActivity.EXTRA_ID, c.getInt(c.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_ID)));
                        startActivity(i4);
                        break;
                    case DbContract.NoteEntry.TYPE_CALENDER:
                        Intent i6 = new Intent(getApplication(), CalenderActivity.class);
                        i6.putExtra(CalenderActivity.EXTRA_ID, c.getInt(c.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_ID)));
                        startActivity(i6);
                        break;
                }
            }
        });
        notesList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                //do nothing
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.main_cab, menu);
                //Temporary fix, otherwise statusbar would be black
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    // or Color.TRANSPARENT or your preferred color
                }
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        deleteSelectedItems();
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                //Temporary fix, otherwise statusbar would be black
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    getWindow().setStatusBarColor(Color.TRANSPARENT);
                    // or Color.TRANSPARENT or your preferred color
                }
                updateList();
            }
        });


        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);
        SharedPreferences sp = getSharedPreferences(Preferences.SP_DATA, Context.MODE_PRIVATE);
        if (sp.getBoolean(Preferences.SP_DATA_DISPLAY_WELCOME_DIALOG, true)) {
            WelcomeDialog welcomeDialog = new WelcomeDialog();
            welcomeDialog.show(getFragmentManager(), TAG_WELCOME_DIALOG);
            SharedPreferences.Editor editor = sp.edit();
            //editor.putBoolean(Preferences.SP_DATA_DISPLAY_WELCOME_DIALOG, false);
            editor.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
        buildDrawerMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_alphabetical) {
            //switch to an alphabetically sorted cursor.
            updateListAlphabetical();
            return true;
        } else if (id == R.id.action_help) {
            startActivity(new Intent(getApplication(), HelpActivity.class));
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(getApplication(), SettingsActivity.class));
        } else if (id == R.id.action_about) {
            startActivity(new Intent(getApplication(), AboutActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        item.setCheckable(true);
        item.setChecked(true);
        int id = item.getItemId();
        if (id == R.id.nav_trash) {
            startActivity(new Intent(getApplication(), RecycleActivity.class));
        } else if (id == R.id.nav_all) {
            selectedCategory = CAT_ALL;
            updateList();
        } else if (id == R.id.nav_manage_categories) {
            startActivity(new Intent(getApplication(), ManageCategoriesActivity.class));
        } else if (id == R.id.nav_platform ){
            startActivity(new Intent(getApplication(), PlatformFragment.class));
        } else if (id == R.id.nav_backup ){
            isBackup = true;
            remoteBackup.connectToDrive(isBackup);
        } else if (id == R.id.nav_import ){
            isBackup = false;
            remoteBackup.connectToDrive(isBackup);
        } else {
            selectedCategory = id;
            updateList();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_text:
                startActivity(new Intent(getApplication(), KeepNoteBridge.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_checklist:
                startActivity(new Intent(getApplication(), ChecklistNoteActivity.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_audio:
                startActivity(new Intent(getApplication(), AudioNoteActivity.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_sketch:
                startActivity(new Intent(getApplication(), SketchActivity.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_find:
                startActivity(new Intent(getApplication(), KeepNoteBridgeTag.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_cal:
                startActivity(new Intent(getApplication(), calender_Show.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_platform:
                startActivity(new Intent(getApplication(), PlatformFragment.class));
                break;
            case R.id.fab_editImage:
                startActivity(new Intent(getApplication(), PhotoEditor.class));
                fabMenu.collapseImmediately();
                break;
        }
    }

    private void buildDrawerMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu navMenu = navigationView.getMenu();
        //reset the menu
        navMenu.clear();
        //Inflate the standard stuff
        MenuInflater menuInflater = new MenuInflater(getApplicationContext());
        menuInflater.inflate(R.menu.activity_main_drawer, navMenu);
        //Get the rest from the database
        Cursor c = DbAccess.getCategories(getBaseContext());
        while (c.moveToNext()){
            String name = c.getString(c.getColumnIndexOrThrow(DbContract.CategoryEntry.COLUMN_NAME));
            int id = c.getInt(c.getColumnIndexOrThrow(DbContract.CategoryEntry.COLUMN_ID));
            navMenu.add(R.id.drawer_group2, id, Menu.NONE, name).setIcon(R.drawable.ic_label_black_24dp);
        }
        c.close();
    }

    private void updateList() {
        ListView notesList = (ListView) findViewById(R.id.notes_list);
        CursorAdapter adapter = (CursorAdapter) notesList.getAdapter();
        if (selectedCategory == -1) { //show all
            String selection = DbContract.NoteEntry.COLUMN_TRASH + " = ? AND " + DbContract.NoteEntry.COLUMN_TYPE + "!=?";
            String[] selectionArgs = { "0","6" };
            adapter.changeCursor(DbAccess.getCursorAllNotes(getBaseContext(), selection, selectionArgs));
        } else {
            String selection = DbContract.NoteEntry.COLUMN_CATEGORY + " = ? AND " + DbContract.NoteEntry.COLUMN_TRASH + " = ?";
            String[] selectionArgs = { String.valueOf(selectedCategory), "0" };
            adapter.changeCursor(DbAccess.getCursorAllNotes(getBaseContext(), selection, selectionArgs));
        }
    }

    private void updateListAlphabetical() {
        ListView notesList = (ListView) findViewById(R.id.notes_list);
        CursorAdapter adapter = (CursorAdapter) notesList.getAdapter();
        if (selectedCategory == -1) { //show all
            String selection = DbContract.NoteEntry.COLUMN_TRASH + " = ? AND " + DbContract.NoteEntry.COLUMN_TYPE + "!=?";
            String[] selectionArgs = { "0","6" };
            adapter.changeCursor(DbAccess.getCursorAllNotesAlphabetical(getBaseContext(), selection, selectionArgs));
        } else {
            String selection = DbContract.NoteEntry.COLUMN_CATEGORY + " = ? AND " + DbContract.NoteEntry.COLUMN_TRASH + " = ?";
            String[] selectionArgs = { String.valueOf(selectedCategory), "0" };
            adapter.changeCursor(DbAccess.getCursorAllNotesAlphabetical(getBaseContext(), selection, selectionArgs));
        }
    }

    private void deleteSelectedItems(){
        ListView notesList = (ListView) findViewById(R.id.notes_list);
        CursorAdapter adapter = (CursorAdapter) notesList.getAdapter();
        SparseBooleanArray checkedItemPositions = notesList.getCheckedItemPositions();
        for (int i=0; i < checkedItemPositions.size(); i++) {
            if(checkedItemPositions.valueAt(i)) {
                DbAccess.trashNote(getBaseContext(), (int) (long) adapter.getItemId(checkedItemPositions.keyAt(i)));
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_SIGN_IN:
                Log.i(TAG, "Sign in request code");
                // Called after user is signed in.
                if (resultCode == RESULT_OK) {
                    remoteBackup.connectToDrive(isBackup);
                }
                break;

            case REQUEST_CODE_CREATION:
                // Called after a file is saved to Drive.
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "Backup successfully saved.");
                    Toast.makeText(this, "Backup successufly loaded!", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CODE_OPENING:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    remoteBackup.mOpenItemTaskSource.setResult(driveId);
                } else {
                    remoteBackup.mOpenItemTaskSource.setException(new RuntimeException("Unable to open file"));
                }

        }


    }

}
