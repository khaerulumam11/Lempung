package com.sourcey.KauLempung.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sourcey.KauLempung.R;

public class UsersViewHolder extends RecyclerView.ViewHolder {

    public TextView aa, bb,cc;

    public CardView cardViewPost;

    public UsersViewHolder(View itemView) {
        super(itemView);

        aa = itemView.findViewById(R.id.tv_username);
        bb = itemView.findViewById(R.id.tv_title_post);

        cardViewPost= itemView.findViewById(R.id.cardViewPost);
    }
}
