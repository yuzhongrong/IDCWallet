package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/5/5.
 */

public interface OTCUploadImgProviderServices extends IProvider{
    Flowable<Object> requestUpload(List<String> path);
}
