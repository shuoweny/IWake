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
import com.COMP900018.finalproject.data.Friend;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReactionAdapter extends RecyclerView.Adapter<ReactionAdapter.ReactionViewHolder> {

    private List<Friend> friendList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ReactionAdapter(List<Friend> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public ReactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reaction_item, parent, false);
        return new ReactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReactionViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        holder.name.setText(friend.getName());

        holder.avatarImageView.setImageResource(AvatarTrans.getInstance().urlToId(friend.getAvatarUrl()));

        if (friend.getReceiveReaction().equals("smile")){
            holder.reaction.setImageResource(R.drawable.smile);
        }else if (friend.getReceiveReaction().equals("star")){
            holder.reaction.setImageResource(R.drawable.superstar);
        }else if (friend.getReceiveReaction().equals("like")){
            holder.reaction.setImageResource(R.drawable.like);
        }

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    class ReactionViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView name ;

        ImageView reaction;

        public ReactionViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            name = itemView.findViewById(R.id.nameTextView);
            reaction =  itemView.findViewById(R.id.getRaction);

        }
    }
}

