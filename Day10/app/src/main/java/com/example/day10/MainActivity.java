package com.example.day10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.day10.databinding.ActivityMainBinding;
import com.example.day10.databinding.ItemChatBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static final String SENDER_NAME = "조국어";

    ActivityMainBinding b;
    ArrayList<Chat> chats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);

        b.rv.setAdapter(new ChatAdapter());
        b.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertChat();
                b.et.setText("");
            }
        });

        getDatabaseReference()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Chat value = dataSnapshot.getValue(Chat.class);
                        value.timestamp = Long.valueOf(dataSnapshot.getKey());

                        chats.add(value);
                        b.rv.smoothScrollToPosition(chats.size() - 1);
                        b.rv.getAdapter().notifyItemInserted(chats.size() - 1);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //nop
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        //nop
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //nop
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //nop
                    }
                });

//        FBDBHelper.requestUserData();
//        FBDBHelper.setUser(new User("관우", "Silver"));
//        FBDBHelper.addChangeListener();
//        FBDBHelper.setUser(
//                Arrays.asList(
//                        new User("가", "GOlD"),
//                        new User("나", "GOLD"),
//                        new User("다", "Silver"),
//                        new User("라", "UNLANK"),
//                        new User("마", "YEAH")
//                )
//        );
    }

    private static DatabaseReference getDatabaseReference() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        return db.getReference("chats");
    }

    void insertChat() {
        String input = b.et.getText().toString();
        if (TextUtils.isEmpty(input)) {
            Toast.makeText(this, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show();
        }

        getDatabaseReference()
                .child(System.currentTimeMillis() + "")
                .setValue(new Chat(SENDER_NAME, input));    //

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemChatBinding ib;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ib = ItemChatBinding.bind(itemView);
        }

        public void bind(Chat item) {
            ib.tvSender.setText(item.sender);
            ib.tvMessage.setText(item.message);
            ib.tvTimestamp.setText(convertToString(item.timestamp));

            ib.layout.setGravity(item.sender.equals(SENDER_NAME)? Gravity.END : Gravity.START);
        }

        String convertToString(long millis) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(millis);

            return String.format("%02d:%02d:%02d",
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    cal.get(Calendar.SECOND)
            );
        }

    }

    class ChatAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.item_chat, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(chats.get(position));
        }

        @Override
        public int getItemCount() {
            return chats.size();
        }
    }

}