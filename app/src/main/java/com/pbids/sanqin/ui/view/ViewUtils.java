package com.pbids.sanqin.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by pbids903 on 2017/12/6.
 */

public class ViewUtils {
    public static int setListViewHeight(ListView listView) {
        try {
            int totalHeight = 0;
            ListAdapter adapter = listView.getAdapter();
            for (int i = 0, len = adapter.getCount(); i < len; i++) { // listAdapter.getCount()
                View listItem = adapter.getView(i, null, listView);
                listItem.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight
                    + (listView.getDividerHeight() * (listView.getCount() - 1));
            listView.setLayoutParams(params);
            return params.height;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    public static int setListViewHeight(RecyclerView listView) {
        try {
            int totalHeight = 0;
            for (int i = 0, len = listView.getChildCount(); i < len; i++) { // listAdapter.getCount()
                View listItem = listView.getChildAt(i);
                listItem.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight
                    + ((listView.getChildCount() - 1));
            listView.setLayoutParams(params);
            return params.height;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
