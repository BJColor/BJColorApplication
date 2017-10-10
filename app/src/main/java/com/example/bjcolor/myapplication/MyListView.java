package com.example.bjcolor.myapplication;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by BJColor on 2017/9/21.
 */

public class MyListView extends ListView implements AbsListView.OnScrollListener,Parcelable {

    /**
     * 头布局
     */
    private View headerView;

    /**
     * 头部布局的高度
     */
    private int headerViewHeight;

    /**
     * 头部旋转的图片
     */
    private ImageView iv_arrow;

    /**
     * 头部下拉刷新时状态的描述
     */
    private TextView tv_state;

    /**
     * 下拉刷新时间的显示控件
     */
    private TextView tv_time;


    /**
     * 底部布局
     */
    private View footerView;

    /**
     * 底部旋转progressbar
     */
    private ProgressBar pb_rotate;


    /**
     * 底部布局的高度
     */
    private int footerViewHeight;


    /**
     * 按下时的Y坐标
     */
    private int downY;

    private final int PULL_REFRESH = 0;//下拉刷新的状态
    private final int RELEASE_REFRESH = 1;//松开刷新的状态
    private final int REFRESHING = 2;//正在刷新的状态

    /**
     * 当前下拉刷新处于的状态
     */
    private int currentState = PULL_REFRESH;

    /**
     * 头部布局在下拉刷新改变时，图标的动画
     */
    private RotateAnimation upAnimation,downAnimation;

    /**
     * 当前是否在加载数据
     */
    private boolean isLoadingMore = false;

