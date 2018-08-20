package foxidcw.android.idcw.foxcommon.provider.services;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface MobShareProviderServices extends IProvider{

      void share(String platform
            ,String title
            ,String url
            ,String text
            ,String imgpath);

}
