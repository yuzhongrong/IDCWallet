package foxidcw.android.idcw.common.model.bean;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class CheckAppVersionBean {

    /**
     * clientName : Android
     * latestVersion : 2.0
     * updateUrl : http://www.idcw.io/#/mobilePages/mobileIndex
     * isShowUpdate : false
     */

    private String clientName;
    private String latestVersion;
    private String updateUrl;
    private boolean isShowUpdate;
    private boolean is_enabled;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public boolean isIsShowUpdate() {
        return isShowUpdate;
    }

    public void setIsShowUpdate(boolean isShowUpdate) {
        this.isShowUpdate = isShowUpdate;
    }

    public boolean isIs_enabled() {
        return is_enabled;
    }

    public void setIs_enabled(boolean is_enabled) {
        this.is_enabled = is_enabled;
    }
}
