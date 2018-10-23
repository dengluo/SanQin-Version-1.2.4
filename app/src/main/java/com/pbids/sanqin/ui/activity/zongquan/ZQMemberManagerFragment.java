package com.pbids.sanqin.ui.activity.zongquan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.core.item.ContactIdFilter;
import com.netease.nim.uikit.business.contact.core.item.ItemTypes;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.common.CommonGroup;
import com.netease.nim.uikit.common.ui.liv.LetterIndexView;
import com.netease.nim.uikit.common.ui.liv.LivRecyclerviewIndex;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.base.ComonRecycerGroup;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.contact.activity.UserProfileActivity;
import com.pbids.sanqin.model.db.FriendGroupManager;
import com.pbids.sanqin.model.db.FriendGroupMemberManager;
import com.pbids.sanqin.model.entity.FriendGroupDb;
import com.pbids.sanqin.model.entity.FriendGroupMemberDb;
import com.pbids.sanqin.presenter.ZQMemberManagerPresenter;
import com.pbids.sanqin.ui.activity.me.MeFeedbackFragment;
import com.pbids.sanqin.ui.adapter.SanQinContactDataAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.dialog.CreateSanQinGroupDialog;
import com.pbids.sanqin.ui.view.dialog.OneTextTwoBtDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:12
 * @desscribe 类描述:成员管理界面
 * @remark 备注:
 * @see
 */
public class ZQMemberManagerFragment extends BaaseToolBarFragment<ZQMemberManagerPresenter> implements AppToolBar.OnToolBarClickLisenear,ZQMemberManagerView {

	public static final int CODE_SELECT_USER = 3200;
	public static final int REQUEST_USERINFO_CODE = 3254;
    public static final int REQUEST_CODE_CONTACT_SELECT = 52032;
    public static final int REQUEST_CODE_SUB_GROUP = 52036;

	public static final int GROUP_SHOW = 1;
    public static final int GROUP_EDIT = 2;

    @Bind(R.id.et_group_name)
    EditText etGroupName;
    @Bind(R.id.tv_group_label)
    TextView tvGroupLabel;
    @Bind(R.id.zongquan_group_text)
    TextView zongquanGroupText;
    @Bind(R.id.rl1)
    LinearLayout rl1;
    @Bind(R.id.zongquan_friends_list)
    RecyclerView ryList;
    @Bind(R.id.zongquan_sv)
    NestedScrollView zongquanSv;
    @Bind(R.id.liv_index)
    LetterIndexView livIndex;
    @Bind(R.id.img_hit_letter)
    ImageView imgBackLetter;
    @Bind(R.id.tv_hit_letter)
    TextView litterHit;
    @Bind(R.id.zq_number_page_)
    ImageView zqNumberPage;
    @Bind(R.id.wv_sanqin_tree)
    WebView webView;


    //当前组名
    String groupId = "";
    int ctrl = 0;
    int curentGroupPosition = 0 ; //当前操作组

	//好友列表 adapter
	private SanQinContactDataAdapter mAdapter;
    private LivRecyclerviewIndex litterIdx;

    //所有称呼列表
    private List<String> callList = new ArrayList<String>();


