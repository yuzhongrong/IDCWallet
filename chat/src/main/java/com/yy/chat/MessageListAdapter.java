package com.yy.chat;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.yy.chat.widget.OnItemChildActionCallBack;
import com.yy.chat.widget.chatrow.ChatRow;
import com.yy.chat.widget.chatrow.ChatRowImg;
import com.yy.chat.widget.chatrow.ChatRowText;
import com.yy.chat.widget.chatrow.CustomChatRowProvider;


/**
 *
 * @author yiyang
 */
public class MessageListAdapter extends BaseMultiItemQuickAdapter<IMessage, MessageListAdapter.MessageHolder> {

    private CustomChatRowProvider mChatRowProvider;

    public void setChatRowProvider(CustomChatRowProvider chatRowProvider){
        mChatRowProvider = chatRowProvider;
    }

    public MessageListAdapter(@Nullable List<IMessage> data) {
        super(data);
    }

    @Override
    protected int getDefItemViewType(int position) {

        int viewType = super.getDefItemViewType(position);
        if(viewType>0){
            if(mChatRowProvider==null)
                throw new  IllegalArgumentException("viewType大于0时必须设置ChatRowProvider");
            return viewType +IMessage.DEFAULT_ITEM_TYPE_COUNT;
        }
        IMessage item = getItem(position);
        Object body = item.getBody();
        if (body instanceof TextMessageBody) {
            return item.isDirction() ? IMessage.TYPE_TEXT_SEND : IMessage.TYPE_TEXT_RECV;
        }
        if (body instanceof ImgMessageBody) {
            return item.isDirction() ? IMessage.TYPE_IMG_SEND : IMessage.TYPE_IMG_RECV;
        }
        return IMessage.TYPE_NEW;
//        if(mChatRowProvider!=null) {
//            int customChatRowType = mChatRowProvider.getCustomChatRowType(getItem(position));
//            if(customChatRowType>0)
//                return IMessage.DEFAULT_ITEM_TYPE_COUNT+ customChatRowType;
//        }
//        return super.getDefItemViewType(position);
    }

    @Override
    protected MessageHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        ChatRow chatRow;
        if(mChatRowProvider!=null){
            chatRow = mChatRowProvider.getCustomChatRow(viewType-IMessage.DEFAULT_ITEM_TYPE_COUNT);
            if(chatRow != null) {
                return new MessageHolder(chatRow);
            }
        }
        if (viewType == IMessage.TYPE_TEXT_SEND) {
            chatRow = new ChatRowText(mContext, R.layout.row_text_send);
        } else if (viewType == IMessage.TYPE_TEXT_RECV) {
            chatRow = new ChatRowText(mContext, R.layout.row_text_recv);
        } else if (viewType == IMessage.TYPE_IMG_SEND) {
            chatRow = new ChatRowImg(mContext, R.layout.row_img_send);
        } else if (viewType == IMessage.TYPE_IMG_RECV) {
            chatRow = new ChatRowImg(mContext, R.layout.row_img_recv);
        } else {
            chatRow = new ChatRow(mContext, android.R.layout.simple_list_item_1) {

                private TextView mViewById;

                @Override
                protected void onFindViewById() {
//                    mViewById = mView.findViewById(android.R.id.text1);
                }

                @Override
                public void onSetUpView(Object o) {
//                    mViewById.setText("not currently supported!");

                }
            };
        }

        return new MessageHolder(chatRow);
    }

    class MessageHolder extends BaseViewHolder implements IMessage.StatusHelper.StatusCallBack {

        private IMessage mMessage;

        MessageHolder(ChatRow view) {
            super(view);
        }

        void setUpView(IMessage message, ChatRow.ChatRowActionCallback callback) {
            mMessage = message;
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
            if(getAdapterPosition()==getData().size()-1){
                int paddingBottom = mContext.getResources().getDimensionPixelSize(R.dimen.dp10);
                if(layoutParams.bottomMargin != paddingBottom) {
                    layoutParams.bottomMargin = paddingBottom;
                    itemView.setLayoutParams(layoutParams);
                }
            }else {
                if(layoutParams.bottomMargin != 0) {
                    layoutParams.bottomMargin = 0;
                    itemView.setLayoutParams(layoutParams);
                }
            }
            getChatRow().setUpView(message, getAdapterPosition(), MessageListAdapter.this, callback);
            handleMessage();
        }

        private void handleMessage(){
            int status = mMessage.getStatus();
            if(status == IMessage.STATUS_SUCCESS || status == IMessage.STATUS_FAIL)return;
            IMessage.StatusHelper.get(mMessage).setStatusCallBack(this);
            if(status == IMessage.STATUS_INPROGRESS)return;
            mMessage.handleMsg(mContext, MessageListAdapter.this);
        }

        protected ChatRow getChatRow(){
            return (ChatRow)itemView;
        }

        @Override
        public void onSuccess() {
            getChatRow().onViewUpdate();
        }

        @Override
        public void onError() {
            getChatRow().onViewUpdate();
        }

        @Override
        public void onProgress(int progress) {
            getChatRow().onViewUpdate();
        }
    }

    private OnItemChildActionCallBack mOnItemChildActionCallBack;
    public void setOnItemChildActionCallBack(OnItemChildActionCallBack callback){
        mOnItemChildActionCallBack = callback;
    }

    @Override
    protected void convert(MessageHolder helper, IMessage item) {
        helper.setUpView(item, new ChatRow.ChatRowActionCallback() {
            @Override
            public void onResendClick(IMessage message) {
                if(mOnItemChildActionCallBack !=null)
                    mOnItemChildActionCallBack.onResendClick(message);
            }

            @Override
            public void onBubbleClick(IMessage message) {
                if(mOnItemChildActionCallBack !=null)
                    mOnItemChildActionCallBack.onBubbleClick(message, helper.getAdapterPosition());
            }

            @Override
            public void onUserAvatarClick(IMessage message) {
                if(mOnItemChildActionCallBack !=null)
                    mOnItemChildActionCallBack.onUserAvatarClick(message);
            }
        });
    }
}
