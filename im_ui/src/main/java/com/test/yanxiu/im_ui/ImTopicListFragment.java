package com.test.yanxiu.im_ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.common_base.utils.ActivityManger;
import com.test.yanxiu.common_base.utils.SharedSingleton;
import com.test.yanxiu.common_base.utils.SrtLogger;
import com.test.yanxiu.common_base.utils.talkingdata.EventUpdate;
import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.im_core.RequestQueueHelper;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbMyMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_core.dealer.MqttProtobufDealer;
import com.test.yanxiu.im_core.http.GetTopicMsgsRequest;
import com.test.yanxiu.im_core.http.GetTopicMsgsResponse;
import com.test.yanxiu.im_core.http.PolicyConfigRequest;
import com.test.yanxiu.im_core.http.PolicyConfigResponse;
import com.test.yanxiu.im_core.http.TopicGetMemberTopicsRequest;
import com.test.yanxiu.im_core.http.TopicGetMemberTopicsResponse;
import com.test.yanxiu.im_core.http.TopicGetTopicsRequest;
import com.test.yanxiu.im_core.http.TopicGetTopicsResponse;
import com.test.yanxiu.im_core.http.common.ImMsg;
import com.test.yanxiu.im_core.http.common.ImTopic;
import com.test.yanxiu.im_core.mqtt.MqttService;
import com.test.yanxiu.im_ui.callback.OnRecyclerViewItemClickCallback;
import com.test.yanxiu.im_ui.callback.OnUserRemoveFromClaszCallback;
import com.test.yanxiu.im_ui.contacts.ContactsActivity;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.LitePal;
import org.litepal.crud.ClusterQuery;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.MODE_PRIVATE;


public class ImTopicListFragment extends FaceShowBaseFragment {
    private ImTitleLayout mTitleLayout;
    private ImageView mNaviLeftImageView;
    private TextView mNaviRightTextView;
    private RecyclerView mTopicListRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static final int ACTIVITY_RESULT_REMOVED_USER = 0X33;
    private List<DbTopic> topics = new ArrayList<>();

    /**
     * 用户被移除班级的回调
     */
    private OnUserRemoveFromClaszCallback mOnUserRemoveFromClaszCallback;

    public void setOnUserRemoveFromClaszCallback(OnUserRemoveFromClaszCallback onUserRemoveFromClaszCallback) {
        mOnUserRemoveFromClaszCallback = onUserRemoveFromClaszCallback;
    }

    public ImTopicListFragment() {
        // Required empty public constructor
    }

    public MqttService.MqttBinder getBinder() {
        return binder;
    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView==null) {
            rootView  = inflater.inflate(R.layout.fragment_topic_list, container, false);
            checkMyMsgStatus();
            startMqttService();
            setupView(rootView);
            setupData();
        }
        return rootView;
    }

    public void initData(){
        setupData();
    }

    @Override
    public void onDestroyView() {
        stopMqttService();
        super.onDestroyView();
    }


    //界面由 msglist返回后 需要重新排序

    public void onMsgListActivityReturned() {
        //由聊天界面返回 直接对操作的操作的聊天界面进行执行操作 置顶排序
        rearrange(DatabaseDealer.topicComparatorInsertTopBaseOnLocalTime);
//        rearrangeTopics(true); // 重新排列群聊、私聊 并以本地操作时间排序依据

        curTopic = null;
        msgShownTopics.clear();
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void setupView(View v) {
        mTitleLayout = v.findViewById(R.id.title_layout);

        // set title
        mTitleLayout.setTitle("聊聊");

        // set navi left
        mNaviLeftImageView = v.findViewById(R.id.navi_left_imageview);
        mNaviLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleActionCallback != null) {
                    titleActionCallback.onLeftImgClicked();
                }
                LitePal.deleteDatabase(Long.toString(Constants.imId) + "_db");
            }
        });
        mTitleLayout.setLeftView(mNaviLeftImageView);

        // set navi right
