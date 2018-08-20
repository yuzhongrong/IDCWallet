package com.idcg.idcw.model.bean;

import com.idcg.idcw.model.params.PhraseDataReqAndResParam;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpz on 2018/1/30.
 */

public class VerfifyListBean implements Serializable {

    private List<PhraseDataReqAndResParam.VerinfyWordBean> mVerify;

    public VerfifyListBean(List<PhraseDataReqAndResParam.VerinfyWordBean> verify) {
        mVerify = verify;
    }

    public List<PhraseDataReqAndResParam.VerinfyWordBean> getPhrase() {
        return mVerify;
    }
}
