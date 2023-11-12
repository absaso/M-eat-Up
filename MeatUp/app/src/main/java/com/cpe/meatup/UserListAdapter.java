package com.cpe.meatup;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cpe.meatup.databinding.ReviewItemBinding;
import com.cpe.meatup.databinding.UserItemBinding;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{

    List<User> listUsers;

    public UserListAdapter(List<User> userList) {
        assert userList != null;
        this.listUsers=userList;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemBinding binding = DataBindingUtil.inflate( LayoutInflater.from(parent.getContext()), R.layout.user_item, parent, false);
        return new UserListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        User user = listUsers.get(position);
        holder.binding.nameText.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private UserItemBinding binding;
        ViewHolder(UserItemBinding binding) { super(binding.getRoot());
            this.binding = binding; }
    }
}
