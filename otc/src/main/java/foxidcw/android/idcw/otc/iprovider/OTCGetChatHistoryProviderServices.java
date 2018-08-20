package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.util.List;
import foxidcw.android.idcw.otc.model.beans.OTCChatHistoryBean;
import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/5/5.
 */

public interface OTCGetChatHistoryProviderServices extends IProvider{
    Flowable<List<OTCChatHistoryBean>> requestChatHistory(String groupid);
}
