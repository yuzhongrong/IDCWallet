package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetChatHistoryProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCChatHistoryBean;
import io.reactivex.Flowable;
import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCGetChatHistoryServices;

/**
 * Created by yuzhongrong on 2018/5/7.
 */
@Route(path = path_OTCGetChatHistoryServices)
public class OTCGetChatHistoryLogic implements OTCGetChatHistoryProviderServices {
    private Context mContext;
    @Override
    public Flowable<List<OTCChatHistoryBean>> requestChatHistory(String groupid) {
       return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .getChatHistory(groupid)
                .compose(new DefaultTransformer<List<OTCChatHistoryBean>>());
    }

    @Override
    public void init(Context context) {
        mContext=context;
    }
}
