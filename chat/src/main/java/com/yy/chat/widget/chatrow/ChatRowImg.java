package com.yy.chat.widget.chatrow;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.cjwsc.idcm.Utils.GlideUtil;
import com.yy.chat.ImgMessageBody;
import com.yy.chat.R;


/**
 *
 * @author yiyang
 */
public class ChatRowImg extends ChatRow<ImgMessageBody> {


    private final int mSize;
    private ImageView mTvContent;

    public ChatRowImg(Context context, @LayoutRes int res) {
        super(context, res);
        mSize = mContext.getResources().getDimensionPixelSize(R.dimen.dp200);
    }

    @Override
    protected void onFindViewById() {
        mTvContent = mView.findViewById(R.id.image);
    }

    @Override
    public void onSetUpView(ImgMessageBody textMessageBody) {
//        GlideUtil.loadImageViewLodingSize(mContext, textMessageBody.getUrl(), mSize, mSize, mTvContent,0,0);
        GlideUtil.loadImageViewContentWithSizeFixRatio(mContext, textMessageBody.getUrl(), 2f, mSize, mSize, mTvContent);
//        String url = textMessageBody.getUrl();
//        if(url.contains("http")) {
//            Glide.with(mContext).load(url).override(mSize, mSize).into(mTvContent);
//        }else {
//            Glide.with(mContext).load(new File(url)).override(mSize, mSize).into(mTvContent);
//        }
    }
}
