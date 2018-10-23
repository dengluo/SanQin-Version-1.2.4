package com.pbids.sanqin.ui.activity.me;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.InviteInfo;
import com.pbids.sanqin.presenter.MeInvitePresenter;
import com.pbids.sanqin.presenter.MeInviteView;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.common.util.ToastUtils;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.SharedUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * @author : 上官名鹏
 * Description : 新人邀请
 * Date :Create in 2018/6/16 16:08
 * Modified By :
 */

public class MeInviteFragment extends BaaseToolBarFragment implements MeInviteView {
    @Bind(R.id.invite_step_bg)
    ImageView inviteStepBg;
    @Bind(R.id.invite_poster_bg)
    ImageView invitePosterBg;
    @Bind(R.id.invite_down_arrow)
    ImageView inviteDownArrow;
    @Bind(R.id.invite_poster_share)
    Button invitePosterShare;
    @Bind(R.id.post_down_iv)
    ImageView postDownIv;
    @Bind(R.id.me_invite_role)
    TextView meInviteRole;

    private MeInvitePresenter meInvitePresenter;
    private Bitmap posterBitmap;
    public static final String BTMESSAGE = "保存高清海报";
    private String imageUrl;
    private File file;
    private PostDownDialog postDownDialog;
    private ImageView postDownStepIv;


    // 1 没有获取海报前   2 获取海报后
    private int step = 1;

    private String inviteUrl = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_INVITE_REGISTER + MyApplication.getUserInfo().getUserId();


    public static MeInviteFragment instance() {
        MeInviteFragment meInviteFragment = new MeInviteFragment();
        return meInviteFragment;
    }


