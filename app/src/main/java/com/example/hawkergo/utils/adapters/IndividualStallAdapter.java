package com.example.hawkergo.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.R;

public class IndividualStallAdapter extends RecyclerView.Adapter<IndividualStallAdapter.MyViewHolder> {

    String data1[], data2[], data3[];
    int images[];
    Context context;

    public IndividualStallAdapter(Context ct, String s1[], String s2[], String s3[], int img[]) {
        context = ct;
        data1 = s1;
        data2 = s2;
        data3 = s3;
        images = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.usernameText.setText(data1[position]);
        holder.ratingText.setText(data2[position]);
        holder.reviewTxt.setText(data3[position]);
        holder.userImage.setImageResource(images[position]);

    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView usernameText, ratingText, reviewTxt;
        ImageView userImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.userNameTV);
            ratingText = itemView.findViewById(R.id.reviewTV);
            reviewTxt = itemView.findViewById(R.id.userReviewTV);
            userImage = itemView.findViewById(R.id.imageView);

        }
    }
}