//        mNaviRightTextView = v.findViewById(R.id.navi_right_textview);
//        mNaviRightTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //事件统计 点击通讯录
//                EventUpdate.onClickContactEvent(getActivity());
//                Intent intent = new Intent(ImTopicListFragment.this.getContext(), ContactsActivity.class);
//                getActivity().startActivityForResult(intent, Constants.IM_REQUEST_CODE_CONTACT);
//            }
//        });
//        mTitleLayout.setRightView(mNaviRightTextView);

        // set topic list
        mSwipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);

        mTopicListRecyclerView = v.findViewById(R.id.topic_list_recyclerview);
        mTopicListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false));
        TopicListAdapter adapter = new TopicListAdapter(getContext(), topics);
        mTopicListRecyclerView.setAdapter(adapter);
        adapter.setmOnItemClickCallback(onDbTopicCallback);
    }

    private void setupData() {
        updateTopicsFromDb();
        // 为了重连等机制，放到mqtt connected以后做http拉取
        // updateTopicsFromHttpWithoutMembers();
    }

    //检查正在发生的
    private void checkMyMsgStatus() {
        DatabaseDealer.useDbForUser(Long.toString(Constants.imId) + "_db");
        ClusterQuery myQuery = null;
        myQuery = DataSupport
                .where("senderId = ?", String.valueOf(Constants.imId));
        List<DbMyMsg> myMsgs = myQuery.find(DbMyMsg.class);
        for (int i = 0; i < myMsgs.size(); i++) {
            switch (myMsgs.get(i).getState()) {
                case 1:
                    myMsgs.get(i).setState(2);
                    myMsgs.get(i).save();
            }
        }
    }

    // 1，从DB列表生成
    private void updateTopicsFromDb() {
        DatabaseDealer.useDbForUser(Long.toString(Constants.imId) + "_db");
        topics.addAll(DatabaseDealer.topicsFromDb());
        for (DbTopic topic : topics) {
            for (DbMsg msg : topic.mergedMsgs) {
                msg.setFrom("http");
            }
        }
        //排序 由数据库获取topiclist 以本地操作时间为依据
        rearrange(DatabaseDealer.topicComparatorBaseOnBothLocalAndServerTime);
//        rearrangeTopics(false);
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
    }


    // 2，从Http获取用户的topic列表，不包含members，完成后继续从Http获取需要更新的topic的信息
    private void updateTopicsFromHttpWithoutMembers() {
        TopicGetMemberTopicsRequest getMemberTopicsRequest = new TopicGetMemberTopicsRequest();
        getMemberTopicsRequest.imToken = Constants.imToken;
        getMemberTopicsRequest.startRequest(TopicGetMemberTopicsResponse.class, new HttpCallback<TopicGetMemberTopicsResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicGetMemberTopicsResponse ret) {
                // 3
                //获取用户服务器上所有的topic
                for (ImTopic imTopic : ret.data.topic) {
                    binder.subscribeTopic(Long.toString(imTopic.topicId));
                }
                //检查用户是否在离线的时候被topic 删除 对象分别为数据库topics 与 ret.data.topic
                checkBeenRemoveFromAnyTopic(ret);
                //
                //检查是否需要 成员更新 使用数据库数据进行判断
                updateTopicsFromHttpAddMembers(ret);
                //更新 topic 列表
                for (ImTopic imTopic : ret.data.topic) {
                    DbTopic dbTopic = DatabaseDealer.updateDbTopicWithImTopic(imTopic);
                    boolean hasThisTopic = false;
                    // 更新uiTopics
                    for (DbTopic uiTopic : topics) {
                        if (uiTopic.getTopicId() == imTopic.topicId) {
                            hasThisTopic = true;
                            uiTopic.setTopicId(imTopic.topicId);
                            uiTopic.setName(imTopic.topicName);
                            uiTopic.setType(imTopic.topicType);
                            uiTopic.setChange(imTopic.topicChange);
                            uiTopic.setGroup(imTopic.topicGroup);
                        }
                    }
                    if (!hasThisTopic) {
                        //新topic 进入UI 记录本地操作时间
                        topics.add(dbTopic);
                        dbTopic.latestMsgTime = imTopic.latestMsgTime;
                        dbTopic.latestMsgId = imTopic.latestMsgId;
                        dbTopic.latestOperateLocalTime = System.currentTimeMillis();
                        dbTopic.shouldInsertToTop = true;
                    }
                }
                updateEachTopicMsgs(topics);
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }

    /**
     * 用 HTTP 请求的 topic 列表与本地数据看的topic 类表进行比较
     * 如果有用户被某些topic 删除 弹出提示跳转 班级选择界面
     */
    private void checkBeenRemoveFromAnyTopic(TopicGetMemberTopicsResponse ret) {
        List<DbTopic> dbHistoryTopic = new ArrayList<>();
        dbHistoryTopic.addAll(topics);
        Iterator<DbTopic> dbTopicIterator = dbHistoryTopic.iterator();

        DbTopic dt = null;
        while (dbTopicIterator.hasNext()) {
            dt = dbTopicIterator.next();
            if (DatabaseDealer.isMockTopic(dt)) {
                dbTopicIterator.remove();
            }
            for (ImTopic imTopic : ret.data.topic) {
                if (imTopic.topicId == dt.getTopicId()) {
                    dbTopicIterator.remove();
                    break;
                }
            }
        }
        if (dbHistoryTopic.size() != 0) {
            //本地有服务器上没有的topic 证明用户在这个topic 中被删除
            if (mOnUserRemoveFromClaszCallback != null) {
                try {
                    Toast.makeText(getContext(), "【已被移出" + dbHistoryTopic.get(0).getGroup() + "】", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
//                        e.printStackTrace();
                    Log.e(TAG, "toast crash : ");
                }

                for (DbTopic dbTopic : dbHistoryTopic) {
                    // 在数据库中删除
                    DatabaseDealer.deleteTopicById(dbTopic.getTopicId());
                    // topic 中删除
                    for (DbTopic topic : topics) {
                        if (topic.getTopicId() == dbTopic.getTopicId()) {
                            topics.remove(topic);
                            break;
                        }
                    }
                }

                //获取群聊数量
                int remainGroup = 0;
                for (DbTopic topic : topics) {
                    if (topic.getType().equals("2")) {
                        remainGroup++;
                    }
                }
                mOnUserRemoveFromClaszCallback.onRemoved(remainGroup);
            }
        }
    }

    // 3，从Http获取需要更新的topic的信息，完成后写入DB，更新UI
    private void updateTopicsFromHttpAddMembers(TopicGetMemberTopicsResponse ret) {
        List<String> idTopicsNeedUpdateMember = new ArrayList<>(); // 因为可能有新的，所以只能用topicId
        List<DbTopic> topicsNotNeedUpdateMember = new ArrayList<>();
        // 所有不在DB中的，以及所有在DB中但change不等于topicChange的topics
        for (ImTopic imTopic : ret.data.topic) {
            boolean needUpdateMembers = true;
            for (DbTopic dbTopic : topics) {
                if (dbTopic.getTopicId() == imTopic.topicId) {
                    dbTopic.latestMsgId = imTopic.latestMsgId;
                    dbTopic.latestMsgTime = imTopic.latestMsgTime;
                    if (dbTopic.getChange().equals(imTopic.topicChange)) {
                        needUpdateMembers = false;
                        topicsNotNeedUpdateMember.add(dbTopic);
                        break;
                    }
                }
            }
            if (needUpdateMembers) {
                idTopicsNeedUpdateMember.add(Long.toString(imTopic.topicId));
            }
        }
        //更新memeber 信息
        if (idTopicsNeedUpdateMember.size() > 0) {
            for (String topicId : idTopicsNeedUpdateMember) {
                // 由于server限制，改成一个个取
                updateTopicsWithMembers(topicId);
            }
        }
    }

    // 4，依次更新topic的最新一页数据，并更新数据库，然后更新UI
    private int totalRetryTimes;
    private RequestQueueHelper rqHelper = new RequestQueueHelper();

    private void updateEachTopicMsgs(List<DbTopic> topics) {
        totalRetryTimes = 10;
        //由于 topics 按时间顺序 逆序排列  如果需要置顶 需要逆序判断 需要将时间较远的先置顶 时间近的后置顶
        for (int i = topics.size() - 1; i >= 0; i--) {
            DbTopic dbTopic = topics.get(i);
            doGetTopicMsgsRequest(dbTopic);
        }
    }


    /**
     * 目的是获取最新一页的msglist 操作不需要参与排序
     */
    private void doGetTopicMsgsRequest(final DbTopic dbTopic) {
        if ((dbTopic.mergedMsgs != null) && (dbTopic.mergedMsgs.size() > 0)) {
            DbMsg dbMsg = dbTopic.mergedMsgs.get(0);
            if (dbMsg.getMsgId() >= dbTopic.latestMsgId) {
                // 数据库中已有最新的msg，不用更新
                dbTopic.latestMsgId = dbMsg.getMsgId();
                return;
            }
        }

        GetTopicMsgsRequest getTopicMsgsRequest = new GetTopicMsgsRequest();
        getTopicMsgsRequest.imToken = Constants.imToken;
        getTopicMsgsRequest.topicId = Long.toString(dbTopic.getTopicId());
        //如果是可空的topic 没有消息记录 赋予latestmsgid 为long最大值
        if (dbTopic.latestMsgId == 0) {
            getTopicMsgsRequest.startId = String.valueOf(Long.MAX_VALUE);
        } else {
            getTopicMsgsRequest.startId = Long.toString(dbTopic.latestMsgId);
        }
        getTopicMsgsRequest.order = "desc";

        rqHelper.addRequest(getTopicMsgsRequest, GetTopicMsgsResponse.class, new HttpCallback<GetTopicMsgsResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetTopicMsgsResponse ret) {
                // 新建topic成功后topicMsg.size为0
                if (ret.data.topicMsg == null || ret.data.topicMsg.size() == 0) {
                    //判断获取的消息数量是否为0 或空  此时 不显示红点
                    dbTopic.setShowDot(false);
                    dbTopic.save();
                    return;
                }

                // 有新消息，UI上应该显示红点
                dbTopic.setShowDot(true);
                dbTopic.save();

                // 用最新一页，取代之前的mergedMsgs，
                // 因为和mqtt是异步，所以这次mqtt连接后新收到的消息不应该删除（所以从DB来的数据，手动设置为from "http"）,有点trick
                mergeMsgHttpAndLocal(dbTopic, ret.data.topicMsg);
                //通知imMsgListActivity刷新列表消息
                SharedSingleton.getInstance().set(Constants.kShareTopic, dbTopic);
                MqttProtobufDealer.onTopicUpdate(dbTopic.getTopicId());

                dbTopic.shouldInsertToTop = true;
                //以 最新时间排序 不考虑 服务器与 本地时差
                rearrange(DatabaseDealer.topicComparatorInsertTopBaseOnLocalTime);
//                rearrangeTopics(false);
                mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                // 重试
                if (totalRetryTimes-- <= 0) {
                    return;
                }
                doGetTopicMsgsRequest(dbTopic);
            }
        });
    }


    /**
     * 更新数据库
     * 对新消息与本地消息进行排序
     *
     * @param dbTopic    当前获取新消息列表的topic
     * @param newMsgList 服务器返回的最新消息列表
     */
    private void mergeMsgHttpAndLocal(DbTopic dbTopic, List<ImMsg> newMsgList) {
        //获取新的列表中 有 上次的,MQTT 推送数据 进行本地更新 以mergedMsg 为主体
        Iterator<ImMsg> imMsgIterator = newMsgList.iterator();
        while (imMsgIterator.hasNext()) {
            ImMsg imMsg = imMsgIterator.next();
            //1、判断 是否本地已有数据
            for (DbMsg dbMsg : dbTopic.mergedMsgs) {
                if (dbMsg.getReqId().equals(imMsg.reqId)) {
                    //mqtt 数据刷新为推送转为http拉取   local 为网络不好时 本地认为未成功的数据
                    if (dbMsg.getFrom().equals("mqtt") || dbMsg.getFrom().equals("local")) {
                        DatabaseDealer.updateDbMsgWithImMsg(imMsg, "http", Constants.imId);
                    }
                    //去重
                    imMsgIterator.remove();
                    break;
                }
            }
        }
        //当获取的
        //2、进行新数据的插入 上面已经完成去重工作  这个应该根据msgId进行排序
        for (int i = newMsgList.size() - 1; i >= 0; i--) {
            DbMsg newMsg = DatabaseDealer.updateDbMsgWithImMsg(newMsgList.get(i), "http", Constants.imId);
            dbTopic.mergedMsgs.add(0, newMsg);
            if (newMsg.getMsgId() > dbTopic.latestMsgId) {
                dbTopic.latestMsgId = newMsg.getMsgId();
                dbTopic.latestMsgTime = newMsg.getSendTime();
                //刷新本地操作时间 更新时刷新 为了有离线消息和无离线消息的topic排序
                dbTopic.latestOperateLocalTime = System.currentTimeMillis();
            }
        }
    }

    public ServiceConnection mqttServiceConnection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SrtLogger.log("im mqtt", "service connectted");
            binder = (MqttService.MqttBinder) iBinder;

            binder.getService().setmMqttServiceCallback(new MqttService.MqttServiceCallback() {
                @Override
                public void onDisconnect() {
                    SrtLogger.log("frc", "service onDisconnect");
                    // 每30秒重试一次
                    if (reconnectTimer != null) {
                        reconnectTimer.cancel();
                        reconnectTimer.purge();
                        reconnectTimer = null;
                    }

                    reconnectTimer = new Timer();
                    reconnectTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // 重连必须重新给一个clientId，否则直接失败
                            binder.init();
                            binder.connect();
                        }
                    }, 30 * 1000);
                }

                @Override
                public void onConnect() {
                    SrtLogger.log("frc", "service onConnect");
                    if (reconnectTimer != null) {
                        reconnectTimer.cancel();
                        reconnectTimer.purge();
                        reconnectTimer = null;
                    }

                    // 为统一处理，移到此处
                    updateTopicsFromHttpWithoutMembers();

                    binder.subscribeMember(Constants.imId);

                    for (DbTopic dbTopic : topics) {
                        binder.subscribeTopic(Long.toString(dbTopic.getTopicId()));
                    }
                }
            });

            binder.init();
            binder.connect();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            SrtLogger.log("im mqtt", "service disconnectted");

            if (reconnectTimer != null) {
                reconnectTimer.cancel();
                reconnectTimer.purge();
                reconnectTimer = null;
            }
        }
    };
    //region MQTT
    private Timer reconnectTimer = new Timer();
    private MqttService.MqttBinder binder = null;
    private DbTopic curTopic = null;                                    // 当前界面上开启msgs的topic，curTopic为空说明是新建私聊
    private ArrayList<DbTopic> msgShownTopics = new ArrayList<>();      // 因为需要可以从群聊点击头像进入私聊，多级msgs界面

    private void startMqttService() {
        EventBus.getDefault().register(this);
        getImHostRequest(new GetImHostCallBack() {
            @Override
            public void onSuccess(String host) {
                Intent intent = new Intent(getActivity(), MqttService.class);
                intent.putExtra("host", host);
                getActivity().bindService(intent, mqttServiceConnection, BIND_AUTO_CREATE);
            }
        });


    }

    private void stopMqttService() {
        // 已经在MqttService的unbind中处理
    }

    @Subscribe
    public void onMqttMsg(MqttProtobufDealer.NewMsgEvent event) {
        ImMsg msg = event.msg;
        DbMsg dbMsg = DatabaseDealer.updateDbMsgWithImMsg(msg, "mqtt", Constants.imId);

        // mqtt上的实时消息，按照接收顺序写入ui的datalist
        // mqtt不更新latestMsg，只有从http确认的消息才更新latestMsg，所以下次进来还是回去http拉取最新页消息
        for (DbTopic dbTopic : topics) {
            if (dbTopic.getTopicId() == msg.topicId) {
                //dbTopic.mergedMsgs.add(0, dbMsg);
                DatabaseDealer.pendingMsgToTopic(dbMsg, dbTopic);
                //判断 如果mqtt 传过来的是自己发送的消息 不显示红点
                dbTopic.setShowDot(msg.senderId != Constants.imId);
                dbTopic.save();
                //需要对topic置顶
                dbTopic.shouldInsertToTop = true;
                break;
            }
        }
//        排序 以收到消息置顶排序 以本地时间为基础的置顶排序
        rearrange(DatabaseDealer.topicComparatorInsertTopBaseOnLocalTime);
//        rearrangeTopics(true);
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();

    }

    @Subscribe
    public void onMqttMsg(final MqttProtobufDealer.TopicChangeEvent event) {
        // 目前只处理AddTo
        if (event.type == MqttProtobufDealer.TopicChange.AddTo) {
            binder.subscribeTopic(Long.toString(event.topicId));
            updateTopicsWithMembers(Long.toString(event.topicId));
        } else if (event.type == MqttProtobufDealer.TopicChange.RemoveFrom) {
            //检查当前用户是否 被某个 topic 除名
            checkUserRemove(event);
        }
//        //排序以服务器时间为依据
//        rearrangeTopics(false);
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
    }

    /**
     * 通过请求用户的最新 topic 列表 检查 通知有除名的 topic
     * 貌似也可以用 topic id  来获取对应 topic 的 member 来检查自己是否还在 memberlist 中
     * 但 已被移除的 member 能否通过 topicid 获取 topic member 信息？
     */
    private void checkUserRemove(final MqttProtobufDealer.TopicChangeEvent event) {
        isUserInTopicByTopicList(event);
//        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
    }

    /**
     * 获取当前用户的 topic 列表  判断用户是否还在这个 topic 中
     */
    private void isUserInTopicByTopicList(final MqttProtobufDealer.TopicChangeEvent event) {
        TopicGetMemberTopicsRequest getMemberTopicsRequest = new TopicGetMemberTopicsRequest();
        getMemberTopicsRequest.imToken = Constants.imToken;
        getMemberTopicsRequest.startRequest(TopicGetMemberTopicsResponse.class, new HttpCallback<TopicGetMemberTopicsResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicGetMemberTopicsResponse ret) {
                // topic list 请求成功 可以进行一次刷新操作
                synchronized (topics) {
                    boolean inTopics = false;
                    //判断本地是否已经移除的 目标topic
                    for (DbTopic topic : topics) {
                        if (topic.getTopicId() == event.topicId) {
                            inTopics = true;
                            break;
                        }
                    }
                    if (!inTopics) {
                        //如果已经不在列表中 已经被移除了
                        return;
                    }

                    //如果还在列表中 为跳转信息做准备 获取班级数量
                    int groupTopicNum = 0;
                    boolean hasTheTopic=false;
                    for (ImTopic imTopic : ret.data.topic) {
                        if (imTopic.topicType.equals("2")) {
                            groupTopicNum++;
                        }
                        if (imTopic.topicId==event.topicId) {
                            hasTheTopic=true;
                        }
                    }

                    if (hasTheTopic) {
                        //还在这个班级中 不操作直接返回
                        return ;
                    }
                    //在 topic 中删除
                    Iterator<DbTopic> iterator = topics.iterator();
                    DbTopic removedTopic = null;
                    while (iterator.hasNext()) {
                        removedTopic = iterator.next();
                        if (removedTopic.getTopicId() == event.topicId) {
                            //取消目标 topic 的 mqtt 的订阅
                            binder.getService().doUnsubscribeTopic(String.valueOf(event.topicId));
                            try {
                                Toast.makeText(getContext(), "【已被移出" + removedTopic.getGroup() + "】", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e(TAG, "toast crash : ");
                                //在UI 列表中删除
                                iterator.remove();
                                // 在数据库中删除
                                DatabaseDealer.deleteTopicById(event.topicId);
                                return ;
                            }
                            //在UI 列表中删除
                            iterator.remove();
                            // 在数据库中删除
                            DatabaseDealer.deleteTopicById(event.topicId);
                            break;
                        }
                    }
                    //完成删除操作
                    mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
                    //判断栈顶Activity 如果聊天界面开启 先关闭聊天界面 然后在mainactivity的onActivityResult 中进行logout
                    Activity currentTopActivity = ActivityManger.getTopActivity();
                    if ("ImMsgListActivity".equals(currentTopActivity.getClass().getSimpleName())) {
                        //这里需要对数量进行判断
                        Intent intent = currentTopActivity.getIntent();
                        intent.putExtra("groupTopicNum", groupTopicNum);
                        currentTopActivity.setResult(ACTIVITY_RESULT_REMOVED_USER, intent);
                        currentTopActivity.finish();
                    } else if (mOnUserRemoveFromClaszCallback != null) {
                        //回调给 mainactivity 用户被除名
                        mOnUserRemoveFromClaszCallback.onRemoved(groupTopicNum);
                    }

                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }


    // http, mqtt 公用
    private void updateTopicsWithMembers(String topicIds) {
        TopicGetTopicsRequest getTopicsRequest = new TopicGetTopicsRequest();
        getTopicsRequest.imToken = Constants.imToken;
        getTopicsRequest.topicIds = topicIds;
        rqHelper.addRequest(getTopicsRequest, TopicGetTopicsResponse.class, new HttpCallback<TopicGetTopicsResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicGetTopicsResponse ret) {
                // 更新数据库
                List<DbTopic> topicsNeedUpdateMember = new ArrayList<>();

                for (ImTopic imTopic : ret.data.topic) {
                    DbTopic dbTopic = DatabaseDealer.updateDbTopicWithImTopic(imTopic);
                    boolean hasThisTopic = false;
                    // 更新uiTopics
                    for (DbTopic uiTopic : topics) {
                        if (uiTopic.getTopicId() == imTopic.topicId) {
                            hasThisTopic = true;
                            uiTopic.setTopicId(imTopic.topicId);
                            uiTopic.setName(imTopic.topicName);
                            uiTopic.setType(imTopic.topicType);
                            uiTopic.setChange(imTopic.topicChange);
                            uiTopic.setGroup(imTopic.topicGroup);
                            uiTopic.setMembers(dbTopic.getMembers());
                        }
                    }
                    if (!hasThisTopic) {
                        //新topic 进入UI 记录本地操作时间
                        topics.add(dbTopic);
                        dbTopic.latestMsgTime = imTopic.latestMsgTime;
                        //关于 topic.latestMsgId=0  在请求时进行判断 如果为0 赋予 Long的最大值
                        dbTopic.latestMsgId = imTopic.latestMsgId;
                        dbTopic.latestOperateLocalTime = System.currentTimeMillis();
                        dbTopic.shouldInsertToTop = true;
                    }
                    topicsNeedUpdateMember.add(dbTopic);
                }

                // 更新UI, 需要重新排列么？
                // Collections.sort(topics, topicComparator);
                //由网络获取的新数据 以服务器时间为依据
//                rearrangeTopics(false);
                rearrange(DatabaseDealer.topicComparatorInsertTopBaseOnLocalTime);
                mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
//                // 4，对于需要更新members的topic，等待更新完members，再去取msgs
                updateEachTopicMsgs(topicsNeedUpdateMember);
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }
    //endregion

    //region 跳转
    private OnRecyclerViewItemClickCallback<DbTopic> onDbTopicCallback = new OnRecyclerViewItemClickCallback<DbTopic>() {
        @Override
        public void onItemClick(int position, DbTopic dbTopic) {

            //事件统计  点击班级群聊
            if (dbTopic != null && dbTopic.getType().equals("2")) {
                EventUpdate.onClickGroupTopicEvent(getActivity());
            }
            SharedSingleton.getInstance().set(Constants.kShareTopic, dbTopic);
            Intent i = new Intent(getActivity(), ImMsgListActivity.class);

            DbMember member = null;
            if (DatabaseDealer.isMockTopic(dbTopic)) {
                // 私聊但topic没有创建成功
                for (DbMember m : dbTopic.getMembers()) {
                    if (m.getImId() != Constants.imId) {
                        member = m;
                    }
                }
                i.putExtra(Constants.kCreateTopicMemberId, member.getImId());
                i.putExtra(Constants.kCreateTopicMemberName, member.getName());
            }

            getActivity().startActivityForResult(i, Constants.IM_REQUEST_CODE_MSGLIST);
            dbTopic.setShowDot(false);
            dbTopic.save();
            //通知mainactivity 是否显示红点
            curTopic = dbTopic;

            msgShownTopics.add(dbTopic);
        }
    };


    @Subscribe
    public void onChatWithContact(DbMember contact) {
        final DbMember member = contact;
        long memberId = member.getImId();
        boolean privateChatExist = false;

        for (DbTopic topic : topics) {
            if (topic.getType().equals("1")) {
                for (DbMember dbMember : topic.getMembers()) {
                    if (dbMember.getImId() == memberId) {
                        privateChatExist = true;
                        break;
                    }
                }
                if (privateChatExist) {
                    curTopic = topic;
                    break;
                }
            }
        }

        if (privateChatExist) {
            SharedSingleton.getInstance().set(Constants.kShareTopic, curTopic);
            Intent i = new Intent(getActivity(), ImMsgListActivity.class);
            getActivity().startActivityForResult(i, Constants.IM_REQUEST_CODE_MSGLIST);
            curTopic.setShowDot(false);

            curTopic.setFromTopic(Long.toString(contact.fromTopicId));

            curTopic.save();
            msgShownTopics.add(curTopic);
            return;
        }

        curTopic = null;
        SharedSingleton.getInstance().set(Constants.kShareTopic, curTopic);
        Intent i = new Intent(getActivity(), ImMsgListActivity.class);
        i.putExtra(Constants.kCreateTopicMemberId, memberId);
        i.putExtra(Constants.kCreateTopicMemberName, member.getName());
        i.putExtra(Constants.kFromTopicId, member.fromTopicId);
        getActivity().startActivityForResult(i, Constants.IM_REQUEST_CODE_MSGLIST);
    }

    @Subscribe
    public void onMockTopicRemoved(ImMsgListActivity.MockTopicRemovedEvent event) {
        for (Iterator<DbTopic> i = topics.iterator(); i.hasNext(); ) {
            DbTopic uiTopic = i.next();
            if (uiTopic.getTopicId() == event.dbTopic.getTopicId()) {
                i.remove();
                DatabaseDealer.removeMockTopic(uiTopic);
                msgShownTopics.remove(event.dbTopic);
            }
        }
    }

    @Subscribe
    public void onNewTopicCreated(ImMsgListActivity.NewTopicCreatedEvent event) {
        // 新建topic成功，curTopic由null转为实际生成的topic
        DbTopic dbTopic = event.dbTopic;
        dbTopic.setShowDot(false);
        dbTopic.save();

        curTopic = dbTopic;
        SharedSingleton.getInstance().set(Constants.kShareTopic, dbTopic);
        msgShownTopics.add(curTopic);

        // 更新uiTopics
        for (Iterator<DbTopic> i = topics.iterator(); i.hasNext(); ) {
            DbTopic uiTopic = i.next();
            if (uiTopic.getTopicId() == dbTopic.getTopicId()) {
                i.remove();
            }
        }
        topics.add(dbTopic);
        //创建topic 本地时间排序 本地行为 置顶操作
//        rearrangeTopics(true);
        rearrange(DatabaseDealer.topicComparatorInsertTopBaseOnLocalTime);
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();

        binder.subscribeTopic(Long.toString(dbTopic.getTopicId()));

    }
    //endregion


    /**
     * 置顶操作
     * 所有本地对topic的操作 包括 收到mqtt 消息 发送消息后
     * 需要对操作的topic 进行置顶
     * 达到按 最近操作排序的目的
     * <p>
     * APP 的首次排序 通过http 回去的最新latestmsgtime 进行排序
     */
    private void rearrange(Comparator<DbTopic> comparator) {
        Collections.sort(topics, comparator);
        // 只区分开群聊、私聊，不改变以前里面的顺序@
        List<DbTopic> privateTopics = new ArrayList<>();
        for (Iterator<DbTopic> i = topics.iterator(); i.hasNext(); ) {
            DbTopic topic = i.next();
            if (topic.getType().equals("1")) {
                // 私聊
                i.remove();
                privateTopics.add(topic);
            }
        }
        topics.addAll(privateTopics);
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
        //每次重排检查一次
        noticeShowRedDot();
    }

    /**
     * title 点击回调
     */
    private TitleActionCallback titleActionCallback;

    public void setTitleActionCallback(TitleActionCallback titleActionCallback) {
        this.titleActionCallback = titleActionCallback;
    }

    public interface TitleActionCallback {
        void onLeftImgClicked();
    }


    /**
     * 新消息红点 监听
     */
    private NewMessageListener newMessageListener;

    private void noticeShowRedDot() {
        if (newMessageListener != null) {
            //遍历 所有topic 是否有显示红点的topic 来通知MainActivity
            newMessageListener.onGetNewMessage(shouldShowRedDot());
        }
    }

    /**
     * 检查是否还有显示红点的topic
     */
    private boolean shouldShowRedDot() {
        for (DbTopic topic : topics) {
            if (topic.isShowDot()) {
                return true;
            }
        }
        return false;
    }

    public void setNewMessageListener(NewMessageListener newMessageListener) {
        this.newMessageListener = newMessageListener;
    }

    public interface NewMessageListener {
        void onGetNewMessage(boolean showRedDot);
    }


    /**
     * 这是个异步过程可能会存在的问题
     * 在何处进行网络请求-->目前可以放在登录完毕后或者进入主页中 但是考虑到module的完整性并且此处做了mqtt连接失败会重连的策略  其实可以放在此处获取host的
     * 但是如果一些极端现象：现在server从host1切花到host2  但是host1仍然可用，这种情况下用户第一次进来还是连的host1  只有退出后再进入才会连host2  考虑到目前切换的频率极低所以就先这样写
     * 数据存储于Sp中：本来sp应用用统一的管理，但是这是个独立的module并且公用的spManager在App中没有放到CommonBase中,而且此处存储的host只在此处获取，就不去动app module中的SPManager了
     */
    private void getImHostRequest(final GetImHostCallBack iGetImHostCallBack) {
        PolicyConfigRequest policyConfigRequest = new PolicyConfigRequest();
        policyConfigRequest.startRequest(PolicyConfigResponse.class, new HttpCallback<PolicyConfigResponse>() {
            @Override
            public void onSuccess(RequestBase request, PolicyConfigResponse ret) {
                if (ret.code == 0 && ret.data != null) {
                    saveHost2Sp(ret.data.getMqttServer());
                    if (iGetImHostCallBack != null) {
                        iGetImHostCallBack.onSuccess(ret.data.getMqttServer());
                    }
                } else {
                    String oldHost = getHostBySp();
                    if (TextUtils.isEmpty(oldHost)) {
                        getImHostRequest(iGetImHostCallBack);
                    } else {
                        if (iGetImHostCallBack != null) {
                            iGetImHostCallBack.onSuccess(oldHost);
                        }
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                String oldHost = getHostBySp();
                if (TextUtils.isEmpty(oldHost)) {
                    getImHostRequest(iGetImHostCallBack);
                } else {
                    if (iGetImHostCallBack != null) {
                        iGetImHostCallBack.onSuccess(oldHost);
                    }
                }
            }
        });
    }


    private void saveHost2Sp(String host) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("FaceShowIm", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit().putString("ImHost", host);
        editor.apply();


    }

    private String getHostBySp() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("FaceShowIm", MODE_PRIVATE);
        return sharedPreferences.getString("ImHost", "");

    }

    interface GetImHostCallBack {
        void onSuccess(String host);

    }

}
