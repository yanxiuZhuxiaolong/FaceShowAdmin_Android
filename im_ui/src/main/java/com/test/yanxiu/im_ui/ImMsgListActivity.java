package com.test.yanxiu.im_ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.test.yanxiu.common_base.ui.KeyboardChangeListener;
import com.test.yanxiu.common_base.utils.NetWorkUtils;
import com.test.yanxiu.common_base.utils.SharedSingleton;
import com.test.yanxiu.common_base.utils.SrtLogger;
import com.test.yanxiu.common_base.utils.permission.OnPermissionCallback;
import com.test.yanxiu.common_base.utils.talkingdata.EventUpdate;
import com.test.yanxiu.faceshow_ui_base.ImBaseActivity;
import com.test.yanxiu.faceshow_ui_base.imagePicker.GlideImageLoader;
import com.test.yanxiu.im_core.RequestQueueHelper;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbMyMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_core.dealer.MqttProtobufDealer;
import com.test.yanxiu.im_core.http.GetQiNiuTokenRequest;
import com.test.yanxiu.im_core.http.GetQiNiuTokenResponse;
import com.test.yanxiu.im_core.http.GetTopicMsgsRequest;
import com.test.yanxiu.im_core.http.GetTopicMsgsResponse;
import com.test.yanxiu.im_core.http.SaveImageMsgRequest;
import com.test.yanxiu.im_core.http.SaveImageMsgResponse;
import com.test.yanxiu.im_core.http.SaveTextMsgRequest;
import com.test.yanxiu.im_core.http.SaveTextMsgResponse;
import com.test.yanxiu.im_core.http.TopicCreateTopicRequest;
import com.test.yanxiu.im_core.http.TopicCreateTopicResponse;
import com.test.yanxiu.im_core.http.TopicGetTopicsRequest;
import com.test.yanxiu.im_core.http.TopicGetTopicsResponse;
import com.test.yanxiu.im_core.http.common.ImMsg;
import com.test.yanxiu.im_core.http.common.ImTopic;
import com.test.yanxiu.im_ui.callback.OnNaviLeftBackCallback;
import com.test.yanxiu.im_ui.callback.OnPullToRefreshCallback;
import com.test.yanxiu.im_ui.callback.OnRecyclerViewItemClickCallback;
import com.test.yanxiu.im_ui.view.ChoosePicsDialog;
import com.test.yanxiu.im_ui.view.PhotoActivity;
import com.test.yanxiu.im_ui.view.RecyclerViewPullToRefreshHelper;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ImMsgListActivity extends ImBaseActivity {
    private final String TAG = getClass().getSimpleName();
    private DbTopic topic;
    private static final int IMAGE_PICKER = 0x03;
    private static final int REQUEST_CODE_SELECT = 0x04;
    private static final int REQUEST_CODE_LOAD_BIG_IMG = 0x05;
    private ImTitleLayout mTitleLayout;
    private RecyclerView mMsgListRecyclerView;
    private MsgListAdapter mMsgListAdapter;
    private RecyclerViewPullToRefreshHelper ptrHelper;
    private EditText mMsgEditText;

    private long memberId = -1;
    private String memberName = null;
    private long fromTopicId = -1;
    private String mQiniuToken;
    private boolean mKeyBoardShown;//键盘已经显示了
    private FoucsLinearLayoutManager layoutManager;

    /**
     * 最新的成员列表
     * 由于存在移除成员的消息，为了对成员存在性进行判断
     */
    private List<ImTopic.Member> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memberId = getIntent().getLongExtra(Constants.kCreateTopicMemberId, -1);
        memberName = getIntent().getStringExtra(Constants.kCreateTopicMemberName);
        fromTopicId = getIntent().getLongExtra(Constants.kFromTopicId, -1);
        setResult(RESULT_CANCELED); // 只为有返回，code无意义

        topic = SharedSingleton.getInstance().get(Constants.kShareTopic);
        if ((topic == null) || (topic.mergedMsgs.size() == 0)) {
            hasMoreMsgs = false;
        }

        view = LayoutInflater.from(this).inflate(R.layout.activity_msg_list, null);
        setContentView(view);
        setupView();
        setupData();
        initImagePicker();
        EventBus.getDefault().register(this);
    }

    /**
     * 为了埋点
     */
    private boolean isPrivatePage = false;
    View view;

    @Override
    protected void onResume() {
        super.onResume();
//        埋点
        if (topic != null) {
            //  判断topic type
            if (topic.getType().equals("1")) {
                isPrivatePage = true;
                EventUpdate.onPrivatePageStart(this);
            } else if (topic.getType().equals("2")) {
                //群聊
                isPrivatePage = false;
                EventUpdate.onGroupPageStart(this);
            }
        } else {
            //topic 为空 确定为私聊
            isPrivatePage = true;
            EventUpdate.onPrivatePageStart(this);
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.e("onGlobalLayout", "onGlobalLayout");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isPrivatePage) {
            EventUpdate.onPrivatePageEnd(this);
        } else {
            EventUpdate.onGroupPageEnd(this);
        }
    }

    @Override
    protected void onDestroy() {
        SharedSingleton.getInstance().set(Constants.kShareTopic, null);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void hideSoftInput(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void setupView() {
        mTitleLayout = findViewById(R.id.title_layout);
        mTitleLayout.setTitle("");

        mTitleLayout.setOnNaviLeftBackCallback(new OnNaviLeftBackCallback() {
            @Override
            public void onNaviBack() {
                //收起软键盘
                hideSoftInput(mMsgEditText);
                finish();
            }
        });

        if (topic == null) {
            mTitleLayout.setTitle(memberName);
        } else {
            mTitleLayout.setTitle(DatabaseDealer.getTopicTitle(topic, Constants.imId));
        }

        mMsgListRecyclerView = findViewById(R.id.msg_list_recyclerview);
         layoutManager = new FoucsLinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false);
//        layoutManager.setStackFromEnd(false);
        mMsgListRecyclerView.setLayoutManager(layoutManager);
//        ((SimpleItemAnimator) mMsgListRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
//        mMsgListRecyclerView.getItemAnimator().setChangeDuration(0);

        mMsgListAdapter = new MsgListAdapter(this);
//        mMsgListAdapter.setHasStableIds(true);
        //内部有空处理
        mMsgListAdapter.setTopic(topic);
        mMsgListRecyclerView.setAdapter(mMsgListAdapter);
        mMsgListAdapter.notifyDataSetChanged();
        moveToBottom();
//        mMsgListRecyclerView.scrollToPosition(mMsgListRecyclerView.getAdapter().getItemCount() - 1);//滚动到底部
        mMsgListAdapter.setmOnItemClickCallback(onDbMsgCallback);
        //会造成进入界面后 从第一条滚动到最后一条的 效果
//        mMsgListRecyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                if (mMsgListRecyclerView.getAdapter().getItemCount() > 1) {
//                    mMsgListRecyclerView.scrollToPosition(mMsgListRecyclerView.getAdapter().getItemCount() - 1);//滚动到底部
//                }
//            }
//        });

        ImageView mTakePicImageView = findViewById(R.id.takepic_imageview);
        mTakePicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送照片入口
                showChoosePicsDialog();
                //事件统计  点击聊聊相机
                EventUpdate.onClickMsgCameraEvent(ImMsgListActivity.this);

            }
        });

        mMsgEditText = findViewById(R.id.msg_edittext);
        mMsgEditText.setImeOptions(EditorInfo.IME_ACTION_NONE);
        mMsgEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
