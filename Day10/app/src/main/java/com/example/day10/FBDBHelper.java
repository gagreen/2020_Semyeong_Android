package com.example.day10;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FBDBHelper {
    private static DatabaseReference getDatabaseReference() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        return db.getReference("user");
    }

    public static void requestUserData() {
        DatabaseReference ref = getDatabaseReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("HYCC", user.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //NOPE
            }
        });
    }

    public static void setUser(User user) {         // 단일 객체의 Set
        DatabaseReference ref = getDatabaseReference();

        ref.setValue(user);
    }

    public static void setUser(List<User> users) {  // List 형태의 set
        DatabaseReference ref = getDatabaseReference();

        ref.setValue(users);
    }

    public static void addChangeListener() {
        DatabaseReference ref = getDatabaseReference();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("HYCC", user.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
