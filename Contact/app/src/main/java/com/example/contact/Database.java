package com.example.contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    private static final int VERSION =1;
    public static final String DB_NAME = "contacts_list";
    private Context context;
    public Database (Context context){
        super(context,DB_NAME,null,VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE CONTACT (ID INTEGER primary key, NAME TEXT, PHONE TEXT, EMAIL TEXT)";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
    }
    //add a new contact
    public void addContact(Contact contact){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME",contact.getName());
        values.put("PHONE",contact.getPhone());
        values.put("EMAIL",contact.getEmail());
        db.insert("CONTACT",null,values);
        db.close();
    }
    //list contacts
    public ArrayList<Contact> getAllContact(){
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        String script = "Select * from CONTACT";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(script,null);
        while (cursor.moveToNext()){
            Contact contact = new Contact();
            contact.setId(cursor.getInt(0));
            contact.setName(cursor.getString(1));
            contact.setPhone(cursor.getString(2));
            contact.setEmail(cursor.getString(3));
            contacts.add(contact);
        }
        return contacts;
    }
    //delete a contact
    public void deleteDatabase(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CONTACT","ID=?", new String[]{String.valueOf(contact.getId())});
    }
    //update a contact
    public int updateContact(Contact contact){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME",contact.getName());
        contentValues.put("PHONE",contact.getPhone());
        contentValues.put("EMAIL",contact.getEmail());
        return db.update("CONTACT",contentValues,"ID=?",new String[]{String.valueOf(contact.getId())});
    }
}