//        mMsgEditText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if ((keyCode == event.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_UP)) {
//                    SrtLogger.log("imui", "TBD: 发送");
//                    //统计
//                    EventUpdate.onClickMsgSendEvent(ImMsgListActivity.this);
//                    String msg = mMsgEditText.getText().toString();
//                    mMsgEditText.setText("");
//                    String trimMsg = msg.trim();
//                    if (trimMsg.length() == 0) {
//                        return true;
//                    }
//
//                    doSend(msg, null);
//
//                    return true;
//                }
//                return false;
//            }
//        });
        //新增的 发送按钮 发送逻辑与 按键发送一样
        final TextView sendTv = findViewById(R.id.tv_sure);
        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SrtLogger.log("imui", "TBD: 发送");
                //统计
                EventUpdate.onClickMsgSendEvent(ImMsgListActivity.this);
                String msg = mMsgEditText.getText().toString();
                mMsgEditText.setText("");
                String trimMsg = msg.trim();
                if (trimMsg.length() == 0) {
                    return;
                }

                doSend(msg, null);
            }
        });
        //添加监听 当有文字输入时 展示发送按钮可点击
        mMsgEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null) {
                    if (charSequence.length() > 0) {
                        //设置 enable 可点击状态
                        sendTv.setEnabled(true);
                        sendTv.setBackgroundResource(R.drawable.im_sendbtn_default);
                        return;
                    }
                }
                //设置颜色为 disable 与 按下颜色一样
                sendTv.setEnabled(false);
                sendTv.setBackgroundResource(R.drawable.im_sendbtn_pressed);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //初始设置为 不可点击 当用户有输入 才进行使能设置
        sendTv.setEnabled(false);
        sendTv.setBackgroundResource(R.drawable.im_sendbtn_pressed);

        // 弹出键盘后处理
        KeyboardChangeListener keyboardListener = new KeyboardChangeListener(this);
        keyboardListener.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if ((isSoftShowing()) && mMsgListRecyclerView.getAdapter().getItemCount() > 1) {
                    mKeyBoardShown = true;
                    mMsgListRecyclerView.scrollToPosition(mMsgListRecyclerView.getAdapter().getItemCount() - 1);//滚动到底部
                }
            }
        });

        // pull to refresh，由于覆盖了OnTouchListener，所以需要在这里处理点击外部键盘收起
        ptrHelper = new RecyclerViewPullToRefreshHelper(this, mMsgListRecyclerView, new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mKeyBoardShown) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(                                                  ImMsgListActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    mMsgListRecyclerView.clearFocus();
                }
                return false;
            }
        });
        ptrHelper.setmCallback(mOnLoadMoreCallback);


        mMsgListAdapter.setShowPreviewPicListener(new MsgListAdapter.ShowPreviewPicListener() {
            @Override
            public void picClick(int position, View view, String url) {
                shouldScrollToBottom = false;
                ArrayList list = new ArrayList();
                list.add(url);
//                PhotoActivity.LaunchActivity(ImMsgListActivity.this, list, 0, 0, 1);
                PhotoActivity.LaunchActivity(ImMsgListActivity.this, REQUEST_CODE_LOAD_BIG_IMG, list, position, 0, 1);
            }
        });
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        this.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
//        return screenHeight*2/3 > rect.bottom;
        return screenHeight - getSoftButtonsBarHeight() > rect.bottom;
    }

    /**
     * 控制收到新消息是否滚动
     * 默认情况是true
     * 当用户点击查看打图时 设置为false
     */
    private boolean shouldScrollToBottom = true;

    @Subscribe
    public void onTopicUpdate(MqttProtobufDealer.TopicUpdateEvent event) {

        //新创建的topic 数据不一致造成 新建对话 无法刷新 mergemsgs
        if (event.topicId != topic.getTopicId()) {
            return;
        }
        mMsgListAdapter.setTopic(topic);
        mMsgListAdapter.notifyDataSetChanged();
        mMsgListRecyclerView.scrollToPosition(mMsgListAdapter.getItemCount() - 1);
    }

    private void setupData() {
        if (topic != null && !DatabaseDealer.isMockTopic(topic)) {
            // 每次进入话题更新用户信息
            updateTopicFromHttp(topic.getTopicId() + "");
        }
    }

    private void updateTopicFromHttp(final String topicId) {
        // http, mqtt 公用
        TopicGetTopicsRequest getTopicsRequest = new TopicGetTopicsRequest();
        getTopicsRequest.imToken = Constants.imToken;
        getTopicsRequest.topicIds = topicId;

        getTopicsRequest.startRequest(TopicGetTopicsResponse.class, new HttpCallback<TopicGetTopicsResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicGetTopicsResponse ret) {
                //正确的长度 为1
                if (ret.code == 0) {
                    //当 用户被移除 目标群组时 data=null
                    if (ret.data == null || ret.data.topic == null) {
                        Toast.makeText(ImMsgListActivity.this, "【已被移出此班】", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (ImTopic imTopic : ret.data.topic) {
                        //更新数据库 topic 信息
                        DbTopic dbTopic = DatabaseDealer.updateDbTopicWithImTopic(imTopic);
                        dbTopic.latestMsgTime = imTopic.latestMsgTime;
                        dbTopic.latestMsgId = imTopic.latestMsgId;
                        //请求成功 消除红点
                        dbTopic.setShowDot(false);
                        dbTopic.save();
                        //保证 topic 的持有
                        topic.setName(dbTopic.getName());
                        topic.setChange(dbTopic.getChange());
                        topic.setGroup(dbTopic.getGroup());
                        topic.setType(dbTopic.getType());
                        topic.setTopicId(dbTopic.getTopicId());
                        topic.latestMsgId = dbTopic.latestMsgId;
                        topic.latestMsgTime = dbTopic.latestMsgTime;
                        topic.setShowDot(dbTopic.isShowDot());
                        //member 持有的更新
                        topic.setMembers(dbTopic.getMembers());
                        //对私聊topic 的 title 进行修正
                        if (topic.getType().equals("1")) {
                            for (DbMember member : topic.getMembers()) {
                                if (member.getImId() != Constants.imId) {
                                    //如果 是 由联系人界面跳转进来  需要通知联系人界面进行信息更新
                                    EventBus.getDefault().post(new MemberInfoUpdateEvent(member.getImId(), member.getName(), member.getAvatar()));
                                    mTitleLayout.setTitle(member.getName());
                                }
                            }
                        }
                    }
                    //使用最新的 成员信息 并对群聊情况下的 title 进行更新
                    if (topic.getType().equals("2")) {
//                        mTitleLayout.setTitle("班级群聊 (" + topic.getMembers().size() + ")");
                        mTitleLayout.setTitle("班级群聊 (" + ret.data.topic.get(0).members.size() + ")");
                    }
                    //持有最新的成员列表
                    memberList = ret.data.topic.get(0).members;
                    mMsgListAdapter.setRemainMemberList(memberList);
                    mMsgListAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });

    }

    private RequestQueueHelper httpQueueHelper = new RequestQueueHelper();

    public class NewTopicCreatedEvent {
        public DbTopic dbTopic;
    }

    public class MockTopicRemovedEvent {
        public DbTopic dbTopic;
    }


    /**
     * 用于传递 member消息更新的类
     * 发送者是 ImMsgListActivity
     * 接受者是 ContactsFragment
     */
    public static class MemberInfoUpdateEvent implements Serializable {
        public MemberInfoUpdateEvent(long imId, String name, String avatar) {
            this.name = name;
            this.avatar = avatar;
            this.imId = imId;
        }

        /**
         * member id
         */
        long imId;
        /**
         * member name
         */
        String name;
        /**
         * member 头像
         */
        String avatar;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    private void doSendMsg(final String msg, final String reqId) {
        SaveTextMsgRequest saveTextMsgRequest = new SaveTextMsgRequest();
        saveTextMsgRequest.imToken = Constants.imToken;
        saveTextMsgRequest.topicId = Long.toString(topic.getTopicId());
        saveTextMsgRequest.msg = msg;

        if (reqId != null) {
            // resend需要走相同的路径，但是msg已经有reqId了
            saveTextMsgRequest.reqId = reqId;
        }

        // 我发送的必然已经存在于队列
        DbMyMsg sendMsg = null;
        for (DbMsg m : topic.mergedMsgs) {
            if (m.getReqId().equals(reqId)) {
                sendMsg = (DbMyMsg) m;
            }
        }
        final DbMyMsg myMsg = sendMsg;
        mMsgListAdapter.setTopic(topic);

        //用户操作 发送行为 当前topic 需要置顶
        topic.shouldInsertToTop=true;
        SharedSingleton.getInstance().set(Constants.kShareTopic,topic);

        // 数据存储，UI显示都完成后，http发送
        httpQueueHelper.addRequest(saveTextMsgRequest, SaveTextMsgResponse.class, new HttpCallback<SaveTextMsgResponse>() {
            @Override
            public void onSuccess(RequestBase request, SaveTextMsgResponse ret) {
                if (ret.data.topicMsg.size() > 0) {
                    ImMsg imMsg = ret.data.topicMsg.get(0);
                    myMsg.setMsgId(imMsg.msgId); // 由于和mqtt异步，这样能保证更新msgId
                    myMsg.setSendTime(imMsg.sendTime);
                }
                myMsg.setState(DbMyMsg.State.Success.ordinal());
                topic.setShowDot(false);
                //新的更新方法
                myMsg.setFrom("mqtt");
                DatabaseDealer.updateResendMsg(myMsg, "mqtt");
                //本地发送成功以后 HTTP 返回 更新latestmsgid与latestmsgtime 当返回topiclist 页面时 排序更新
                if (myMsg.getMsgId()>topic.getTopicId()) {
                    topic.latestMsgId=myMsg.getMsgId();
                    topic.latestMsgTime=myMsg.getSendTime();
                }
//                myMsg.save();
                mMsgListAdapter.notifyDataSetChanged();
                moveToBottom();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                myMsg.setState(DbMyMsg.State.Failed.ordinal());
                //更新数据库的方法
                DatabaseDealer.updateResendMsg(myMsg, "local");
//                myMsg.save();
                mMsgListAdapter.notifyDataSetChanged();
                moveToBottom();
            }
        });

        mMsgEditText.setText("");
    }


    private void doSend(final String msg, final String reqId) {
        final String msgReqId = (reqId == null ? UUID.randomUUID().toString() : reqId);

        // 预先插入mock topic
        if ((memberId > 0) && (topic == null)) {
            // 私聊尚且没有topic，需要创建mock topic
            DbTopic mockTopic = DatabaseDealer.mockTopic();
            DbMember myself = DatabaseDealer.getMemberById(Constants.imId);
            DbMember member = DatabaseDealer.getMemberById(memberId);
            mockTopic.getMembers().add(myself);
            mockTopic.getMembers().add(member);
            if (fromTopicId > 0) { // 来自群聊点击的私聊
                mockTopic.setFromTopic(Long.toString(fromTopicId));
            }
            mockTopic.save();
            topic = mockTopic;
            mMsgListAdapter.setTopic(topic);

            NewTopicCreatedEvent newTopicEvent = new NewTopicCreatedEvent();
            newTopicEvent.dbTopic = mockTopic;
            EventBus.getDefault().post(newTopicEvent);
        }

        // 预先插入mock msg
        //if (DatabaseDealer.isMockTopic(topic)) {
        DbMyMsg myMsg = new DbMyMsg();
        myMsg.setState(DbMyMsg.State.Sending.ordinal());
        myMsg.setReqId(msgReqId);
        myMsg.setMsgId(latestMsgId());
        myMsg.setTopicId(topic.getTopicId());
        myMsg.setSenderId(Constants.imId);
        myMsg.setSendTime(new Date().getTime());
        myMsg.setContentType(10);
        myMsg.setMsg(msg);
        myMsg.setFrom("local");
        //新的更新数据库的方法 如果数据库没有这条数据  内部进行save 操作
        DbMyMsg dbMyMsg = DatabaseDealer.updateResendMsg(myMsg, "local");
//        myMsg.save();
        //记录下操作的本地时间  用于本地排序
        topic.latestOperateLocalTime = System.currentTimeMillis();

        topic.mergedMsgs.add(0, dbMyMsg);

        mMsgListAdapter.setTopic(topic);
        SharedSingleton.getInstance().set(Constants.kShareTopic, topic);
        // TODO: 2018/4/17  头像晃动
//        mMsgListAdapter.notifyDataSetChanged();
        //}

        // 对于是mock topic的需要先创建topic
        if (DatabaseDealer.isMockTopic(topic)) {
            TopicCreateTopicRequest createTopicRequest = new TopicCreateTopicRequest();
            createTopicRequest.imToken = Constants.imToken;
            createTopicRequest.topicType = "1"; // 私聊
            createTopicRequest.imMemberIds = Long.toString(Constants.imId) + "," + Long.toString(memberId);
            createTopicRequest.fromGroupTopicId = topic.getFromTopic();
            createTopicRequest.startRequest(TopicCreateTopicResponse.class, new HttpCallback<TopicCreateTopicResponse>() {
                @Override
                public void onSuccess(RequestBase request, TopicCreateTopicResponse ret) {
                    ImTopic imTopic = null;
                    for (ImTopic topic : ret.data.topic) {
                        imTopic = topic;
                    }
                    // 应该只有一个imTopic

                    DbTopic realTopic = translateMockTopicToReal(imTopic);
                    // 4, 通知新增real topic
                    NewTopicCreatedEvent newTopicEvent = new NewTopicCreatedEvent();
                    newTopicEvent.dbTopic = realTopic;
                    //数据集合变了 需要更新
                    mMsgListAdapter.setTopic(topic);
                    SharedSingleton.getInstance().set(Constants.kShareTopic, topic);
//                    mMsgListAdapter.setmDatas(realTopic.mergedMsgs);
                    EventBus.getDefault().post(newTopicEvent);
                    mMsgListAdapter.setTopic(realTopic);
                    mMsgListAdapter.notifyDataSetChanged();
                    doSendMsg(msg, msgReqId);
                }

                @Override
                public void onFail(RequestBase request, Error error) {
                    DatabaseDealer.topicCreateFailed(topic);
                    mMsgListAdapter.notifyDataSetChanged();
                }
            });
        } else {
            // 已经有对话，直接发送即可
            doSendMsg(msg, msgReqId);
        }
    }


    // region handler
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mMsgListAdapter.setIsLoading(false);
            ptrHelper.loadingComplete();
        }
    };
    // endregion

    //region callback
    private OnRecyclerViewItemClickCallback<DbMsg> onDbMsgCallback = new OnRecyclerViewItemClickCallback<DbMsg>() {
        @Override
        public void onItemClick(int position, DbMsg dbMsg) {
            if (dbMsg instanceof DbMyMsg) {
                final DbMyMsg myMsg = (DbMyMsg) dbMsg;
                if (myMsg.getState() == DbMyMsg.State.Failed.ordinal()) {
                    // 重新发送
                    topic.mergedMsgs.remove(myMsg);
                    myMsg.setState(DbMyMsg.State.Sending.ordinal());
                    myMsg.setMsgId(latestMsgId());
                    DatabaseDealer.updateResendMsg(myMsg, "local");
                    if (myMsg.getContentType() == 20) {
                        //上传七牛成功  如果存储了七牛返回的key 表示上传成功
                        myMsg.setState(DbMyMsg.State.Sending.ordinal());
//                        myMsg.save();
                        DatabaseDealer.updateResendMsg(myMsg, "local");
                        topic.mergedMsgs.add(0, myMsg);
                        mMsgListAdapter.setTopic(topic);
                        mMsgListAdapter.notifyDataSetChanged();
                        mMsgListRecyclerView.scrollToPosition(mMsgListAdapter.getItemCount() - 1);
                        if (!TextUtils.isEmpty(myMsg.getQiNiuKey())) {
                            doSendImgMsg(myMsg.getQiNiuKey(), myMsg.getWith(), myMsg.getHeight(), myMsg);
                        } else {
                            //上传七牛失败
                            getQiNiuToken(myMsg.getLocalViewUrl(), myMsg);
                        }
                    } else {
                        // 1, 先更新数据库中
                        doSend(myMsg.getMsg(), myMsg.getReqId());
                    }
                }
            }
        }
    };


    private boolean hasMoreMsgs = true;
    private OnPullToRefreshCallback mOnLoadMoreCallback = new OnPullToRefreshCallback() {
        @Override
        public void onLoadMore() {
            if (hasMoreMsgs) {
                mMsgListAdapter.setIsLoading(true);
                mMsgListAdapter.notifyItemRangeInserted(0, 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 先从网络取，如果失败了则由数据库重建
                        final DbMsg earliestMsg = topic.mergedMsgs.get(topic.mergedMsgs.size() - 1);
                        GetTopicMsgsRequest getTopicMsgsRequest = new GetTopicMsgsRequest();
                        getTopicMsgsRequest.imToken = Constants.imToken;
                        getTopicMsgsRequest.topicId = Long.toString(topic.getTopicId());
                        getTopicMsgsRequest.startId = Long.toString(earliestMsg.getMsgId());
                        getTopicMsgsRequest.order = "desc";
                        getTopicMsgsRequest.startRequest(GetTopicMsgsResponse.class, new HttpCallback<GetTopicMsgsResponse>() {
                            @Override
                            public void onSuccess(RequestBase request, GetTopicMsgsResponse ret) {
                                ptrHelper.loadingComplete();
                                mMsgListAdapter.setIsLoading(false);
                                mMsgListAdapter.notifyItemRangeRemoved(0, 1);

                                final DbMsg theRefreshingMsg = topic.mergedMsgs.get(topic.mergedMsgs.size() - 1);

                                if (ret.data.topicMsg.size() < DatabaseDealer.pagesize) {
                                    hasMoreMsgs = false;
                                }

                                if (ret.data.topicMsg.size() > 0) {
                                    // 去除最后一条重复的
                                    ret.data.topicMsg.remove(0);
                                }

                                for (ImMsg msg : ret.data.topicMsg) {
                                    DbMsg dbMsg = DatabaseDealer.updateDbMsgWithImMsg(msg, "http", Constants.imId);
                                    topic.mergedMsgs.add(dbMsg);

                                    if (dbMsg.getMsgId() > topic.latestMsgId) {
                                        topic.latestMsgId = dbMsg.getMsgId();
                                    }
                                }

                                mMsgListAdapter.setTopic(topic);

                                //fix  FSAPP-1369
//                                mMsgListAdapter.notifyDataSetChanged();
                                int num = mMsgListAdapter.uiAddedNumberForMsg(earliestMsg);
                                if (num > 0) {
                                    //这里造成了 FSAPP-1369
                                    mMsgListAdapter.notifyItemRangeRemoved(0, 1); // 最后的Datetime需要去掉
                                    mMsgListAdapter.notifyItemRangeInserted(0, num);
                                }
                            }

                            @Override
                            public void onFail(RequestBase request, Error error) {
                                // 从数据库获取
                                ptrHelper.loadingComplete();
                                mMsgListAdapter.setIsLoading(false);
                                mMsgListAdapter.notifyItemRangeRemoved(0, 1);

                                final DbMsg theRefreshingMsg = topic.mergedMsgs.get(topic.mergedMsgs.size() - 1);

                                List<DbMsg> msgs = DatabaseDealer.getTopicMsgs(topic.getTopicId(),
                                        earliestMsg.getMsgId(),
                                        DatabaseDealer.pagesize);

                                // 从数据库取回的消息，包含了startIndex这一条，而对于未发送成功的MyMsg则可能有多条
                                for (Iterator<DbMsg> i = msgs.iterator(); i.hasNext(); ) {
                                    DbMsg uiMsg = i.next();
                                    if (uiMsg.getMsgId() == earliestMsg.getMsgId()) {
                                        i.remove();
                                    }
                                }

                                if (msgs.size() < DatabaseDealer.pagesize) {
                                    hasMoreMsgs = false;
                                }
                                topic.mergedMsgs.addAll(msgs);
                                mMsgListAdapter.setTopic(topic);
                                int num = mMsgListAdapter.uiAddedNumberForMsg(theRefreshingMsg);

                                if (num > 0) {
                                    mMsgListAdapter.notifyItemRangeRemoved(0, 1); // 最后的Datetime需要去掉
                                    mMsgListAdapter.notifyItemRangeInserted(0, num);
                                    //
                                }

                            }
                        });
                    }
                }, 500);

            }

        }
    };
    //endregion

    //region mqtt
    @Subscribe
    public void onMqttMsg(MqttProtobufDealer.NewMsgEvent event) {
        ImMsg msg = event.msg;
        DbMsg dbMsg = DatabaseDealer.updateDbMsgWithImMsg(msg, "mqtt", Constants.imId);
        if (msg.topicId != topic.getTopicId()) {
            // 不是本topic的直接抛弃
            return;
        }

        //topic.mergedMsgs.add(0, dbMsg);
        DatabaseDealer.pendingMsgToTopic(dbMsg, topic);
//
        //在对话内收到消息 默认取消红点的显示  bug1307
        topic.setShowDot(false);
        topic.save();
        if (msg.senderId == Constants.imId) {
            if (msg.contentType == 20 || msg.contentType == 10 && TextUtils.equals("qiniu", msg.contentData.msg) && !TextUtils.isEmpty(msg.contentData.viewUrl)) {
//                mMsgListAdapter.notifyDataSetChanged();
                return;
            }
        }
        //需要重新生成UI  比如 timeline 目前
        mMsgListAdapter.setTopic(topic);
        mMsgListAdapter.notifyDataSetChanged();
//        mMsgListAdapter.notifyItemRangeChanged(0, mMsgListAdapter.getItemCount() - 1);
//        mMsgListRecyclerView.scrollToPosition(mMsgListAdapter.getItemCount() - 1);
        //是否要滑动到最新一条
        if (shouldScrollToBottom) {
            moveToBottom();
        }

    }
    //endregion

    //region util
    private long latestMsgId() {
        long latestMsgId = -1;
        for (DbMsg dbMsg : topic.mergedMsgs) {
            if (dbMsg.getMsgId() > latestMsgId) {
                latestMsgId = dbMsg.getMsgId();
            }
        }
        return latestMsgId;
    }
    //endregion


    /*-------------------------------  发送图片逻辑     ------------------------------------*/
    private ImagePicker imagePicker;

    private void initImagePicker() {
        GlideImageLoader glideImageLoader = new GlideImageLoader();
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(glideImageLoader);
        //显示拍照按钮
        imagePicker.setShowCamera(true);
        //允许裁剪（单选才有效）
        imagePicker.setCrop(false);
        //选中数量限制
        imagePicker.setSelectLimit(9);
        //裁剪框的形状
    }

    private ChoosePicsDialog mClassCircleDialog;

    private void showChoosePicsDialog() {
        if (mClassCircleDialog == null) {
            mClassCircleDialog = new ChoosePicsDialog(ImMsgListActivity.this);
            mClassCircleDialog.setClickListener(new ChoosePicsDialog.OnViewClickListener() {
                @Override
                public void onAlbumClick() {
                    ImMsgListActivity.requestWriteAndReadPermission(new OnPermissionCallback() {
                        @Override
                        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                            Intent intent = new Intent(ImMsgListActivity.this, ImageGridActivity.class);
                            startActivityForResult(intent, IMAGE_PICKER);
                        }

                        @Override
                        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                            Toast.makeText(ImMsgListActivity.this, R.string.no_storage_permissions, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCameraClick() {
                    ImMsgListActivity.requestCameraPermission(new OnPermissionCallback() {
                        @Override
                        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {

                            Intent intent = new Intent(ImMsgListActivity.this, ImageGridActivity.class);
                            // 是否是直接打开相机
                            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true);
                            startActivityForResult(intent, REQUEST_CODE_SELECT);
                        }

                        @Override
                        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                            Toast.makeText(ImMsgListActivity.this, R.string.no_storage_permissions, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        mClassCircleDialog.show();
    }

    private int positionFromPreViewActivity = -1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOAD_BIG_IMG:
                shouldScrollToBottom=true;
                break;
            case IMAGE_PICKER:
            case REQUEST_CODE_SELECT:
                if (data!=null) {
                    isNeedMockTopic();
                    reSizePics(createSelectedImagesList(data));
                }
                break;
            default:
                break;
        }

    }

    /**
     * 是否需要先创建一个mock topic
     */
    private void isNeedMockTopic() {
        if ((memberId > 0) && (topic == null)) {
            // 私聊尚且没有topic，需要创建mock topic
            DbTopic mockTopic = DatabaseDealer.mockTopic();
            DbMember myself = DatabaseDealer.getMemberById(Constants.imId);
            DbMember member = DatabaseDealer.getMemberById(memberId);
            mockTopic.getMembers().add(myself);
            mockTopic.getMembers().add(member);
            if (fromTopicId > 0) { // 来自群聊点击的私聊
                mockTopic.setFromTopic(Long.toString(fromTopicId));
            }
            mockTopic.save();
            topic = mockTopic;
            mMsgListAdapter.setTopic(topic);

            NewTopicCreatedEvent newTopicEvent = new NewTopicCreatedEvent();
            newTopicEvent.dbTopic = mockTopic;
            EventBus.getDefault().post(newTopicEvent);
        }
    }


    /**
     * 构造需要的图片数据
     *
     * @param data
     */
    private ArrayList<ImageItem> createSelectedImagesList(Intent data) {
        ArrayList<ImageItem> images = null;
        try {
            images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
        } catch (Exception e) {

        }
        if (images == null) {
            return null;
        }

        return images;

    }

    /**
     * 使用鲁班压缩图片至200kb左右
     *
     * @param imageItemArrayList 需要压缩的图片列表
     */
    private void reSizePics(List<ImageItem> imageItemArrayList) {
        final List<String> imagePathList = new ArrayList<>();
        for (ImageItem imageItem : imageItemArrayList) {
            imagePathList.add(imageItem.path);
        }
        for (int i = 0; i < imagePathList.size(); i++) {
            final String path = imagePathList.get(i);
            final DbMyMsg myMsg = new DbMyMsg();
            String msgReqId = UUID.randomUUID().toString();
            myMsg.setState(DbMyMsg.State.Sending.ordinal());
            myMsg.setReqId(msgReqId);
            myMsg.setMsgId(latestMsgId());
            myMsg.setTopicId(topic.getTopicId());
            myMsg.setSenderId(Constants.imId);
            myMsg.setSendTime(new Date().getTime());
            myMsg.setContentType(20);
            myMsg.setFrom("local");
            myMsg.setMsg("");
            myMsg.setLocalViewUrl(path);
            Integer[] wh = getPicWithAndHeight(path);
            myMsg.setWith(wh[0]);
            myMsg.setHeight(wh[1]);
            DatabaseDealer.updateResendMsg(myMsg, "local");
            topic.mergedMsgs.add(0, myMsg);
            mMsgListAdapter.setTopic(topic);
            mMsgListRecyclerView.scrollToPosition(mMsgListAdapter.getItemCount() - 1);
            if (!NetWorkUtils.isNetworkAvailable(ImMsgListActivity.this)) {
                sendPicFailure(myMsg);
                continue;
            }
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/faceShow/");
            if(!file.exists()){
                file.mkdirs();
            }
            Luban.with(ImMsgListActivity.this)
                    .load(imagePathList.get(i))
                    .setTargetDir(file.getAbsolutePath())
                    .ignoreBy(200)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            MsgListAdapter.PayLoad payLoad = new MsgListAdapter.PayLoad(MsgListAdapter.PayLoad.CHANG_SEND_PROGRESS, 0d);
                            mMsgListAdapter.notifyItemChanged(mMsgListAdapter.getCurrentDbMsgPosition(myMsg), payLoad);
                        }

                        @Override
                        public void onSuccess(File file) {
                            getQiNiuToken(file.getAbsolutePath(), myMsg);
                            Integer[] wh = getPicWithAndHeight(file.getAbsolutePath());
                            myMsg.setWith(wh[0]);
                            myMsg.setHeight(wh[1]);
                            myMsg.setLocalViewUrl(file.getAbsolutePath());
                            myMsg.save();
                        }

                        @Override
                        public void onError(Throwable e) {


                        }
                    }).launch();

        }


    }

    UUID mGetQiNiuTokenUUID;

    /**
     * 获取七牛token
     */
    private void getQiNiuToken(final String picPath, final DbMyMsg myMsg) {
        if (!TextUtils.isEmpty(mQiniuToken)) {
            uploadPicByQiNiu(picPath, myMsg);
        } else {
            GetQiNiuTokenRequest getQiNiuTokenRequest = new GetQiNiuTokenRequest();
            getQiNiuTokenRequest.from = "100";
            getQiNiuTokenRequest.dtype = "app";
            getQiNiuTokenRequest.token = Constants.token;
            mGetQiNiuTokenUUID = getQiNiuTokenRequest.startRequest(GetQiNiuTokenResponse.class, new HttpCallback<GetQiNiuTokenResponse>() {
                @Override
                public void onSuccess(RequestBase request, GetQiNiuTokenResponse ret) {
                    mGetQiNiuTokenUUID = null;
                    mCancelQiNiuUploadPics = false;
                    if (ret != null) {
                        if (ret.code == 0) {
                            mQiniuToken = ret.getData().getToken();
                            uploadPicByQiNiu(picPath, myMsg);
                        } else {
                            // TODO: 2018/3/29 error
                            sendPicFailure(myMsg);
                        }

                    }
                }

                @Override
                public void onFail(RequestBase request, Error error) {
                    // TODO: 2018/3/29 error
                    mGetQiNiuTokenUUID = null;
                    sendPicFailure(myMsg);

                }
            });
        }
    }

    private UploadManager uploadManager = null;
    /**
     * 此参数设置为true时 则正在执行的七牛上传图片将被停止
     */
    private boolean mCancelQiNiuUploadPics = false;
    private Configuration config = new Configuration.Builder()
            // 分片上传时，每片的大小。 默认256K
            .chunkSize(2 * 1024 * 1024)
            // 启用分片上传阀值。默认512K
            .putThreshhold(4 * 1024 * 1024)
            // 链接超时。默认10秒
            .connectTimeout(10)
            // 服务器响应超时。默认60秒
            .responseTimeout(60)
            .build();

    private void uploadPicByQiNiu(final String picPath, final DbMyMsg myMsg) {

        if (uploadManager == null) {
            uploadManager = new UploadManager(config);
        }


        uploadManager.put(picPath, null, mQiniuToken, new UpCompletionHandler() {
            @Override
            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                if (!responseInfo.isOK()) {
                    sendPicFailure(myMsg);
                } else {
                    try {
                        if (jsonObject != null) {
                            String key = jsonObject.getString("key");
                            myMsg.setQiNiuKey(key);
                            DatabaseDealer.updateResendMsg(myMsg, "local");
                            if (!TextUtils.isEmpty(key)) {
                                Integer[] widthAndHeight = getPicWithAndHeight(picPath);
                                doSendImgMsg(key, widthAndHeight[0], widthAndHeight[1], myMsg);
                            } else {
                                sendPicFailure(myMsg);
                            }
                        } else {
                            sendPicFailure(myMsg);
                        }

                    } catch (JSONException e) {
                        sendPicFailure(myMsg);
                    }
                    MsgListAdapter.PayLoad payLoad = new MsgListAdapter.PayLoad(MsgListAdapter.PayLoad.CHANG_SEND_PROGRESS, 0.99d);
                    mMsgListAdapter.notifyItemChanged(mMsgListAdapter.getCurrentDbMsgPosition(myMsg), payLoad);

                }
            }


        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String s, double v) {
                Double progress;
                if (v > 0.99) {
                    progress = 0.99d;
                } else {
                    progress = v;
                }
                MsgListAdapter.PayLoad payLoad = new MsgListAdapter.PayLoad(MsgListAdapter.PayLoad.CHANG_SEND_PROGRESS, progress);
                mMsgListAdapter.notifyItemChanged(mMsgListAdapter.getCurrentDbMsgPosition(myMsg), payLoad);

            }

        }, new

                UpCancellationSignal() {
                    @Override
                    public boolean isCancelled() {
                        return mCancelQiNiuUploadPics;
                    }

                }));
    }

    private void sendPicFailure(DbMyMsg myMsg) {
        myMsg.setState(DbMyMsg.State.Failed.ordinal());
        DatabaseDealer.updateResendMsg(myMsg, "local");
        MsgListAdapter.PayLoad payLoad = new MsgListAdapter.PayLoad(MsgListAdapter.PayLoad.CHANG_SEND_STATUE);
        mMsgListAdapter.notifyItemChanged(mMsgListAdapter.getCurrentDbMsgPosition(myMsg), payLoad);
    }

    /**
     * 计算图片的宽高
     *
     * @param imgPath 图片路径
     * @return Integer【】 第一个参数表示width 第二个参数表示height
     */
    private Integer[] getPicWithAndHeight(String imgPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        return new Integer[]{options.outWidth, options.outHeight};
    }


    private void doSendImgMsg(final String rid, final int with, final int height, final DbMyMsg dbMyMsg) {
        isNeedMockTopic();
        // 对于是mock topic的需要先创建topic
        if (DatabaseDealer.isMockTopic(topic)) {
            TopicCreateTopicRequest createTopicRequest = new TopicCreateTopicRequest();
            createTopicRequest.imToken = Constants.imToken;
            createTopicRequest.topicType = "1"; // 私聊
            createTopicRequest.imMemberIds = Long.toString(Constants.imId) + "," + Long.toString(memberId);
            createTopicRequest.fromGroupTopicId = topic.getFromTopic();
            createTopicRequest.startRequest(TopicCreateTopicResponse.class, new HttpCallback<TopicCreateTopicResponse>() {
                @Override
                public void onSuccess(RequestBase request, TopicCreateTopicResponse ret) {
                    ImTopic imTopic = null;
                    for (ImTopic topic : ret.data.topic) {
                        imTopic = topic;
                    }
                    // 应该只有一个imTopic
                    DbTopic realTopic = translateMockTopicToReal(imTopic);

                    mMsgListAdapter.setTopic(realTopic);
                    // 4, 通知新增real topic
                    NewTopicCreatedEvent newTopicEvent = new NewTopicCreatedEvent();
                    newTopicEvent.dbTopic = realTopic;
                    EventBus.getDefault().post(newTopicEvent);

                    mMsgListAdapter.notifyDataSetChanged();
                    doSendImage(rid, with, height, dbMyMsg);
                }

                @Override
                public void onFail(RequestBase request, Error error) {
                    DatabaseDealer.topicCreateFailed(topic);
                    mMsgListAdapter.notifyDataSetChanged();
                }
            });
        } else {
            // 已经有对话，直接发送即可
            topic.shouldInsertToTop=true;
            doSendImage(rid, with, height, dbMyMsg);
        }
    }

    @NonNull
    private DbTopic translateMockTopicToReal(ImTopic imTopic) {
        // 1，通知移除mock topic
        DbTopic mockTopic = getSharedTopic();
        MockTopicRemovedEvent mockRemoveEvent = new MockTopicRemovedEvent();
        mockRemoveEvent.dbTopic = mockTopic;
        EventBus.getDefault().post(mockRemoveEvent);


        // 2，添加server返回的real topic
        DbTopic realTopic = DatabaseDealer.updateDbTopicWithImTopic(imTopic);
        realTopic.latestMsgTime = imTopic.latestMsgTime;
        realTopic.latestMsgId = imTopic.latestMsgId;
        realTopic.save();
        topic = realTopic;
        // 3，做mock topic 和 real topic间msgs的转换
        DatabaseDealer.migrateMsgsForMockTopic(mockTopic, realTopic);
        SharedSingleton.getInstance().set(Constants.kShareTopic, realTopic);
        return realTopic;
    }

    private DbTopic getSharedTopic() {
        return SharedSingleton.getInstance().get(Constants.kShareTopic);
    }


    /**
     * 发送图片
     */

    private void doSendImage(String rid, int width, int height, final DbMyMsg myMsg) {
        if (TextUtils.isEmpty(rid)) {
            return;
        }

        SaveImageMsgRequest saveImageMsgRequest = new SaveImageMsgRequest();
        saveImageMsgRequest.imToken = Constants.imToken;
        saveImageMsgRequest.topicId = Long.toString(topic.getTopicId());
        saveImageMsgRequest.rid = rid;
        saveImageMsgRequest.height = String.valueOf(height);
        saveImageMsgRequest.width = String.valueOf(width);
        saveImageMsgRequest.reqId = myMsg.getReqId();

        //用户操作 发送行为 当前topic 需要置顶
        topic.shouldInsertToTop=true;
        // 数据存储，UI显示都完成后，http发送
        httpQueueHelper.addRequest(saveImageMsgRequest, SaveImageMsgResponse.class, new HttpCallback<SaveImageMsgResponse>() {
            @Override
            public void onSuccess(RequestBase request, SaveImageMsgResponse ret) {
                if (ret.data.topicMsg.size() > 0) {
                    ImMsg imMsg = ret.data.topicMsg.get(0);
                    myMsg.setReqId(imMsg.reqId); // 由于和mqtt异步，这样能保证更新msgId
                    myMsg.setMsgId(imMsg.msgId);
                    myMsg.setState(DbMyMsg.State.Success.ordinal());
                    myMsg.setViewUrl(ret.data.topicMsg.get(0).contentData.viewUrl);
                    myMsg.setWith(ret.data.topicMsg.get(0).contentData.width);
                    myMsg.setHeight(ret.data.topicMsg.get(0).contentData.height);
                    myMsg.setFrom("mqtt");
                    myMsg.setSendTime(imMsg.sendTime);
                    topic.setShowDot(false);
                    DatabaseDealer.updateResendMsg(myMsg, "mqtt");
                    //发送成功以后 更新
                    if (myMsg.getMsgId()>topic.latestMsgId) {
                        topic.latestMsgId=myMsg.getMsgId();
                        topic.latestMsgTime=myMsg.getSendTime();
                    }

                    MsgListAdapter.PayLoad payLoad = new MsgListAdapter.PayLoad(MsgListAdapter.PayLoad.CHANG_SEND_STATUE);
                    mMsgListAdapter.notifyItemChanged(mMsgListAdapter.getCurrentDbMsgPosition(myMsg), payLoad);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                myMsg.setState(DbMyMsg.State.Failed.ordinal());
                DatabaseDealer.updateResendMsg(myMsg, "local");
                MsgListAdapter.PayLoad payLoad = new MsgListAdapter.PayLoad(MsgListAdapter.PayLoad.CHANG_SEND_STATUE);
                mMsgListAdapter.notifyItemChanged(mMsgListAdapter.getCurrentDbMsgPosition(myMsg), payLoad);
            }
        });

//        mMsgEditText.setText("");
    }


    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager       设置RecyclerView对应的manager
     * @param mRecyclerView 当前的RecyclerView
     * @param n             要跳转的位置
     */
    public void moveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }


    public void moveToBottom() {
//        moveToPosition((LinearLayoutManager)mMsgListRecyclerView.getLayoutManager()
//                ,mMsgListRecyclerView,mMsgListRecyclerView.getAdapter().getItemCount()-1);
        mMsgListRecyclerView.scrollToPosition(mMsgListRecyclerView.getAdapter().getItemCount() - 1);
    }


}
