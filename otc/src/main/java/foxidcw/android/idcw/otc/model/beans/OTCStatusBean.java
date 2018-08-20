package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

/**
 * Created by hpz on 2018/5/31.
 */

public class OTCStatusBean implements Serializable {

         /** statusCode : 0
         * statusMessage : Success
         * txId : 0x49d002c167d3664d70a3e3db8819b612db1e2d8eb081161f08267276a03b40b2
         */

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
