package com.example.hawkergo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.R;
import com.example.hawkergo.models.Review;
import com.example.hawkergo.utils.DownloadImageTask;

import java.text.SimpleDateFormat;
import java.util.List;

public class IndividualStallAdapter extends RecyclerView.Adapter<IndividualStallAdapter.MyViewHolder> {

    List<Review> reviews;
    List<String> images;
    Context context;


    public IndividualStallAdapter(Context ct, List<Review> reviewsList, List<String> img) {
        context = ct;
        reviews = reviewsList;
        images = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (reviews.get(position).getName() != null) {
            holder.usernameText.setText(reviews.get(position).getName());
        } else {
            holder.usernameText.setText("Anonymous");
        }

        if(reviews != null && reviews.size()>0 && reviews.get(position).getStars() != null){
            holder.ratingText.setText(reviews.get(position).getStars().toString());
        }
        if(reviews != null && reviews.size()>0 && reviews.get(position).getComment() != null){
            holder.reviewTxt.setText(reviews.get(position).getComment());
        }
        if(images != null && images.size() > 0){
            DownloadImageTask task = new DownloadImageTask(holder.userImage, context);

            task.execute(images.get(position));
        }
        //set date format
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(reviews.get(position).getDateReviewed());
        holder.reviewDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView usernameText, ratingText, reviewTxt, reviewDate;
        ImageView userImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.userNameTV);
            ratingText = itemView.findViewById(R.id.reviewTV);
            reviewTxt = itemView.findViewById(R.id.userReviewTV);
            userImage = itemView.findViewById(R.id.imageView);
            reviewDate = itemView.findViewById(R.id.review_date);
        }
    }
}
