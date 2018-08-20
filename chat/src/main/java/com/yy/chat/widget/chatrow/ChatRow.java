package com.yy.chat.widget.chatrow;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cjwsc.idcm.Utils.GlideApp;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.yy.chat.IMessage;
import com.yy.chat.MessageListAdapter;
import com.yy.chat.R;

import java.util.Date;


/**
 * @author yiyang
 */
public abstract class ChatRow<MessageBody> extends LinearLayout {

    private ImageView mIvAvatar;
    private View mLayoutBubble;
    private View mMsgStatus;
    private ImageView mProgressBar;
    private AnimationDrawable animationDrawable;
    private View mLayoutLoding;
    private View mPrecentage;
    private int position;
    private MessageListAdapter mAdapter;

    public interface ChatRowActionCallback {
        void onResendClick(IMessage message);

        void onBubbleClick(IMessage message);

        void onUserAvatarClick(IMessage message);
    }

    protected ChatRowActionCallback mCallback;

    //    protected  ChatMessage mMessage;
    protected final Context mContext;
    protected final View mView;

    protected MessageBody mMessageBody;
    protected IMessage mMessage;

    public ChatRow(Context context, @LayoutRes int res) {
        super(context);
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(res, this);
        mView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        initView();
//        mMessage = message;
//        mBody = (BODY) message.getBody();
    }

    private void initView() {
        mIvAvatar = (ImageView) findViewById(R.id.iv_userhead);
        mLayoutBubble = findViewById(R.id.bubble);
        mMsgStatus = findViewById(R.id.msg_status);
        mProgressBar = findViewById(R.id.progress_bar);
        mPrecentage = findViewById(R.id.percentage);
        mLayoutLoding = findViewById(R.id.ll_loading);
        onFindViewById();
    }

    public void setUpView(IMessage message, int pos, MessageListAdapter adapter, ChatRowActionCallback callback) {
        mMessage = message;
        position = pos;
        mAdapter = adapter;
        if (message.getBody() != null)
            mMessageBody = (MessageBody) message.getBody();
        mCallback = callback;
        setBaseView();
        onSetUpView(mMessageBody);
    }

    private void setBaseView() {
        refreshStatus();

        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        if (timestamp != null) {
            if (mMessage.timeStamp() < 1 || TextUtils.isEmpty(mMessage.getTime())) {
                timestamp.setVisibility(GONE);
            } else {
                if (position == 0) {
                    timestamp.setText(mMessage.getTime());
                    timestamp.setVisibility(View.VISIBLE);
                } else {
                    // show time stamp if interval with last message is > 30 seconds
                    IMessage prevMessage = (IMessage) mAdapter.getItem(position - 1);
                    if (prevMessage != null && isCloseEnough(mMessage.timeStamp(), prevMessage.timeStamp())) {
                        timestamp.setVisibility(View.GONE);
                    } else {
                        timestamp.setText(mMessage.getTime());
                        timestamp.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        if (mIvAvatar != null) {
            if (TextUtils.isEmpty(mMessage.getAvatar())) {

            } else {
                GlideApp.with(mContext).load(mMessage.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).into(mIvAvatar);
            }
        }
        setListener();
    }

    public static boolean isCloseEnough(long var0, long var2) {
        long var4 = var0 - var2;
        if (var4 < 0L) {
            var4 = -var4;
        }
//3分钟
        return var4 < /*30000L*/1000 * 60 * 3;
    }

    private void refreshStatus() {
        //消息状态
        int status = mMessage.getStatus();
        if (IMessage.STATUS_INPROGRESS == status) {
            if (mMsgStatus != null)
                mMsgStatus.setVisibility(GONE);
            if (mProgressBar != null) {
                animationDrawable = (AnimationDrawable) mProgressBar.getDrawable();
                mProgressBar.setVisibility(VISIBLE);
                if (animationDrawable != null) {
                    animationDrawable.start();
                }
            }
            if (mLayoutLoding != null)
                mLayoutLoding.setVisibility(VISIBLE);
            if (mPrecentage != null)
                mPrecentage.setVisibility(GONE);
        } else if (IMessage.STATUS_FAIL == status) {
            if (mMsgStatus != null)
                mMsgStatus.setVisibility(VISIBLE);
            if (mProgressBar != null) {
                mProgressBar.setVisibility(GONE);
                if (animationDrawable != null) {
                    animationDrawable.stop();
                }
            }

            if (mLayoutLoding != null)
                mLayoutLoding.setVisibility(GONE);
            if (mPrecentage != null)
                mPrecentage.setVisibility(GONE);
        } else {
            if (mMsgStatus != null)
                mMsgStatus.setVisibility(GONE);
            if (mProgressBar != null) {
                mProgressBar.setVisibility(GONE);
                if (animationDrawable != null) {
                    animationDrawable.stop();
                }
            }
            if (mLayoutLoding != null)
                mLayoutLoding.setVisibility(GONE);
            if (mPrecentage != null)
                mPrecentage.setVisibility(GONE);
        }
    }

    private void setListener() {
        if (mCallback == null) return;
        if (mIvAvatar != null) {
            mIvAvatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onUserAvatarClick(mMessage);
                }
            });
        }
        if (mMsgStatus != null) {
            mMsgStatus.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onResendClick(mMessage);
                }
            });
        }
        if (mLayoutBubble != null) {
            mLayoutBubble.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onBubbleClick(mMessage);
                }
            });
        }
    }

    public void onViewUpdate() {
        refreshStatus();
    }

    protected abstract void onFindViewById();


    public abstract void onSetUpView(MessageBody messageBody);

}
