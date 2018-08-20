package foxidcw.android.idcw.otc.model.beans;

/**
 * Created by hpz on 2018/5/9.
 */

public class OTCWithdrawResultBean {
    private int statusCode;
    private String statusMessage;
    private String txId;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
