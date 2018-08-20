package com.idcg.idcw.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpz on 2018/4/25.
 */

public class WalletListBean implements Serializable {
    private List<WalletAssetBean> mList;
    public WalletListBean(List<WalletAssetBean> list) {
        mList = list;
    }
    public List<WalletAssetBean> getWalletList(){
        return mList;
    }
}
