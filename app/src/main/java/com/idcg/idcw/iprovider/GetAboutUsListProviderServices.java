package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.bean.AboutUsBean;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/5/30.
 */

public interface GetAboutUsListProviderServices extends IProvider {
    Flowable<List<AboutUsBean>> requestAboutUsListProvider(String lang);
}
