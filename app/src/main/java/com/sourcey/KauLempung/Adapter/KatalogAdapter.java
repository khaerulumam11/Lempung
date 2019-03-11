package com.sourcey.KauLempung.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sourcey.KauLempung.Admin.DetailProduk;
import com.sourcey.KauLempung.Admin.KatalogProduk;
import com.sourcey.KauLempung.Model.Item2;
import com.sourcey.KauLempung.Model.Produk;
import com.sourcey.KauLempung.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class KatalogAdapter extends RecyclerView.Adapter<KatalogAdapter.ItemViewHolder> {

    //definisi untuk ArrayList -> ini kita gunakan krena kita menggunkan list
    public List<Produk> mExampleList;

    //definis untuk context -> sama seperti 'this' kita gunakan untuk menunjukkan identitas kelas dimana dia berada
    // dan karena kita menggunakan intent maka perlu ada nya ini
    Context mContext;

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView mUsername;

        public TextView mTitlePost;

        public TextView mPost;

        public ImageView mImagePost;

        public CardView cardViewPost;


        public  ItemViewHolder(View itemView){
            super(itemView);
            mUsername= itemView.findViewById(R.id.tv_username);

            mTitlePost = itemView.findViewById(R.id.tv_title_post);

            mPost = itemView.findViewById(R.id.tv_post);

            mImagePost=itemView.findViewById(R.id.img_post);

            cardViewPost= itemView.findViewById(R.id.cardViewPost);

        }
    }

    public KatalogAdapter(Context context, List<Produk> exampleItems){

        mExampleList = exampleItems;
        mContext = context;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post,parent,false);
        ItemViewHolder evh = new ItemViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {

         Produk currentItem = mExampleList.get(position);

//         String[] user = currentItem.user.split("@");
//
//        holder.mUsername.setText(user[0]);
        holder.mUsername.setText(currentItem.getNamaprod());
        holder.mTitlePost.setText("Nama Produk : "+currentItem.getTitle());
        holder.mPost.setText("Rp. "+currentItem.getHarga());
        holder.mPost.setTag(currentItem.getImage());

        Glide.with(mContext).load(currentItem.getImage()).into(holder.mImagePost);

        holder.cardViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kk = new Intent(mContext, DetailProduk.class);
                mContext.startActivity(kk);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
