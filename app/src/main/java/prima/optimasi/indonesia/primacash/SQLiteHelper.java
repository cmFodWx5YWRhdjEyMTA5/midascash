package prima.optimasi.indonesia.primacash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import prima.optimasi.indonesia.primacash.objects.account;
import prima.optimasi.indonesia.primacash.objects.category;
import prima.optimasi.indonesia.primacash.objects.income;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";


    Calendar cal = Calendar.getInstance();
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "cash";

    // Table Names
    private static final String TABLE_ACCOUNT = "account";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_COST = "cost";
    private static final String TABLE_EXPENSE = "expense";
    private static final String TABLE_TRANSFER = "transfer";
    private static final String TABLE_INCOME = "income";
    private static final String TABLE_LOG = "log";
    private static final String TABLE_REMINDER = "reminder";

    // Common column names
    private static final String KEY_USERNAME = "username";

    // account

    String account_name;
    String account_category;
    String account_currency;
    String fullaccount_currency;
    Object account_createdate;
    String account_balance;
    String account_balance_current;
    String username;
    int account_status;
    int createorlast;

    private static final String KEY_ACCOUNT_NAME = "account_name";
    private static final String KEY_ACCOUNT_CATEGORY = "account_category";
    private static final String KEY_ACCOUNT_CREATEDATE = "account_createdate";
    private static final String KEY_ACCOUNT_CREATEORLAST = "account_createorlast";
    private static final String KEY_ACCOUNT_BALANCE = "account_balance";
    private static final String KEY_ACCOUNT_BALANCE_CURRENT = "account_balance_current";
    private static final String KEY_ACCOUNT_STATUS = "account_status";
    private static final String KEY_ACCOUNT_CURRENCY = "account_currency";
    private static final String KEY_ACCOUNT_FULLCURRENCY = "fullaccount_curency";

    //category

    private static final String KEY_CATEGORY_NAME = "category_name";
    private static final String KEY_CATEGORY_IMAGE = "category_image";
    private static final String KEY_CATEGORY_CREATEDATE = "category_createdate";
    private static final String KEY_CATEGORY_STATUS = "category_status";

    //cost

    private static final String KEY_COST_NAME = "cost_name";
    private static final String KEY_COST_CATEGORY = "cost_category";
    private static final String KEY_COST_CREATEDATE = "cost_createdate";
    private static final String KEY_COST_IMAGE = "cost_image";
    private static final String KEY_COST_AMOUNT = "cost_amount";
    private static final String KEY_COST_TYPE = "cost_type";
    private static final String KEY_COST_DURATION = "cost_duration";

    //expense

    private static final String KEY_EXPENSE_ACCOUNT = "expense_account";
    private static final String KEY_EXPENSE_TO = "expense_to";
    private static final String KEY_EXPENSE_CREATEDATE = "expense_createdate";
    private static final String KEY_EXPENSE_IMAGE = "expense_image";
    private static final String KEY_EXPENSE_DATE = "expense_date";
    private static final String KEY_EXPENSE_ID = "expense_id";
    private static final String KEY_EXPENSE_TYPE = "expense_type";
    private static final String KEY_EXPENSE_NOTES = "expense_notes";
    private static final String KEY_EXPENSE_AMOUNT = "expense_amount";
    private static final String KEY_EXPENSE_CATEGORY = "expense_category";


    //transfer

    private static final String KEY_TRANSFER_SRC = "transfer_src";
    private static final String KEY_TRANSFER_DEST = "transfer_dest";
    private static final String KEY_TRANSFER_CREATEDATE = "transfer_createdate";
    private static final String KEY_TRANSFER_DATE = "transfer_date";
    private static final String KEY_TRANSFER_RATE = "transfer_rate";
    private static final String KEY_TRANSFER_NOTES = "transfer_notes";
    private static final String KEY_TRANSFER_AMOUNT = "transfer_amount";
    private static final String KEY_TRANSFER_CATEGORY = "transfer_category";

    //income

    private static final String KEY_INCOME_ACCOUNT = "income_account";
    private static final String KEY_INCOME_FROM = "income_from";
    private static final String KEY_INCOME_CREATEDATE = "income_createdate";
    private static final String KEY_INCOME_IMAGE = "income_image";
    private static final String KEY_INCOME_DATE = "income_date";
    private static final String KEY_INCOME_ID = "income_id";
    private static final String KEY_INCOME_TYPE = "income_type";
    private static final String KEY_INCOME_NOTES = "income_notes";
    private static final String KEY_INCOME_AMOUNT = "income_amount";
    private static final String KEY_INCOME_CATEGORY = "expense_category";

    //log

    private static final String KEY_LOG_ACTION = "log_Action";
    private static final String KEY_LOG_DATE = "log_date";

    //reminder

    private static final String KEY_REMINDER_CREATEDATE = "reminder_createdate";
    private static final String KEY_REMINDER_NAME = "reminder_name";
    private static final String KEY_REMINDER_STATUS = "reminder_status";
    private static final String KEY_REMINDER_INCOME = "reminder_income";
    private static final String KEY_REMINDER_TIME = "reminder_time";
    private static final String KEY_REMINDER_NOTE = "reminder_note";
    private static final String KEY_REMINDER_EXPENSE = "reminder_expense";

    //Create table query

    private static final String CREATE_TABLE_ACCOUNT="CREATE TABLE "
            + TABLE_ACCOUNT + "(" + KEY_USERNAME + " TEXT," + KEY_ACCOUNT_CREATEORLAST + " INTEGER,"
            + KEY_ACCOUNT_CREATEDATE+ " DATETIME," + KEY_ACCOUNT_BALANCE + " TEXT,"+ KEY_ACCOUNT_BALANCE_CURRENT + " TEXT,"
            + KEY_ACCOUNT_CATEGORY + " TEXT," + KEY_ACCOUNT_NAME + " TEXT," +KEY_ACCOUNT_STATUS+ " INTEGER," +KEY_ACCOUNT_CURRENCY
            + " TEXT," +KEY_ACCOUNT_FULLCURRENCY+ " TEXT ,"+KEY_ACCOUNT_CREATEORLAST + "INTEGER)";

    private static final String CREATE_TABLE_CATEGORY= "CREATE TABLE "
            + TABLE_CATEGORY + "(" + KEY_USERNAME + " TEXT,"
            + KEY_CATEGORY_CREATEDATE+ " DATETIME," + KEY_CATEGORY_IMAGE + " TEXT,"
            + KEY_CATEGORY_NAME + " TEXT," +KEY_CATEGORY_STATUS+ " INTEGER)";

    private static final String CREATE_TABLE_COST= "CREATE TABLE "
            + TABLE_COST + "(" + KEY_USERNAME + " TEXT,"
            + KEY_COST_CREATEDATE+ " DATETIME," + KEY_COST_AMOUNT + " REAL,"
            + KEY_COST_CATEGORY + " TEXT," + KEY_COST_NAME + " TEXT," +KEY_COST_TYPE+ " INTEGER,"+ KEY_COST_DURATION
            + " INTEGER,"+ KEY_COST_IMAGE + " TEXT" +")";

    private static final String CREATE_TABLE_EXPENSE="CREATE TABLE "
            + TABLE_EXPENSE + "(" + KEY_USERNAME + " TEXT," + KEY_EXPENSE_CATEGORY + " TEXT,"
            + KEY_EXPENSE_CREATEDATE+ " DATETIME," + KEY_EXPENSE_AMOUNT + " TEXT,"
            + KEY_EXPENSE_ID + " TEXT," + KEY_EXPENSE_NOTES + " TEXT," +KEY_EXPENSE_TYPE+ " TEXT,"+ KEY_EXPENSE_DATE
            + " DATETIME,"+ KEY_EXPENSE_IMAGE + " TEXT," + KEY_EXPENSE_TO + " TEXT," + KEY_EXPENSE_ACCOUNT + " TEXT" +")";

    private static final String CREATE_TABLE_TRANSFER ="CREATE TABLE "
            + TABLE_TRANSFER + "(" + KEY_USERNAME + " TEXT,"
            + KEY_TRANSFER_CREATEDATE+ " DATETIME," + KEY_TRANSFER_AMOUNT + " TEXT,"
            + KEY_TRANSFER_DEST + " TEXT," + KEY_TRANSFER_NOTES + " TEXT," +KEY_TRANSFER_SRC+ " TEXT,"+ KEY_TRANSFER_DATE
            + " DATETIME,"+ KEY_TRANSFER_RATE + " REAL," + KEY_TRANSFER_CATEGORY + " TEXT)";

    private static final String CREATE_TABLE_INCOME="CREATE TABLE "
            + TABLE_INCOME + "(" + KEY_USERNAME + " TEXT," + KEY_INCOME_CATEGORY + " TEXT,"
            + KEY_INCOME_CREATEDATE+ " DATETIME," + KEY_INCOME_AMOUNT + " TEXT,"
            + KEY_INCOME_ID + " TEXT," + KEY_INCOME_NOTES + " TEXT," +KEY_INCOME_TYPE
            + " TEXT,"+ KEY_INCOME_DATE + " DATETIME,"+ KEY_INCOME_IMAGE + " TEXT,"
            + KEY_INCOME_FROM + " TEXT," + KEY_INCOME_ACCOUNT + " TEXT" +")";

    private static final String CREATE_TABLE_LOG="CREATE TABLE "
            + TABLE_LOG + "(" + KEY_USERNAME + " TEXT,"
            + KEY_LOG_DATE + " DATETIME,"+ KEY_LOG_ACTION + " TEXT)";

    private static final String CREATE_TABLE_REMINDER="CREATE TABLE "
            + TABLE_REMINDER + "(" + KEY_USERNAME + " TEXT,"
            + KEY_REMINDER_CREATEDATE+ " DATETIME," + KEY_REMINDER_TIME + " TIME,"
            + KEY_REMINDER_EXPENSE + " TEXT," + KEY_REMINDER_INCOME + " TEXT," +KEY_REMINDER_NAME
            + " TEXT,"+ KEY_REMINDER_STATUS + " INTEGER,"+ KEY_REMINDER_NOTE + " TEXT)";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_ACCOUNT);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_COST);
        db.execSQL(CREATE_TABLE_INCOME);
        db.execSQL(CREATE_TABLE_EXPENSE);
        db.execSQL(CREATE_TABLE_REMINDER);
        db.execSQL(CREATE_TABLE_TRANSFER);
        db.execSQL(CREATE_TABLE_LOG);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " +TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_COST);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_TRANSFER);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_LOG);

        onCreate(db);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
    // ------------------------ "account" table methods ----------------//
    public long createAccount(account accs, String user) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(KEY_ACCOUNT_STATUS, accs.getAccount_status());
        values.put(KEY_ACCOUNT_BALANCE, accs.getAccount_balance());
        values.put(KEY_ACCOUNT_CATEGORY, accs.getAccount_category());
        values.put(KEY_ACCOUNT_CURRENCY, accs.getAccount_currency());
        values.put(KEY_ACCOUNT_FULLCURRENCY, accs.getFullaccount_currency());
        values.put(KEY_ACCOUNT_BALANCE_CURRENT, accs.getAccount_balance_current());
        values.put(KEY_ACCOUNT_NAME, accs.getAccount_name());
        values.put(KEY_ACCOUNT_CREATEORLAST, 0);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        values.put(KEY_ACCOUNT_CREATEDATE, Calendar.getInstance().getTimeInMillis());
        values.put(KEY_USERNAME,user);

        // insert row
        long todo_id = db.insert(TABLE_ACCOUNT, null, values);

        return todo_id;
    }

    public account getaccount(String accountname) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNT + " WHERE "
                + KEY_ACCOUNT_NAME + " = '" + accountname+"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if( c != null && c.moveToFirst() ) {

            account td = new account();
            td.setAccount_name(c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME)));
            td.setAccount_category(c.getString(c.getColumnIndex(KEY_ACCOUNT_CATEGORY)));
            td.setAccount_status(c.getInt(c.getColumnIndex(KEY_ACCOUNT_STATUS)));
            td.setAccount_currency(c.getString(c.getColumnIndex(KEY_ACCOUNT_CURRENCY)));
            td.setFullaccount_currency(c.getString(c.getColumnIndex(KEY_ACCOUNT_FULLCURRENCY)));

            Date date=null;
            cal.setTimeInMillis(c.getColumnIndex(KEY_CATEGORY_CREATEDATE));

            date = cal.getTime() ;

            td.setAccount_createdate(date);
            td.setAccount_balance(c.getString(c.getColumnIndex(KEY_ACCOUNT_BALANCE)));
            td.setAccount_balance_current(c.getString(c.getColumnIndex(KEY_ACCOUNT_BALANCE_CURRENT)));
            td.setCreateorlast(c.getInt(c.getColumnIndex(KEY_ACCOUNT_CREATEORLAST)));
            td.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));

            return td;
        }
        else {
            return null;
        }
    }

    public List<account> getAllaccount() {
        List<account> todos = new ArrayList<account>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNT +" ORDER BY "+KEY_ACCOUNT_NAME + " ASC";

        Log.e(LOG, selectQuery);

        Calendar cal = Calendar.getInstance();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                account td = new account();
                td.setAccount_name(c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME)));
                td.setAccount_category(c.getString(c.getColumnIndex(KEY_ACCOUNT_CATEGORY)));
                td.setAccount_status(c.getInt(c.getColumnIndex(KEY_ACCOUNT_STATUS)));
                td.setAccount_currency(c.getString(c.getColumnIndex(KEY_ACCOUNT_CURRENCY)));
                td.setFullaccount_currency(c.getString(c.getColumnIndex(KEY_ACCOUNT_FULLCURRENCY)));

                Date date=null;
                cal.setTimeInMillis(c.getColumnIndex(KEY_CATEGORY_CREATEDATE));

                date = cal.getTime() ;

                td.setAccount_createdate(date);
                td.setAccount_balance(c.getString(c.getColumnIndex(KEY_ACCOUNT_BALANCE)));
                td.setAccount_balance_current(c.getString(c.getColumnIndex(KEY_ACCOUNT_BALANCE_CURRENT)));
                td.setCreateorlast(c.getInt(c.getColumnIndex(KEY_ACCOUNT_CREATEORLAST)));
                td.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    /*public List<account> getAllaccountorderedbycategory() {
        List<account> todos = new ArrayList<account>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNT + "ORDER BY " + KEY_ACCOUNT_CATEGORY + "DESC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query (false,
        TABLE_ACCOUNT,
        null,
        null,
        null,
        null,
        null,
        KEY_ACCOUNT_CATEGORY + " ASC",null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                account td = new account();
                td.setAccount_name(c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME)));
                td.setAccount_category(c.getString(c.getColumnIndex(KEY_ACCOUNT_CATEGORY)));
                td.setAccount_status(c.getInt(c.getColumnIndex(KEY_ACCOUNT_STATUS)));

                Date date=null;
                String dtStart = c.getString(c.getColumnIndex(KEY_ACCOUNT_CREATEDATE));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date = format.parse(dtStart);
                    System.out.println(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                td.setAccount_createdate(c.getString(c.getColumnIndex(KEY_ACCOUNT_CREATEDATE)));
                td.setAccount_balance(c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME)));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }*/

    public int getaccountCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ACCOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a todo
     */
    public int updateaccount(account accs,String user,String accountname) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        account td = new account();

        values.put(KEY_ACCOUNT_STATUS, accs.getAccount_status());
        values.put(KEY_ACCOUNT_BALANCE, accs.getAccount_balance());
        values.put(KEY_ACCOUNT_BALANCE_CURRENT, accs.getAccount_balance_current());
        values.put(KEY_ACCOUNT_CATEGORY, accs.getAccount_category());
        values.put(KEY_ACCOUNT_CURRENCY, accs.getAccount_currency());
        values.put(KEY_ACCOUNT_FULLCURRENCY, accs.getFullaccount_currency());

        Date date=(Date) accs.getAccount_createdate();
        cal.setTimeInMillis(date.getTime());


        values.put(KEY_ACCOUNT_CREATEDATE, cal.getTimeInMillis());
        values.put(KEY_ACCOUNT_CREATEORLAST,accs.getCreateorlast());
        values.put(KEY_ACCOUNT_NAME,accs.getAccount_name());
        values.put(KEY_USERNAME,accs.getUsername());

        // updating row
        return db.update(TABLE_ACCOUNT, values, KEY_ACCOUNT_NAME + " = ?",
                new String[] { accountname });
    }

    public int updateaccountstatus(account accs,String user,String accountname) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        account td = new account();

        values.put(KEY_ACCOUNT_STATUS, accs.getAccount_status());
        values.put(KEY_ACCOUNT_BALANCE, accs.getAccount_balance());
        values.put(KEY_ACCOUNT_BALANCE_CURRENT, accs.getAccount_balance_current());
        values.put(KEY_ACCOUNT_CATEGORY, accs.getAccount_category());
        values.put(KEY_ACCOUNT_CURRENCY, accs.getAccount_currency());
        values.put(KEY_ACCOUNT_FULLCURRENCY, accs.getFullaccount_currency());

        Date date=(Date) accs.getAccount_createdate();
        cal.setTimeInMillis(date.getTime());


        values.put(KEY_ACCOUNT_CREATEDATE, cal.getTimeInMillis());
        values.put(KEY_ACCOUNT_CREATEORLAST,accs.getCreateorlast());
        values.put(KEY_ACCOUNT_NAME,accs.getAccount_name());
        values.put(KEY_USERNAME,accs.getUsername());

        // updating row
        return db.update(TABLE_ACCOUNT, values, KEY_ACCOUNT_NAME + " = ?",
                new String[] { accountname });
    }

    /**
     * Deleting a todo
     */
    public void deleteaccount(String accs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNT, KEY_ACCOUNT_NAME + " = ?",
                new String[] { accs });
    }

    //------------------------Category method------------------------------//
    public long createCategory(category accs, String user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_STATUS, accs.getCategory_status());
        values.put(KEY_CATEGORY_IMAGE, accs.getCategory_image());
        values.put(KEY_CATEGORY_NAME, accs.getCategory_name());

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        values.put(KEY_CATEGORY_CREATEDATE, Calendar.getInstance().getTimeInMillis());
        values.put(KEY_USERNAME,user);

        // insert row
        long todo_id = db.insert(TABLE_CATEGORY, null, values);

        return todo_id;
    }

    public int updatecategory(String oldcat,category newcat,String user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_STATUS, newcat.getCategory_status());
        values.put(KEY_CATEGORY_IMAGE, newcat.getCategory_image());
        values.put(KEY_CATEGORY_NAME, newcat.getCategory_name());

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        values.put(KEY_CATEGORY_CREATEDATE, Calendar.getInstance().getTimeInMillis());
        values.put(KEY_USERNAME,user);


        // updating row
        return db.update(TABLE_CATEGORY, values, KEY_CATEGORY_NAME + " = ?",
                new String[]{oldcat});
    }

    public category getcategory(String categoryname) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY + " WHERE "
                + KEY_CATEGORY_NAME + " = '" + categoryname+"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if( c != null && c.moveToFirst() ){

            category td = new category();
            td.setCategory_name(c.getString(c.getColumnIndex(KEY_CATEGORY_NAME)));
            td.setCategory_image(c.getInt(c.getColumnIndex(KEY_CATEGORY_IMAGE)));
            td.setCategory_status(c.getInt(c.getColumnIndex(KEY_CATEGORY_STATUS)));

            Date date=null;
            cal.setTimeInMillis(c.getColumnIndex(KEY_CATEGORY_CREATEDATE));

            date = cal.getTime() ;
            td.setCategory_createdate(date);
            return  td;
        }
        else {
            return null;
        }
    }

    public List<category> getAllcategory() {
        List<category> allcat = new ArrayList<category>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY + " ORDER BY "+KEY_CATEGORY_NAME+" ASC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);


        Calendar calender = Calendar.getInstance();
        // looping through all rows and adding to list
        if( c != null && c.moveToFirst() ){
            do {

                category td = new category();
                td.setCategory_name(c.getString(c.getColumnIndex(KEY_CATEGORY_NAME)));
                td.setCategory_status(c.getInt(c.getColumnIndex(KEY_CATEGORY_STATUS)));
                td.setCategory_image(c.getInt(c.getColumnIndex(KEY_CATEGORY_IMAGE)));

                Date date=null;
                calender.setTimeInMillis(c.getColumnIndex(KEY_CATEGORY_CREATEDATE));

                date = calender.getTime() ;

                td.setCategory_createdate(date);

                // adding to todo list
                allcat.add(td);
            } while (c.moveToNext());
        }

        return allcat;
    }
    /**
     * Deleting a todo
     */
    public void deletecategory(String accs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, KEY_CATEGORY_NAME + " = ?",
                new String[] {  accs});
    }

    //------------------------- income methods ------------------------//


    public long createincome(income accs, String user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INCOME_ACCOUNT, accs.getIncome_account());
        values.put(KEY_INCOME_AMOUNT, accs.getIncome_amount());
        values.put(KEY_INCOME_FROM, accs.getIncome_from());
        values.put(KEY_INCOME_NOTES, accs.getIncome_notes());

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c);

        values.put(KEY_INCOME_CREATEDATE, formattedDate);

        DateFormat dated = new SimpleDateFormat("dd/MM/yyyy");

