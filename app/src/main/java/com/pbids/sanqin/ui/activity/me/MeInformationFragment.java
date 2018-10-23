package com.pbids.sanqin.ui.activity.me;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonFinalVariable;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.presenter.UserInformationPresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.CircleImageView;
import com.pbids.sanqin.ui.view.dialog.PhotoChosseDialog;
import com.pbids.sanqin.ui.view.dialog.SexChangeDialog;
import com.pbids.sanqin.utils.AppUtils;
import com.pbids.sanqin.utils.FileUtil;
import com.pbids.sanqin.utils.eventbus.CameraEvent;
import com.pbids.sanqin.utils.eventbus.LocationEvent;
import com.pbids.sanqin.utils.eventbus.PermissionEvent;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

import static android.content.ContentValues.TAG;

/**
 * Created by pbids903 on 2017/11/29.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:25
 * @desscribe 类描述:我的个人信息界面
 * @remark 备注:
 * @see
 */
public class MeInformationFragment extends ToolbarFragment implements PhotoChosseDialog.OnPhotoResultLisener
        ,AppToolBar.OnToolBarClickLisenear,MePageView,SexChangeDialog.OnPopSexClickLisenear {
    public static final String ME_LOCATION = "me_location";

    @Bind(R.id.me_information_head)
    CircleImageView meInformationHead;
    @Bind(R.id.me_information_name_layout)
    RelativeLayout meInformationNameLayout;
    @Bind(R.id.me_information_sex_layout)
    RelativeLayout meInformationSexLayout;
    @Bind(R.id.me_information_sex_text)
    TextView meInformationSexText;
    @Bind(R.id.me_information_qr_layout)
    RelativeLayout meInformationQrLayout;
    @Bind(R.id.me_information_location_place)
    TextView meInformationLocationPlace;
    @Bind(R.id.me_information_location_layout)
    RelativeLayout meInformationLocationLayout;
    @Bind(R.id.me_information_level_layout)
    RelativeLayout meInformationLevelLayout;
    @Bind(R.id.me_information_autopragh_layout)
    RelativeLayout meInformationAutopraghLayout;
    @Bind(R.id.me_information_username)
    TextView meInformationUsername;
    @Bind(R.id.me_information_autopragh_content)
    TextView meInformationAutopraghContent;
    @Bind(R.id.me_information_firstname)
    TextView meInformationFirstname;
    @Bind(R.id.me_information_level_name)
    TextView meInformationLevelName;
    @Bind(R.id.ll3)
    LinearLayout ll3;
    @Bind(R.id.me_home_zong)
    ImageView meHomeZong;
    @Bind(R.id.me_information_vip)
    TextView meInformationVip;
    @Bind(R.id.me_information_name)
    TextView meInformationName;
    @Bind(R.id.me_information_nick_name)
    TextView nickNameTv;
    PhotoChosseDialog photoChosseDialog;
    SexChangeDialog sexChangeDialog;
    UserInformationPresenter mePresenter;
    int touxiangSize;
    private String provice="";
    private String city="";

    public static MeInformationFragment newInstance() {
        MeInformationFragment meInformationFragment = new MeInformationFragment();
        Bundle bundle = new Bundle();
        meInformationFragment.setArguments(bundle);
        return meInformationFragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_information, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("我的个人信息",_mActivity);
    }

    public void initView(View view) {
        touxiangSize = (int)_mActivity.getResources().getDimension(R.dimen.dp_120);
        meInformationLevelName.setText(MyApplication.getUserInfo().getLevelName());
        EventBusActivityScope.getDefault(_mActivity).register(this);
//        meInformationHead = (CircleImageView) view.findViewById(R.id.me_information_head);
        updateHeadPortrait();
        initPhotoDialog();
        meInformationHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoChosseDialog.show();
            }
        });

        initUserInformation();
        initLocation();
    }

    public void initLocation(){
        if(AppUtils.checkLocationPermission(_mActivity)){
            MyApplication.getApplication().startLocation();
        }
    }

    private void initPhotoDialog(){
        photoChosseDialog = new PhotoChosseDialog(_mActivity);
        photoChosseDialog.setOnPhotoResultLisener(this);
        photoChosseDialog.setBottomUpAnimation();
        photoChosseDialog.setGrayBottom();
    }

    private void initSexDialog(){
        if(sexChangeDialog == null){
            sexChangeDialog = new SexChangeDialog(_mActivity,-1);
            sexChangeDialog.setOnPopSexClickLisenear(this);
            sexChangeDialog.setGrayCenter();
            sexChangeDialog.setCancelable(true);
        }
        String sex = MyApplication.getUserInfo().getSex();
        switch (sex){
            case "男":
                sexChangeDialog.setSexType(0);
                break;
            case "女":
                sexChangeDialog.setSexType(1);
                break;
            default:
                sexChangeDialog.setSexType(-1);
                break;
        }
        sexChangeDialog.show();
    }

    //初始化用户信息
    public void initUserInformation(){
        UserInfo userInfo = MyApplication.getUserInfo();
        meInformationSexText.setText(userInfo.getSex());
//        meInformationLocationPlace.setText(MyApplication.getUserInfo().getLocation());
        meInformationLocationPlace.setText("正在定位");
        meInformationAutopraghContent.setText(userInfo.getSignature());
        meInformationUsername.setText(userInfo.getName());
        meInformationName.setText(userInfo.getName());
        Log.i("wzh","MyApplication.getUserInfo().getFaceUrl(): "+userInfo.getFaceUrl());
        if(userInfo.getVip()!=0){
            ll3.setVisibility(View.VISIBLE);
            meInformationVip.setText("VIP"+userInfo.getVip());
        }
        if(userInfo.getClanStatus()==1){
            meHomeZong.setVisibility(View.VISIBLE);
        }
        nickNameTv.setText(userInfo.getNickName());
    }

    @Override
    public BasePresenter initPresenter() {
        return mePresenter = new UserInformationPresenter(this);
    }

    public void updateAutograph(){
        meInformationAutopraghContent.setText(MyApplication.getUserInfo().getSignature());
    }

    @Override
    public boolean onBackPressedSupport() {
        if(photoChosseDialog.isShowing()){
            photoChosseDialog.dismiss();
            return true;
        }
        return super.onBackPressedSupport();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ButterKnife.unbind(this);
        if(sexChangeDialog!=null){
            sexChangeDialog.dismiss();
        }
    }

    @OnClick({ R.id.me_information_name_layout
            , R.id.me_information_sex_layout, R.id.me_information_qr_layout
            , R.id.me_information_location_layout, R.id.me_information_level_layout
            , R.id.me_information_autopragh_layout,R.id.me_information_nick_name_layout,R.id.me_information_native_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_information_name_layout:
                Log.i("wzh","meInformationUsername.getText().toString().trim(): "+meInformationUsername.getText().toString().trim());
                MeReviseNameFragment meReviseNameFragment = MeReviseNameFragment.newInstance();
                meReviseNameFragment.getArguments().putString("username",meInformationUsername.getText().toString().trim());
                startForResult(meReviseNameFragment,MeReviseNameFragment.REQUEST_CODE);
                break;
            case R.id.me_information_sex_layout:
                initSexDialog();
                break;
            case R.id.me_information_qr_layout:
                start(MeInformationQR.newInstance());
                break;
            case R.id.me_information_location_layout:
                MeProvinceSelectorFragment fragment = MeProvinceSelectorFragment.newInstance();
                fragment.getArguments().putString("city",city);
                fragment.getArguments().putString("provice",provice);
                start(fragment);
                break;
            case R.id.me_information_level_layout:
                start(MeInformationLevelFragment.newInstance());
                break;
            case R.id.me_information_autopragh_layout:
                start(MeInformationAutograhtFragment.newInstance());
                break;
            case R.id.me_information_nick_name_layout:
                start(MeNickSavaFragment.instance());
                break;
            case R.id.me_information_native_layout:
                start(MeDescentFragment.newInstance());
                break;
        }
    }

    public static String path = "";
    private Uri imageUri;

    //得到图片PATH
    @Subscribe
    public void onCamearEventEvent(CameraEvent activityResultEvent){
        Log.i("wzh","activityResultEvent: "+activityResultEvent.resultCode);
        Log.i("wzh","activityResultEvent.requestCode: "+activityResultEvent.requestCode);
        if(activityResultEvent.resultCode == Activity.RESULT_OK
                && activityResultEvent.requestCode==CommonFinalVariable.REQUEST_CODE_TAKE_PICTURE){
            Log.i("wzh","photoUri: "+photoUri.toString());
            updateFaceUrl(photoUri,1);
        }  else if(activityResultEvent.resultCode == Activity.RESULT_OK
                && activityResultEvent.requestCode == CommonFinalVariable.IMAGE_REQUEST_CODE){
            Log.i("wzh","activityResultEvent.uri: "+activityResultEvent.uri.toString());
            Log.i("wzh","activityResultEvent.resultCode: "+activityResultEvent.resultCode);
            Log.i("wzh","activityResultEvent.uri.getPath(): "+activityResultEvent.uri.getPath());
            imageUri = activityResultEvent.uri;
            updateFaceUrl(activityResultEvent.uri,0);
        }
    }

    @Subscribe
    public void onPermissionEvent(PermissionEvent permissionEvent){
        int request = permissionEvent.getPessionRequestCode();
        Log.i("wzh","request："+request);
        Log.i("wzh","permissionEvent.getPessionResultCode():"+permissionEvent.getPessionResultCode());
        Log.i("wzh","pPermissionEvent.REQUEST_CODE_TAKE_PICTURE_PESSION:"+PermissionEvent.REQUEST_CODE_TAKE_PICTURE_PESSION);
        Log.i("wzh","pPermissionEvent.REQUEST_CODE_IMAGES_PESSION:"+PermissionEvent.REQUEST_CODE_IMAGES_PESSION);
        switch (request){
            case PermissionEvent.REQUEST_CODE_TAKE_PICTURE_PESSION:
//                Log.i("wzh","REQUEST_CODE_TAKE_PICTURE_PESSION");
                photoChosseDialog.dismiss();
                if(permissionEvent.getPessionResultCode()!=PermissionEvent.RESULT_ERROR_CODE){
                    toTakePhoto();
                }
                break;
            case PermissionEvent.REQUEST_CODE_IMAGES_PESSION:
                Log.i("wzh","REQUEST_CODE_IMAGES_PESSION");
                photoChosseDialog.dismiss();
                if(permissionEvent.getPessionResultCode()!=PermissionEvent.RESULT_ERROR_CODE){
                    openAlbum();
                }
                break;
            case PermissionEvent.REQUEST_CODE_LOCATION:
                if(permissionEvent.getPessionResultCode()!=PermissionEvent.RESULT_ERROR_CODE){
//                    meInformationLocationPlace.setText("");
                    MyApplication.getApplication().startLocation();
                }else{
                    meInformationLocationPlace.setText("无法获得定位信息");
                }
                break;
        }
    }

    @Subscribe
    public void onLocationEvent(LocationEvent locationEvent){
        Log.i("wzw","onLocationEvent-------1111");
        if(locationEvent.getProvince()!=null && locationEvent.getCtiy()!=null
                &&!"".equals(locationEvent.getProvince()) && !"".equals(locationEvent.getCtiy())){
            provice = locationEvent.getProvince();
            city = locationEvent.getCtiy();
            meInformationLocationPlace.setText(locationEvent.getProvince()+" "+locationEvent.getCtiy());
        }else{
            meInformationLocationPlace.setText("无法获得定位信息");
        }
    }

    @Override
    public void takePhoto() {
        //只是加了一个uri作为地址传入
        if(AppUtils.checkTakePhotoPermission(_mActivity,PermissionEvent.REQUEST_CODE_TAKE_PICTURE_PESSION)){
            toTakePhoto();
        }
    }

    @Override
    public void openAlem() {
        if(AppUtils.checkOpenAlemPermission(_mActivity,PermissionEvent.REQUEST_CODE_IMAGES_PESSION)){
            openAlbum();
        }
    }

    public void toTakePhoto(){
        takePictureFromCamera();
    }

    Uri photoUri;
    String photoPath;

    private void takePictureFromCamera() {
        File file = getImgFile();
        Log.i("wzh","file.getAbsolutePath(): "+file.getAbsolutePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.i("wzh","asd-----");
            photoUri = FileProvider.getUriForFile(_mActivity, "com.pbids.sanqin.fileprovider", file);
            Log.i("wzh","asd2-----");
            Log.e(TAG,photoUri.getPath());
        } else {
            photoUri = Uri.fromFile(file);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        ComponentName componentName = intent.resolveActivity(_mActivity.getPackageManager());
        if (componentName != null) {
            _mActivity.startActivityForResult(intent, CommonFinalVariable.REQUEST_CODE_TAKE_PICTURE);
        }
    }

    /**
     * 设置文件存储路径，返回一个file
     * @return
     */
    private File getImgFile(){
//        File file = new File(Environment.getExternalStorageDirectory()+"/sanqin/images");
        File file = new File(FileUtil.PATH_IMAGES);
        if (!file.exists()){
            //要点！
            file.mkdirs();
        }
        String fileName="img_"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".jpg";
        File imgFile = new File(file,fileName);
        photoPath = imgFile.getAbsolutePath();
        return imgFile;
    }

    private final String IMAGE_TYPE = "image/*";

    public void openAlbum(){
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(IMAGE_TYPE);
        if (Build.VERSION.SDK_INT <19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        _mActivity.startActivityForResult(intent, CommonFinalVariable.IMAGE_REQUEST_CODE);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if(requestCode == MeReviseNameFragment.REQUEST_CODE){
//            meInformationUsername.setText();
            if(data!=null){
                String nameText = data.getString(MeReviseNameFragment.BUNDLE_KEY);
                if(nameText!=null && !"".equals(nameText)){
                    meInformationUsername.setText(nameText);
                }
            }
        }
        super.onFragmentResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
        }
    }

    //所在地更新
    public void locationCall(String location,String locationId){
        if(location!=null && !"".equals(location)){
            String[] strings = location.split("[,]");
            String str="";
            for(int i =0;i<strings.length;i++){
                str= str+" "+strings[i];
            }
            meInformationLocationPlace.setText(str);
            HttpParams params = new HttpParams();
            params.put("userId",MyApplication.getUserInfo().getUserId());
            params.put("location",location);
            params.put("locationId",locationId);
//            Disposable tring = OkGo.<String>post(SERVER_ADDRESS_USER+ADDRESS_ME_UPDATE_USERINFORMATION)
//                    .headers("token", MyApplication.getUserInfo().getToken())
//                    .params(params)
//                    .converter(new StringConvert())
//                    .adapt(new ObservableResponse<String>())
//                    .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Response<String>>() {
//                        @Override
//                        public void accept(@NonNull Response<String> stringResponse) throws Exception {
//                            Log.i("wzh","stringResponse: "+stringResponse.body());
//                        }
//                    });
            mePresenter.reviseUserInformation(params,MePageView.ME_REVICE_LOCATION);
        }
    }

    //上传头像uri
    public void updateFaceUrl(Uri uri, int type){
        Log.i("wzh","updateFaceUrl: "+type);
        final File[] files = new File[1];
        if(type==0){
            Glide.with(_mActivity).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    files[0] = FileUtil.saveMyBitmap(resource);
                    upImage(files);
                }
            });
