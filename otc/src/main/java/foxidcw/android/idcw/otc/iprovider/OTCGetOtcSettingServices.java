package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.model.beans.OTCGetOtcSettingBean;
import io.reactivex.Flowable;

// OTC配置信息
public interface OTCGetOtcSettingServices
        extends IProvider {
    Flowable<OTCGetOtcSettingBean> getOtcSetting();
}
