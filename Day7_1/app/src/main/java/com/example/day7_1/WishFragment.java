package com.example.day7_1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.day7_1.databinding.FragmentWishBinding;
import com.example.day7_1.databinding.ItemSearchBinding;
import com.example.day7_1.db.SearchItem;
import com.example.day7_1.helper.DBAsync;
import com.example.day7_1.helper.PrefHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class WishFragment extends Fragment {

    FragmentWishBinding b;
    List<SearchItem> mItemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        b = FragmentWishBinding.inflate(inflater);

        return b.getRoot();
    }

    /** 로직 처리 구간 **/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        b.rv.setAdapter(new WishAdapter()); // Adapter 지정

        new DBAsync<Void, Void, List<SearchItem>>(requireContext()) {

            @Override
            protected List<SearchItem> doInBackground(Void... voids) {
                return  mDB.dao().getAll(); //불러오기 및 데이터 반환
            }

            @Override
            protected void onPostExecute(List<SearchItem> searchItems) {
                super.onPostExecute(searchItems);
                //TODO: RecyclerView 업데이트
                mItemList = searchItems;
                b.rv.getAdapter().notifyDataSetChanged();
            }
        }.execute();
    }

    /** ViewHolder **/
    class ViewHolder extends RecyclerView.ViewHolder {

        ItemSearchBinding iB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iB = ItemSearchBinding.bind(itemView);
        }

        void updateWishState(SearchItem item) {
            boolean isWished = PrefHelper.isWish(requireContext(), item.getProductId()); // 위시리스트에 추가되어 있는지 확인
            iB.ivWish.setImageResource(
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
            iB.tvTitle.setText(Html.fromHtml(item.getTitle()));
//            b.tvTitle.setSelected(true); // marquee를 위한 설정
            iB.tvMallName.setText(item.getMallName());
            iB.tvPrice.setText(DecimalFormat.getInstance().format(item.getLprice()) + "원");
            Glide.with(WishFragment.this /*requireContext()*/)
                    .load(item.getImage())
                    .into(iB.image);
            updateWishState(item);

            iB.ivWish.setOnClickListener(new View.OnClickListener() {
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
                    b.rv.getAdapter().notifyItemRemoved(mItemList.indexOf(item));
                    mItemList.remove(item);

                }
            });


            /** 요소 클릭 시 링크로 이동 **/
            iB.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                    startActivity(i);
                }
            });
        }
    }


    /** Adapter **/
    class WishAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_search ,parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.onBind(mItemList.get(position));
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }
}
