package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ViewEditContactActivity extends AppCompatActivity {
    TextView edtNameView,edtMobileView,edtEmailView;
    Button btnCancel,btnEdit;
    Contact contact;
    private static final int REQUEST_CODE_EDIT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_contact);

        edtNameView = (TextView) findViewById(R.id.tv_nameedit);
        edtMobileView = (TextView) findViewById(R.id.tv_mobileedit);
        edtEmailView = (TextView) findViewById(R.id.tv_emailedit);

        Bundle bundle = getIntent().getBundleExtra("vieweditsend");
        contact = (Contact) bundle.getSerializable("viewedit");

        btnCancel = (Button) findViewById(R.id.btn_cancelview);
        btnEdit = (Button) findViewById(R.id.btn_edit);

        edtNameView.setText(contact.getName());
        edtMobileView.setText(contact.getPhone());
        edtEmailView.setText(contact.getEmail());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("edit", contact);

                Intent intent = new Intent(ViewEditContactActivity.this, EditContactActivity.class);
                intent.putExtra("editsend", bundle);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == REQUEST_CODE_EDIT) {
            if(resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getBundleExtra("edited");
                Contact contact = (Contact) bundle.getSerializable("editback");

                edtNameView.setText(contact.getName());
                edtMobileView.setText(contact.getPhone());
                edtEmailView.setText(contact.getEmail());

                bundle.putSerializable("afteredit", contact);

                Intent resultIntent = getIntent();
                resultIntent.putExtra("aftereditsend", bundle);
                setResult(Activity.RESULT_OK,resultIntent);
                finish();
            }
        }
    }
}