    PlatformActionListener listener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            //转发量加1
            showToast("分享成功");
            // isCancel = true;
            // setClickable(true);
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            // isCancel = true;
            // setClickable(true);
            showToast("分享错误");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            // isCancel = true;
            // setClickable(true);
            showToast("分享返回");
        }
    };


    @Override
    protected BaasePresenter initPresenter() {
        return meInvitePresenter = new MeInvitePresenter(this);
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_invite, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
//        String url = inviteUrl;
//        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
//        Bitmap whiteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_bg);
//        Bitmap qrImage = null;
//        try {
//            Bitmap bitmap = ZXingUtils.modifyLogo(whiteBitmap, logoBitmap);
//            qrImage = ZXingUtils.createCode(url, bitmap);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        codeImg.setImageBitmap(qrImage);
        //邀请规则设置下划线
        meInviteRole.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        Glide.with(_mActivity).load(R.drawable.invite_step).into(inviteStepBg); //设置图片  --- 记得压缩
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("新人邀请", _mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public void onClick(View v) {
        pop();
    }

    @OnClick({R.id.share_weibo, R.id.share_wx, R.id.share_wx_friend, R.id.invite_poster_share, R.id.me_invite_role})
    public void onclickView(View view) {
//        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.sina_weibo_share_img);
        String btMessage = (String) invitePosterShare.getText();
        InviteInfo inviteInfo = new InviteInfo();

        inviteInfo.setTitle("#求围观，您的好友喊你来华亲池！#快注册找到你的小伙伴吧");
//        inviteInfo.setImg(logo);
        inviteInfo.setContent("#求围观，您的好友喊你来华亲池！#快注册找到你的小伙伴吧");
        inviteInfo.setLink(inviteUrl);
        if (step == 2) {
            //海报
            inviteInfo.setTitle("");
            inviteInfo.setContent("");
            inviteInfo.setImgUrl(imageUrl);
            inviteInfo.setImg(posterBitmap);
        } else {
            //logo
            inviteInfo.setImgUrl("http://web.huaqinchi.com:8080/img/share_weibo.png");
//            inviteInfo.setImg(logo);
        }
        //分享是判断 有没有获取海报
        switch (view.getId()) {
            case R.id.share_weibo:
                //微博
                if (btMessage.equals(BTMESSAGE)) {
                    SharedUtils.sharedSinaWeibo(getContext(), imageUrl, listener);
                } else {
                    SharedUtils.sharedSinaWeibo(getContext(), inviteInfo, listener);
                }
                break;
            case R.id.share_wx:
                // 微信
                if (btMessage.equals(BTMESSAGE)) {
                    SharedUtils.sharedWechat(getContext(), imageUrl, listener);
                } else {
                    SharedUtils.sharedWechat(getContext(), inviteInfo, listener);
                }
                break;
            case R.id.share_wx_friend:
                // 朋友圈
                if (btMessage.equals(BTMESSAGE)) {
                    shareToWeChat(getContext());
                } else {
                    SharedUtils.sharedWechatMoments(getContext(), inviteInfo, listener);
                }
                break;
            case R.id.invite_poster_share:
                //保存图片到本地
                if (btMessage.equals(BTMESSAGE)) {
                    if (posterBitmap != null) {
                        boolean isSuccess = saveImageToGallery(getContext(), posterBitmap);
                        if (isSuccess) {
                            showToast("图片保存成功！");
                        } else {
                            showToast("图片保存异常！");
                        }
                    }
                } else {
                    //加载海报文件
                    HttpParams httpParams = new HttpParams();
                    String name = MyApplication.getUserInfo().getName();
                    httpParams.put("name", name);
                    httpParams.put("url", inviteUrl);
                    if (postDownDialog == null) {
                        postDownDialog = new PostDownDialog(getContext(), R.style.post_down_dialog);
                    }
                    postDownDialog.show();
                    //获取空间，加载gif图
                    postDownStepIv = postDownDialog.getPostDownStepIv();
                    intoImgGif(R.drawable.post_down, postDownStepIv);
                    meInvitePresenter.posterUrl(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_INVITE_GETIMAGESURL, httpParams);
                }
                break;
            case R.id.me_invite_role:
                //显示 邀请规则
                MeInviteRuleDialog meInviteRuleDialog = new MeInviteRuleDialog(getContext());
                //showDialog(meInviteRuleDialog, 1,R.dimen.dp_472);  // getResources().getDimensionPixelSize(R.dimen.dp_370)
                showDialog(meInviteRuleDialog, 0.90, getResources().getDimensionPixelSize(R.dimen.dp_422));
                break;
            default:
                break;
        }

    }

    // 获取海报完成后
    @Override
    public void generatingPoster(File file) {
        try {
            this.file = file;
            posterBitmap = BitmapFactory.decodeFile(String.valueOf(file));
            inviteStepBg.setVisibility(View.GONE);
            inviteDownArrow.setVisibility(View.VISIBLE);
            invitePosterBg.setVisibility(View.VISIBLE);
            invitePosterBg.setBackground(null);
            invitePosterBg.setImageBitmap(posterBitmap);
//        Glide.with(_mActivity).load(this.file).intso(invitePosterBg); //设置图片  使用这个方法一直会显示之前的图片
            //invitePosterShare.setText("图片保存到本地");
            invitePosterShare.setText(BTMESSAGE);
            //标记 记获取海报
            step = 2;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //取到url 上传图片
    @Override
    public void getUrlSuccess(String imageUrl) {
        Log.i("url", imageUrl);
        this.imageUrl = imageUrl;
//        getLoadingPop("正在生成海报...").show();
        meInvitePresenter.generatingPoster(getContext(), imageUrl);
    }

    @Override
    public void error() {
        showToast("获取海报地址失败，请重试");
    }

    @Override
    public void dismissDown() {
//        dismiss();
        postDownDialog.dismiss();

    }

    /**
     * 生成海报回调
     *
     * @param schedule
     */
    @Override
    public void downSchedule(int schedule) {
//        if(schedule==20){
//            intoImg(R.drawable.post_down_20,postDownStepIv);
//        }else if(schedule==40){
//            intoImg(R.drawable.post_down_40,postDownStepIv);
//        }else if(schedule==60){
//            intoImg(R.drawable.post_down_60,postDownStepIv);
//        }else if(schedule==80){
//            intoImg(R.drawable.post_down_80,postDownStepIv);
//        }else if(schedule==95){
//            intoImg(R.drawable.post_down_95,postDownStepIv);
//        }else if(schedule==96){
//            intoImg(R.drawable.post_down_96,postDownStepIv);
//        }else if(schedule==97){
//            intoImg(R.drawable.post_down_97,postDownStepIv);
//        }else if(schedule==98){
//            intoImg(R.drawable.post_down_98,postDownStepIv);
//        }else if(schedule==99){
//            intoImg(R.drawable.post_down_99,postDownStepIv);
//        }else if(schedule==100){
//            intoImg(R.drawable.post_down_100,postDownStepIv);
//        }
    }

    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean checkInstallation(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void shareToWeChat(Context context) {
        // TODO: 2015/12/13 将需要分享到微信的图片准备好
        try {
            if (!checkInstallation(context, "com.tencent.mm")) {
                return;
            }
            Intent intent = new Intent();
            //分享精确到微信的页面，朋友圈页面，或者选择好友分享页面
            ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
            intent.setComponent(comp);
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");
//        intent.setType("text/plain");
            //添加Uri图片地址
//        String msg=String.format(getString(R.string.share_content), getString(R.string.app_name), getLatestWeekStatistics() + "");
            String msg = context.getString(R.string.avchat_invalid_channel_id);
            intent.putExtra("Kdescription", msg);
            ArrayList<Uri> imageUris = new ArrayList<Uri>();
            // TODO: 2016/3/8 根据不同图片来设置分享
            File dir = context.getExternalFilesDir(null);
            if (dir == null || dir.getAbsolutePath().equals("")) {
                dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            }
            File pic = new File(dir, "bigbang.jpg");
            pic.deleteOnExit();
            Bitmap bitmapDrawable;
            if (Build.VERSION.SDK_INT < 22) {
                bitmapDrawable = posterBitmap;
            } else {
                bitmapDrawable = posterBitmap;
            }
            try {
                bitmapDrawable.compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(pic));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUris.add(Uri.fromFile(pic));
            } else {
                //修复微信在7.0崩溃的问题
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), pic.getAbsolutePath(), "bigbang.jpg", null));
                imageUris.add(uri);
            }

            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            ((Activity) context).startActivityForResult(intent, 1000);
        } catch (Throwable e) {
            ToastUtils.showShortToast(context, "失败");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