    public MyListView(Context context) {
        this(context,null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        //设置滑动监听
        setOnScrollListener(this);
        //初始化头布局
        initHeaderView();
        //初始化头布局中图标的旋转动画
        initRotateAnimation();
        //初始化为尾布局
        initFooterView();
    }


    /**
     * 初始化headerView
     */
    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.head_custom_listview, null);
        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pb_rotate = (ProgressBar) headerView.findViewById(R.id.pb_rotate);
        tv_state = (TextView) headerView.findViewById(R.id.tv_state);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);
        //测量headView的高度
        headerView.measure(0, 0);
        //获取高度，并保存
        headerViewHeight = headerView.getMeasuredHeight();
        //设置paddingTop = -headerViewHeight;这样，该控件被隐藏
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        //添加头布局
        addHeaderView(headerView);
    }

    /**
     * 初始化旋转动画
     */
    private void initRotateAnimation() {

        upAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(300);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, -360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(300);
        downAnimation.setFillAfter(true);
    }

    //初始化底布局，与头布局同理
    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.foot_custom_listview, null);
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        addFooterView(footerView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取按下时y坐标
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                if(currentState==REFRESHING){
                    //如果当前处在滑动状态，则不做处理
                    break;
                }
                //手指滑动偏移量
                int deltaY = (int) (ev.getY() - downY);

                //获取新的padding值
                int paddingTop = -headerViewHeight + deltaY;
                if(paddingTop>-headerViewHeight && getFirstVisiblePosition()==0){
                    //向下滑，且处于顶部，设置padding值，该方法实现了顶布局慢慢滑动显现
                    headerView.setPadding(0, paddingTop, 0, 0);

                    if(paddingTop>=0 && currentState==PULL_REFRESH){
                        //从下拉刷新进入松开刷新状态
                        currentState = RELEASE_REFRESH;
                        //刷新头布局
                        refreshHeaderView();
                    }else if (paddingTop<0 && currentState==RELEASE_REFRESH) {
                        //进入下拉刷新状态
                        currentState = PULL_REFRESH;
                        refreshHeaderView();
                    }


                    return true;//拦截TouchMove，不让listview处理该次move事件,会造成listview无法滑动
                }


                break;
            case MotionEvent.ACTION_UP:
                if(currentState==PULL_REFRESH){
                    //仍处于下拉刷新状态，未滑动一定距离，不加载数据，隐藏headView
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                }else if (currentState==RELEASE_REFRESH) {
                    //滑倒一定距离，显示无padding值得headcView
                    headerView.setPadding(0, 0, 0, 0);
                    //设置状态为刷新
                    currentState = REFRESHING;

                    //刷新头部布局
                    refreshHeaderView();

                    if(listener!=null){
                        //接口回调加载数据
                        listener.onPullRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据currentState来更新headerView
     */
    private void refreshHeaderView(){
        switch (currentState) {
            case PULL_REFRESH:
                tv_state.setText("下拉刷新");
                iv_arrow.startAnimation(downAnimation);
                break;
            case RELEASE_REFRESH:
                tv_state.setText("松开刷新");
                iv_arrow.startAnimation(upAnimation);
                break;
            case REFRESHING:
                iv_arrow.clearAnimation();//因为向上的旋转动画有可能没有执行完
                iv_arrow.setVisibility(View.INVISIBLE);
                pb_rotate.setVisibility(View.VISIBLE);
                tv_state.setText("正在刷新...");
                break;
        }
    }

    /**
     * 完成刷新操作，重置状态,在你获取完数据并更新完adater之后，去在UI线程中调用该方法
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void completeRefresh(){
        if(isLoadingMore){
            //重置footerView状态
            footerView.setPadding(0, -footerViewHeight, 0, 0);
            isLoadingMore = false;
        }else {
            //重置headerView状态
            headerView.setPadding(0, -headerViewHeight, 0, 0);
            currentState = PULL_REFRESH;
            pb_rotate.setVisibility(View.INVISIBLE);
            iv_arrow.setVisibility(View.VISIBLE);
            tv_state.setText("下拉刷新");
        }
    }


    private OnRefreshListener listener;
    public void setOnRefreshListener(OnRefreshListener listener){
        this.listener = listener;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public interface OnRefreshListener{
        void onPullRefresh();
        void onLoadingMore();
    }

    /**
     * SCROLL_STATE_IDLE:闲置状态，就是手指松开
     * SCROLL_STATE_TOUCH_SCROLL：手指触摸滑动，就是按着来滑动
     * SCROLL_STATE_FLING：快速滑动后松开
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==OnScrollListener.SCROLL_STATE_IDLE
                && getLastVisiblePosition()==(getCount()-1) &&!isLoadingMore){
            isLoadingMore = true;

            footerView.setPadding(0, 0, 0, 0);//显示出footerView
            setSelection(getCount());//让listview最后一条显示出来，在页面完全显示出底布局

            if(listener!=null){
                listener.onLoadingMore();
            }
        }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }

    //    private Context context;
//
//    private int firstVisibleItem = 0;
//    private int visibleItemCount = 0;
//    private int totalItemCount = 0;
//
//    // 一个简单的圆球形进度滚动球，向用户表明正在加载
//    private ProgressDialog progressDialog = null;
//
//    private OnPullToRefreshListener mOnPullToRefreshListener = null;
//
//    public MyListView(Context context) {
//        super(context);
//        this.context = context;
//    }
//
//    public MyListView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.context = context;
//    }
//
//    // 此处对外开放的回调接口，让用户可以使用上拉见底刷新或者下拉见顶刷新。
//    public interface OnPullToRefreshListener {
//
//        // 当用户的手指在屏幕上往上拉见到ListView的底部最后一个元素时候回调。
//        public void onBottom();
//
//        // 当用户的手指在屏幕上往下拉见到ListView的顶部第一个元素时候回调。
//        public void onTop();
//    }
//
//    public void setOnPullToRefreshListener(OnPullToRefreshListener listener) {
//        mOnPullToRefreshListener = listener;
//
//        this.setOnScrollListener(new ListView.OnScrollListener() {
//
//            // 把最新值赋给firstVisibleItem , visibleItemCount , totalItemCount.
//            @Override
//            public void onScroll(AbsListView view, int arg0, int arg1, int arg2) {
//                firstVisibleItem = arg0;
//                visibleItemCount = arg1;
//                totalItemCount = arg2;
//            }
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//        });
//
//        // mGestureDetector用于监测用户在手机屏幕上的上滑和下滑事件。
//        // 之所以用GestureDetector而不完全依赖ListView.OnScrollListener，
//        // 主要是因为当ListView在0个元素，或者当数据元素不多不足以多屏幕滚动显示时候
//        // （换句话说，正常情况假设一屏可以显示15个，但ListView只有3个元素
//        // ，那么ListView下方就会剩余空出很多空白空间，在此空间上的事件不
//        // 触发ListView.OnScrollListener）。
//        final GestureDetector mGestureDetector = new GestureDetector(context,
//                new GestureDetector.SimpleOnGestureListener() {
//
//                    @Override
//                    public boolean onFling(MotionEvent e1, MotionEvent e2,
//                                           float velocityX, float velocityY) {
//
//                        // velocityY是矢量，velocityY表明手指在竖直方向（y坐标轴）移动的矢量距离。
//                        // 当velocityY >0时，表明用户的手指在屏幕上往下移动。
//                        // 即e2事件发生点在e1事件发生点的下方。
//                        // 我们可以据此认为用户在下拉：用户下拉希望看到位于顶部的数据。
//                        // 然后在这个代码块中，判断ListView中的第一个条目firstVisibleItem是否等于0 ,
//                        // 等于0则意味着此时的ListView已经滑到顶部了。
//                        // 然后开始回调mOnPullToRefreshListener.onTop()触发onTop()事件。
//                        if (velocityY > 0) {
//                            if (firstVisibleItem == 0) {
//                                mOnPullToRefreshListener.onTop();
//                            }
//                        }
//
//                        // 与上面的道理相同，velocityY < 0，此时的e1在e2的下方。
//                        // 表明用户的手指在屏幕上往上移动，希望看到底部的数据。
//                        // firstVisibleItem表明屏幕当前可见视野上第一个item的值，
//                        // visibleItemCount是可见视野中的数目。
//                        // totalItemCount是ListView全部的item数目
//                        // 如果 firstVisibleItem + visibleItemCount ==
//                        // totalItemCount，则说明此时的ListView已经见底。
//                        if (velocityY < 0) {
//                            int cnt = firstVisibleItem + visibleItemCount;
//                            if (cnt == totalItemCount) {
//                                mOnPullToRefreshListener.onBottom();
//                            }
//                        }
//
//                        return super.onFling(e1, e2, velocityX, velocityY);
//                    }
//                });
//
//        this.setOnTouchListener(new View.OnTouchListener() {
//
//            // 用mGestureDetector监测Touch事件。
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                requestDisallowInterceptTouchEvent(true);
//                return mGestureDetector.onTouchEvent(event);
//            }
//        });
//    }
//
//
//    /**
//     * @param refreshing
//     *
//     * 控制在加载过程中的动画显示
//     * ture：显示.
//     * false：关闭
//     */
//    public void onRefresh(boolean refreshing) {
//        if (refreshing)
//            showProgress();
//        else
//            closeProgress();
//    }
//
//    // 显示ProgressDialog，表明正在加载...
//    private void showProgress() {
//        progressDialog = ProgressDialog.show(context, "MyListView",
//                "加载中,请稍候...", true, true);
//    }
//
//    // 关闭加载显示
//    private void closeProgress() {
//        if (progressDialog != null)
//            progressDialog.dismiss();
//    }
}
