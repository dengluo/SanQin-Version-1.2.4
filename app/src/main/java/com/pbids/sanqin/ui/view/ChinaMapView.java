package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.Provice;
import com.pbids.sanqin.utils.PathParser;
import com.pbids.sanqin.utils.eventbus.OnLoadMapDistributionDataEvent;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by ZX on 2017/8/23.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:14
 * @desscribe 类描述:中国地图
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeMamberDistributionFragment
 */
public class ChinaMapView extends View {

//    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFFB0D7F8};
    private int[] colorArray = new int[]{0xFFA42121, 0xFFCB3131, 0xFFE0712F, 0xFFFBD477,0xFFFFE5CC};

    private Context context;
    private Paint paint;

    private List<String> proviceId;
    //省份数组
    private List<Provice> proviceList;
    //选中的省份
    private Provice selectItem;
    //缩放比例
    private float scale = 0f;
    private float mapWidth = 773.0f, mapHeight = 568.0f;

//    Map<String,Provice> proviceMap;
    private SparseArray<Provice> proviceMap;

    public ChinaMapView(Context context) {
        this(context, null);
    }

    public ChinaMapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChinaMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

//    public ChinaMapView(Context mContext, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(mContext, attrs, defStyleAttr, defStyleRes);
//    }

    private void init(Context context) {

        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);

        proviceList = new ArrayList<>();
        proviceId = new ArrayList<>();
        proviceMap = new SparseArray<Provice>();
        loadThread.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        scale = Math.min(width/mapWidth, height/mapHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (proviceList != null) {
            canvas.scale(scale, scale);
            for(int i=0;i<proviceList.size();i++){
                Provice item = proviceList.get(i);
                if (item != selectItem) {
                    item.draw(canvas, paint, false);
                }
            }
//            for (Provice item : proviceList) {
//                if (item != selectItem) {
//                    item.draw(canvas, paint, false);
//                }
//            }
            if (selectItem != null) {
                selectItem.draw(canvas, paint, true);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (proviceList != null) {
            Provice provice = null;
            for(int i=0;i<proviceList.size();i++){
                Provice item = proviceList.get(i);
                if (item.isSelect((int) (event.getX() / scale), (int) (event.getY() / scale))) {
                    provice = item;
                    Log.i("wzh","id: "+proviceId.get(i));
                    break;
                }
            }
//            for ( Provice item : proviceList) {
//                if (item.isSelect((int) (event.getX() / scale), (int) (event.getY() / scale))) {
//                    provice = item;
//                    break;
//                }
//            }
            if (provice != null) {
                selectItem = provice;
                postInvalidate();
            }
        }
        return true;
    }

    private Thread loadThread=new Thread(){
        @Override
        public void run() {
            InputStream inputStream = context.getResources().openRawResource(R.raw.china_svg);
            try {
                //取得 DocumentBuilderFactory 实例
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                //从 factory 获取 DocumentBuilder 实例
                DocumentBuilder builder = factory.newDocumentBuilder();
                //解析输入流,得到 Document 实例
                Document doc = builder.parse(inputStream);
                Element rootElement = doc.getDocumentElement();
                NodeList items = rootElement.getElementsByTagName("path");

                for (int i = 0; i < items.getLength(); i ++) {

                    Element element= (Element) items.item(i);
                    String pathData=element.getAttribute("android:pathData");
                    String id=element.getAttribute("android:id");
                    Path path = PathParser.createPathFromPathData(pathData);
                    Provice provice = new Provice(path);
                    proviceList.add(provice);
                    proviceId.add(id);
                    proviceMap.put(Integer.valueOf(id),provice);
                }

                Message message = new Message();
                message.what=1;
                handler.sendMessage(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
//            handler.sendEmptyMessage(0);

        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Log.i("proviceList","")
            switch (msg.what){
                case 1:
                    if (proviceList == null) {
                        return;
                    }
                    /*for (int i = 0; i < proviceList.size(); i++) {
                        int color;
                        int flag =  i%4;
                        switch (flag) {
                            case 1:
                                color = colorArray[0];
                                break;
                            case 2:
                                color = colorArray[1];
                                break;
                            case 3:
                                color = colorArray[2];
                                break;
                            default:
                                color = colorArray[3];
                                break;
                        }
                        proviceList.get(i).setDrawColor(color);
                        //?proviceList.get(i).setDrawColor(colorArray[3]);
                    }*/

                    //postInvalidate();
                    invalidate();
                    OnLoadMapDistributionDataEvent evt = new OnLoadMapDistributionDataEvent(true);
                    EventBus.getDefault().post(evt);

            }
        }
    };

    public SparseArray<Provice> getProviceMap() {
        return proviceMap;
    }

    public int[] getColorArray() {
        return colorArray;
    }

}
