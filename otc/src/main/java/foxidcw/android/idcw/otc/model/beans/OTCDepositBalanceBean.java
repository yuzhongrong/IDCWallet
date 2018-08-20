package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

/**
 * Created by hpz on 2018/5/9.
 */

public class OTCDepositBalanceBean implements Serializable {
    /**
     * currentBalance : 833.8961
     * realityBalance : 833.8961
     */

    private double currentBalance;
    private double realityBalance;

    public TokenInfoBean getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(TokenInfoBean tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    private TokenInfoBean tokenInfo;

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public double getRealityBalance() {
        return realityBalance;
    }

    public void setRealityBalance(double realityBalance) {
        this.realityBalance = realityBalance;
    }

    public static class TokenInfoBean {
        /**
         * isToken : true
         * tokenCategory : eth
         * coinUnit : LGZ
         * ethBalanceForToken : 0.0
         */

        private boolean isToken;
        private String tokenCategory;
        private String coinUnit;
        private double ethBalanceForToken;

        public boolean isIsToken() {
            return isToken;
        }

        public void setIsToken(boolean isToken) {
            this.isToken = isToken;
        }

        public String getTokenCategory() {
            return tokenCategory;
        }

        public void setTokenCategory(String tokenCategory) {
            this.tokenCategory = tokenCategory;
        }

        public String getCoinUnit() {
            return coinUnit;
        }

        public void setCoinUnit(String coinUnit) {
            this.coinUnit = coinUnit;
        }

        public double getEthBalanceForToken() {
            return ethBalanceForToken;
        }

        public void setEthBalanceForToken(double ethBalanceForToken) {
            this.ethBalanceForToken = ethBalanceForToken;
        }
    }
}
