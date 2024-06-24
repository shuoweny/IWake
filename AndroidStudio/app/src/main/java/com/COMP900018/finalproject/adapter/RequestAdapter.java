package com.COMP900018.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.data.AvatarTrans;
import com.COMP900018.finalproject.data.DatabaseApi;
import com.COMP900018.finalproject.data.Friend;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<Friend> friendList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public RequestAdapter(List<Friend> friendList) {
        this.friendList = friendList;
    }


    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        String nameDisplay = friend.getEmail() + "(" + friend.getName()+")";
        holder.name.setText(nameDisplay);
        holder.avatarImageView.setImageResource(AvatarTrans.getInstance().urlToId(friend.getAvatarUrl()));
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseApi.addFriend(friend.getuId());
                remove(holder.getAdapterPosition());
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseApi.rejectFriend(friend.getuId());
                remove(holder.getAdapterPosition());

            }
        });

    }

    public void remove(int position){
        friendList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        ImageView accept;
        ImageView reject;
        TextView name ;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.request_avatar);
            name = itemView.findViewById(R.id.nameTextView);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);

        }
    }
}

