package com.example.hawkergo.utils.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.R;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.utils.DownloadImageTask;

import java.util.List;

public class HawkerStallAdapter extends RecyclerView.Adapter<HawkerStallAdapter.HawkerStallViewHolder> {
    private static final String TAG = "HawkerStallAdapter";

    private List<HawkerStall> mHawkerStalls;
    private Context mContext;

    public HawkerStallAdapter(Context context, List<HawkerStall> stallList) {
        this.mContext = context;
        this.mHawkerStalls = stallList;
    }

    class HawkerStallViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "HawkerStallViewHolder";
        ImageView stallImage = null;
        TextView stallName = null;
        TextView stallAddress = null;
        TextView stallRating = null;
        TextView stallReviews = null;



        public HawkerStallViewHolder(@NonNull View itemView) {
            super(itemView);
            this.stallName = itemView.findViewById(R.id.stall_name);
            this.stallAddress = itemView.findViewById(R.id.stall_address);
            this.stallRating = itemView.findViewById(R.id.rating_number);
            this.stallReviews = itemView.findViewById(R.id.review_number);
            this.stallImage = itemView.findViewById(R.id.stall_image);
        }
    }

    @NonNull
    @Override
    public HawkerStallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hawker_list_item, parent, false);
        return new HawkerStallViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull HawkerStallViewHolder holder, int position) {
        HawkerStall stallItem = mHawkerStalls.get(position);


        holder.stallName.setText(stallItem.getName());
        holder.stallAddress.setText(stallItem.getAddress());
        Double avgRating = stallItem.getAverageReview();
        holder.stallRating.setText(avgRating != null && avgRating != 0.0 ? avgRating.toString() : "No Rating");

        if(stallItem.getImageUrls().size() > 0){
            DownloadImageTask task = new DownloadImageTask(holder.stallImage);
            for(String url : stallItem.getImageUrls()){
                if(url != null && url.trim().length()>0){
                    task.execute(url);
                    break;
                }
            }
        }

        if (stallItem.getReviews() != null) {
            int numReviews = stallItem.getReviews().size();
            if (numReviews == 1) {
                holder.stallReviews.setText("(" + numReviews + R.string.review  + ")");
            } else {
                holder.stallReviews.setText("("  + numReviews + R.string.reviews +")");
            }
        } else {
            StringBuilder formattedCount = null;
            if(stallItem.getReviewCount() != null && stallItem.getReviewCount() > 0){
                formattedCount = new StringBuilder();
                formattedCount.append("(");
                formattedCount.append(stallItem.getReviewCount().toString());
                formattedCount.append(" reviews)");
                holder.stallReviews.setText(formattedCount.toString());
            }else{
                holder.stallReviews.setText(R.string.no_reviews);
            }

        }

    }


    @Override
    public int getItemCount() {
        if (mHawkerStalls == null) {
            return 0;
        } else {
            return mHawkerStalls.size();
        }
    }



}
