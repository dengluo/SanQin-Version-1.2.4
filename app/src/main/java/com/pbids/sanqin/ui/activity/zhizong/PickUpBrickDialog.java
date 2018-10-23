package com.pbids.sanqin.ui.activity.zhizong;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.PickBrick;
import com.pbids.sanqin.ui.activity.zhizong.component.PickUpBrickView;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;


/**
 * @author : 上官名鹏
 *         Description : 领取砖头
 *         Date :Create in 2018/6/12 11:21
 *         Modified By :
 */
public class PickUpBrickDialog extends Dialog {

    private static final String TAG = PickUpBrickDialog.class.getSimpleName();
    @Bind(R.id.pick_up_brick_number)
    TextView pickUpBrickNumber;

    private Context mContext;

    private PickUpBrickView pickUpBrickView;

    private int number;


    public PickUpBrickDialog(@NonNull Context context, int themeResId, PickUpBrickView pickUpBrickView , int number) {
        super(context, themeResId);
        this.mContext = context;
        this.pickUpBrickView = pickUpBrickView;
        this.number = number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_pick_up_brick, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        pickUpBrickNumber.setText("砖头 X " + number);
        setCanceledOnTouchOutside(false);
    }

    @OnClick({R.id.pick_up_brick_lingqu_bt, R.id.pick_up_brick_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pick_up_brick_lingqu_bt:
                DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
                    @Override
                    public void onNext(Response<String> stringResponse) {
                        String body = stringResponse.body();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            int status = jsonObject.getInt("status");
                            if(status == 1){
                                dismiss();
                                pickUpBrickView.pickBrickSuccess("领取成功");
                            }else{
                                dismiss();
                                pickUpBrickView.pickBrickSuccess("领取失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        pickUpBrickView.pickBrickFailed("请求失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                };
                HttpParams httpParams = new HttpParams();
                long userId = MyApplication.getUserInfo().getUserId();
                OkGoUtil.getStringObservableForPost(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_PICK_UP_BRICK+"id="+userId, httpParams, observer);
                break;
            case R.id.pick_up_brick_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
