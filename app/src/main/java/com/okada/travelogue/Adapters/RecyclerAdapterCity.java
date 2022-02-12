package com.okada.travelogue.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.okada.travelogue.HelperClasses.CityClass;
import com.okada.travelogue.HelperClasses.GetImageFromURL;
import com.okada.travelogue.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class RecyclerAdapterCity extends RecyclerView.Adapter<RecyclerAdapterCity.CityHolder> {
    Map<Long, CityClass> cityClassMap;
    Context context;

    public RecyclerAdapterCity(Map<Long, CityClass> cityClassMap, Context context) {
        this.context = context;
        this.cityClassMap = cityClassMap;
    }

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycyler_country_item, parent, false);
        return new CityHolder(view);
    }

    static int a = 0;
    Map<Integer, Bitmap> map=new HashMap<>();

    @Override
    public void onBindViewHolder(@NonNull CityHolder holder, int position) {
        if (a == 6) {
            a = 1;
        } else a++;

        if (cityClassMap.size() > 0) {

            if (!map.containsKey(position)){
                holder.imageView.setVisibility(View.VISIBLE);
                //holder.gifImageView.setVisibility(View.VISIBLE);
                Picasso.get().load(cityClassMap.get(Long.parseLong(position+"")).getImageUrl()).into(holder.imageView);
                //new GetImageFromURL(holder.imageView, holder.gifImageView,map,position).execute(cityClassMap.get(Long.parseLong(position + "")).getImageUrl());
            }else {
                holder.imageView.setImageBitmap(map.get(position));
            }

            holder.ratingBar.setRating(cityClassMap.get(Long.parseLong(position + "")).getRate());
            holder.textName.setText(cityClassMap.get(Long.parseLong(position + "")).getName());
            holder.textDesc.setText(cityClassMap.get(Long.parseLong(position + "")).getDescription());
            switch (a) {
                case 1:
                    holder.cityCardView.setCardBackgroundColor(Color.parseColor("#caf7e3"));
                    break;
                case 2:
                    holder.cityCardView.setCardBackgroundColor(Color.parseColor("#f39189"));
                    break;
                case 3:
                    holder.cityCardView.setCardBackgroundColor(Color.parseColor("#edffec"));
                    break;
                case 4:
                    holder.cityCardView.setCardBackgroundColor(Color.parseColor("#f2edd7"));
                    break;
                case 5:
                    holder.cityCardView.setCardBackgroundColor(Color.parseColor("#a2b29f"));
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return cityClassMap.size();
    }

    class CityHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RatingBar ratingBar;
        TextView textName, textDesc;
        CardView cityCardView;
        GifImageView gifImageView;

        public CityHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_city_image);
            ratingBar = itemView.findViewById(R.id.item_rating_bar);
            textName = itemView.findViewById(R.id.item_city_name);
            textDesc = itemView.findViewById(R.id.item_city_desc);
            cityCardView = itemView.findViewById(R.id.item_city_card_view);
            gifImageView = itemView.findViewById(R.id.gif_loading);
        }
    }
}
