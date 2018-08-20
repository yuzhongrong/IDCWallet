package com.yy.chat.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;


import com.yy.chat.IMessage;
import com.yy.chat.MessageListAdapter;
import com.yy.chat.OnMessageChangeListener;
import com.yy.chat.R;
import com.yy.chat.widget.chatrow.CustomChatRowProvider;

/**
 *
 * @author yiyang
 */
public class MessageList extends FrameLayout implements OnMessageChangeListener {
    private RecyclerView mRecyclerView;
    private List<IMessage> mMessages;
    private LinearLayoutManager mLayoutManager;
    private MessageListAdapter mAdapter;
    private Context mContext;

    public MessageList(@NonNull Context context) {
        this(context, null);
    }

    public MessageList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.chat_message_list, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mMessages = new ArrayList<>();

        mAdapter = new MessageListAdapter(mMessages);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    public void setChatRowProvider(CustomChatRowProvider chatRowProvider){
        mAdapter.setChatRowProvider(chatRowProvider);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setData(List<IMessage> messages) {
        mMessages.clear();
        mMessages.addAll(messages);
        mAdapter.notifyDataSetChanged();
        refreshSelectLast();
    }

    public void addData(IMessage message) {
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshSelectLast();
            }
        },50);
//        if(message!=null){
//            message.handleMsg(mContext, this);
//        }
    }

    public void addData(List<IMessage> messages) {
        mMessages.addAll(messages);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshSelectLast();

            }
        }, 50);
    }

    public List<IMessage> getData(){
        return mAdapter.getData();
    }
    public MessageListAdapter getAdapter(){
        return mAdapter;
    }
    @Override
    public void onChanged(){
        mAdapter.notifyDataSetChanged();
    }

    private void refreshSelectLast() {
        refreshSelectLast(false);
    }

    public void refreshSelectLast(boolean isSmooth) {
        if (mMessages.size() < 1)
            return;
        if (isSmooth)
            mRecyclerView.smoothScrollToPosition(mMessages.size() - 1);
        else
            mLayoutManager.scrollToPositionWithOffset(mMessages.size() - 1, 0);
    }


    public void setItemClickListener(OnItemChildActionCallBack callback){
        mAdapter.setOnItemChildActionCallBack(callback);
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        IMessage.StatusHelper.clear();
    }

    public void clearAll(){

        if(mMessages!=null&&mMessages.size()>0){
            mMessages.clear();
            mAdapter.notifyDataSetChanged();

        }

    }

}
