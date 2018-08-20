package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.params.PhraseDataReqAndResParam;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.presenter.interfaces ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/16 9:44
 **/

public interface BackPhraseContract {

    interface View extends BaseView {
        void updateRequestPhraseData(PhraseDataReqAndResParam phraseDataReqAndResBean);

        void updateRequestSavePhrase(Object obj);
    }

    interface Model extends BaseModel {
        Flowable<PhraseDataReqAndResParam> requestPhraseData(String lang);

        Flowable<Object> requestSavePhrase(PhraseDataReqAndResParam reqAndResBean);
    }

    abstract class BackPhrasePresenter extends BasePresenter<View, Model> {
        public abstract void requestPhraseData(String lang);

        public abstract void requestSavePhrase(PhraseDataReqAndResParam reqAndResBean);
    }

}
