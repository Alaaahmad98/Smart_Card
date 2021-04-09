package com.example.smartcard.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcard.R;
import com.example.smartcard.activities.CategoryActivity;
import com.example.smartcard.activities.TypeActivity;
import com.example.smartcard.activities.admin.AdminHomeActivity;
import com.example.smartcard.activities.admin.CardInfoActivity;
import com.example.smartcard.helper.AdminHomeHelper;
import com.example.smartcard.helper.NumberCardHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NumberCardAdapter extends RecyclerView.Adapter<NumberCardAdapter.ViewHolder> {

    public Context context;
    public List<NumberCardHelper> list;


    public NumberCardAdapter(Context context, List<NumberCardHelper> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_item_card_number, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final NumberCardHelper helper = list.get(position);

        holder.tvNumberCard.setText(helper.getNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView tvNumberCard;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumberCard = itemView.findViewById(R.id.tv_number_card);


        }
    }
}
