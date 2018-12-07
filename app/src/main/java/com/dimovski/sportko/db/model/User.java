package com.dimovski.sportko.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    private String email;
    private String username;

    public User() {
    }

    @Ignore
    public User(@NonNull  String email) {
        this.email = email;
        this.username = "test";
    }

    @Ignore
    public User(@NonNull String email, String username) {
        this.email = email;
        this.username = username;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
