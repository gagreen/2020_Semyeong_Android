package com.example.day2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.day2.databinding.ActivityMainBinding;
import com.example.day2.databinding.ItemRecyclerviewBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    RecyclerView recyclerView;
    ArrayList<MyItem> dataList = new ArrayList<>();
    ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        b = DataBindingUtil.setContentView(this, R.layout.activity_main);

        for (int i = 0; i < 30; i++) {
            dataList.add(new MyItem(i + "번 훈련생", i + " 올빼미", i * i));
        }

//        recyclerView = findViewById(R.id.recyclerView);
        b.recyclerView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

        // 1. 데이터의 개수 알기
        @Override
        public int getItemCount() {
            return dataList.size();
        }

        // 2. 담아줄 위치 지정
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);                             // LayoutInflater를 가져와서
            View view = layoutInflater.inflate(R.layout.item_recyclerview, b.recyclerView, false);   // item_recycleview를 inflate하기(R.layout.item_recyclerview를 객체화하는 작업)
            ViewHolder vh = new ViewHolder(view);                                                               // view를 Holding하고 있는 ViewHolder 생성
            return vh;
        }

        // 3. ViewHolder에 바인딩 해줌
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // 전달받은 ViewHolder에 position 위치에 있는 데이터를 가져와서 연결
//            MyItem item = dataList.get(position);
//            holder.name.setText(item.getName());
//            holder.nickname.setText(item.getNickname());
//            holder.number.setText("" + item.getNumber());
            holder.onBind(dataList.get(position));
        }

    }

    /* RecyclerView 안에 넣을 뷰를 담아주는 객체 AND View에 관한 처리도 맡음 */
    class ViewHolder extends RecyclerView.ViewHolder {
        //Item_recycleView의 위젯들을 멤버변수로 선언
//        TextView name;
//        TextView nickname;
//        TextView number;
        ItemRecyclerviewBinding b;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //전달받은 itemView(Item_recycleView)에서 위젯을 찾아 멤버변수에 저장
//            name = itemView.findViewById(R.id.tvName);
//            nickname = itemView.findViewById(R.id.tvNickName);
//            number = itemView.findViewById(R.id.tvNumber);
        }

        public void onBind(MyItem myItem) {
            b.tvName.setText(myItem.getName());
            b.tvNickName.setText(myItem.getNickname());
            b.tvNumber.setText("" + myItem.getNumber());
        }
    }

    /*ArrayList에 담을 객체 생성*/
    class MyItem {
        private String name;
        private String nickname;
        private int number;

        public MyItem(String name, String nickname, int number) {
            this.name = name;
            this.nickname = nickname;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public String getNickname() {
            return nickname;
        }

        public int getNumber() {
            return number;
        }
    }
}