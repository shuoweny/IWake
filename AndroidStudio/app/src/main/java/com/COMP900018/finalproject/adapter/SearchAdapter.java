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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<Friend> friendList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public SearchAdapter(List<Friend> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        String nameDisplay = friend.getEmail() + "(" + friend.getName()+")";
        holder.name.setText(nameDisplay);
        holder.avatarImageView.setImageResource(AvatarTrans.getInstance().urlToId(friend.getAvatarUrl()));

        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseApi.sentRequest(friend.getuId());
                holder.send.setImageResource(R.drawable.add_to_friend);
            }
        });

    }

    public int getItemCount() {
        return friendList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        ImageView accept;
        ImageView send;
        TextView name ;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.request_avatar);
            name = itemView.findViewById(R.id.nameTextView);
            accept = itemView.findViewById(R.id.accept);
            send = itemView.findViewById(R.id.reject);
            accept.setVisibility(View.INVISIBLE);
            send.setImageResource(R.drawable.send_request);

        }
    }
}
