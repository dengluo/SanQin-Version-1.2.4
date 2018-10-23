package com.pbids.sanqin.ui.activity.zhizong.component;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class GenealogyPaperAdapter extends FragmentStatePagerAdapter {

    private FragmentManager mfragmentManager;

    private List<Fragment> mlist;

    public GenealogyPaperAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mfragmentManager = fm;
        this.mlist = list;
    }

    public void addPaper( Fragment fragment){
        mlist.add(fragment);
    }

    public List<Fragment> getList(){
        return this.mlist;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mlist.get(arg0);//显示第几个页面
    }

    @Override
    public int getCount() {
        return mlist.size();//有几个页面
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
