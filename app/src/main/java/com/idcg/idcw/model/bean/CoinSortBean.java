package com.idcg.idcw.model.bean;

/**
 *
 * @author yiyang
 */
public class CoinSortBean {

    /**
     * Coin : string
     * Sort : 0
     * id : 0
     */

    private String Coin;
    private long Sort;
    private long id;

    public String getCoin() {
        return Coin;
    }

    public void setCoin(String Coin) {
        this.Coin = Coin;
    }

    public long getSort() {
        return Sort;
    }

    public void setSort(long Sort) {
        this.Sort = Sort;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