    public static ZQMemberManagerFragment newInstance() {
        ZQMemberManagerFragment fragment = new ZQMemberManagerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public ZQMemberManagerPresenter initPresenter() {
        return new ZQMemberManagerPresenter();
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zq_number_manage, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @SuppressLint("JavascriptInterface")
    private void initView() {
        //所有称呼
        callList.add("爷爷");
        callList.add("奶奶");
        callList.add("父亲");
        callList.add("母亲");
        callList.add("叔叔");
        callList.add("姑姑");
        callList.add("兄弟");
        callList.add("姐妹");

        //取传递的数据
        initGroupData();
        switch (ctrl){
            case GROUP_EDIT:
                //添加组
                etGroupName.setText("");
                etGroupName.setVisibility(View.VISIBLE);
                tvGroupLabel.setVisibility(View.VISIBLE);
                mPresenter.initData();
                break;

            case GROUP_SHOW:
                //展示 及 成员编辑
                etGroupName.setVisibility(View.GONE);
                tvGroupLabel.setVisibility(View.GONE);
                mPresenter.loadData();
                break;

            default:
                pop();
                return;
        }
		mAdapter = new SanQinContactDataAdapter(_mActivity);
        //如果是三亲 需要分组显示
        if(mPresenter.friendGroupDb.getGroupName().equals(CommonGroup.GROUP_SANQIN)){
            mAdapter.setBlock(true);
            zongquanGroupText.setText("+添加称呼");
            //检查默认的父亲和母亲分组
            List<String> defaultBlocks = new ArrayList<>();
            defaultBlocks.add("父亲");
            defaultBlocks.add("母亲");
            mPresenter.checkDefaultBlock(defaultBlocks);
            //这里二级分组显示
            for(FriendGroupDb item:mPresenter.subGroups){
                mAdapter.createBlock(item);
            }
        } else {
            mAdapter.createBlock(mPresenter.friendGroupDb);
        }

        mAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
                curentGroupPosition = groupPosition;
                NimUserInfo nimUserInfo = mAdapter.getNimInfo(groupPosition,childPosition);
                Intent intent = new Intent(_mActivity, UserProfileActivity.class);
                intent.putExtra(Extras.EXTRA_ACCOUNT, nimUserInfo.getAccount());
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent,REQUEST_USERINFO_CODE);
            }
        });

        if(mAdapter.isBlock()){
            //成员组创建
            mAdapter.setOnAddGroupAddListener(new SanQinContactDataAdapter.OnAddGroupAddListener() {
                @Override
                public void onAdd(int groupPosition) {
                    //去用户添加面板
                    toUserSelected(groupPosition);
                   /* curentGroupPosition = groupPosition;
                    //showToast("添加分组成员");
                    ContactSelectActivity.Option option = new ContactSelectActivity.Option();
                    option.title = "添加成员";
                    //这里加入不可选的好友
                    List<String> disableAccounts = mAdapter.getAccounts() ;
                    if(disableAccounts.size()>0){
                        option.itemDisableFilter = new ContactIdFilter(disableAccounts);
                    }
                    option.minSelectedTip = "请选择好友";
                    NimUIKit.startContactSelector(_mActivity, option, REQUEST_CODE_SUB_GROUP);*/
                }
            });
        }
        mAdapter.setDeleteItemListener(new SanQinContactDataAdapter.DeleteListItemListener<NimUserInfo>() {
            @Override
            public void deleteItem(final NimUserInfo nimUserInfo, final int groupPosition, final int childPosition) {
                String msg = "是否从该组移出好友["+nimUserInfo.getName()+"]";
                final OneTextTwoBtDialog dialog = new OneTextTwoBtDialog(_mActivity);
                dialog.setContentText(msg);
                dialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
                    @Override
                    public void confirm(View view) {
                        //确认
                        mAdapter.removeMember(nimUserInfo.getAccount());//del list
                        if(mAdapter.isBlock()){
                            //这里删除二级分组成员
                            mPresenter.delMemberInDb(mAdapter.getList().get(groupPosition).getFag(),nimUserInfo.getAccount(),groupPosition);
                            callJsClearMembers();
                            callJsShowMembers();
                        }else{
                            //这里删除一级分组成员
                            mPresenter.delMemberInDb(groupId,nimUserInfo.getAccount(),groupPosition);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void cancel( View view) {
                        //取消
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


		//init adapter
		LinearLayoutManager mFriendManager = new LinearLayoutManager(_mActivity);
		mFriendManager.setOrientation(LinearLayoutManager.VERTICAL);
		//初始化好友列表
		ryList.setLayoutManager(mFriendManager);
		ryList.setAdapter(mAdapter);
		ryList.setNestedScrollingEnabled(false); //停止滑动
        buildLitterIdx();

        //显示三亲树
        if(CommonGroup.GROUP_SANQIN.equals(mPresenter.friendGroupDb.getGroupName())){
            webView.setVisibility(View.VISIBLE);
            initWebViewSttting(webView);
            webView.setBackgroundColor(0);
            webView.addJavascriptInterface(this, "sanqin");
            //不显示滚动条
            webView.setHorizontalScrollBarEnabled(false);//水平不显示
            webView.setVerticalScrollBarEnabled(false); //垂直不显示
            //设置 web url
            //Glide.with(getContext()).load(R.mipmap.kiss_piture_sanqinshu_default).into(imgSanQinTree);
            //回调 webview
            //zzHomeWebview.loadUrl("javascript:rewardCb(1);");
            webView.loadUrl("file:///android_asset/html/tree.html?v=13");
        }else {
            webView.setVisibility(View.GONE);
        }
    }

    private void callJsShowMembers(){
        Log.v("cgl","callJsShowMembers");
        if(mAdapter.isBlock()){
            String mems = "";
            for(ComonRecycerGroup item:mAdapter.getList()){
                for(int i=0;i<item.getList().size();i++){
                    mems += item.getAttr("groupName")+",";
                }
            }
            final String finalMems = mems;
            _mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!finalMems.isEmpty()){
                        String js = "javascript:addMembers('"+ finalMems +"');";
                        Log.v("cgl",js);
                        webView.loadUrl(js);
                    }
                }
            });
        }
    }
    // add list
    private void callJsClearMembers(){
        Log.v("cgl","callJsClearMembers");
        if(mAdapter.isBlock()){
            _mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String js = "javascript:clearMember();";
                    webView.loadUrl(js);
                }
            });
        }
    }
    //add one
    private void callJsAddMember(String item){
        if(mAdapter.isBlock()) {
            final String finalMems = item;
            _mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!finalMems.isEmpty()) {
                        String js = "javascript:addMember('" + finalMems + "');";
                        webView.loadUrl(js);
                    }
                }
            });
        }
    }

    @JavascriptInterface
    public void loadMembers(String s) {
        callJsShowMembers();
    }

    //到用户选择面板
    private void toUserSelected(int groupPosition){
        curentGroupPosition = groupPosition;
        //showToast("添加分组成员");
        ContactSelectActivity.Option option = new ContactSelectActivity.Option();
        option.title = "添加成员";
        //这里加入不可选的好友
        List<String> disableAccounts = mAdapter.getAccounts() ;
        if(disableAccounts.size()>0){
            option.itemDisableFilter = new ContactIdFilter(disableAccounts);
        }
        option.minSelectedTip = "请选择好友";
        NimUIKit.startContactSelector(_mActivity, option, REQUEST_CODE_SUB_GROUP);
    }


    private void buildLitterIdx( ) {
        livIndex.setNormalColor(getResources().getColor(com.netease.nim.uikit.R.color.contacts_letters_color));
        litterIdx = mAdapter.createLivIndex(ryList, livIndex, litterHit, imgBackLetter);
//        litterIdx.show();
        litterIdx.hide();
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        if (mPresenter. friendGroupDb.getType() == ItemTypes.FRIEND_GROUP) {
            //固定组不可删除
            toolBar.setLeftArrowCenterTextTitle(mPresenter.friendGroupDb.getGroupName(), _mActivity);
        } else {
            switch (ctrl) {
                case GROUP_EDIT:
                    toolBar.setLeftArrowCenterTextTitleRightText(mPresenter.friendGroupDb.getGroupName(), "完成", _mActivity);
                    break;
                case GROUP_SHOW:
                    toolBar.setLeftArrowCenterTextTitleRightText(mPresenter.friendGroupDb.getGroupName(), "删除组", _mActivity);
                    break;

                default:
                    toolBar.setLeftArrowCenterTextTitle(mPresenter.friendGroupDb.getGroupName(), _mActivity);
                    break;
            }
        }
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public void pop(){
        hideSoftInput();
        super.pop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                //返回
                if(ctrl==GROUP_SHOW){
                    if(mPresenter.friendGroupDb.changed){
                        //save to server;
                        //有改动 ，需要更新列表
                        Bundle bundle = new Bundle() ;
                        bundle.putInt("act",ContactListFragment.ACT_UPDATE);
                        bundle.putString("groupId",mPresenter.friendGroupDb.getGroupId());
                        setFragmentResult(RESULT_OK,bundle);
                        pop();
                    }else{
                        pop();
                    }
                } else {
                    //新建返回时确认
                    if(etGroupName.getText().toString().length()>0 || mAdapter.memeberCount()>0){
                        //如果有内容就提示一下
                        String msg = "是否要退出？";
                        final OneTextTwoBtDialog dialog = new OneTextTwoBtDialog(_mActivity);
                        dialog.setContentText(msg);
                        dialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
                            @Override
                            public void confirm(View view) {
                                //确认
                                dialog.dismiss();
                                pop();
                            }

                            @Override
                            public void cancel(View view) {
                                //取消
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }else {
                        pop();
                    }
                }
                break;
            case R.id.main_right_layout:
                switch (ctrl){
                    case GROUP_EDIT:
                        String gname = etGroupName.getText().toString();
                        //完成
                        if (gname.equals("")) {
                            showToast("请填写组名");
                            return;
                        }else{
                            //这里处理新建分组
                            FriendGroupDb fgd = mPresenter.saveGroupToDb(gname,ItemTypes.FRIEND_GROUP_CUSTOM,"");
                            //mPresenter.friendGroupDb = fgd;
                            List<NimUserInfo> uInfos = mAdapter.getList().get(0).getList();
                            for(NimUserInfo one : uInfos){
                                mPresenter.addMemberToDb(fgd.getGroupId(),one.getAccount());
                            }
                            showToast("创建成功");
                            Bundle bundle = new Bundle() ;
                            bundle.putInt("act",ContactListFragment.ACT_CREATE);
                            bundle.putString("groupId",gname);
                            setFragmentResult(RESULT_OK,bundle);
                            hideSoftInput();
                            pop();
                        }
                        break;
                    case GROUP_SHOW:
                        //删除
                        String msg = "是否删除该组？";
                        final OneTextTwoBtDialog dialog = new OneTextTwoBtDialog(_mActivity);
                        dialog.setContentText(msg);
                        dialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
                            @Override
                            public void confirm(View view) {
                                //确认
                                dialog.dismiss();
                                //del
                                //清除组及下面所有成员
                                mPresenter.delGroup();
                                //返回数据
                                Bundle bundle = new Bundle() ;
                                bundle.putInt("act",ContactListFragment.ACT_DEL);
                                bundle.putString("groupId",mPresenter.friendGroupDb.getGroupId());
                                setFragmentResult(RESULT_OK,bundle);
                                pop();
                            }

                            @Override
                            public void cancel(View view) {
                                //取消
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(zqNumberPage.getVisibility() == View.VISIBLE){
            zqNumberPage.setBackgroundResource(0);
            zqNumberPage.setImageResource(0);
            zqNumberPage.setBackground(null);
            zqNumberPage = null;
        }
        if(webView!=null){
            webView.loadUrl("about:blank");
            webView.freeMemory();
            webView.destroy();
            webView = null;
        }
        ButterKnife.unbind(this);
    }


	//点击添加组成员
    @OnClick(R.id.zongquan_group_text)
	public void onViewClicked() {
        if(mAdapter.isBlock()){
            //showToast("创建分组");
            //删除
            final String gname = "";
            final CreateSanQinGroupDialog dialog = new CreateSanQinGroupDialog(_mActivity,gname);
            //设置下拉内容
            List<String> hasSel = new ArrayList<>();
            for(int i=0;i<callList.size();i++){
                String one = callList.get(i);
                if(!mAdapter.hasHeaderName(one)){
                    hasSel.add(one);
                }
            }
            if(hasSel.size()==0){
                Toast.makeText(getContext(),"所有三亲已创建完毕",Toast.LENGTH_LONG).show();
                return;
            }
            dialog.setSpinCall(hasSel);
            dialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
                @Override
                public void confirm(View view) {
                    String cname = dialog.getSelectCall();
                    if(cname==null||cname.isEmpty()){
                        showToast("请选择");
                        return;
                    }
                    //添加三亲二级分组
                    FriendGroupDb addOne = mPresenter.saveGroupToDb(cname, ItemTypes.FRIEND_GROUP_CUSTOM, mPresenter.friendGroupDb.getGroupId());
                    mPresenter.subGroups.add(addOne);
                    mAdapter.createBlock(addOne);
                    mAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

                @Override
                public void cancel(View view) {
                    //取消
                    dialog.dismiss();
                }
            });
            dialog.show();
        }else{
            //去用户添加面板
            toUserSelected(curentGroupPosition);
            /*
            ContactSelectActivity.Option option = new ContactSelectActivity.Option();
            option.title = "添加成员";
            //这里加入不可选的好友
            List<String> disableAccounts = mPresenter.friendGroupDb.getAccounts() ;
            if(disableAccounts.size()>0){
                option.itemDisableFilter = new ContactIdFilter(disableAccounts);
            }
            option.minSelectedTip = "请选择好友";
            NimUIKit.startContactSelector(_mActivity, option, REQUEST_CODE_CONTACT_SELECT);*/
        }
	}


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTACT_SELECT && resultCode == Activity.RESULT_OK) {
            final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
            if (selected != null && !selected.isEmpty()) {
                switch (ctrl) {
                    case GROUP_EDIT:
                        //这里处理新建分组
                        mAdapter.addMember(selected,0);
                        //mPresenter.friendGroupDb.
                        //for(String one:selected){
                        //    mPresenter.addMemberToDb(mPresenter.friendGroupDb.getGroupId(),one);
                        //}
                        break;
                    case GROUP_SHOW:
                        if(mAdapter.isBlock()){
                             //三亲分组添加成员不在这里
                        }else{
                            mAdapter.addMember(selected,0);
                            mPresenter.addMemberToDb(mPresenter.friendGroupDb.getGroupId(),selected);
                            mPresenter.friendGroupDb.loadMemberDb();
                        }
                        break;
                }
            }
        }else if (requestCode == REQUEST_USERINFO_CODE && resultCode == Activity.RESULT_OK) {
            //显示用户信息
            if(data!=null){
                String  del = data.getStringExtra("del");
                if (del != null && !del.isEmpty()) {
                    //这里是处理用户在信息面板上删除了用户后 这里也要清除
                    mAdapter.removeMember(del);
                    String groupId = mAdapter.getList().get(curentGroupPosition).getFag();
                    mPresenter.delMemberInDb( groupId, del, curentGroupPosition);
                    if(ctrl==GROUP_SHOW){
                        //保存 update to server
                        //TODO

                    }
                }
            }
        }else if(requestCode == REQUEST_CODE_SUB_GROUP && resultCode == Activity.RESULT_OK){
            //三亲组添加成员
            final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
            if (selected != null && !selected.isEmpty()) {
                mAdapter.addMember(selected,curentGroupPosition);
                String groupId = mAdapter.getList().get(curentGroupPosition).getFag();
                mPresenter.addMemberToDb(groupId,selected);
                callJsClearMembers();
                callJsShowMembers();
            }
        }
    }


    //取传过来的中组名
    private void initGroupData() {
        groupId = getArguments().getString("group", "");
        ctrl = getArguments().getInt("ctrl");
    }

    @Override
    public SanQinContactDataAdapter getContactAdapter() {
        return mAdapter;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public int getCtrl() {
        return ctrl;
    }

    @Override
    public void updateAllUser() {

    }
}
