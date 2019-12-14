package com.example.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.EditText;

import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Database db;
    ArrayList<Contact> contacts;
    ListView lvContact;
    FloatingActionButton btnAdd;
    CustomAdapter customAdapter,searchAdapter;
    EditText searchContact;
    int index;

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_ADD = 2;
    private static final int REQUEST_CODE_VIEWEDIT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContact = (ListView) findViewById(R.id.lv_contact);
        btnAdd = (FloatingActionButton) findViewById(R.id.btn_add);

        db = new Database(this);
        contacts = db.getAllContact();
        customAdapter = new CustomAdapter(
                this, R.layout.row_listview,
                contacts);
        lvContact.setAdapter(customAdapter);

        searchContact = (EditText) findViewById(R.id.edt_search);
        searchContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int textlength = charSequence.length();
                final ArrayList<Contact> searchContacts = new ArrayList<Contact>();
                for (Contact c : contacts) {
                    if (textlength <= c.getName().length()) {
                        if (c.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            searchContacts.add(c);
                        }
                    }
                }

                searchAdapter = new CustomAdapter(MainActivity.this, R.layout.row_listview, searchContacts);
                lvContact.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();

                lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        int indexSearch;
                        for (indexSearch = 0; indexSearch < contacts.size(); indexSearch++) {
                            if (contacts.get(indexSearch).getPhone() == searchContacts.get(i).getPhone()) {
                                index = indexSearch;
                                break;
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("viewedit", searchContacts.get(i));
                        Intent intent = new Intent(MainActivity.this, ViewEditContactActivity.class);
                        intent.putExtra("vieweditsend", bundle);
                        startActivityForResult(intent, REQUEST_CODE_VIEWEDIT);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //them vao 1 contact
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });

        //edit contact
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;

                Bundle bundle = new Bundle();
                bundle.putSerializable("viewedit", contacts.get(i));

                Intent intent = new Intent(MainActivity.this, ViewEditContactActivity.class);
                intent.putExtra("vieweditsend", bundle);
                startActivityForResult(intent, REQUEST_CODE_VIEWEDIT);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_VIEWEDIT) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getBundleExtra("aftereditsend");
                Contact contact = (Contact) bundle.getSerializable("afteredit");
                db.updateContact(contact);
                contacts.set(index, contact);
                customAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == REQUEST_CODE_ADD) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getBundleExtra("add");
                Contact contact = (Contact) bundle.getSerializable("addback");
                db.addContact(contact);
                contacts.add(contact);
                customAdapter.notifyDataSetChanged();
            }
        }
        lvContact.setAdapter(customAdapter);
        searchContact.setText("");
    }
    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        ActivityCompat.requestPermissions(
                MainActivity.this,
                permissions,
                REQUEST_CODE
        );
    }
    public boolean checkPermission(String[] permission){
        Log.d(TAG, "checkPermission: checking permissions for:" + permission[0]);

        int permissionRequest = ActivityCompat.checkSelfPermission(
                MainActivity.this,
                permission[0]);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermission: \n Permissions was not granted for: " + permission[0]);
            return false;
        }else{
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: requestCode: " + requestCode);

        switch(requestCode){
            case REQUEST_CODE:
                for(int i = 0; i < permissions.length; i++){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG, "onRequestPermissionsResult: User has allowed permission to access: " + permissions[i]);
                    }else{
                        break;
                    }
                }
                break;
        }
    }
}
