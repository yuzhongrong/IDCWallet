package com.idcg.idcw.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.idcg.idcw.R;
import com.cjwsc.idcm.Utils.QRCodeUtil;
import com.cjwsc.idcm.Utils.ScreenUtil;

import java.io.File;

/**
 * Created by hpz on 2018/7/3.
 */

public class MyInvitationCodeDiaLog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Bitmap mZxBitmap;
    private String filePath;
    private String mInvitationUrl;
    private String title;
    private String subTitle;

    public MyInvitationCodeDiaLog(@NonNull Context context, String invitationUrl,String title,String subTitle) {
        this(context,0);
        this.mInvitationUrl=invitationUrl;
        this.title=title;
        this.subTitle=subTitle;
    }

    public MyInvitationCodeDiaLog(@NonNull Context context, int themeResId) {
        super(context, R.style.pay_dialog_type);
        mContext =context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取屏幕manager
        WindowManager manager =((Activity)mContext).getWindowManager();
        //获取屏幕的宽高
        Display display = manager.getDefaultDisplay();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_myinvitation_code, null);
        inflate.setMinimumWidth((int)(display.getWidth()*0.92f));
        setContentView(inflate);
        ImageView inVitationIV=inflate.findViewById(R.id.iv_invitation_zxing);
        TextView tv_invitation_title=inflate.findViewById(R.id.tv_invitation_title);
        TextView tv_sub_title=inflate.findViewById(R.id.tv_sub_title);
        tv_invitation_title.setText(title);
        tv_sub_title.setText(subTitle);
        filePath = QRCodeUtil.getFileRoot(mContext) + File.separator
                + "invitation" + System.currentTimeMillis() + ".png";
        mZxBitmap = QRCodeUtil.createQRImage(mInvitationUrl, ScreenUtil.dp2px(280, mContext), ScreenUtil.dp2px(280, mContext), null, filePath);
        inVitationIV.setImageBitmap(mZxBitmap);
        ImageView iVClose=inflate.findViewById(R.id.iv_close);
        iVClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
