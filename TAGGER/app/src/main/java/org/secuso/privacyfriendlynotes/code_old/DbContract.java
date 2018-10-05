package org.secuso.privacyfriendlynotes.code_old;

import android.provider.BaseColumns;

/**
 * Contract used for accessing the Database
 * Created by Robin on 11.06.2016.
 */
public class DbContract {
    public DbContract(){}

    public static abstract class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_TRASH = "in_trash";
        public static final String COLUMN_TAG = "tag";
        public static final String COLUMN_PHOTO = "photo";
        public static final String COLUMN_TAG1 = "tag1";
        public static final String COLUMN_TAG2 = "tag2";
        public static final String COLUMN_TAG3 = "tag3";
        public static final String COLUMN_NOTICE = "notice";
        public static final int TYPE_TEXT = 1;
        public static final int TYPE_AUDIO = 2;
        public static final int TYPE_CHECKLIST = 3;
        public static final int TYPE_SKETCH = 4;
        public static final int TYPE_PHOTO = 5;
        public static final int TYPE_CALENDER = 6;
    }

    public static abstract class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
    }

    public static abstract class NotificationEntry implements BaseColumns {
        public static final String TABLE_NAME = "notifications";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_TIME = "time";
    }


    public static abstract class SoundEntry implements BaseColumns {
        public static final String TABLE_NAME = "sound";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_TAG = "tag";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_SOUND = "name";
    }

    public static abstract class ImageEntry implements BaseColumns {
        public static final String TABLE_NAME = "image";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_TAG = "tag";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_IMAGE = "name";
    }

    public static abstract class CalendarEntry implements BaseColumns {
        public static final String TABLE_NAME = "Calender";
        public static final String COLUMN_TAG1 = "tag1";
        public static final String COLUMN_TAG2 = "tag2";
        public static final String COLUMN_TAG3 = "tag3";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME1 = "time1";
        public static final String COLUMN_TIME2 = "time2";
        public static final String COLUMN_CALENDAR = "name";
    }


}
