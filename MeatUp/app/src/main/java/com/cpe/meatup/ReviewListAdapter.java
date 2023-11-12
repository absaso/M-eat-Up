package com.cpe.meatup;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cpe.meatup.databinding.ReviewItemBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    List<String> listReviews;

    public ReviewListAdapter(List<String> reviewList) {
        assert reviewList != null;
        this.listReviews=reviewList;
    }

    @NonNull
    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReviewItemBinding binding = DataBindingUtil.inflate( LayoutInflater.from(parent.getContext()), R.layout.review_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String review = listReviews.get(position);
        holder.binding.reviewText.setText(review);

    }

    @Override
    public int getItemCount() {
        return listReviews.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ReviewItemBinding binding;
        ViewHolder(ReviewItemBinding binding) { super(binding.getRoot());
            this.binding = binding; }
    }
}
