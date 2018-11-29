package org.secuso.privacyfriendlynotes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.secuso.privacyfriendlynotes.code_old.AudioNoteActivity;
import org.secuso.privacyfriendlynotes.code_old.ChecklistNoteActivity;
import org.secuso.privacyfriendlynotes.code_old.DbAccess;
import org.secuso.privacyfriendlynotes.code_old.DbContract;
import org.secuso.privacyfriendlynotes.code_old.SketchActivity;
import org.secuso.privacyfriendlynotes.code_old.TextNoteActivity;

public class FindTagFragment extends AppCompatActivity  {

    FloatingActionsMenu fabMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findtagfragment);


        //傳辨識到的參數

        Bundle bundle = getIntent().getExtras();
        String CText = bundle.getString("detectedText");

        //set the OnClickListeners
        //set the OnClickListeners

        final ActionBar asd =getSupportActionBar();
        asd.setTitle("查詢:\""+CText+"\"的搜尋結果");




        //Fill the list from database
        ListView notesList = (ListView) findViewById(R.id.notes_list);

        notesList.setAdapter(new CursorAdapter(getApplicationContext(), DbAccess.getCursorFindNotes(getBaseContext(),CText), CursorAdapter.FLAG_AUTO_REQUERY) {
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
                    case DbContract.NoteEntry.TYPE_PHOTO:
                        Intent i5 = new Intent(getApplication(), PhotoNoteActivity.class);
                        i5.putExtra(PhotoNoteActivity.EXTRA_ID, c.getInt(c.getColumnIndexOrThrow(DbContract.NoteEntry.COLUMN_ID)));
                        startActivity(i5);
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

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        //buildDrawerMenu();
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

}