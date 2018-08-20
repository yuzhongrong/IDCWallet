package foxidcw.android.idcw.common.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.common.model.bean.CheckAppVersionBean;
import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public interface CheckVersionProviderServices extends IProvider {

    Flowable<CheckAppVersionBean> CheckVersionProvider();

}