//            Glide.with(_mActivity).load(imageUri).asBitmap().override(touxiangSize,touxiangSize)
//                    .placeholder(R.drawable.me_avatar_moren_default).error(R.drawable.me_avatar_moren_default).dontAnimate()
//                    .into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    files[0] = FileUtil.saveMyBitmap(resource);
//                    upImage(files);
//                }
//            });
        }else if(type == 1){
            Glide.with(_mActivity).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    files[0] = FileUtil.saveMyBitmap(resource);
                    upImage(files);
                }
            });
//            Glide.with(_mActivity).load(uriStr).asBitmap().override(touxiangSize,touxiangSize)
//                    .placeholder(R.drawable.me_avatar_moren_default).error(R.drawable.me_avatar_moren_default).dontAnimate()
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            files[0] = FileUtil.saveMyBitmap(resource);
//                            upImage(files);
//                        }});
//            files[0] = new File(uriStr);
//            upImage(files);
        }
    }

    public void upImage(File[] files){
//        if(files[0]==null){
//
//        }
        final HttpParams params = new HttpParams();
//        String base64Str="";
//        try {
//            if(files[0]!=null){
//                base64Str = FileUtil.encodeBase64File(files[0]);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i("wzh","Exception: "+e.getMessage());
//        }
//        if(!"".equals(base64Str)){
//            Log.i("wzh","files[0].getAbsolutePath(): "+files[0].getAbsolutePath());
//            String[] strings = files[0].getAbsolutePath().split("[.]");
//            String str = strings[strings.length-1];
//            String data = "data:image/"+str+";base64,";
            params.put("userId",MyApplication.getUserInfo().getUserId());
//            params.put("faceUrl",data+base64Str);
            params.put("faceImg",files[0]);
            mePresenter.reviseUserInformation(params,ME_REVICE_PICTURE);
//        }
    }

    //更新性别
    public void updateSex(int type){
        String sexStr="";
        switch (type){
            case CommonFinalVariable.SEX_TYPE_MAN:
                sexStr = "男";
                break;
            case CommonFinalVariable.SEX_TYPE_WOMEN:
                sexStr = "女";
                break;
        }
        if(!"".equals(sexStr)){
            meInformationSexText.setText(sexStr);
            HttpParams params = new HttpParams();
            params.put("userId",MyApplication.getUserInfo().getUserId());
            params.put("sex",sexStr);
            mePresenter.reviseUserInformation(params,MePageView.ME_REVICE_SEX);
        }
    }

    public void updateHeadPortrait(){
        new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity
                ,meInformationHead,MyApplication.getUserInfo().getFaceUrl(), new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                if(meInformationFirstname!=null){
                    meInformationFirstname.setVisibility(View.VISIBLE);
                    Typeface typeface = Typeface.createFromAsset(_mActivity.getAssets(), "fonts/汉仪颜楷繁.ttf");
                    meInformationFirstname.getPaint().setTypeface(typeface);
//                MyApplication.getUserInfo().setSurname("速度");
                    if(MyApplication.getUserInfo().getSurname().length()==1){
                        meInformationFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_15));
                        meInformationFirstname.setLineSpacing(0,0.9F);
                    }else if(MyApplication.getUserInfo().getSurname().length()==2){
                        meInformationFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_11));
                        meInformationFirstname.setLineSpacing(0,0.8F);
                    }
                    meInformationFirstname.setText(MyApplication.getUserInfo().getSurname()+"府");
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                if(meInformationFirstname!=null){
                    meInformationFirstname.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
//        Glide.with(_mActivity).load(MyApplication.getUserInfo().getFaceUrl())
//                .placeholder(R.drawable.me_avatar_moren_default).error(R.drawable.me_avatar_moren_default_)
//                .dontAnimate()
//                .override(touxiangSize,touxiangSize).listener(new RequestListener<String, GlideDrawable>() {
//            @Override
//            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                meInformationFirstname.setVisibility(View.VISIBLE);
//                Typeface typeface = Typeface.createFromAsset(_mActivity.getAssets(), "fonts/华文行楷.ttf");
//                meInformationFirstname.getPaint().setTypeface(typeface);
////                MyApplication.getUserInfo().setSurname("速度");
//                if(MyApplication.getUserInfo().getSurname().length()==1){
//                    meInformationFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_15));
//                    meInformationFirstname.setLineSpacing(0,0.9F);
//                }else if(MyApplication.getUserInfo().getSurname().length()==2){
//                    meInformationFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_11));
//                    meInformationFirstname.setLineSpacing(0,0.8F);
//                }
//                meInformationFirstname.setText(MyApplication.getUserInfo().getSurname()+"府");
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                meInformationFirstname.setVisibility(View.INVISIBLE);
//                return false;
//            }
//        }).into(meInformationHead);
    }

    @Override
    public void reviseSuccess(String type) {
        //Todo 626行发生空指针异常
        try{
            MainFragment mainFragment = findFragment(MainFragment.class);
            MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
            switch (type){
                case MePageView.ME_REVICE_LOCATION:
                    break;
                case MePageView.ME_REVICE_NAME:
                    meFragment.updateName();
                    break;
                case MePageView.ME_REVICE_PICTURE:
//                Glide.with(_mActivity).load(MyApplication.getUserInfo().getFaceUrl())
//                        .placeholder(R.drawable.me_avatar_moren_default).error(R.drawable.me_avatar_moren_default)
//                        .dontAnimate()
//                    .override(touxiangSize,touxiangSize).into(meInformationHead);
                    updateHeadPortrait();
                    meFragment.updateHeadPortrait();
                    if(photoChosseDialog!=null){
                        photoChosseDialog.dismiss();
                    }
                    break;
                case MePageView.ME_REVICE_SEX:
//                initSexDialog();
                    sexChangeDialog.dismiss();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateNikcName(){
        nickNameTv.setText(MyApplication.getUserInfo().getNickName());
    }

    @Override
    public void reviseError(String type) {
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(int type) {
        updateSex(type);
    }

}
