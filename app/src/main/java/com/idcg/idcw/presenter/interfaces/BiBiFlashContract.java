package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.bean.CoinExchangeRateBean;
import com.idcg.idcw.model.bean.CoinPairBean;
import com.idcg.idcw.model.params.BIBIHelpReqParam;
import com.idcg.idcw.model.params.CoinRateReqParam;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import java.util.List;

import io.reactivex.Flowable;


public interface BiBiFlashContract {
    interface View extends BaseView {
        void updateCoinList(List<CoinPairBean> coinBeans);

        void updateRate(CoinExchangeRateBean coinExchangeRateBean);
        void updateHelpString(String helpString);
    }

    interface Model extends BaseModel {
        Flowable<List<CoinPairBean>> requestCoinList();
        Flowable<CoinExchangeRateBean> requestCoinRate(CoinRateReqParam param);
        Flowable<String> requestHelpString(BIBIHelpReqParam param);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void requestCoinList(boolean isBackground);
        public abstract void requestCoinRate(CoinRateReqParam param, boolean isBackground);
        public abstract void requestHelpString(BIBIHelpReqParam param);
    }
}
