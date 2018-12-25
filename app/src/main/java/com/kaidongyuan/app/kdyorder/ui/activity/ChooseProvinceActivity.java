package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.ProvincesAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.NormalAddress;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.ChooseProvinceActivityBiz;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.List;


/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/1.
 */
public class ChooseProvinceActivity extends BaseActivity implements View.OnClickListener {
    private ListView lv_proviences;
    private TextView tv_nodata;
    private ProvincesAdapter madapter;
    private List<NormalAddress> addresses;
    private String strtype=EXTRAConstants.EXTRA_PROVINCE;
    private final int mRequestcode=1003;
    private NormalAddress mProvince,mCity,mArea,mRural;
    private ChooseProvinceActivityBiz mBiz;
    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseprovince);
        init();
        initView();
        getProvinces();
        mImageViewGoBack.setOnClickListener(this);
    }

    private void init() {
        try {
            mBiz =new ChooseProvinceActivityBiz(this);
        }catch (Exception ex){
            ExceptionUtil.handlerException(ex);
        }

    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_goback:
                    this.finish();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }




//    XML解析
//    private List<String> getXMLProvinces() {
//        ArrayList<String> strArray =new ArrayList<>();
//        XmlResourceParser XmlParser = getResources().getXml(R.xml.address);//从xml资源文件夹加载address.xml文件
//        try {
//            int type=XmlParser.getEventType();
//            while (type!=XmlResourceParser.END_DOCUMENT){
//                switch (type){
//                    case XmlResourceParser.START_DOCUMENT:      //开始节点(  )
//                        Logger.w("开始解析");
//                        break;
//                    case XmlResourceParser.START_TAG:
//                        if ("province".equals(XmlParser.getName())){
//                            strArray.add(XmlParser.getAttributeValue(null,"name"));
//                        }
//                        break;
//                    case XmlResourceParser.END_TAG:
//                        if ("Movie".equals(XmlParser.getName())) {  //结束元素（</Movie>）
//                           Logger.w("解析结束");
//                        }
//                        break;
//                    default:
//                        break;
//                }
//                //获取下一个解析事件（（即开始文档，结束文档，开始标签，结束标签））
//                type = XmlParser.next();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return strArray;
//    }

    private void initView() {
        mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        lv_proviences= (ListView) this.findViewById(R.id.lv_provinces);
        tv_nodata= (TextView) this.findViewById(R.id.textview_nodata);
        tv_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (strtype){
                    case EXTRAConstants.EXTRA_PROVINCE: getProvinces();
                        break;
                    case EXTRAConstants.EXTRA_CITY:getCitys();
                        break;
                    case EXTRAConstants.EXTRA_AREA:getAreas();
                        break;
                    case EXTRAConstants.EXTRA_RURAL:getRurals();
                        break;
                }

            }
        });
        madapter=new ProvincesAdapter();
        lv_proviences.setAdapter(madapter);
        lv_proviences.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (strtype==null||strtype.isEmpty()){
                    ChooseProvinceActivity.this.finish();
                    ToastUtil.showToastBottom("收货地址配置异常，请退出重进",Toast.LENGTH_SHORT);
                    return;
                }
                switch (strtype){
                    case EXTRAConstants.EXTRA_PROVINCE:
                         mProvince = addresses.get(i);
                         ToastUtil.showToastBottom(mProvince.getITEM_NAME(),Toast.LENGTH_SHORT);
                         break;
                    case EXTRAConstants.EXTRA_CITY:
                        mCity = addresses.get(i);
                        break;
                    case EXTRAConstants.EXTRA_AREA:
                        mArea=addresses.get(i);
                        break;
                    case EXTRAConstants.EXTRA_RURAL:
                        mRural=addresses.get(i);
                        break;
                    default:
                        break;
                }
                if (strtype.equals(EXTRAConstants.EXTRA_PROVINCE)){
                    strtype=EXTRAConstants.EXTRA_CITY;
                    getCitys();
                }else if (strtype.equals(EXTRAConstants.EXTRA_CITY)){
                    strtype=EXTRAConstants.EXTRA_AREA;
                    getAreas();
                }else if (strtype.equals(EXTRAConstants.EXTRA_AREA)){
                    strtype=EXTRAConstants.EXTRA_RURAL;
                    getRurals();
                }else if (strtype.equals(EXTRAConstants.EXTRA_RURAL)){
                    Intent intent1=new Intent();
                    intent1.putExtra("province",mProvince);
                    intent1.putExtra("city",mCity);
                    intent1.putExtra("area",mArea);
                    intent1.putExtra("rural",mRural);
                    ChooseProvinceActivity.this.setResult(RESULT_OK,intent1);
                    finish();
                }
            }
        });
    }


    private void getProvinces(){
        if (mBiz.getAddresses("0", EXTRAConstants.EXTRA_PROVINCE)){
            lv_proviences.setVisibility(View.VISIBLE);
            tv_nodata.setVisibility(View.GONE);
        }else {
            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            lv_proviences.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
        }
    }
    private void getCitys(){
        if (mProvince==null||mProvince.getITEM_IDX().isEmpty()){
            strtype=EXTRAConstants.EXTRA_PROVINCE;
            getProvinces();
        }
        if (mBiz.getAddresses(mProvince.getITEM_IDX(), EXTRAConstants.EXTRA_CITY)){
            lv_proviences.setVisibility(View.VISIBLE);
            tv_nodata.setVisibility(View.GONE);
        }else {
            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            lv_proviences.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
        }
    }
    private void getAreas(){
        if (mCity==null||mCity.getITEM_IDX().isEmpty()){
            strtype=EXTRAConstants.EXTRA_CITY;
            getCitys();
        }
        if (mBiz.getAddresses(mCity.getITEM_IDX(), EXTRAConstants.EXTRA_AREA)){
            lv_proviences.setVisibility(View.VISIBLE);
            tv_nodata.setVisibility(View.GONE);
        }else {
            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            lv_proviences.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
        }
    }
    private void getRurals(){
        if (mArea==null||mArea.getITEM_IDX().isEmpty()){
            strtype=EXTRAConstants.EXTRA_AREA;
            getCitys();
        }
        if (mBiz.getAddresses(mArea.getITEM_IDX(), EXTRAConstants.EXTRA_AREA)){
            lv_proviences.setVisibility(View.VISIBLE);
            tv_nodata.setVisibility(View.GONE);
        }else {
            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            lv_proviences.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
        }
    }
    public void getAddressError(String msg,String extra_type) {
        try {
            strtype=extra_type;
            if (msg != null) {
                ToastUtil.showToastBottom(msg, Toast.LENGTH_SHORT);
                lv_proviences.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    public void getAddressSuccess(List<NormalAddress> normalAddressList,String extratype) {
         try {
             addresses=normalAddressList;
             madapter.setData(addresses);
             lv_proviences.setVisibility(View.VISIBLE);
             tv_nodata.setVisibility(View.GONE);
         }catch (Exception e){
             ExceptionUtil.handlerException(e);
         }

    }


}
