package com.itsolution.horizon;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter < MessageAdapter.MessageViewHolder > {
    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_BOT = 1;
    private List <com.itsolution.horizon.Message> mMessages;
    public MessageAdapter(List <com.itsolution.horizon.Message> messages) {
        mMessages = messages;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == VIEW_TYPE_USER) {
            view = inflater.inflate(R.layout.item_message_user, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_message_bot, parent, false);
        }
        return new MessageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        com.itsolution.horizon.Message message = mMessages.get(position);
        holder.bind(message);
    }
    @Override
    public int getItemCount() {
        return mMessages.size();
    }
    @Override
    public int getItemViewType(int position) {
        com.itsolution.horizon.Message message = mMessages.get(position);
        return message.isSentByUser() ? VIEW_TYPE_USER : VIEW_TYPE_BOT;
    }
    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_message_user);
        }
        public void bind(com.itsolution.horizon.Message message) {
            if (message.isSentByUser()) {
                mTextView = itemView.findViewById(R.id.text_message_user);
                mTextView.setText(message.getText());
            } else {
                mTextView = itemView.findViewById(R.id.text_message_bot);
                mTextView.setText(message.getText());
            }
        }
    }
}