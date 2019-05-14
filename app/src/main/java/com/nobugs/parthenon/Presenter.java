package com.nobugs.parthenon;

import android.provider.ContactsContract;

import androidx.core.util.Pair;

import java.util.ArrayList;

import io.realm.RealmObject;

public class Presenter extends RealmObject {

    private String name;
    private String email;
   // private ArrayList<Pair<String, String>> studies;
    //private ArrayList<Pair<String, String>> jobs;

    public Presenter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
