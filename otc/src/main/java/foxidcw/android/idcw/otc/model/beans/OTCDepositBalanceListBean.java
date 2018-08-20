package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpz on 2018/5/9.
 */

public class OTCDepositBalanceListBean implements Serializable {

    private int Status;
    private int CurrentStep;
    private boolean HasPayment;
    private boolean HasBindPhone;
    private String AcceptantUserId;
    private List<DepositListBean> DepositList;

    public boolean isHasPayment() {
        return HasPayment;
    }

    public void setHasPayment(boolean hasPayment) {
        HasPayment = hasPayment;
    }

    public boolean isHasBindPhone() {
        return HasBindPhone;
    }

    public void setHasBindPhone(boolean hasBindPhone) {
        HasBindPhone = hasBindPhone;
    }

    public String getAcceptantUserId() {
        return AcceptantUserId;
    }

    public void setAcceptantUserId(String acceptantUserId) {
        AcceptantUserId = acceptantUserId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getCurrentStep() {
        return CurrentStep;
    }

    public void setCurrentStep(int currentStep) {
        CurrentStep = currentStep;
    }

    public List<DepositListBean> getDepositList() {
        return DepositList;
    }

    public void setDepositList(List<DepositListBean> DepositList) {
        this.DepositList = DepositList;
    }

    public static class DepositListBean {
        /**
         * id : 1014
         * CoinId : 1
         * CoinCode : btc
         * CoinName : Bitcoin
         * Logo : http://192.168.1.36:8888//group1/M00/00/00/wKgBJFrkHCyAO8FmAAANuGlxpRk308.png
         * UseNum : 7.0
         * Precision : 1.0E-5
         * Sort : 0
         */

        private int id;
        private int CoinId;
        private String CoinCode;
        private String CoinName;
        private String Logo;
        private double UseNum;
        private double Precision;
        private int Sort;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCoinId() {
            return CoinId;
        }

        public void setCoinId(int CoinId) {
            this.CoinId = CoinId;
        }

        public String getCoinCode() {
            return CoinCode;
        }

        public void setCoinCode(String CoinCode) {
            this.CoinCode = CoinCode;
        }

        public String getCoinName() {
            return CoinName;
        }

        public void setCoinName(String CoinName) {
            this.CoinName = CoinName;
        }

        public String getLogo() {
            return Logo;
        }

        public void setLogo(String Logo) {
            this.Logo = Logo;
        }

        public double getUseNum() {
            return UseNum;
        }

        public void setUseNum(double UseNum) {
            this.UseNum = UseNum;
        }

        public double getPrecision() {
            return Precision;
        }

        public void setPrecision(double Precision) {
            this.Precision = Precision;
        }

        public int getSort() {
            return Sort;
        }

        public void setSort(int Sort) {
            this.Sort = Sort;
        }
    }

}