// Get the date today using Calendar object.
// Using DateFormat format method we can create a string
// representation of a date with the defined format.

        values.put(KEY_INCOME_IMAGE,accs.getIncome_image());
        values.put(KEY_INCOME_ID,this.getincomeCount()+1);
        values.put(KEY_USERNAME,user);

        // insert row
        long todo_id = db.insert(TABLE_INCOME, null, values);

        return todo_id;
    }

    public income getincome(String incomeid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_INCOME + " WHERE "
                + KEY_INCOME_ID + " = '" + incomeid+"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        income td = new income();
        td.setIncome_account(c.getString(c.getColumnIndex(KEY_INCOME_ACCOUNT)));
        td.setIncome_amount(c.getInt(c.getColumnIndex(KEY_INCOME_AMOUNT)));

        Date date=null;
        String dtStart = c.getString(c.getColumnIndex(KEY_INCOME_CREATEDATE));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(dtStart);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date1=null;
        String dtStart1 = c.getString(c.getColumnIndex(KEY_INCOME_CREATEDATE));
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = format1.parse(dtStart1);
            System.out.println(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        td.setIncome_from(c.getString(c.getColumnIndex(KEY_INCOME_FROM)));
        td.setIncome_id(c.getString(c.getColumnIndex(KEY_INCOME_ID)));

        td.setIncome_image(c.getInt(c.getColumnIndex(KEY_INCOME_IMAGE)));
        td.setIncome_notes(c.getString(c.getColumnIndex(KEY_INCOME_NOTES)));
        td.setIncome_type(c.getString(c.getColumnIndex(KEY_INCOME_TYPE)));

        return td;
    }

    public List<income> getAllincome() {
        List<income> todos = new ArrayList<income>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                income td = new income();
                td.setIncome_account(c.getString(c.getColumnIndex(KEY_INCOME_ACCOUNT)));
                td.setIncome_amount(c.getInt(c.getColumnIndex(KEY_INCOME_AMOUNT)));

                Date date=null;
                String dtStart = c.getString(c.getColumnIndex(KEY_INCOME_CREATEDATE));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date = format.parse(dtStart);
                    System.out.println(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date date1=null;
                String dtStart1 = c.getString(c.getColumnIndex(KEY_INCOME_CREATEDATE));
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date = format1.parse(dtStart1);
                    System.out.println(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                td.setIncome_from(c.getString(c.getColumnIndex(KEY_INCOME_FROM)));
                td.setIncome_id(c.getString(c.getColumnIndex(KEY_INCOME_ID)));

                td.setIncome_image(c.getInt(c.getColumnIndex(KEY_INCOME_IMAGE)));
                td.setIncome_notes(c.getString(c.getColumnIndex(KEY_INCOME_NOTES)));
                td.setIncome_type(c.getString(c.getColumnIndex(KEY_INCOME_TYPE)));

                todos.add(td);
                // adding to todo list
            } while (c.moveToNext());
        }

        return todos;
    }

    public int getincomeCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a todo
     */
    public int updateincome(income accs,String catname,String user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INCOME_ACCOUNT, accs.getIncome_account());
        values.put(KEY_INCOME_AMOUNT, accs.getIncome_amount());
        values.put(KEY_INCOME_FROM, accs.getIncome_from());
        values.put(KEY_INCOME_NOTES, accs.getIncome_notes());

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c);

        values.put(KEY_INCOME_CREATEDATE, formattedDate);

        DateFormat dated = new SimpleDateFormat("dd/MM/yyyy");

// Get the date today using Calendar object.
// Using DateFormat format method we can create a string
// representation of a date with the defined format.

        values.put(KEY_INCOME_IMAGE,accs.getIncome_image());
        values.put(KEY_INCOME_ID,this.getincomeCount()+1);
        values.put(KEY_USERNAME,user);

        // updating row
        return db.update(TABLE_CATEGORY, values, KEY_CATEGORY_NAME + " = ?",
                new String[] { catname });
    }

    /**
     * Deleting a todo
     */
    public void deleteincome(String accs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, KEY_CATEGORY_NAME + " = ?",
                new String[] {  accs});
    }
}
