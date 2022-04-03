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
import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.utils.DownloadImageTask;

import java.util.List;

public class HawkerCentreAdapter extends RecyclerView.Adapter<HawkerCentreAdapter.HawkerCentreViewHolder> {
    private static final String TAG = "HawkerAdapter";

    private List<HawkerCentre> mHawkerCentres;
    private Context mContext;

    public HawkerCentreAdapter(Context context, List<HawkerCentre> stallList) {
        this.mContext = context;
        this.mHawkerCentres = stallList;
    }

    class HawkerCentreViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "HawkerCentreViewHolder";
        ImageView stallImage;
        TextView stallName;
        TextView stallAddress;
        TextView stallRating;
        TextView stallReviews;



        public HawkerCentreViewHolder(@NonNull View itemView) {
            super(itemView);


            Log.d(TAG, "HawkerCentreViewHolder: starts");
            this.stallName = itemView.findViewById(R.id.stall_name);
            this.stallAddress = itemView.findViewById(R.id.stall_address);
            this.stallRating = itemView.findViewById(R.id.rating_number);
            this.stallReviews = itemView.findViewById(R.id.review_number);
            this.stallImage = itemView.findViewById(R.id.stall_image);

            ImageView starIcon = itemView.findViewById(R.id.star_icon);

            // Removes rating and review views from hawker centre list
            starIcon.setVisibility(View.GONE);
            stallRating.setVisibility(View.GONE);
            stallReviews.setVisibility(View.GONE);



        }
    }

    @NonNull
    @Override
    public HawkerCentreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hawker_list_item, parent, false);
        return new HawkerCentreViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull HawkerCentreViewHolder holder, int position) {
        HawkerCentre centreItem = mHawkerCentres.get(position);

        holder.stallName.setText(centreItem.getName());
        holder.stallAddress.setText(centreItem.getAddress());

        new DownloadImageTask(holder.stallImage).execute(centreItem.getImageUrl());


    }


    @Override
    public int getItemCount() {
        if (mHawkerCentres == null) {
            return 0;
        } else {
            return mHawkerCentres.size();
        }
    }




}
