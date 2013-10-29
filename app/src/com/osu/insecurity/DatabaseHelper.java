package com.osu.insecurity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
	/**
	 * The name of the database.
	 */
	private static final String DATABASE_NAME = "Hermes.db";
	/**
	 * The database version.
	 */
	private static final int DATABASE_VERSION = 1;
	/**
	 * The activity context used.
	 */
	private Context context;
	/**
	 * SQLite db object
	 */
	private SQLiteDatabase db;
	/**
	 * SQlite insert statement.
	 */
	private SQLiteStatement insertStmt;
	/**
	 * Boolean to check if the DB is populated.
	 */
	private static boolean isPopulated = false;

	/**
	 * Constructor for DatabaseHelper.
	 * 
	 * @param context
	 * 		The context used to represent an activity.
	 */
	public DatabaseHelper(Context context) {
		this.context = context;
		HermesOpenHelper openHelper = new HermesOpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();
		if (!isPopulated) {
			openHelper.onUpgrade(db, 1, 2);
			populate();
			isPopulated = true;
		}
	}

	/**
	 * Populates the db with a set of example values to use for illustrations.
	 */
	public void populate() {		
		
		String insertUser = "INSERT INTO CUSTOMER VALUES (?,?,?,?)";
		List<String> phoneUser = new ArrayList<String>();
		phoneUser.add("blackburn.171@osu.edu"); // customer ID - unique
		phoneUser.add("password"); // password
		phoneUser.add("code"); // code distress password
		phoneUser.add("null"); // description
		phoneUser.add("null"); // image
		insert(insertUser, phoneUser);
		
		// insert default contact

		String contactInsert = "INSERT INTO CONTACTS VALUES(?,?,?,?)";
		List<String> parameters1 = new ArrayList<String>();
		parameters1.add("Kathy Blackburn"); // name
		parameters1.add("3308534696"); // phone number - unique
		parameters1.add("blackburn.171@osu.edu"); // email    
		parameters1.add("2880 Ravine Way, Dublin OH 43017"); // address
		insert(contactInsert, parameters1);

		List<String> parameters2 = new ArrayList<String>();
		parameters2.add("Cameron Hopkins"); // name
		parameters2.add("6148861066"); // phone number - unique
		parameters2.add("hopkins.653@osu.edu"); // email    
		parameters2.add("null"); // address
		insert(contactInsert, parameters2);

	}

	/**
	 * SQLite update query.
	 * @param table
	 * 		The table to update.
	 * @param parameters
	 * 		The parameters to update.
	 * @param where
	 * 		Where clause in update statement.
	 * @param whereArgs
	 * 		Arguments in where clause.
	 */
	public void update(String table, ContentValues parameters, String where,
			String[] whereArgs) {
		db.update(table, parameters, where, whereArgs);
	}

	/**
	 * SQLite insert statement
	 * @param queryText
	 * 		The text of the insert query.
	 * @param parameters
	 * 		Parameters to insert
	 * @return
	 * 		The last rowID inserted.
	 */
	public long insert(String queryText, List<String> parameters) {
		this.insertStmt = this.db.compileStatement(queryText);
		Integer i = 1;
		for (String s : parameters) {
			this.insertStmt.bindString(i, s);
			i++;
		}
		return this.insertStmt.executeInsert();
	}

	/**
	 * SQLite query to delete a table.
	 * 
	 * @param tableName
	 * 		The table to delete.
	 */
	public void deleteAll(String tableName) {
		this.db.delete(tableName, null, null);
	}

	/**
	 * SQLite Selection query.
	 * 
	 * @param query
	 * 		The string version of the query.
	 * @param arguments
	 * 		The arguments of the query
	 * @return
	 * 		List<List<String>> containing the values of the selection query.
	 */
	public List<List<String>> parsedSelect(String query, String[] arguments) {
		List<List<String>> fullList = new ArrayList<List<String>>();
		Cursor cursor = this.db.rawQuery(query, arguments);

		if (cursor.moveToFirst()) {
			do {
				Integer i = 0;
				List<String> list = new ArrayList<String>();

				while (i < cursor.getColumnCount()) {
					list.add(cursor.getString(i));
					i++;
				}
				fullList.add(list);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return fullList;
	}

	/**
	 * SQLite Selection query #2
	 * 
	 * @param query
	 * 		The string version of the query.
	 * @param arguments
	 * 		The arguments of the query.
	 * @return
	 * 		List<String> containing the values of the selection query.
	 */
	public List<String> selectAll(String query, String[] arguments) {

		List<String> list = new ArrayList<String>();
		Cursor cursor = this.db.rawQuery(query, arguments);
		if (cursor.moveToFirst()) {
			do {
				Integer i = 0;

				while (i < cursor.getColumnCount()) {
					list.add(cursor.getString(i));
					i++;
				}
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	/**
	 * Static class used to generate the database used in the application. 
	 */
	private static class HermesOpenHelper extends SQLiteOpenHelper {
		HermesOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE CUSTOMER ("
					+ "Customer_ID varchar(128) not null,"
					+ "Password varchar(128) not null,"
					+ "Code_Password varchar(128) not null"
					+ "Description varchar(256)," 
					+ "ImagePath varchar(500),"
					+ "primary key(Customer_ID));");
			db.execSQL("CREATE TABLE CONTACTS ("
					+ "ContactName  varchar(128) not null,"
					+ "Phone varchar(10) not null," 
					+ "Email  varchar(128),"
					+ "Address varchar(500)," 
					+ "primary key(Phone),");
			db.execSQL("CREATE TABLE MESSAGES ("
					+ "Sender varchar(128)    not null,"
					+ "Receiver varchar(10)    not null,"
					+ "Time time not null," 
					+ "Title varchar(128),"
					+ "Text varchar(1000),"
					+ "primary key(Sender, Receiver, Time),"
					+ "foreign key(Sender) references CUSTOMER(Customer_ID),"
					+ "foreign key(Receiver) references CONTACTS(Phone))");
			// TODO: longitude and latitude map points
			db.execSQL("CREATE TABLE POINTS ("
					+ "Latitude real,"
					+ "Longitude real,"
					+ "primary key( Latitude, Longitude))");
		}

		/**
		 * Upgrade and recreate the database.
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Full Upgrade",
					"Upgrading database; this will drop and recreate the tables.");
			db.execSQL("DROP TABLE IF EXISTS \"CUSTOMER\"");
			db.execSQL("DROP TABLE IF EXISTS \"CONTACTS\"");
			db.execSQL("DROP TABLE IF EXISTS \"MESSAGES\"");
			db.execSQL("DROP TABLE IF EXISTS \"POINTS\"");
			onCreate(db);
		}
	}
}
