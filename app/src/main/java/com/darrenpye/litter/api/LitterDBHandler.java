package com.darrenpye.litter.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by darrenpye on 16-02-07.
 *
 * This class shouldn't really exist in this system, although it could for a local storage.
 * I'm using it to simulate the remote "Litter" service which would house the data and calls
 * in the cloud. This SQLite database is simple but sufficient to represent the basics of the
 * Litter service.
 *
 */
public class LitterDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "litterDB.db";

    // Tables

    // Users
    private static final String TABLE_USERS = "users";
    public static final String USERS_ID = "user_id";
    public static final String USERS_USERNAME = "username";
    public static final String USERS_EMAIL = "email";
    public static final String USERS_PASSWORD = "password";
    public static final String USERS_FIRST_NAME = "firs_tname";
    public static final String USERS_LAST_NAME = "last_name";
    public static final String USERS_REMEMBER_ME = "remember_me";
    public static final String USERS_IMAGE_ID = "image_id";
    public static final String USERS_CREATED_ON = "created_on";

    // Litters
    private static final String TABLE_LITTERS = "litters";
    private static final String LITTERS_ID = "litter_id";
    private static final String LITTERS_USER_ID = "user_id";
    private static final String LITTERS_TIMESTAMP = "timestamp";
    private static final String LITTERS_MESSAGE = "message";
    private static final String LITTERS_IMAGE_ID = "image_id";
    private static final String LITTERS_RELITTERS = "relitters";
    private static final String LITTERS_LIKES = "likes";


    public LitterDBHandler(Context context) {
        this(context, null, null, 1);
    }

    public LitterDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the users table
        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "(" +
                USERS_ID + " INTEGER PRIMARY KEY," +
                USERS_USERNAME + " VARCHAR(25)," +
                USERS_EMAIL + "  VARCHAR(255)," +
                USERS_PASSWORD + "  VARCHAR(255)," +
                USERS_FIRST_NAME + "  VARCHAR(25)," +
                USERS_LAST_NAME + "  VARCHAR(25)," +
                USERS_REMEMBER_ME + "  BOOLEAN," +
                USERS_IMAGE_ID + " INTEGER," +
                USERS_CREATED_ON + " INTEGER" +");";
        db.execSQL(CREATE_USERS_TABLE);

        // Create the litters table
        String CREATE_LITTERS_TABLE = "CREATE TABLE " +
                TABLE_LITTERS + "(" +
                LITTERS_ID + " INTEGER PRIMARY KEY," +
                LITTERS_USER_ID + " INTEGER," +
                LITTERS_TIMESTAMP + " INTEGER," +
                LITTERS_MESSAGE + "  VARCHAR(255)," +
                LITTERS_IMAGE_ID + "  INTEGER," +
                LITTERS_RELITTERS + "  INTEGER," +
                LITTERS_LIKES + "  INTEGER" + ");";
        db.execSQL(CREATE_LITTERS_TABLE);


        // SAMPLE SHORTCUT: Because this sample isn't going to include an add user function, add one
        ContentValues values = new ContentValues();
        values.put(USERS_USERNAME, "EmoKylo");
        values.put(USERS_EMAIL, "kyloren@emo-sith.com");
        values.put(USERS_PASSWORD, "password");
        values.put(USERS_FIRST_NAME, "Emo Kylo");
        values.put(USERS_LAST_NAME, "Ren");
        values.put(USERS_REMEMBER_ME, true);
        values.put(USERS_IMAGE_ID, 1);
        values.put(USERS_CREATED_ON, (new java.util.Date()).getTime());

        long userId = db.insert(TABLE_USERS, null, values);

        // SAMPLE SHORTCUT: Add in some Litter as well

        long nextid;

        values = new ContentValues();
        values.put(LITTERS_USER_ID, userId);
        values.put(LITTERS_MESSAGE, "I hate my life. That is all.");
        values.put(LITTERS_TIMESTAMP, (new java.util.Date()).getTime());
        values.put(LITTERS_IMAGE_ID, 1);
        values.put(LITTERS_RELITTERS, 0);
        values.put(LITTERS_LIKES, 0);
        nextid = db.insert(TABLE_LITTERS, null, values);


        values = new ContentValues();
        values.put(LITTERS_USER_ID, userId);
        values.put(LITTERS_MESSAGE, "I was weak, but now I'm strong! Really! I am! Well.. kinda. Well... Not really... I suck.");
        values.put(LITTERS_TIMESTAMP, (new java.util.Date()).getTime());
        values.put(LITTERS_IMAGE_ID, 1);
        values.put(LITTERS_RELITTERS, 0);
        values.put(LITTERS_LIKES, 0);
        nextid = db.insert(TABLE_LITTERS, null, values);

        values = new ContentValues();
        values.put(LITTERS_USER_ID, userId);
        values.put(LITTERS_MESSAGE, "Dad was kind of a douche. He never really loved me. I want cheese sticks.");
        values.put(LITTERS_TIMESTAMP, (new java.util.Date()).getTime());
        values.put(LITTERS_IMAGE_ID, 1);
        values.put(LITTERS_RELITTERS, 0);
        values.put(LITTERS_LIKES, 0);
        nextid = db.insert(TABLE_LITTERS, null, values);

        values = new ContentValues();
        values.put(LITTERS_USER_ID, userId);
        values.put(LITTERS_MESSAGE, "I have a red light saber so everyone knows I'm a bad ass. Otherwise they'd try to tease me or something.");
        values.put(LITTERS_TIMESTAMP, (new java.util.Date()).getTime());
        values.put(LITTERS_IMAGE_ID, 1);
        values.put(LITTERS_RELITTERS, 0);
        values.put(LITTERS_LIKES, 0);
        nextid = db.insert(TABLE_LITTERS, null, values);

        values = new ContentValues();
        values.put(LITTERS_USER_ID, userId);
        values.put(LITTERS_MESSAGE, "I may have emotional problems, but i have GREAT hair!");
        values.put(LITTERS_TIMESTAMP, (new java.util.Date()).getTime());
        values.put(LITTERS_IMAGE_ID, 1);
        values.put(LITTERS_RELITTERS, 0);
        values.put(LITTERS_LIKES, 0);
        nextid = db.insert(TABLE_LITTERS, null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LITTERS);

        // Drop other tables here as well (imags, etc)
        // NOTE: If data can be quickly syncrhonized with the Glitter servers, this is fine,
        // otherwise a migration strategy should be used.

        onCreate(db);
    }


    // Database functions

    /** Add a new litter record
     *
     * @param litter a populated Litter object
     * @return true if successful
     */
    public boolean addLitter(Litter litter) {

        ContentValues values = new ContentValues();

        // We won't be assiging a value to litter_id to force the DB to autoincrement, we'll extract it after
        // and assign it back into the object on behalf of the caller

        values.put(LITTERS_USER_ID, litter.getUserid());
        values.put(LITTERS_TIMESTAMP, litter.getTimestamp());
        values.put(LITTERS_MESSAGE, litter.getMessage());
        values.put(LITTERS_IMAGE_ID, litter.getImageId());
        values.put(LITTERS_RELITTERS, litter.getRelitters());
        values.put(LITTERS_LIKES, litter.getLikes());


        SQLiteDatabase db = this.getWritableDatabase();

        long litterId = db.insert(TABLE_LITTERS, null, values);

        if (litterId != -1) {
            // Set the id back into the litter object
            litter.setLitterId(litterId);
        }

        db.close();

        return litterId != -1 ? true : false; // id of -1 means a DB error occurred on insert
    }


    /** Get the litters for a user
     *
     * @param userId the userId of the user
     * @return an Array list of Litter objects or null if none found
     */
    public ArrayList<Litter> getLittersForUser(long userId) {

        ArrayList<Litter> results = null;

        String query = "SELECT * FROM " + TABLE_LITTERS +
                " WHERE " + LITTERS_USER_ID + " = "+ userId +
                " ORDER BY "+ LITTERS_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            results = new ArrayList<Litter>();

            do {
                Litter litter = new Litter();

                litter.setLitterId(cursor.getLong(0));
                litter.setUserid(cursor.getLong(1));
                litter.setTimestamp(cursor.getLong(2));
                litter.setMessage(cursor.getString(3));
                litter.setImageId(cursor.getLong(4));
                litter.setRelitters(cursor.getLong(5));
                litter.setLikes(cursor.getLong(6));

                // Add it in
                results.add(litter);

            } while (cursor.moveToNext()); // Continue while more records

            cursor.close();
        }

        db.close();

        return results;
    }


    /** Find a user by their username or email and password
     * The serach will use either the username or email depending
     * on which is supplied
     *
     * @param usernameOrEmail The username or Email
     * @param password The password
     * @return The user if found, otherwise null
     */
    public User findUser(String usernameOrEmail, String password) {

        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " +
                "("+USERS_USERNAME + " =  \"" + usernameOrEmail + "\" OR " +
                USERS_EMAIL + " = \""+ usernameOrEmail + "\") AND " +
                USERS_PASSWORD + " =  \"" + password + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user = null;

        if (cursor.moveToFirst()) {
            user = new User();

            user.setUserId(cursor.getLong(0));
            user.setUsername(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPassword(cursor.getString(3));
            user.setFirstName(cursor.getString(4));
            user.setLastName(cursor.getString(5));
            user.setRememberMe(cursor.getInt(6) > 0);
            user.setImageId(cursor.getLong(7));
            user.setCreatedOn(cursor.getLong(8));

            cursor.close();
        }

        db.close();

        return user;
    }
}
