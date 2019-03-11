package com.sourcey.KauLempung.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sourcey.KauLempung.Model.Item;
import com.sourcey.KauLempung.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    //definisi untuk ArrayList -> ini kita gunakan krena kita menggunkan list
    private ArrayList<Item> mExampleList;

    //definis untuk context -> sama seperti 'this' kita gunakan untuk menunjukkan identitas kelas dimana dia berada
    // dan karena kita menggunakan intent maka perlu ada nya ini
    private Context mContext;

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView mTextView,mTextView2;
        LinearLayout linearLayout;
        public  ItemViewHolder(View itemView){
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout);
            imageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.judul);
            mTextView2 = itemView.findViewById(R.id.harga);
        }
    }

    public ItemAdapter(Context context,ArrayList<Item> exampleItems){

        mExampleList = exampleItems;
        mContext = context;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        ItemViewHolder evh = new ItemViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        final Item currentItem = mExampleList.get(position);

        holder.imageView.setImageResource(currentItem.getmImageResources());
        holder.mTextView.setText(currentItem.getText());
        holder.mTextView2.setText(currentItem.getText2());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext,DetailActivity.class);
//                intent.putExtra("gambar",String.valueOf(currentItem.getmImageResources()));
//                intent.putExtra("nama",currentItem.getText());
//                intent.putExtra("pekerjaan",currentItem.getText2());
//                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
