package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.model.beans.OTCGetOtcTradeSettingBean;
import io.reactivex.Flowable;

// 获取OTC交易相关设置
public interface OTCGetOtcTradeSettingServices
        extends IProvider {
    Flowable<OTCGetOtcTradeSettingBean> getOtcTradeSetting();
}
