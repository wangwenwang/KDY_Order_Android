package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.kaidongyuan.app.kdyorder.adapter.OrderCategoryAdapter;
import com.kaidongyuan.app.kdyorder.adapter.OrderCommodityAdapter;
import com.kaidongyuan.app.kdyorder.adapter.OrderGiftChoiceDetialAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.OrderGift;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.bean.PromotionDetail;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.util.ChangeProductToPromotionDetailUtil;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/20.
 * 添加赠品界面
 */
public class AddGiftActivity extends BaseActivity implements View.OnClickListener {

    /**
     * Intent 传值时传递商品分类时的字符常量
     */
    public static final String EXTRA_GIFT_DATA = "EXTRA_GIFT_DATA";
    /**
     * 选完赠品后销毁 Activity 返回赠品给调用的 Activity Intent 传值时的字符常量
     */
    public static final String EXTRA_RETURN_GIFT_DATA = "EXTRA_RETURN_GIFT_DATA";
    /**
     * Intent 传值时传递 PARTYID 用的字符常量
     */
    public static final String EXTRA_STR_PARTYID = "strPartyId";
    /**
     * Intent 传值时传递 PARTY_ADDRESSID 用的字符常量
     */
    public static final String EXTRA_STR_PARTY_ADDRESSID = "strPartyAddressIdx";
    /**
     * Intent 传值时传递已选产品中赠品行数用的字符常量
     */
    public static final String EXTRA_PROMOTION_START_LINE_NO = "EXTRA_PROMOTION_LINE_NO";
    /**
     * Intent 传值时传递整个产品列表时用得字符常量（包含产品和赠品）
     */
    public static final String EXTRA_ORDER_DETAIL = "EXTRA_ORDER_DETAIL";
    /**
     * Handler 发送消息是已选赠品列表详情显时的 what
     */
    private static final int WHAT_CHOICE_DETIAL_IN = 1;
    /**
     * Handler 发送消息是已选赠品列表详情隐藏时的 what
     */
    private static final int WHAT_CHOICE_DETIAL_OUT = 2;
    /**
     * 显示已选赠品列表时发送消息的时间间隔
     */
    private static final int DETIAL_IN_PICE_TIME = 4;
    /**
     * 隐藏已选赠品列表时发送消息的时间间隔
     */
    private static final int DETIAL_OUT_PICE_TIME = 6;
    /**
     * 屏幕高度 px
     */
    private static final int SCREEN_HEIGHT = DensityUtil.getHeight();
    /**
     * 已选赠品详情列表显示动画时每次移动的距离 px
     */
    private static final int DETIAL_PICE_PADDING_SIZE_IN = SCREEN_HEIGHT / 10;
    /**
     * 已选赠品详情列表隐藏动画时每次移动的距离 px
     */
    private static final int DETIAL_PICE_PADDING_SIZE_OUT = SCREEN_HEIGHT / 5;
    /**
     * 显示赠品，添加减少赠品时是否需要考虑仓库库存的字符串 "Y"需要 "N" 不需要
     */
    public static final String NEED_CARE_STOCK = "Y";

    /**
     * 确认添加赠品按钮
     */
    private Button mBtAddGift;
    /**
     * 赠品分类列表
     */
    private ListView mListViewCategory;
    /**
     * 赠品列表
     */
    private ListView mListViewCommodity;
    /**
     * 已选赠品明细列表
     */
    private ListView mListViewChoicegiftdetial;
    /**
     * 可选赠品总数
     */
    private TextView mTextViewTotalgiftSize;
    /**
     * 已选赠品总数
     */
    private TextView mTextViewChoicegiftSize;
    /**
     * 已选赠品布局
     */
    private RelativeLayout mRlChoicegiftdetial;
    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;

    /**
     * 网络请求获取商品列表参数
     */
    private String mStrPartyId;
    /**
     * 网络请求获取商品列表参数
     */
    private String mStrPartyAddressIdx;
    /**
     * 用户购买产品赠品的行数，这里添加的赠品需设置行数，以传过来的行数+1为起始行数
     */
    private int mPromotionStartLineNo;
    /**
     * 网络请求是的 Dialog 防止用户在网络请求是再次点击请求
     */
    private MyLoadingDialog mLoadingDialog;

