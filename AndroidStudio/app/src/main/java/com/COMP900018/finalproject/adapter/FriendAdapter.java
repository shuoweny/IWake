package com.COMP900018.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.data.AvatarTrans;
import com.COMP900018.finalproject.data.DatabaseApi;
import com.COMP900018.finalproject.data.Friend;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<Friend> friendList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FriendAdapter(List<Friend> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        holder.nameTextView.setText(friend.getName());
        holder.statusTextView.setText(friend.getStatus());
        holder.avatarImageView.setImageResource(AvatarTrans.getInstance().urlToId(friend.getAvatarUrl()));
        if (friend.getGivenReaction().equals("smile")){
            holder.actionImageView.setImageResource(R.drawable.smile);
        }else if (friend.getGivenReaction().equals("star")){
            holder.actionImageView.setImageResource(R.drawable.superstar);
        }else if (friend.getGivenReaction().equals("like")){
            holder.actionImageView.setImageResource(R.drawable.like);
        }

//        if (friend.getReceiveReaction().equals("smile")){
//            holder.receiveReaction.setImageResource(R.drawable.smile);
//        }else if (friend.getReceiveReaction().equals("star")){
//            holder.receiveReaction.setImageResource(R.drawable.superstar);
//        }else if (friend.getReceiveReaction().equals("like")){
//            holder.receiveReaction.setImageResource(R.drawable.like);
//        }

        List<ImageView> reactions = new ArrayList<>();
        reactions.add(holder.smile);
        reactions.add(holder.star);
        reactions.add(holder.like);
        for (ImageView reaction: reactions){
            reaction.setOnClickListener(v -> {
                holder.reactionBar.setVisibility(View.GONE);
                holder.actionImageView.setImageDrawable(reaction.getDrawable());
                String idString = reaction.getResources().getResourceEntryName(reaction.getId());
                if (idString.equals("smile")){
                    friend.setGivenReaction("smile");
                }else if (idString.equals("star")){
                    friend.setGivenReaction("star");
                }else if (idString.equals("positive_vote")){
                    friend.setGivenReaction("like");
                }

//                //TODO: update data in database according to new image
                DatabaseApi.updateReaction(friend, db);
            });
        }

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView nameTextView;
        TextView statusTextView;
        ImageView actionImageView;
//        ImageView receiveReaction;

        ImageView smile;
        ImageView star;
        ImageView like;

        ConstraintLayout reactionBar = itemView.findViewById(R.id.reactionBar);
        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            actionImageView = itemView.findViewById(R.id.actionImageView);
//            receiveReaction = itemView.findViewById(R.id.recivedReaction);
            actionImageView.setOnClickListener(v -> {
                reactionBar.setVisibility(View.VISIBLE);
            });
            smile = itemView.findViewById(R.id.smile);
            star = itemView.findViewById(R.id.star);
            like = itemView.findViewById(R.id.positive_vote);

        }
    }
}

