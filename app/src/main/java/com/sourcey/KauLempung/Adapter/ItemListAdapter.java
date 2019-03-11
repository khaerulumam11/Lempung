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

import com.sourcey.KauLempung.Admin.DaftarUser;
import com.sourcey.KauLempung.Admin.KatalogProduk;
import com.sourcey.KauLempung.Admin.ListPesanan;
import com.sourcey.KauLempung.Model.Item;
import com.sourcey.KauLempung.Model.Item2;
import com.sourcey.KauLempung.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {

    //definisi untuk ArrayList -> ini kita gunakan krena kita menggunkan list
    private ArrayList<Item2> mExampleList;

    //definis untuk context -> sama seperti 'this' kita gunakan untuk menunjukkan identitas kelas dimana dia berada
    // dan karena kita menggunakan intent maka perlu ada nya ini
    Context mContext;

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView imageView;
        public TextView mTextView;
        CardView cardView;
        Context mContext;
        public  ItemViewHolder(View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.cardLayout);
            imageView = itemView.findViewById(R.id.gambar);
            mTextView = itemView.findViewById(R.id.judul_list);
            Typeface customfont=Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-Regular.ttf");
            mTextView.setTypeface(customfont);

        }
    }

    public ItemListAdapter(Context context, ArrayList<Item2> exampleItems){

        mExampleList = exampleItems;
        mContext = context;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_admin,parent,false);
        ItemViewHolder evh = new ItemViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {

        final Item2 currentItem = mExampleList.get(position);

        holder.imageView.setImageResource(currentItem.getmImageResources());
        holder.mTextView.setText(currentItem.getText());
//        holder.cardView.setCardBackgroundColor(currentItem.getColor());
        int colorId = mExampleList.get(position).getColor();
        int color = holder.cardView.getContext().getResources().getColor(colorId);
        holder.cardView.setCardBackgroundColor(color);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(mContext, KatalogProduk.class);
                        mContext.startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(mContext, DaftarUser.class);
                        mContext.startActivity(intent1);
                        break;

                    case 2:
                        Intent intent2 = new Intent(mContext, ListPesanan.class);
                        mContext.startActivity(intent2);
                        break;
                }
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