    /**
     * 用户选择的产品（包含产品和赠品）
     */
    private List<PromotionDetail> mOrder;
    /**
     * 处理消息的 Handler
     */
    private Handler mHandler;
    /**
     * 赠品类别列表
     */
    private List<OrderGift> mCategoryData;
    /**
     * 赠品列表适配器
     */
    private OrderCategoryAdapter mCategoryAdapter;
    /**
     * 存放产品品类信息的数组
     */
    private List<PromotionDetail>[] mCategoryDataDetial;
    /**
     * 赠品产品信息适配器
     */
    private OrderCommodityAdapter mCommodityAdapter;
    /**
     * 已选赠品数据
     */
    private List<PromotionDetail> mChoiceGiftData;
    /**
     * 已选赠品适配器
     */
    private OrderGiftChoiceDetialAdapter mGiftChoiceDetailAdapter;
    /**
     * 产品品类列表中之前选中的品类
     */
    private int mPerviousTagIndex;
    /**
     * 产品品类中目前选中的品类
     */
    private int mCategoryIndex = 0;
    /**
     * 刷新是是否要将 ListView 移动到 Position 为 0 的位置
     */
    private boolean mIsShouldMoveToHead;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgift);
        try {
            initData();
            setTop();
            initView();
            setListener();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mPerviousTagIndex = 0;
            mCategoryIndex = 0;
            mIsShouldMoveToHead = false;
            mLoadingDialog = new MyLoadingDialog(this);
            mHandler = new InnerHandler(this);
            Intent intent = this.getIntent();
            if (intent.hasExtra(AddGiftActivity.EXTRA_GIFT_DATA)) {
                mCategoryData = intent.getParcelableArrayListExtra(AddGiftActivity.EXTRA_GIFT_DATA);
                mCategoryDataDetial = new List[mCategoryData.size()];
            } else {
                Toast.makeText(this, "没有可选赠品分类列表！", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (intent.hasExtra(EXTRA_STR_PARTYID)) {
                mStrPartyId = intent.getStringExtra(EXTRA_STR_PARTYID);
            }
            if (intent.hasExtra(EXTRA_STR_PARTY_ADDRESSID)) {
                mStrPartyAddressIdx = intent.getStringExtra(EXTRA_STR_PARTY_ADDRESSID);
            }
            if (intent.hasExtra(EXTRA_PROMOTION_START_LINE_NO)) {
                mPromotionStartLineNo = intent.getIntExtra(EXTRA_PROMOTION_START_LINE_NO, 0);
            }
            if (intent.hasExtra(EXTRA_ORDER_DETAIL)) {
                mOrder = intent.getParcelableArrayListExtra(EXTRA_ORDER_DETAIL);
            } else {
                mOrder = new ArrayList<>();
            }

            mChoiceGiftData = new ArrayList<>();
            mGiftChoiceDetailAdapter = new OrderGiftChoiceDetialAdapter(this, mChoiceGiftData, new InnerOrderGiftAdapterInterface());

            mCategoryData.get(0).setIsChecked(true);
            mCategoryAdapter = new OrderCategoryAdapter(this, mCategoryData);

            getCommodityData(0);
            mCommodityAdapter = new OrderCommodityAdapter(this, mCategoryDataDetial[mCategoryIndex], mChoiceGiftData, new InnerCommodityAdapterInterface());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setTop () {
        //版本4.4以上设置状态栏透明，界面布满整个界面
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View topView = findViewById(R.id.topview);
            ViewGroup.LayoutParams topParams = topView.getLayoutParams();
            topParams.height = DensityUtil.getStatusHeight()*16/30;
            topView.setLayoutParams(topParams);
        }
    }

    private void initView() {
        try {
            mBtAddGift = (Button) this.findViewById(R.id.button_sureAdd);
            mListViewCategory = (ListView) this.findViewById(R.id.listview_category);
            mListViewCategory.setAdapter(mCategoryAdapter);
            mListViewCommodity = (ListView) this.findViewById(R.id.listview_commodity);
            mListViewCommodity.setAdapter(mCommodityAdapter);
            mListViewChoicegiftdetial = (ListView) this.findViewById(R.id.listview_choicegiftdetial);
            mListViewChoicegiftdetial.setAdapter(mGiftChoiceDetailAdapter);
            mTextViewTotalgiftSize = (TextView) this.findViewById(R.id.tv_totalgiftsize);
            mTextViewTotalgiftSize.setText(getTotalGiftCount() + "");
            mTextViewChoicegiftSize = (TextView) this.findViewById(R.id.tv_choicegiftsize);
            mTextViewChoicegiftSize.setText(0 + "");
            mRlChoicegiftdetial = (RelativeLayout) this.findViewById(R.id.rl_choicegiftdetial);
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mBtAddGift.setOnClickListener(this);
            mListViewCategory.setOnItemClickListener(new CategoryItemClickListener());
            this.findViewById(R.id.bt_cleargiftchoice).setOnClickListener(this);
            this.findViewById(R.id.bt_gigrchoicedetial).setOnClickListener(this);
            this.findViewById(R.id.bt_hidgiftdetail).setOnClickListener(this);
            mImageViewGoBack.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_sureAdd://确定选择，将赠品信息返回调用的界面
                    returnData();
                    break;
                case R.id.bt_cleargiftchoice://重新选择赠品
                    clearChoiceGift();
                    break;
                case R.id.bt_gigrchoicedetial://选择赠品详情
                    shouldShowChoiceGift();
                    break;
                case R.id.bt_hidgiftdetail://隐藏赠品详情
                    choiceGiftDetialStrartOut();
                    break;
                case R.id.button_goback://返回上一界面
                    finish();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mRlChoicegiftdetial.getVisibility() == View.VISIBLE) {
                choiceGiftDetialStrartOut();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            mHandler.removeCallbacksAndMessages(null);
            HttpUtil.cancelRequest(mTagGetCommodityData);
            Button mBtAddGift = null;
            ListView mListViewCategory = null;
            ListView mListViewCommodity = null;
            ListView mListViewChoicegiftdetial = null;
            TextView mTextViewTotalgiftSize = null;
            TextView mTextViewChoicegiftSize = null;
            RelativeLayout mRlChoicegiftdetial = null;
            ImageView mImageViewGoBack = null;
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            MyLoadingDialog mLoadingDialog = null;
            List<PromotionDetail> mOrder = null;
            Handler mHandler = null;
            List<OrderGift> mCategoryData = null;
            OrderCategoryAdapter mCategoryAdapter = null;
            List<PromotionDetail>[] mCategoryDataDetial = null;
            OrderCommodityAdapter mCommodityAdapter = null;
            List<PromotionDetail> mChoiceGiftData = null;
            OrderGiftChoiceDetialAdapter mGiftChoiceDetailAdapter = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 处理消息的 Handler 防止内存泄漏
     */
    private static class InnerHandler extends Handler {
        private WeakReference<AddGiftActivity> addGiftActivity;

        public InnerHandler(AddGiftActivity addGiftActivity) {
            this.addGiftActivity = new WeakReference<>(addGiftActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                super.handleMessage(msg);
                AddGiftActivity activity = addGiftActivity.get();
                if (activity != null) {
                    switch (msg.what) {
                        case WHAT_CHOICE_DETIAL_IN://赠品详细信息列表进入
                            choiceGiftDetialIn(msg, activity);
                            break;
                        case WHAT_CHOICE_DETIAL_OUT://赠品详细列表退出
                            choiceGiftDetialOut(msg, activity);
                            break;
                    }
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }

        /**
         * 已选赠品信息进入动画
         *
         * @param msg      Message
         * @param activity AddGiftActivity
         */
        private void choiceGiftDetialIn(Message msg, AddGiftActivity activity) {
            try {
                int paddingTopIn = msg.arg1 - AddGiftActivity.DETIAL_PICE_PADDING_SIZE_IN;
                activity.mRlChoicegiftdetial.setPadding(0, paddingTopIn, 0, 0);
                if (paddingTopIn > 0) {
                    Message message = activity.mHandler.obtainMessage();
                    message.what = AddGiftActivity.WHAT_CHOICE_DETIAL_IN;
                    message.arg1 = paddingTopIn;
                    activity.mHandler.sendMessageDelayed(message, AddGiftActivity.DETIAL_IN_PICE_TIME);
                } else {
                    activity.mRlChoicegiftdetial.setPadding(0, 0, 0, 0);
                    Logger.w("AddGiftActivity:已选赠品详细列表进入动画结束");
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }

        /**
         * 已选赠品信息退出动画
         *
         * @param msg      Message
         * @param activity AddGiftActivity
         */
        private void choiceGiftDetialOut(Message msg, AddGiftActivity activity) {
            try {
                int paddingTopOut = msg.arg1 + AddGiftActivity.DETIAL_PICE_PADDING_SIZE_OUT;
                activity.mRlChoicegiftdetial.setPadding(0, paddingTopOut, 0, 0);
                if (paddingTopOut < AddGiftActivity.SCREEN_HEIGHT) {
                    Message messageOut = activity.mHandler.obtainMessage();
                    messageOut.what = AddGiftActivity.WHAT_CHOICE_DETIAL_OUT;
                    messageOut.arg1 = paddingTopOut;
                    activity.mHandler.sendMessageDelayed(messageOut, AddGiftActivity.DETIAL_OUT_PICE_TIME);
                } else {
                    activity.mRlChoicegiftdetial.setVisibility(View.GONE);
                    Logger.w("AddGiftActivity:已选赠品详细列退出动画结束");
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    /**
     * 获取品类的赠品详细信息
     *
     * @param categoryIndex 在品类数据中的角标
     */
    private void getCommodityData(final int categoryIndex) {
        try {
            if (mCategoryDataDetial[categoryIndex] != null && mCategoryDataDetial[categoryIndex].size() > 0) {
                return;
            }
            final StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_COMMODITY_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            getCommondyDataSuccess(response, categoryIndex);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mLoadingDialog.cancel();
                    Toast.makeText(AddGiftActivity.this, "获取失败!", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strBusinessId", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("strPartyIdx", mStrPartyId);
                    params.put("strPartyAddressIdx", mStrPartyAddressIdx);
                    params.put("strLicense", "");
                    String strLicense = mCategoryData.get(categoryIndex).getTYPE_NAME();
                    params.put("strProductType", strLicense);
                    params.put("strProductClass", "");
                    return params;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setTag(mTagGetCommodityData);
            HttpUtil.getRequestQueue().add(request);
            if (mLoadingDialog == null) {
                mLoadingDialog = new MyLoadingDialog(this);
            }
            mLoadingDialog.showDialog();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        }
    }

    /**
     * 网络获取产品信息时的标记
     */
    private final String mTagGetCommodityData = "getCommodityData";

    /**
     * 获取数据成功
     *
     * @param response 返回的信息
     * @param index    在产品分类中的角标
     */
    private void getCommondyDataSuccess(String response, int index) {
        mLoadingDialog.cancel();
        try {
            JSONObject jo = JSON.parseObject(response);
            int type = Integer.parseInt(jo.getString("type"));
            if (type == 1) {
                List<Product> promotionData = JSON.parseArray(jo.getString("result"), Product.class);
                mCategoryDataDetial[index] = ChangeProductToPromotionDetailUtil.change(promotionData);
                mCategoryDataDetial[index] = checkAndSetCategoryStock(mCategoryDataDetial[index]);
                notifyGiftChoiceChanged(index);
            } else {
                Toast.makeText(getApplicationContext(), "获取赠品列表失败！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回赠品数据给调用界面
     */
    private void returnData() {
        try {
            Intent intent = new Intent();
            setChoiceGiftLineNumber();
            intent.putParcelableArrayListExtra(EXTRA_RETURN_GIFT_DATA, (ArrayList<? extends Parcelable>) mChoiceGiftData);
            AddGiftActivity.this.setResult(Activity.RESULT_OK, intent);
            AddGiftActivity.this.finish();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 产品分类 ListView 的点击事件实现类
     */
    private class CategoryItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                if (position == mPerviousTagIndex) {
                    return;
                }
                mCategoryIndex = position;
                mCategoryData.get(mPerviousTagIndex).setIsChecked(false);
                mCategoryData.get(mCategoryIndex).setIsChecked(true);
                mPerviousTagIndex = position;

                getCommodityData(position);
                mIsShouldMoveToHead = true;
                notifyGiftChoiceChanged(mCategoryIndex);
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    /**
     * 处理产品列表 ListView 中点击事件的实现类
     */
    private class InnerCommodityAdapterInterface implements OrderCommodityAdapter.CommodityAdapterInterface {
        @Override
        public void addGift(int dataIndex) {
            PromotionDetail gift = mCategoryDataDetial[mCategoryIndex].get(dataIndex);
            addGiftToGiftDetial(gift);
        }

        @Override
        public void deleteGift(int dataIndex) {
            PromotionDetail promotionDetail = mCategoryDataDetial[mCategoryIndex].get(dataIndex);
            deleteGiftFromGiftDetial(promotionDetail);
        }

        @Override
        public void setGiftCount(int dataIndex, int giftCount) {
            if (giftCount < 0) {
                Toast.makeText(AddGiftActivity.this, "请输入大于零的数量！", Toast.LENGTH_SHORT).show();
                return;
            }
            PromotionDetail promotionDetail = mCategoryDataDetial[mCategoryIndex].get(dataIndex);
            setGiftCountInGiftDetial(promotionDetail, giftCount);
        }
    }

    /**
     * 处理已选赠品详情列表 ListView 中点击事件的实现类
     */
    private class InnerOrderGiftAdapterInterface implements OrderGiftChoiceDetialAdapter.OrderGiftDetialAdapterInterface {
        @Override
        public void addGift(int dataIndex) {
            if (mChoiceGiftData.size() > dataIndex) {
                PromotionDetail promotionDetail = mChoiceGiftData.get(dataIndex);
                addGiftToGiftDetial(promotionDetail);
            }
        }

        @Override
        public void deleteGift(int dataIndex) {
            if (mChoiceGiftData.size() > dataIndex) {
                PromotionDetail promotionDetail = mChoiceGiftData.get(dataIndex);
                deleteGiftFromGiftDetial(promotionDetail);
            }
        }

        @Override
        public void setGiftCount(int dataIndex, int giftCount) {
            PromotionDetail promotionDetail = mChoiceGiftData.get(dataIndex);
            setGiftCountInGiftDetial(promotionDetail, giftCount);
        }
    }

    /**
     * 单个添加赠品
     *
     * @param promotionDetail 要添加的赠品
     */
    private void addGiftToGiftDetial(PromotionDetail promotionDetail) {
        try {
            if (NEED_CARE_STOCK.equals(promotionDetail.LOTTABLE09) && promotionDetail.LOTTABLE11 < 1) {//仓库剩余产品数量小于1
                Toast.makeText(this, "仓库剩余库存不足！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mChoiceGiftData == null) {
                mChoiceGiftData = new ArrayList<>();
            }
            int index = getPromotionDetailInCategoryIndex(promotionDetail);
            if (mCategoryData.get(index).getChoiceCount() >= mCategoryData.get(index).getQTY()) {
                Toast.makeText(AddGiftActivity.this, "超出当前赠品可选数额！", Toast.LENGTH_SHORT).show();
                return;
            }
            int giftIndex = mChoiceGiftData.indexOf(promotionDetail);
            if (giftIndex != -1) {
                mChoiceGiftData.get(giftIndex).PO_QTY = mChoiceGiftData.get(giftIndex).PO_QTY + 1;
            } else {
                promotionDetail.PO_QTY = 1;
                mChoiceGiftData.add(promotionDetail);
            }
            mCategoryData.get(index).setChoiceCount(mCategoryData.get(index).getChoiceCount() + 1);
            if (NEED_CARE_STOCK.equals(promotionDetail.LOTTABLE09)) {//是否需要考虑库存
                promotionDetail.LOTTABLE11 -= 1;
            }
            notifyGiftChoiceChanged(mCategoryIndex);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 单个减少赠品数量
     *
     * @param promotionDetail 要减少的赠品
     */
    private void deleteGiftFromGiftDetial(PromotionDetail promotionDetail) {
        try {
            if (mChoiceGiftData == null) {
                mChoiceGiftData = new ArrayList<>();
            }
            int giftIndex = mChoiceGiftData.indexOf(promotionDetail);
            if (giftIndex != -1) {
                int giftCount = mChoiceGiftData.get(giftIndex).PO_QTY;
                if (giftCount > 0) {
                    mChoiceGiftData.get(giftIndex).PO_QTY = mChoiceGiftData.get(giftIndex).PO_QTY - 1;
                    int index = getPromotionDetailInCategoryIndex(promotionDetail);
                    mCategoryData.get(index).setChoiceCount(mCategoryData.get(index).getChoiceCount() - 1);
                }
                if (mChoiceGiftData.get(giftIndex).PO_QTY <= 0) {
                    mChoiceGiftData.remove(giftIndex);
                }
                if (NEED_CARE_STOCK.equals(promotionDetail.LOTTABLE09)) {
                    promotionDetail.LOTTABLE11 += 1;
                }
            }
            notifyGiftChoiceChanged(mCategoryIndex);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 手动设置赠品数量
     *
     * @param promotionDetail 要设置的赠品
     * @param giftCount       要设置的数量
     */
    private void setGiftCountInGiftDetial(PromotionDetail promotionDetail, int giftCount) {
        try {
            if (giftCount < 0) {
                Toast.makeText(AddGiftActivity.this, "请输入大于零的数量！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mChoiceGiftData == null) {
                mChoiceGiftData = new ArrayList<>();
            }
            int giftIndex = mChoiceGiftData.indexOf(promotionDetail);
            int categoryIndex = getPromotionDetailInCategoryIndex(promotionDetail);
            int maxCount = (int) mCategoryData.get(categoryIndex).getQTY() - (int) mCategoryData.get(categoryIndex).getChoiceCount();
            maxCount += giftIndex != -1 ? mChoiceGiftData.get(giftIndex).PO_QTY : 0;

            if (giftCount > maxCount) {
                Toast.makeText(AddGiftActivity.this, "超出可选数量，最多可输入：" + maxCount, Toast.LENGTH_SHORT).show();
                return;
            }

            //判断输入的数量是否大于仓库剩余数量
            if (NEED_CARE_STOCK.equals(promotionDetail.LOTTABLE09)) {
                if (giftIndex != -1 && giftCount > (promotionDetail.LOTTABLE11 + mChoiceGiftData.get(giftIndex).PO_QTY)) {
                    Toast.makeText(this, "仓库剩余库存不足！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (giftIndex == -1 && giftCount > promotionDetail.LOTTABLE11) {
                    Toast.makeText(this, "仓库剩余库存不足！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            double choiceCount = mCategoryData.get(categoryIndex).getChoiceCount();
            if (giftIndex != -1) {//设置promotionDetail中仓库剩余数量，已选数量，分类中可选数量
                if (NEED_CARE_STOCK.equals(promotionDetail.LOTTABLE09)) {
                    promotionDetail.LOTTABLE11 = promotionDetail.LOTTABLE11 - giftCount + mChoiceGiftData.get(giftIndex).PO_QTY;
                }
                choiceCount = choiceCount - mChoiceGiftData.get(giftIndex).PO_QTY;
                mChoiceGiftData.get(giftIndex).PO_QTY = giftCount;
            } else {//设置promotionDetail中仓库剩余数量，已选数量，分类中可选数量
                if (NEED_CARE_STOCK.equals(promotionDetail.LOTTABLE09)) {
                    promotionDetail.LOTTABLE11 = promotionDetail.LOTTABLE11 - giftCount;
                }
                promotionDetail.PO_QTY = giftCount;
                mChoiceGiftData.add(promotionDetail);
            }
            choiceCount += giftCount;
            mCategoryData.get(categoryIndex).setChoiceCount(choiceCount);
            giftIndex = mChoiceGiftData.indexOf(promotionDetail);
            if (mChoiceGiftData.get(giftIndex).PO_QTY <= 0) {
                mChoiceGiftData.remove(giftIndex);
            }
            notifyGiftChoiceChanged(mCategoryIndex);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 添加赠品后刷新相关 ListView
     *
     * @param index 赠品在产品分类中的位置
     */
    private synchronized void notifyGiftChoiceChanged(int index) {
        try {
            mCategoryAdapter.notifyChange(mCategoryData);
            mCommodityAdapter.notifyChange(mCategoryDataDetial[index], mChoiceGiftData);
            mGiftChoiceDetailAdapter.notifyChange(mChoiceGiftData);
            mTextViewChoicegiftSize.setText(getChoiceGiftCount() + "");
            if (mIsShouldMoveToHead) {
                mListViewCommodity.setSelection(0);
                mIsShouldMoveToHead = false;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 清除已选商品，重选
     */
    private void clearChoiceGift() {
        try {
            mChoiceGiftData.clear();
            int size2 = mCategoryData.size();
            for (int i = 0; i < size2; i++) {
                mCategoryData.get(i).setChoiceCount(0);
            }
            notifyGiftChoiceChanged(mCategoryIndex);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 判断是显示还是隐藏已选商品详情
     */
    private void shouldShowChoiceGift() {
        try {
            if (mRlChoicegiftdetial.getVisibility() == View.VISIBLE) {
                choiceGiftDetialStrartOut();
            } else {
                choiceGiftDetialStartIn();
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 开始已选赠品信息进入界面
     */
    private void choiceGiftDetialStartIn() {
        try {
            mRlChoicegiftdetial.setVisibility(View.VISIBLE);
            int paddingTop = DensityUtil.getHeight();
            mRlChoicegiftdetial.setPadding(0, paddingTop, 0, 0);
            Message message = mHandler.obtainMessage();
            message.what = WHAT_CHOICE_DETIAL_IN;
            message.arg1 = paddingTop - DETIAL_PICE_PADDING_SIZE_IN;
            mHandler.sendMessageDelayed(message, DETIAL_OUT_PICE_TIME);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 开始已选赠品信息退出界面
     */
    private void choiceGiftDetialStrartOut() {
        try {
            int paddintTop = 0;
            Message message = mHandler.obtainMessage();
            message.what = WHAT_CHOICE_DETIAL_OUT;
            message.arg1 = paddintTop;
            mHandler.sendMessageDelayed(message, DETIAL_OUT_PICE_TIME);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取列表中赠品的总数量
     *
     * @return 赠品总数量
     */
    private int getTotalGiftCount() {
        try {
            int sum = 0;
            for (OrderGift orderGift : mCategoryData) {
                sum += orderGift.getQTY();
            }
            return sum;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return 0;
        }
    }

    /**
     * 获取列已选赠品总数量
     *
     * @return 已选赠品总数量
     */
    private int getChoiceGiftCount() {
        try {
            int sum = 0;
            for (OrderGift orderGift : mCategoryData) {
                sum += orderGift.getChoiceCount();
            }
            return sum;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return 0;
        }
    }

    /**
     * 获取赠品在赠品品类数组中的位置
     *
     * @param promotionDetail 查询的赠品
     * @return 在赠品品类数组中的角标
     */
    private int getPromotionDetailInCategoryIndex(PromotionDetail promotionDetail) {
        try {
            int size = mCategoryDataDetial.length;
            List<PromotionDetail> commodityList;
            for (int i = 0; i < size; i++) {
                commodityList = mCategoryDataDetial[i];
                if (commodityList != null && commodityList.indexOf(promotionDetail) != -1) {
                    return i;
                }
            }
            return -1;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return -1;
        }
    }

    /**
     * 设置选择的赠品的 LINE_NO
     */
    private void setChoiceGiftLineNumber() {
        try {
            int lineNumber = mPromotionStartLineNo + 1;
            for (PromotionDetail promotionDetail : mChoiceGiftData) {
                promotionDetail.LINE_NO = lineNumber;
                promotionDetail.PO_VOLUME = promotionDetail.PO_VOLUME * promotionDetail.PO_QTY;
                promotionDetail.PO_WEIGHT = promotionDetail.PO_WEIGHT * promotionDetail.PO_QTY;
                lineNumber++;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 检查和设置库存，减去用户已选择的产品和赠送的赠品
     *
     * @param promotionDetails 赠品分类集合
     * @return 检查完库存的赠品集合
     */
    private List<PromotionDetail> checkAndSetCategoryStock(List<PromotionDetail> promotionDetails) {
        try {
            if (promotionDetails == null || mOrder == null) {
                return null;
            }
            for (PromotionDetail promotionDetailOuter : promotionDetails) {
                if (NEED_CARE_STOCK.equals(promotionDetailOuter.LOTTABLE09)) {
                    for (PromotionDetail promotionDetailInner : mOrder) {
                        if (promotionDetailInner.PRODUCT_IDX == promotionDetailOuter.PRODUCT_IDX) {
                            promotionDetailOuter.LOTTABLE11 -= promotionDetailInner.PO_QTY;
                        }
                    }
                }
            }
            return promotionDetails;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return null;
        }
    }

}

























