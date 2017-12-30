package emp.quezy.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class used for database query's
 */

public class DatabaseConnector extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DB_Score";
    private static final String TABLE_NAME = "Score";
    private static final String C_ID = "_id";
    private static final String C_Date = "Date";
    public static final String C_Category = "Category";
    public static final String C_Difficulty = "Difficulty";
    public static final String C_Score = "Score";
    public static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database;

    public DatabaseConnector(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + C_ID + " INTEGER PRIMARY KEY," + C_Date + " TEXT, " + C_Category + " TEXT, " + C_Difficulty + " TEXT, " + C_Score + " INTEGER);";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    public void close() {
        if (database != null)
            database.close();
    }

    public void clear() {
        open();
        database.execSQL("delete from " + TABLE_NAME);
        close();
    }

    public void insertScore(String date, String category, String difficulty, int score) {
        ContentValues content = new ContentValues();
        content.put(C_Date, date);
        content.put(C_Category, category);
        content.put(C_Difficulty, difficulty);
        content.put(C_Score, score);

        open();
        database.insert(TABLE_NAME, null, content);
        close();
    }

    public Cursor getScoreFromCategory(String category) {
        String[] cols = new String[]{C_Date, C_Category, C_Difficulty, C_Score};
        return database.query(TABLE_NAME, cols, C_Category + "=" + category,
                null, null, null, C_Score + " DESC", null);
    }

    public Cursor getTopGames(int num, String direction, String orderByColumn) {
        String[] cols = new String[]{C_ID, C_Category, C_Difficulty, C_Score, C_Date};
        return database.query(TABLE_NAME, cols,
                null, null, null, null,
                orderByColumn + " " + direction, " " + num);
    }

}
