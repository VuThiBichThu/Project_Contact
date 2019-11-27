package com.example.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        return view;
    }

    // search contact
    public void filter(String characterText) {
        characterText = characterText.toLowerCase(Locale.getDefault());
        contacts.clear();
        if (characterText.length() == 0) {
            contacts.addAll(arrayList);
        } else {
            contacts.clear();
            for (Contact contact : arrayList) {
                if (contact.getName().toLowerCase(Locale.getDefault()).contains(characterText)) {
                    contacts.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }
}