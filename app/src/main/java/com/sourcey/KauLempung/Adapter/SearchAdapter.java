package com.sourcey.KauLempung.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourcey.KauLempung.Admin.DetailUser;
import com.sourcey.KauLempung.Model.User;
import com.sourcey.KauLempung.Model.UserSearch;
import com.sourcey.KauLempung.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemViewHolder> {

    //definisi untuk ArrayList -> ini kita gunakan krena kita menggunkan list
    ArrayList<UserSearch> arrayList;
//    ArrayList<String> emailList;

    //definis untuk context -> sama seperti 'this' kita gunakan untuk menunjukkan identitas kelas dimana dia berada
    // dan karena kita menggunakan intent maka perlu ada nya ini
    Context mContext;

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView mUsername;

        public TextView mTitlePost;

        public CardView cardViewPost;


        public  ItemViewHolder(View itemView){
            super(itemView);
            mUsername= itemView.findViewById(R.id.tv_username);

            mTitlePost = itemView.findViewById(R.id.tv_title_post);

            cardViewPost= itemView.findViewById(R.id.cardViewPost);

        }
    }

    public SearchAdapter(Context context, ArrayList<UserSearch> fullnameList){

        mContext = context;
        this.arrayList = fullnameList;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user,parent,false);
        ItemViewHolder evh = new ItemViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
         final UserSearch userSearch = arrayList.get(position);


//         String[] user = currentItem.user.split("@");
//
//        holder.mUsername.setText(user[0]);
        holder.mUsername.setText(userSearch.getName());
        holder.mTitlePost.setText(userSearch.getEmail());

        holder.cardViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kk = new Intent(mContext, DetailUser.class);
                kk.putExtra("user", userSearch.getUser());
                kk.putExtra("key", userSearch.getKey() );
                kk.putExtra("name", userSearch.getName());
                kk.putExtra("alamat", userSearch.getAlamat());
                kk.putExtra("nohp", userSearch.getNohp());
                kk.putExtra("id", userSearch.getId());
                kk.putExtra("image", userSearch.getImage());
                kk.putExtra("email", userSearch.getEmail());
                mContext.startActivity(kk);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
