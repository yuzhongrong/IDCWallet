package foxidcw.android.idcw.otc.adapter.viewholder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import io.reactivex.disposables.Disposable;

/**
 *
 * @project name : FoxIDCW
 * @class name :   OTCTimerViewHolder
 * @package name : foxidcw.android.idcw.otc.adapter.viewholder
 * @author :       MayerXu10000@gamil.com
 * @date :         2018/5/9 21:29
 * @describe :     TODO
 *
 */
public class OTCTimerViewHolder
        extends BaseViewHolder
{

    public Disposable mSubscribe;
    public OTCTimerViewHolder(View view) {
        super(view);
    }
}
