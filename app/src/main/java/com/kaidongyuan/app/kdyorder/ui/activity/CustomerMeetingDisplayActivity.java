package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.GridImageAdapter;
import com.kaidongyuan.app.kdyorder.adapter.LineChoiceAdapter;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeetingLine;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.model.CustomerMeetingDisplayActivityBiz;
import com.kaidongyuan.app.kdyorder.model.CustomerMeetingsActivityBiz;
import com.kaidongyuan.app.kdyorder.util.BitmapUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerMeetingDisplayActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 对应业务类
     */
    private CustomerMeetingDisplayActivityBiz mBiz;

    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;

    private LinearLayout llMeetingDisplay;

    /**
     * 选择生动化陈列的 Dialog
     */
    private Dialog mChoiceDisplayDialog;

    /**
     * 显示生动化陈列的 ListView
     */
    private ListView mListViewChoiceDisplays;

    /**
     * 选择生动化陈列的 Adapter
     */
    private LineChoiceAdapter mDisplaysChoiceAdapter;

    private TextView strVividDisplayCbx;

    // 照片
    private List<LocalMedia> selectList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    /**
     * 　签名和照片文件宽度 单位（px）
     */
    private final int mBitmapWidth = 400;

    // 备注
    private EditText remark;

    private final String mTagDisplay = "mTagDisplay";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_display);

        initView();
        setListener();
        mBiz = new CustomerMeetingDisplayActivityBiz(this);
        mBiz.VividDisplayCBX();
    }

    private void initView() {
        mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        llMeetingDisplay = (LinearLayout) findViewById(R.id.llMeetingDisplay);
        strVividDisplayCbx = (TextView) this.findViewById(R.id.strVividDisplayCbx);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        remark = (EditText) findViewById(R.id.display_mark);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(CustomerMeetingDisplayActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(CustomerMeetingDisplayActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            int themeId = R.style.picture_default_style;
                            // 预览图片 可自定长按保存路径
                            PictureSelector.create(CustomerMeetingDisplayActivity.this).externalPicturePreview(position, selectList);
                            break;
                    }
                }
            }
        });
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            llMeetingDisplay.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 单独拍照
            PictureSelector.create(CustomerMeetingDisplayActivity.this)
                    .openCamera(PictureMimeType.ofImage())
                    .selectionMedia(selectList)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }

    };

    private String changeImageToString(int index) {
        LocalMedia LM = selectList.get(index);
        Bitmap bit = BitmapUtil.resizeImage(LM.getPath(), mBitmapWidth);
        Log.d("LM", "生动画图片" + index + "大小|" + BitmapUtil.getBitmapSize(bit));
        if (bit != null) {
            return BitmapUtil.changeBitmapToString(bit);
        } else {
            return "";
        }
    }

    public void confirmOnclick(View view) {

        final CustomerMeeting customerM = (CustomerMeeting) getIntent().getParcelableExtra("CustomerMeeting");
        final String displayCbx = strVividDisplayCbx.getText().toString();
        if (displayCbx.equals("")) {
            ToastUtil.showToastBottom(String.valueOf("请选择生成化陈列类型"), Toast.LENGTH_SHORT);
            return;
        }
        if(selectList.size() <= 0) {
            ToastUtil.showToastBottom(String.valueOf("请拍照片"), Toast.LENGTH_SHORT);
            return;
        }
        showLoadingDialog();

        String image1 = "";
        String image2 = "";
        String image3 = "";
        String image4 = "";
        String image5 = "";
        String image6 = "";
        String image7 = "";
        String image8 = "";
        String image9 = "";

        if (selectList.size() > 0) {
            image1 = changeImageToString(0);
        }
        if (selectList.size() > 1) {
            image2 = changeImageToString(1);
        }
        if (selectList.size() > 2) {
            image3 = changeImageToString(2);
        }
        if (selectList.size() > 3) {
            image4 = changeImageToString(3);
        }
        if (selectList.size() > 4) {
            image5 = changeImageToString(4);
        }
        if (selectList.size() > 5) {
            image6 = changeImageToString(5);
        }
        if (selectList.size() > 6) {
            image7 = changeImageToString(6);
        }
        if (selectList.size() > 7) {
            image8 = changeImageToString(7);
        }
        if (selectList.size() > 8) {
            image9 = changeImageToString(8);
        }

        final String PictureFile1 = image1;
        final String PictureFile2 = image2;
        final String PictureFile3 = image3;
        final String PictureFile4 = image4;
        final String PictureFile5 = image5;
        final String PictureFile6 = image6;
        final String PictureFile7 = image7;
        final String PictureFile8 = image8;
        final String PictureFile9 = image9;

        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetVisitVividDisplay, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(this.getClass() + ".Display:" + response);
                    DisplaySuccess(response, customerM);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(this.getClass() + ".Display:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mLoadingDialog.dismiss();
                        ToastUtil.showToastBottom(String.valueOf("请求失败!"), Toast.LENGTH_SHORT);
                    } else {
                        mLoadingDialog.dismiss();
                        ToastUtil.showToastBottom(String.valueOf("请检查网络是否正常连接！"), Toast.LENGTH_SHORT);
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strVisitIdx", customerM.getVISIT_IDX());
                    params.put("strVividDisplayCbx", displayCbx);
                    params.put("strVividDisplayText", remark.getText().toString());
                    params.put("PictureFile1", PictureFile1);
                    params.put("PictureFile2", PictureFile2);
                    params.put("PictureFile3", PictureFile3);
                    params.put("PictureFile4", PictureFile4);
                    params.put("PictureFile5", PictureFile5);
                    params.put("PictureFile6", PictureFile6);
                    params.put("PictureFile7", PictureFile7);
                    params.put("PictureFile8", PictureFile8);
                    params.put("PictureFile9", PictureFile9);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagDisplay);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            super.onDestroy();
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            mImageViewGoBack = null;
            HttpUtil.cancelRequest(mTagDisplay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络请求是显示 Dilaog
     */
    private void showLoadingDialog() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new MyLoadingDialog(this);
            }
            mLoadingDialog.showDialog();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 处理网络请求返回数据成功
     *
     * @param response 返回的数据
     */
    private void DisplaySuccess(String response, CustomerMeeting customerM) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            String msg = object.getString("msg");
            mLoadingDialog.dismiss();
            if (type == 1) {

                Intent intent = new Intent(this, CustomerMeetingShowStepActivity.class);
                intent.putExtra("CustomerMeeting", customerM);
                intent.putExtra("isShowStep", false);
                startActivity(intent);
            } else {

                ToastUtil.showToastBottom(String.valueOf(msg), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            ToastUtil.showToastBottom("服务器返回数据异常！", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    ToastUtil.showToastBottom("图片传输失败，请重新取图", Toast.LENGTH_LONG);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_goback:
                    this.finish();
                    break;
                case R.id.llMeetingDisplay:
                    showChoiceDispalyDialog();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示生动化陈列 Dialog
     */
    private void showChoiceDispalyDialog() {
        try {
            if (mChoiceDisplayDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.create();
                mChoiceDisplayDialog = builder.show();
                mChoiceDisplayDialog.setCanceledOnTouchOutside(false);
                Window window = mChoiceDisplayDialog.getWindow();
                window.setContentView(R.layout.dialog_display_choice);
                window.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mChoiceDisplayDialog.dismiss();
                    }
                });
                mListViewChoiceDisplays = (ListView) window.findViewById(R.id.listView_chart_choice);
                mDisplaysChoiceAdapter = new LineChoiceAdapter(null, this);
                mListViewChoiceDisplays.setAdapter(mDisplaysChoiceAdapter);
                mListViewChoiceDisplays.setOnItemClickListener(new CustomerMeetingDisplayActivity.InnerOnItemClickListener());
            }
            mChoiceDisplayDialog.show();
            mDisplaysChoiceAdapter.notifyChange(mBiz.getMeetingDisplay());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * Dialog 动化陈列监听
     */
    private class InnerOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mChoiceDisplayDialog.dismiss();
                String Display = mBiz.getMeetingDisplay().get(position).getITEM_NAME();
                strVividDisplayCbx.setText(Display);
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    public void VividDisplayCBXError(String msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        try {
            ToastUtil.showToastBottom(String.valueOf(msg), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    public void VividDisplayCBXSuccess(List<CustomerMeetingLine> customerMeetingLines) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
    }
}
