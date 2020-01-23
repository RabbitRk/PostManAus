package com.rabbitt.smarttech.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rabbitt.smarttech.Fragment.MainFragment;
import com.rabbitt.smarttech.MemberActivity;
import com.rabbitt.smarttech.R;

import java.util.List;

public class MTimeLineAdapter extends RecyclerView.Adapter<MTimeLineAdapter.holder> {

    private static final String TAG = "CompanyListing";
    private ImageView item_image;
    private List<RecycleAdapter> dataModelArrayList;
    private MemberActivity context;
    private OnRecycleItemListener mOnRecycleItemListener;

    public MTimeLineAdapter(List<RecycleAdapter> productAdapter, MemberActivity context, OnRecycleItemListener onRecycleItemListener) {
        this.dataModelArrayList = productAdapter;
        this.context = context;
        this.mOnRecycleItemListener = onRecycleItemListener;
        Log.i(TAG, dataModelArrayList.toString());
    }

    @NonNull
    @Override
    public MTimeLineAdapter.holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.company_listing, null);
        return new holder(view, mOnRecycleItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int i) {
        RecycleAdapter dataModel = dataModelArrayList.get(i);

        Log.i(TAG, "" + i);
        Log.i(TAG, dataModel.getItem_date());

        //Load text
        holder.item_date.setText(dataModel.getItem_date());
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }


    public interface OnRecycleItemListener {
        void OnItemClick(int position);
    }

    class holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView item_date;
        OnRecycleItemListener onRecycleItemListener;

        holder(@NonNull View itemView, OnRecycleItemListener onRecycleItemListener) {
            super(itemView);
            this.onRecycleItemListener = onRecycleItemListener;
            item_date = itemView.findViewById(R.id.timeline_txt);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecycleItemListener.OnItemClick(getAdapterPosition());
        }
    }
}
