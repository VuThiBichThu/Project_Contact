package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;

import android.widget.AdapterView;
import android.widget.EditText;

import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Database db;
    ArrayList<Contact> contacts;
    ListView lvContact;
    FloatingActionButton btnAdd;
    CustomAdapter customAdapter;
    EditText searchContact;
    int index;

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;

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
                String text = searchContact.getText().toString().toLowerCase(Locale.getDefault());
                customAdapter.filter(text);
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
                startActivityForResult(intent, 2);
            }
        });
        //edit contact
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;

                Bundle bundle = new Bundle();
                bundle.putSerializable("edit", contacts.get(i));

                Intent intent = new Intent(MainActivity.this, EditContactActivity.class);
                intent.putExtra("editsend", bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getBundleExtra("edited");
                Contact contact = (Contact) bundle.getSerializable("editback");
                db.updateContact(contact);
                contacts.set(index, contact);
                customAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getBundleExtra("add");
                Contact contact = (Contact) bundle.getSerializable("addback");
                db.addContact(contact);
                contacts.add(contact);
                customAdapter.notifyDataSetChanged();
            }
        }
    }
}
