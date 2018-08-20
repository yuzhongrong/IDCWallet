package foxidcw.android.idcw.foxcommon.provider.params;

import java.io.Serializable;

/**
 * Created by admin-2 on 2018/3/31.
 */

public class ScanLoginReqParam implements Serializable{

    /**
     * client_id (string, optional): 客户端id 扫描后获取的唯一id ,
     type (integer, optional): 类型 0-扫描成功 1-确认登录 ,
     device_Id (string, optional): 设备id （确认登录时必须传递）
     */
    String client_id;
    int type;
    String device_Id;

    public ScanLoginReqParam(String client_id, int type, String device_Id) {
        this.client_id = client_id;
        this.type = type;
        this.device_Id = device_Id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDevice_Id() {
        return device_Id;
    }

    public void setDevice_Id(String device_Id) {
        this.device_Id = device_Id;
    }

}
