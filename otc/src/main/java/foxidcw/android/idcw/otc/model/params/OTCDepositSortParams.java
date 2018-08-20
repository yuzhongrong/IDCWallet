package foxidcw.android.idcw.otc.model.params;

import java.io.Serializable;

/**
 * Created by hpz on 2018/5/12.
 */

public class OTCDepositSortParams implements Serializable {

    /**
     * serial_number : 1
     * phrase : toss
     */
    private int id;
    private int Sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }
}
