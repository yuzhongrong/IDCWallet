package foxidcw.android.idcw.otc.model.params;

import java.io.Serializable;

/**
 *
 * @project name : FoxIDCW
 * @class name :   OTCSendOrderParams
 * @package name : foxidcw.android.idcw.otc.model.params
 * @author :       MayerXu10000@gamil.com
 * @date :         2018/5/6 16:44
 * @describe :     发送订单请求参数
 *
 */
public class OTCSendOrderParams
        implements Serializable {
    private int CoinId;
    private int CurrencyId;
    private int PaymentModeId;
    private double QuoteQuantity;
    private int TradeDirection;
    public OTCSendOrderParams(){}

    public double getQuoteQuantity() {
        return QuoteQuantity;
    }

    public void setQuoteQuantity(double quoteQuantity) {
        QuoteQuantity = quoteQuantity;
    }

    public int getCoinId() {
        return CoinId;
    }

    public void setCoinId(int coinId) {
        CoinId = coinId;
    }

    public int getCurrencyId() {
        return CurrencyId;
    }

    public void setCurrencyId(int currencyId) {
        CurrencyId = currencyId;
    }

    public int getPaymentModeId() {
        return PaymentModeId;
    }

    public void setPaymentModeId(int paymentModeId) {
        PaymentModeId = paymentModeId;
    }

    public int getTradeDirection() {
        return TradeDirection;
    }

    public void setTradeDirection(int tradeDirection) {
        TradeDirection = tradeDirection;
    }

    @Override
    public String toString() {
        return "OTCSendOrderParams{" + "CoinId=" + CoinId + ", CurrencyId=" + CurrencyId + ", PaymentModeId=" + PaymentModeId + ", QuoteQuantity=" + QuoteQuantity + ", TradeDirection=" + TradeDirection + '}';
    }
}

