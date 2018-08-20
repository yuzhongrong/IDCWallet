package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import io.reactivex.Flowable;

public interface OTCGetSignalrUrlServices extends IProvider {
    Flowable<Object> getSignalrUrl();
}
