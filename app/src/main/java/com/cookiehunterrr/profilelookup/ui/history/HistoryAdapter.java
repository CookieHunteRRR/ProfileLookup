package com.cookiehunterrr.profilelookup.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookiehunterrr.profilelookup.MainActivity;
import com.cookiehunterrr.profilelookup.R;
import com.cookiehunterrr.profilelookup.database.DBUser;
import com.cookiehunterrr.profilelookup.database.Database;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>
{
    private MainActivity activity;
    private Database db;
    private ArrayList<DBUser> users;

    public HistoryAdapter(MainActivity activity, Database db)
    {
        this.activity = activity;
        this.db = db;
        this.users = db.getCachedUsers();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.history_recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text_username.setText(users.get(position).getName());

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition();
                removeRow(currentPosition);
            }
        });

        Glide.with(activity)
                .load(users.get(position).getAvatarLink())
                .into(holder.image_avatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition();
                activity.requestUserFromHistory(users.get(currentPosition).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void removeRow(int index)
    {
        db.deleteUser(users.get(index).getId());
        users.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, users.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_username;
        ImageView image_avatar;
        Button button_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_username = itemView.findViewById(R.id.text_username_row);
            image_avatar = itemView.findViewById(R.id.image_avatar_row);
            button_delete = itemView.findViewById(R.id.button_delete_row);
        }
    }
}
