package foxidcw.android.idcw.common.model.bean;

import java.io.Serializable;

/**
 *
 * @project name : FoxIDCW
 * @class name :   OTCDotShowBean
 * @package name : com.idcg.idcw.model.bean
 * @author :       MayerXu10000@gamil.com
 * @date :         2018/6/13 19:26
 * @describe :     TODO
 *
 */
public class OTCDotShowBean implements Serializable{
    private boolean isShow;

    public OTCDotShowBean(){}

    public OTCDotShowBean(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
