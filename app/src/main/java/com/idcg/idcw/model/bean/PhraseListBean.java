package com.idcg.idcw.model.bean;

import com.idcg.idcw.model.params.PhraseDataReqAndResParam;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpz on 2018/1/24.
 */

public class PhraseListBean implements Serializable{
    private List<PhraseDataReqAndResParam.RandomWordBean> mPhrase;
    public PhraseListBean(List<PhraseDataReqAndResParam.RandomWordBean> phrase) {
        mPhrase = phrase;
    }
    public List<PhraseDataReqAndResParam.RandomWordBean> getPhrase(){
        return mPhrase;
    }

}
