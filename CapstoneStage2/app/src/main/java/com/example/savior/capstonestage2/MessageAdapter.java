package com.example.savior.capstonestage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.savior.capstonestage2.model.Message;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    private Context mContext;
    private List<Message> mMessages;
    public static Message messagesFavorite;


    public MessageAdapter(final Context context, List<Message> messages) {

        this.mContext = context;
        this.mMessages = messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);

        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, final int position) {
        messagesFavorite = mMessages.get(position);

        boolean isPhoto = mMessages.get(position).getPhotoUrl() != null;
        if (isPhoto) {
            holder.messageTextView.setVisibility(View.GONE);
            holder.photoImageView.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(mMessages.get(position).getPhotoUrl())
                    .into(holder.photoImageView);
        } else {
            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.photoImageView.setVisibility(View.GONE);
            holder.messageTextView.setText(mMessages.get(position).getText());
        }
        holder.authorTextView.setText(mMessages.get(position).getName());


    }

    @Override
    public int getItemCount() {
        if (mMessages == null) {
            return 0;
        }
        return mMessages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photoImageView)
        ImageView photoImageView;
        @BindView(R.id.messageTextView)
        TextView messageTextView;
        @BindView(R.id.nameTextView)
        TextView authorTextView;

        public MessageViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    public void clear() {
        int size = this.mMessages.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mMessages.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

}