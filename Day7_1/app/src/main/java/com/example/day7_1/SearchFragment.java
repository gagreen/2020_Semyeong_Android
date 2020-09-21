package com.example.day7_1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.LogPrinter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.day7_1.databinding.FragmentSearchBinding;
import com.example.day7_1.databinding.ItemSearchBinding;
import com.example.day7_1.db.SearchDB;
import com.example.day7_1.db.SearchItem;
import com.example.day7_1.helper.ApiHelper;
import com.example.day7_1.helper.DBAsync;
import com.example.day7_1.helper.PrefHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    FragmentSearchBinding b;
//    RecyclerView rv;
//    EditText et;
//    Button btn;
    ArrayList<SearchItem> mDataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        rv = view.findViewById(R.id.rv);
//        et = view.findViewById(R.id.etSearch);
//        btn = view.findViewById(R.id.btnSubmit);

        /** Fragment.onCreateView에서 데이터바인딩 객체 초기화하기 **/
        // 1. 뷰를 직접 inflate해서 초기화하기
//        View view = inflater.inflate(R.layout.fragment_search, container, false);
//        b = FragmentSearchBinding.bind(view);
        // 2. LayoutInflater를 던져주고 알아서 초기화하기
        b = FragmentSearchBinding.inflate(inflater);

        return /*view*/ b.getRoot();
    }


    // 로직은 여기서 구성한다.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /** xml 문서가 아닌 java에서 속성 지정 가능 **/
        LinearLayoutManager lm = new LinearLayoutManager(requireContext());
        b.rv.setLayoutManager(lm);

        /** EditText imeOptions 설정을 위한 코드 **/
        b.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH) {
                    b.btnSubmit.performClick();
                }
                return false;
            }
        });

        /** 버튼 이벤트 추가 **/
        b.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = b.etSearch.getText().toString();
                searchApi(keyword);
                b.etSearch.setText("");
            }
        });

        /** 어댑터 셋팅 **/
        b.rv.setAdapter(new SearchAdapter());
    }
    /** : API 호출 및 datalist 업데이트 시 RecyclerView 업데이트 **/
    /************* Retro2 *************/
    private void searchApi(String keyword) {
        ApiHelper.getService().search(keyword)
            .enqueue(new Callback<ApiHelper.Res>() {
                @Override
                public void onResponse(Call<ApiHelper.Res> call, Response<ApiHelper.Res> response) {
                    ApiHelper.Res res = response.body();
                    mDataList = res.items;
                    b.rv.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ApiHelper.Res> call, Throwable t) { }
            });
    }



    /** 어댑터 선언 및 뷰홀더 생성 **/
    /* ViewHolder */
    class ViewHolder extends RecyclerView.ViewHolder {
        ItemSearchBinding b;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            b = ItemSearchBinding.bind(itemView);
        }

        void updateWishState(SearchItem item) {
            boolean isWished = PrefHelper.isWish(requireContext(), item.getProductId()); // 위시리스트에 추가되어 있는지 확인
            b.ivWish.setImageResource(
                    isWished ? R.drawable.ic_baseline_favorite_24
                            : R.drawable.ic_baseline_favorite_border_24
            );
        }

        void insertItem(SearchItem item) {
            Log.d("insert", "insert");
            new DBAsync<SearchItem, Void, Void>(requireContext()) {

                @Override
                protected Void doInBackground(SearchItem... searchItems) {

                    for(SearchItem item : searchItems){
                        mDB.dao().insertItem(item);
                    }
                    return null;
                }
            }.execute(item);
        }

        void deleteItem(SearchItem item) {
            new DBAsync<SearchItem, Void, Void>(requireContext()) {

                @Override
                protected Void doInBackground(SearchItem... searchItems) {
                    for(SearchItem item : searchItems){
                        mDB.dao().deleteItem(item);
                    }
                    return null;
                }
            }.execute(item);
        }

        public void onBind(final SearchItem item) { // Adapter에서 바인딩해주기 위한 메서드
            b.tvTitle.setText(Html.fromHtml(item.getTitle()));
//            b.tvTitle.setSelected(true); // marquee를 위한 설정
            b.tvMallName.setText(item.getMallName());
            b.tvPrice.setText(DecimalFormat.getInstance().format(item.getLprice()) + "원");
            Glide.with(SearchFragment.this /*requireContext()*/)
                    .load(item.getImage())
                    .into(b.image);
            updateWishState(item);

            b.ivWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean bool = PrefHelper.isWish(requireContext(), item.getProductId());

                    if(!bool) { // insert
                        insertItem(item);
                    } else { // deletes
                        deleteItem(item);
                    }

                    PrefHelper.setWish(requireContext(), item.getProductId(), !bool);
                    updateWishState(item);
                }
            });


            /** 요소 클릭 시 링크로 이동 **/
            b.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                    startActivity(i);
                }
            });
        }
    }

    /** Adapter **/
    class SearchAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()/*parent.getContext()*/) // requireContext: fragment에 붙어있는 Activity의 Context 가져오기
                    .inflate(R.layout.item_search, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SearchItem item = mDataList.get(position);
            holder.onBind(item);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

}
