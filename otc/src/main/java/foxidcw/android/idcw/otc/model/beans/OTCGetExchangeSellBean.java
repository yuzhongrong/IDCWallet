package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpz on 2018/5/5.
 */

public class OTCGetExchangeSellBean implements Serializable {

    private List<OTCGetExchangeSellBean.ExchangeCoinListBean> ExchangeCoinList;
    private List<OTCGetExchangeSellBean.ExchangePayListBean> ExchangePayList;

    public List<OTCGetExchangeSellBean.ExchangeCoinListBean> getExchangeCoinList() {
        return ExchangeCoinList;
    }

    public void setExchangeCoinList(List<OTCGetExchangeSellBean.ExchangeCoinListBean> exchangeCoinList) {
        ExchangeCoinList = exchangeCoinList;
    }

    public List<OTCGetExchangeSellBean.ExchangePayListBean> getExchangePayList() {
        return ExchangePayList;
    }

    public void setExchangePayList(List<OTCGetExchangeSellBean.ExchangePayListBean> exchangePayList) {
        ExchangePayList = exchangePayList;
    }

    public static class ExchangeCoinListBean implements Serializable {
        /**
         * id : 1011
         * CoinId : 4
         * CoinCode : vhkd
         * Direction : 1
         * Min : 12.0
         * Max : 11.0
         * Premium : 0.2
         */

        private int id;
        private int CoinId;
        private String CoinCode;
        private int Direction;
        private double Min;
        private double Max;
        private double Premium;

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

        public int getDirection() {
            return Direction;
        }

        public void setDirection(int Direction) {
            this.Direction = Direction;
        }

        public double getMin() {
            return Min;
        }

        public void setMin(double Min) {
            this.Min = Min;
        }

        public double getMax() {
            return Max;
        }

        public void setMax(double Max) {
            this.Max = Max;
        }

        public double getPremium() {
            return Premium;
        }

        public void setPremium(double Premium) {
            this.Premium = Premium;
        }
    }

    public static class ExchangePayListBean implements Serializable {
        private int id;
        private int LocalCurrencyId ;
        private String LocalCurrencyCode;
        private double Amount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLocalCurrencyId() {
            return LocalCurrencyId;
        }

        public void setLocalCurrencyId(int localCurrencyId) {
            LocalCurrencyId = localCurrencyId;
        }

        public String getLocalCurrencyCode() {
            return LocalCurrencyCode;
        }

        public void setLocalCurrencyCode(String localCurrencyCode) {
            LocalCurrencyCode = localCurrencyCode;
        }

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double amount) {
            Amount = amount;
        }
    }
}
