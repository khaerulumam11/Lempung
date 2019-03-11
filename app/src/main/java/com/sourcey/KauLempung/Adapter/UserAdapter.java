package com.sourcey.KauLempung.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sourcey.KauLempung.Admin.DetailProduk;
import com.sourcey.KauLempung.Admin.DetailUser;
import com.sourcey.KauLempung.Model.Produk;
import com.sourcey.KauLempung.Model.User;
import com.sourcey.KauLempung.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ItemViewHolder> {

    //definisi untuk ArrayList -> ini kita gunakan krena kita menggunkan list
    public List<User> mExampleList;

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

    public UserAdapter(Context context, List<User> exampleItems){

        mExampleList = exampleItems;
        mContext = context;

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

         final User currentItem = mExampleList.get(position);

//         String[] user = currentItem.user.split("@");
//
//        holder.mUsername.setText(user[0]);
        holder.mUsername.setText(currentItem.getName());
        holder.mTitlePost.setText(currentItem.getEmail());

        holder.cardViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kk = new Intent(mContext, DetailUser.class);
                kk.putExtra("user", currentItem.getUser());
                kk.putExtra("key", currentItem.getKey() );
                kk.putExtra("name", currentItem.getName());
                kk.putExtra("alamat", currentItem.getAlamat());
                kk.putExtra("nohp", currentItem.getNohp());
                kk.putExtra("id", currentItem.getId());
                kk.putExtra("image", currentItem.getImage());
                kk.putExtra("email", currentItem.getEmail());
                mContext.startActivity(kk);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
