package com.example.day3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.day3.databinding.ItemSearchBinding;
import com.example.day3.helper.ApiHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<ApiHelper.Res.Item> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.recyclerview);
        rv.setAdapter(new SearchAdapter());

        /************* Retrofit2 **************/
        ApiHelper.getService().search("DR-747")
                .enqueue(new Callback<ApiHelper.Res>() { // enqueue : 큐에 데이터 삽입
                    @Override
                    public void onResponse(Call<ApiHelper.Res> call, Response<ApiHelper.Res> response) {
                        ApiHelper.Res res = response.body();
                        dataList = res.items;
                        rv.getAdapter().notifyDataSetChanged(); // 데이터가 변경되었으므로 RecyclerView 다시 뿌려라
                    }

                    @Override
                    public void onFailure(Call<ApiHelper.Res> call, Throwable t) {

                    }
                });

    }

    /************* RecyclerView **************/
    class ViewHolder extends RecyclerView.ViewHolder {
        ItemSearchBinding b;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            b = ItemSearchBinding.bind(itemView);
        }

        public void onBind(ApiHelper.Res.Item item) {
            b.tvTitle.setText(Html.fromHtml(item.getTitle()));
            b.tvMallName.setText(item.getMallName());
            b.tvPrice.setText(DecimalFormat.getInstance().format(item.getLprice()) + "원");
            Glide.with(MainActivity.this)
                    .load(item.getImage())
                    .into(b.image);


        }
    }

    class SearchAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.item_search, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ApiHelper.Res.Item item = dataList.get(position);
            holder.onBind(item);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
}