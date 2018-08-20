package foxidcw.android.idcw.foxcommon.provider.bean;

import java.io.Serializable;

/**
 * Created by admin-2 on 2018/3/31.
 */

public class ScanLoginBean implements Serializable {
    /**
     *status (integer, optional): 扫描状态值 (枚举 0-等待扫描 1-扫描成功,2-授权成功,3-二维码失效) = ['0', '1', '2', '3'],
     description (string, optional): 状态描述
     */

    String status;
    String description;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
