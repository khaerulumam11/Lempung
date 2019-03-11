package com.sourcey.KauLempung.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sourcey.KauLempung.R;

public class ProdukViewHolder extends RecyclerView.ViewHolder {

    public TextView aa, bb,cc;

    public ImageView dd;

    public CardView cardViewPost;

    public ProdukViewHolder(View itemView) {
        super(itemView);

        aa = itemView.findViewById(R.id.tv_title_post);
        bb = itemView.findViewById(R.id.tv_post);
        cc = itemView.findViewById(R.id.detail);
        dd = itemView.findViewById(R.id.img_post);

        cardViewPost= itemView.findViewById(R.id.cardViewPost);
    }
}
