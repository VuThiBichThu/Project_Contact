package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ViewEditContactActivity extends AppCompatActivity {
    TextView edtNameView,edtMobileView,edtEmailView;
    Button btnCancel,btnEdit,btnDelete;
    Contact contact;
    private static final int REQUEST_CODE_EDIT = 4;
    ImageView ivMessage;
    ImageView ivEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_contact);

        edtNameView = (TextView) findViewById(R.id.tv_nameedit);
        edtMobileView = (TextView) findViewById(R.id.tv_mobileedit);
        edtEmailView = (TextView) findViewById(R.id.tv_emailedit);

        ivMessage = (ImageView) findViewById(R.id.iv_message);
        ivEmail = (ImageView) findViewById(R.id.iv_email);

        Bundle bundle = getIntent().getBundleExtra("vieweditsend");
        contact = (Contact) bundle.getSerializable("viewedit");

        btnCancel = (Button) findViewById(R.id.btn_cancelview);
        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnDelete = (Button) findViewById(R.id.btn_delete);

        edtNameView.setText(contact.getName());
        edtMobileView.setText(contact.getPhone());
        edtEmailView.setText(contact.getEmail());

        ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{contact.getEmail()});
                startActivity(emailIntent);
            }
        });

        ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = contact.getPhone();
                Intent messageIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
                startActivity(messageIntent);
            }
        });

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
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteConfirm();
            }
        });
    }
    protected void DeleteConfirm(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Warning!");
        alertDialog.setIcon(R.drawable.warning);
        alertDialog.setMessage("Do you want to delete this contact?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent resultDelete = getIntent();
                String confirm= "Delete Contact";
                resultDelete.putExtra("delete",confirm);
                setResult(Activity.RESULT_OK,resultDelete);
                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
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
