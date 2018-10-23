package com.pbids.sanqin.ui.activity.zhizong;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.OnOccurrenceLisenear;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.CampaignEnrollExtendEntity;
import com.pbids.sanqin.presenter.CampaignEnrollPresenter;
import com.pbids.sanqin.ui.pay.union_pay.UnionPayActivity;
import com.pbids.sanqin.ui.recyclerview.adapter.CampaignEnrollAdapter;
import com.pbids.sanqin.model.entity.CampaignEnrollEntity;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.dialog.PayPasswordDialog;
import com.pbids.sanqin.ui.view.dialog.PaymentModeDialog;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.AppUtils;
import com.pbids.sanqin.utils.Shotter;
import com.pbids.sanqin.utils.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:07
 * @desscribe 类描述:活动报名填写界面
 * @remark 备注:
 * @see
 */
public class CampaignEnrollFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,CampaignEnrollView {

    //public static final int UNION_PAY_ACTIVE_REUQEST_CODE = 1151;

    @Bind(R.id.name_et)
    EditText nameEt;
    @Bind(R.id.idcard_et)
    EditText idcardEt;
    @Bind(R.id.sex_spinner)
    Spinner sexSpinner;
//    @Bind(R.id.sex_et)
//    EditText sexEt;
    @Bind(R.id.enroll_rv)
    RecyclerView enrollRv;
    @Bind(R.id.enroll_add)
    LinearLayout enrollAdd;
    @Bind(R.id.enroll_remark_et)
    EditText enrollRemarkEt;
    @Bind(R.id.campaign_enroll_unitprice)
    TextView campaignEnrollUnitprice;
    @Bind(R.id.campaign_enroll_number)
    TextView campaignEnrollNumber;
    @Bind(R.id.campaign_enroll_total)
    TextView campaignEnrollTotal;
    @Bind(R.id.campaign_enroll_discount)
    TextView campaignEnrollDiscount;
    @Bind(R.id.campaign_enroll_bt)
    Button campaignEnrollBt;
//    @Bind(R.id.campaign_enroll_matter_bg)
//    View campaignEnrollMatterBg;
//    @Bind(R.id.campaign_enroll_matter_content)
//    RelativeLayout campaignEnrollMatterContent;
    @Bind(R.id.campaign_enroll_matter)
    View campaignEnrollMatter;
    @Bind(R.id.campaign_enroll_title)
    TextView campaignEnrollTitle;
    @Bind(R.id.phone_et)
    EditText phoneEt;
    private long aid;
    private long tid;
    private float price;
    private float discountNum;
    private String title;

    private float moeny;

    private Animation enrollEnter;
    private Animation enrollExit;
    CampaignEnrollPresenter presenter;
    CampaignEnrollAdapter adapter;
    InputFilter nameFilter;
    InputFilter sexFilter;

    PaymentModeDialog paymentModeDialog;
    PayPasswordDialog payPasswordDialog;
    List<CampaignEnrollEntity> allEntities;
    String key;

    private String[] sexStrs={"男","女"};
    ArrayAdapter spinnerAdapter;



    public static CampaignEnrollFragment newInstance() {
        CampaignEnrollFragment fragment = new CampaignEnrollFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_campaign_enroll, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    float blance;

    public void calculateMoeny(int size) {
        float favorable = price * size * discountNum;
        //Log.i("wzh","discountNum: "+discountNum);
        float count = price * size;
        blance = favorable;
        campaignEnrollDiscount.setText(String.format("%.2f", count-favorable));
        campaignEnrollTotal.setText(String.format("%.2f", blance));
        campaignEnrollNumber.setText("x"+size);
    }

    private void initView() {
        key = getArguments().getString("key","");
        if(key.equals("webview")){
            aid = getArguments().getLong("aid",-1);
            tid = getArguments().getLong("tid",-1);
            price = getArguments().getFloat("price",-1);
            discountNum = getArguments().getFloat("discountNum",-1);
            title = getArguments().getString("title","");
            //Log.i("wzh","discountNum: "+discountNum);

            initInfomation();
        }else if(key.equals("me")){
            aid = getArguments().getLong("aid",-1);
            tid = getArguments().getLong("tid",-1);
            title = getArguments().getString("title","");
            HttpParams params = new HttpParams();
            params.put("tid",tid);
            addDisposable(presenter.submitInformation(AddrConst.SERVER_ADDRESS_NEWS+ AddrConst.ADDRESS_ACTIVITY_PAYINFO,params,"1"));
        }

//        enrollRv.setItemAnimator(new DefaultItemAnimator());
    }

    private void initInfomation(){
        campaignEnrollTitle.setText(title);
        campaignEnrollUnitprice.setText(String.format("%.2f", price) );
        campaignEnrollNumber.setText("x1");

        calculateMoeny(1);

        initMoeny();

        enrollEnter = AnimationUtils.loadAnimation(_mActivity, R.anim.enroll_enter);
        enrollExit = AnimationUtils.loadAnimation(_mActivity, R.anim.enroll_exit);

        nameFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                String regEx = "^[\\u4e00-\\u9fa5]*$";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(source.toString());
                if (m.matches()){
                    return source.toString();
                } else {
                    return "";
                }
            }
        };

        sexFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
//                String regEx = "^[\\u4e00-\\u9fa5]*$";
//                Pattern p = Pattern.compile(regEx);
//                Matcher m = p.matcher(source.toString());

                if ("男".equals(source.toString()) || "女".equals(source.toString())) {
                    return source.toString();
                } else {
                    return "";
                }
            }
        };

        spinnerAdapter = ArrayAdapter.createFromResource(_mActivity, R.array.sex, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(spinnerAdapter);

        if("男".equals(MyApplication.getUserInfo().getSex())){
            sexSpinner.setSelection(0);
        }else if("女".equals(MyApplication.getUserInfo().getSex())){
            sexSpinner.setSelection(1);
        }

        nameEt.setFilters(new InputFilter[]{nameFilter, new InputFilter.LengthFilter(6)});
//        sexEt.setFilters(new InputFilter[]{sexFilter, new InputFilter.LengthFilter(1)});

        nameEt.setText(MyApplication.getUserInfo().getName());
        idcardEt.setText(MyApplication.getUserInfo().getIdNumber());
        phoneEt.setText(MyApplication.getUserInfo().getPhone());

        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        adapter = new CampaignEnrollAdapter(_mActivity);
        adapter.setOnOccurrenceLisenear(new OnOccurrenceLisenear() {
            @Override
            public void onOccurrence() {
                enrollRemarkEt.setFocusable(true);
                enrollRemarkEt.requestFocus();
                hideSoftInput();
            }

            @Override
            public void onOccurrenceAfter() {
                int size = adapter.getEntities().size() + 1;
                campaignEnrollNumber.setText("x" + size);
                calculateMoeny(size);
            }
        });
        enrollRv.setLayoutManager(manager);
        enrollRv.setAdapter(adapter);
        enrollRv.setNestedScrollingEnabled(false);
        paymentModeDialog = new PaymentModeDialog(this);
        paymentModeDialog.setOnPaymentModeLisenear(new PaymentModeDialog.OnPaymentModeLisenear() {
            @Override
            public void onPaymentModeClick(String paymentMode) {
                HttpParams params = new HttpParams();
                params.put("totalAmount", String.format("%.2f", blance));
                params.put("purpose", Const.PURPOSE_ACTIVE_PAYMENT);
                params.put("aid", aid);
                params.put("tid", tid);
                params.put("phone", phoneEt.getText().toString().trim());
                params.put("clientIp", AppUtils.getIp());
                params.put("remark", enrollRemarkEt.getText().toString().trim());
                params.put("members", new Gson().toJson(allEntities));
                params.put("orderType", "2");
                switch (paymentMode) {
                    case PaymentModeDialog.PAYMENT_WECHAT:
//                        Toast.makeText(_mActivity, "微信支付未开通", Toast.LENGTH_SHORT).show();
//                        HttpParams params3 = new HttpParams();
//                        params3.put("totalAmount", String.format("%.2f", blance));
//                        params3.put("payCode", "wechatpay");
//                        params3.put("purpose", "活动支付");
//                        params3.put("orderType", "2");
//                        params3.put("clientIp", AppUtils.getIp());
//                        params3.put("phone", phoneEt.getText().toString().trim());
//                        params3.put("aid", aid);
//                        params3.put("tid", tid);
//                        params3.put("remark", enrollRemarkEt.getText().toString().trim());
//                        params3.put("members", new Gson().toJson(allEntities));
//                        paymentModeDialog.setHttpParams(params3);
                        params.put("payCode", "wechatpay");
                        paymentModeDialog.setHttpParams(params);
                        break;

                    case PaymentModeDialog.PAYMENT_ZHIFUBAO:
//                        HttpParams params = new HttpParams();
//                        params.put("totalAmount", String.format("%.2f", blance));
                        params.put("payCode", "alipay");
//                        params.put("purpose", "活动支付");
//                        params.put("orderType", "2");
//                        params.put("phone", phoneEt.getText().toString().trim());
//                        params.put("aid", aid);
//                        params.put("tid", tid);
//                        params.put("remark", enrollRemarkEt.getText().toString().trim());
//                        params.put("members", new Gson().toJson(allEntities));
                        paymentModeDialog.setHttpParams(params);
                        break;
                    case PaymentModeDialog.PAYMENT_QIANBAO:
//                        HttpParams params2 = new HttpParams();
//                        params2.put("totalAmount", String.format("%.2f", blance));
//                        params2.put("purpose", "活动支付");
//                        params2.put("orderType", "2");
//                        params2.put("aid", aid);
//                        params2.put("tid", tid);
//                        params2.put("phone", phoneEt.getText().toString().trim());
//                        params2.put("remark", enrollRemarkEt.getText().toString().trim());
//                        params2.put("members", new Gson().toJson(allEntities));
//                        Log.i("wzh","allEntities: "+new Gson().toJson(allEntities).toString());
//                        paymentModeDialog.setHttpParams(params2);
                        paymentModeDialog.setHttpParams(params);
                        break;
                    case PaymentModeDialog.PAYMENT_YINLIAN:
                        //银联
                        paymentModeDialog.dismiss();
                        Intent intent = new Intent(_mActivity, UnionPayActivity.class);
                        intent.putExtra("pay",PaymentModeDialog.PAYMENT_YINLIAN);
                        intent.putExtra("totalAmount",  String.format("%.2f", blance));
                        intent.putExtra("purpose", Const.PURPOSE_ACTIVE_PAYMENT);
                        intent.putExtra("aid",  aid+"");
                        startActivityForResult(intent,ZhiZongWebFragment.UNION_PAY_REUQEST_CODE);
                        return;
                }
            }

            @Override
            public void payOverInfo(Map<String, String> map) {
                String status = map.get("status");
                String payCode = map.get("payCode");
                if("1".equals(status)){
                    Bundle bundle = new Bundle();
                    bundle.putString("payCode",payCode);
                    CampaignEnrollFragment.this.setFragmentResult(RESULT_OK,bundle);
                    CampaignEnrollFragment.this.pop();
                }else if("0".equals(status)){
                    String errorMessage = map.get("errorMessage");
                    Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initMoeny() {
//        startForResult();
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("活动信息", _mActivity);
    }

    @Override
    public BasePresenter initPresenter() {
        return presenter = new CampaignEnrollPresenter(this);
//        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(paymentModeDialog!=null){
            paymentModeDialog.dispose();
        }
//        campaignEnrollMatterContent.clearAnimation();
        ButterKnife.unbind(this);
    }

//    private void setMatterVisiable() {
//        campaignEnrollMatterContent.setVisibility(View.VISIBLE);
////        campaignEnrollMatterContent.setAnimation();
//        campaignEnrollMatterContent.startAnimation(enrollEnter);
//        campaignEnrollMatterBg.setVisibility(View.VISIBLE);
//    }

//    private void setMatterGone() {
//        campaignEnrollMatterContent.startAnimation(enrollExit);
//    }

    @OnClick({R.id.enroll_add, R.id.campaign_enroll_bt, R.id.campaign_enroll_matter
//            , R.id.campaign_enroll_matter_bg
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.enroll_add:
                //Log.i("wzh", "enroll_add");
                //Log.i("wzh", "adapter.getChildrenCount(0): " + adapter.getChildrenCount(0));
                if(adapter.getEntities().size()>=5){
                    Toast.makeText(_mActivity,"随从人员不能大于5人!",Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter.addData();
                calculateMoeny(adapter.getEntities().size() + 1);
                break;

            case R.id.campaign_enroll_bt:

                enrollRemarkEt.setFocusable(true);
                enrollRemarkEt.requestFocus();
                hideSoftInput();
                String status = checkInfo();
                if (!"1".equals(status)) {
                    Toast.makeText(_mActivity, status, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(allEntities==null){
                    allEntities = new ArrayList<>();
                }else{
                    allEntities.clear();
                }
                CampaignEnrollEntity enrollEntity = new CampaignEnrollEntity();
                enrollEntity.setUsername(nameEt.getText().toString().trim());
                enrollEntity.setIdcard(idcardEt.getText().toString().trim());
                enrollEntity.setSex(sexStrs[sexSpinner.getSelectedItemPosition()]);
                enrollEntity.setMaster(1);
                allEntities.add(enrollEntity);
                allEntities.addAll(adapter.getEntities());

                paymentModeDialog.setAccountTv("" + String.format("%.2f", blance));
                paymentModeDialog.show();

//                HttpParams params = new HttpParams();
//                params.put("aid", aid);
//                params.put("tid", tid);
//                params.put("uid", MyApplication.getUserInfo().getUserId());
//                params.put("remark", enrollRemarkEt.getText().toString().trim());
//                params.put("members", new Gson().toJson(allEntities));
////                Log.i("wzh","members1: "+adapter.getEntities().toString());
////                Log.i("wzh","members2: "+new Gson().toJson(adapter.getEntities()));
//
////                JsonArray jsonElements = new JsonArray();
//                addDisposable(presenter.submitInformation("http://192.168.5.32:9092/activity/savemember", params, ""));
                break;
            case R.id.campaign_enroll_matter:
                CampaignEnrollRuleFragment fragment = CampaignEnrollRuleFragment.newInstance();
                fragment.getArguments().putLong("aid", aid);
                start(fragment);
//                if (campaignEnrollMatterContent.getVisibility() == View.GONE) {
//                    setMatterVisiable();
//                } else if (campaignEnrollMatterContent.getVisibility() == View.VISIBLE) {
//                    setMatterGone();
//                }
                break;
//            case R.id.campaign_enroll_matter_bg:
//                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ZhiZongWebFragment.UNION_PAY_REUQEST_CODE:
                // 银行支付 返回
                if (data != null && data.hasExtra("pay") && data.hasExtra("status") && data.hasExtra("purpose")) {
                    String pay = data.getStringExtra("pay");
                    String status = data.getStringExtra("status");
                    final String purpose = data.getStringExtra("purpose");
                    if (pay.contains("union") && status.contains("1")) {
                        //银生宝支付完成
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bundle bundle = new Bundle();
                                bundle.putString("payCode",PaymentModeDialog.PAYMENT_YINLIAN);
                                CampaignEnrollFragment.this.setFragmentResult(RESULT_OK,bundle);
                                CampaignEnrollFragment.this.pop();
                            }
                        });
                    }
                }
                break;
        }

    }

    public String checkInfo() {
        if ("".equals(nameEt.getText().toString().trim())
                || "".equals(idcardEt.getText().toString().trim())
                || "".equals(phoneEt.getText().toString().trim())
//                || "".equals(sexEt.getText().toString().trim())
                ){
            return "请完善报名人员信息";
        }
//        if(!ValidatorUtil.isChineseName(nameEt.getText().toString().trim())){
//            return "报名人员信息";
//        }
        if (!ValidatorUtil.isIDCard(idcardEt.getText().toString().trim())
                || !ValidatorUtil.isChineseName(nameEt.getText().toString().trim())
                || !ValidatorUtil.isMobile(phoneEt.getText().toString().trim())){
            return "报名人员信息填写错误";
        }

        for (int i = 0; i < adapter.getEntities().size(); i++) {
            CampaignEnrollEntity enrollEntity = adapter.getEntities().get(i);
            if ("".equals(enrollEntity.getUsername())
                    || "".equals(enrollEntity.getIdcard())
                    || "".equals(enrollEntity.getSex())) {
                return "请完善随行人员信息";
            }
            if (!ValidatorUtil.isIDCard(enrollEntity.getIdcard())
                    || !ValidatorUtil.isChineseName(enrollEntity.getUsername())) {
                return "随行人员信息填写错误";
            }
            //解决身份证 大小写X问题
            if (MyApplication.getUserInfo().getIdNumber().toUpperCase().equals(enrollEntity.getIdcard().toUpperCase())) {
                return "随行人员身份证号不能是自己的";
            }
        }
        return "1";
    }

    @Override
    public void onHttpSuccess(String type) {

    }

    @Override
    public void onHttpError(String type) {
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getInfomation(CampaignEnrollExtendEntity enrollExtendEntity) {
        aid = enrollExtendEntity.getAid();
        tid = enrollExtendEntity.getId();
        price = enrollExtendEntity.getPrice();
        discountNum = enrollExtendEntity.getDiscountNum();
        initInfomation();
    }
}
