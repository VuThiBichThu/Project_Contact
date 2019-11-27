package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactActivity extends AppCompatActivity {
    EditText edtNameAdd,edtMobileAdd,edtEmailAdd;
    Button btnCancel,btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        edtNameAdd = (EditText) findViewById(R.id.edt_nameadd);
        edtMobileAdd = (EditText) findViewById(R.id.edt_mobileadd);
        edtEmailAdd = (EditText) findViewById(R.id.edt_emailadd);

        btnCancel = (Button) findViewById(R.id.btn_canceladd);
        btnFinish = (Button) findViewById(R.id.btn_finish);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameAdd = edtNameAdd.getText().toString();
                String phoneAdd = edtMobileAdd.getText().toString();
                String emailAdd = edtEmailAdd.getText().toString();

                if (nameAdd.length()== 0) {
                    Toast.makeText(AddContactActivity.this,"Insert Name",Toast.LENGTH_LONG).show();
                }
                else if (phoneAdd.length()==0)
                {
                    Toast.makeText(AddContactActivity.this,"Insert Phone Number",Toast.LENGTH_LONG).show();
                }
                else {
                    Contact contact = new Contact(nameAdd,phoneAdd,emailAdd);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("addback",contact);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("add",bundle);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}