package com.example.carworkshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserReviewOwnerAdapter extends RecyclerView.Adapter<UserReviewOwnerAdapter.UserReviewOwnerViewHolder> {

    ArrayList<Owner_review> ownerReviews;
    Context c;
    View view;

    public UserReviewOwnerAdapter(Context c, ArrayList<Owner_review> ownerReviews) {
        this.ownerReviews = ownerReviews;
        this.c = c;
    }

    @NonNull
    @Override
    public UserReviewOwnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(c).inflate(R.layout.review_owner_list,parent,false);
        return new UserReviewOwnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReviewOwnerViewHolder holder, int position) {
        final Owner_review owner_review = ownerReviews.get(position);

        holder.userName.setText(owner_review.getUserName());
        holder.reviewDescription.setText(owner_review.getDescription());

        //Review Rating
        final float rating1 = Float.parseFloat(owner_review.getRating());
        holder.ratingBar.setRating(rating1);

    }

    @Override
    public int getItemCount() {
        return ownerReviews.size();
    }

    public static class UserReviewOwnerViewHolder extends RecyclerView.ViewHolder {

        TextView userName, rating, reviewDescription;
        RatingBar ratingBar;

        public UserReviewOwnerViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            reviewDescription = itemView.findViewById(R.id.reviewDescription);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
