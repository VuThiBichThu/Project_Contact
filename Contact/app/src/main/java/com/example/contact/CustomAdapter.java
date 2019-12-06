package com.example.contact;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapter extends ArrayAdapter<Contact> {
    private final ArrayList<Contact> arrayList;
    private Context context;
    private ArrayList<Contact> contacts;
    private int layoutResource;

    public CustomAdapter(Context context, int resource, ArrayList<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.contacts = objects;
        this.layoutResource = resource;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(contacts);
    }

    @NonNull
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(layoutResource, null);
        TextView name = (TextView) view.findViewById(R.id.tv_namecontact);
        name.setText(contacts.get(i).getName());
        ImageView ivPhone = (ImageView) view.findViewById(R.id.iv_phone);
        //call a contact
        ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = contacts.get(i).getPhone();
                if(((MainActivity)context).checkPermission(Init.PHONE_PERMISSIONS)){
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
                    context.startActivity(callIntent);
                }
                else {
                    ((MainActivity)context).verifyPermissions(Init.PHONE_PERMISSIONS);
                }
            }
        });

        return view;
    }
}