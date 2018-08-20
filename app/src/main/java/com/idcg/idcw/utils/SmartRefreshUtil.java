package com.idcg.idcw.utils;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * Created by yuzhongrong on 2018/2/3.
 */

public class SmartRefreshUtil {


    public   static void onUpdateSmartRefresh(SmartRefreshLayout smartRefreshLayout){

         if(smartRefreshLayout==null)return;
        if(smartRefreshLayout.isRefreshing()){
            smartRefreshLayout.finishRefresh();

        }else if(smartRefreshLayout.isLoading()){
            smartRefreshLayout.finishLoadmore();
        }

    }
}
