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
import com.sourcey.KauLempung.Admin.KonfirmasiPembayaranAdmin;
import com.sourcey.KauLempung.Model.Pesanan;
import com.sourcey.KauLempung.R;
import com.sourcey.KauLempung.User.KonfirmasiPembayaran;

import java.util.List;

public class ListPesananAdapter extends RecyclerView.Adapter<ListPesananAdapter.ItemViewHolder> {

    //definisi untuk ArrayList -> ini kita gunakan krena kita menggunkan list
    public List<Pesanan> mExampleList;

    //definis untuk context -> sama seperti 'this' kita gunakan untuk menunjukkan identitas kelas dimana dia berada
    // dan karena kita menggunakan intent maka perlu ada nya ini
    Context mContext;

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView mJudulProduk;

        public TextView mJumlahPesanan;
        public TextView mTanggalPesan;
        public TextView mStatusPesanan;
        public ImageView mGambarProduk;

        public CardView cardViewPost;


        public  ItemViewHolder(View itemView){
            super(itemView);
            mJudulProduk= itemView.findViewById(R.id.nama_produk);

            mJumlahPesanan = itemView.findViewById(R.id.tv_jmlhpesanan);
            mTanggalPesan = itemView.findViewById(R.id.tv_tglpesan);
            mStatusPesanan = itemView.findViewById(R.id.tv_statuspesan);
            mGambarProduk = itemView.findViewById(R.id.gambar_produk);

            cardViewPost= itemView.findViewById(R.id.cardViewPost);

        }
    }

    public ListPesananAdapter(Context context, List<Pesanan> exampleItems){

        mExampleList = exampleItems;
        mContext = context;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_keranjang1,parent,false);
        ItemViewHolder evh = new ItemViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {

         final Pesanan currentItem = mExampleList.get(position);

//         String[] user = currentItem.user.split("@");
//
//        holder.mUsername.setText(user[0]);
        holder.mJudulProduk.setText(currentItem.getNamaproduk());
        holder.mJumlahPesanan.setText(currentItem.getJumlahpesanan());
        holder.mTanggalPesan.setText(currentItem.getTglpesan());
        holder.mStatusPesanan.setText(currentItem.getStatus());
        holder.mTanggalPesan.setTag(currentItem.getGambar());

        Glide.with(mContext).load(currentItem.getGambar()).into(holder.mGambarProduk);

        holder.cardViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kk = new Intent(mContext, KonfirmasiPembayaranAdmin.class);
                kk.putExtra("user", currentItem.getUser());
                kk.putExtra("key", currentItem.getKey() );
                kk.putExtra("namaproduk", currentItem.getNamaproduk());
                kk.putExtra("tanggalpesanan", currentItem.getTglpesan());
                kk.putExtra("jumlahpesanan", currentItem.getJumlahpesanan());
                kk.putExtra("hargaproduk", currentItem.getHargaproduk());
                kk.putExtra("id", currentItem.getId());
                kk.putExtra("image", currentItem.getGambar());
                kk.putExtra("total", currentItem.getTotal());
                kk.putExtra("status", currentItem.getStatus());
                kk.putExtra("bukti", currentItem.getBukti());
                mContext.startActivity(kk);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
