package com.test.yanxiu.im_ui.contacts.view;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.common_base.ui.InputMethodUtil;
import com.test.yanxiu.common_base.ui.PublicLoadLayout;
import com.test.yanxiu.common_base.utils.talkingdata.EventUpdate;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_ui.Constants;
import com.test.yanxiu.im_ui.ImMsgListActivity;
import com.test.yanxiu.im_ui.R;
import com.test.yanxiu.im_ui.contacts.ChangeClassAdapter;
import com.test.yanxiu.im_ui.contacts.ContactsAdapter;
import com.test.yanxiu.im_ui.contacts.bean.ClassBean;
import com.test.yanxiu.im_ui.contacts.bean.ContactsPlayerBean;
import com.test.yanxiu.im_ui.contacts.presenter.ContactsPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 显示通讯录的View
 *
 * @author by frc on 2018/3/13.
 */

public class ContactsFragment extends ContactMvpBaseFragment<IContactsView, ContactsPresenter<IContactsView>> implements IContactsView {
    private ImageView mImgBack;
    private ImageView mImgChangeClass;
    private SearchView mSearchView;
    private TextView mTvCurrentClassName, mTvSureChangeClass;
    private View mChangeClassBackView;
    private LinearLayout mLlChangeClass;
    public ContactsAdapter mContactsAdapter;
    private ChangeClassAdapter mChangeClassAdapter;
    private RecyclerView mContactsList;
    private RecyclerView changeClassList;
    private RelativeLayout mRlChangeClass;
    private int mCurrentItemClassSelected;
    private View rootView;
    public PublicLoadLayout mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contacts_layout, container, false);
        mRootView = new PublicLoadLayout(this.getContext());
        mRootView.setContentView(rootView);
        mImgBack = rootView.findViewById(R.id.img_back);
        mImgChangeClass = rootView.findViewById(R.id.img_change_class);
        mRlChangeClass=rootView.findViewById(R.id.rl_change_class);
        mTvCurrentClassName = rootView.findViewById(R.id.tv_current_class_name);
        mLlChangeClass = rootView.findViewById(R.id.ll_change_class);
        mChangeClassBackView = rootView.findViewById(R.id.back_view);
        mTvSureChangeClass = rootView.findViewById(R.id.tv_sure_change_class);
        mTvSureChangeClass.setVisibility(View.GONE);
        mSearchView = rootView.findViewById(R.id.searchView);
        modifySearchViewStyle(mSearchView);
        /*初始化通讯录列表*/
        mContactsAdapter = new ContactsAdapter();
        mContactsList = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mContactsList.setLayoutManager(linearLayoutManager);
        mContactsList.setAdapter(mContactsAdapter);
        /*初始化切换班级列表*/
        mChangeClassAdapter = new ChangeClassAdapter();
        changeClassList = rootView.findViewById(R.id.recyclerView_change_class);

        //此处是为了实现切换班级列表限制最多显示三行半
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this.getContext()) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                View view = recycler.getViewForPosition(0);
                measureChild(view, widthSpec, heightSpec);
                int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                int measuredHeight = view.getMeasuredHeight();
                if (mChangeClassAdapter.getItemCount() > 3) {
                    setMeasuredDimension(measuredWidth, (int) (measuredHeight * 3.5));
                } else {
                    setMeasuredDimension(measuredWidth, measuredHeight * mChangeClassAdapter.getItemCount());
                }
            }
        };
        linearLayoutManager1.setAutoMeasureEnabled(false);
        changeClassList.setHasFixedSize(false);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        changeClassList.setLayoutManager(linearLayoutManager1);
        changeClassList.setAdapter(mChangeClassAdapter);
        addCallBack();
        presenter.loadDataByNet();
        //订阅 eventbus 消息接收 由聊天界面 获取的最新联系人信息
        EventBus.getDefault().register(this);
        return mRootView;
    }


    @Override
    public void onStop() {
        super.onStop();
        hideChangeClassWindow();
    }

    /**
     * member infor
     * 用户点击的 联系人信息
     * 创建这个引用是为了在 聊天界面 返回eventbus 信息更新后更新列表信息
     * */
    private ContactsPlayerBean targetMemberInfo;
    /**
     *EventBus 监听 获取 聊天界面 得到的最新的member 信息
     * */
    @Subscribe
    public void onContactUpdate(ImMsgListActivity.MemberInfoUpdateEvent memberInfo){
        if (targetMemberInfo != null) {
            targetMemberInfo.setAvatar(memberInfo.getAvatar());
            targetMemberInfo.setName(memberInfo.getName());
            mContactsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消eventbus 订阅
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected ContactsPresenter<IContactsView> createPresenter() {
        return new ContactsPresenter(this);
    }

    /**
     * V7包原生的SearchView在UI上有些不符  所以做了些修改
     * 可参考: https://tanjundang.github.io/2016/11/17/SearchView/
     *
     * @param searchView searchView
     */
    private void modifySearchViewStyle(SearchView searchView) {
        SearchView.SearchAutoComplete tv = searchView.findViewById(R.id.search_src_text);
        tv.setTextColor(ContextCompat.getColor(this.getContext(), R.color.color_333333));
        tv.setHintTextColor(ContextCompat.getColor(this.getContext(), R.color.color_999999));
        tv.setTextSize(14);

        ImageView imageView = searchView.findViewById(R.id.search_mag_icon);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.width = 51;
        layoutParams.height = 51;
        imageView.setLayoutParams(layoutParams);
    }

    @Override
    public void addCallBack() {
        mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadDataByNet();
            }
        });
        mContactsAdapter.addItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int position) {
                presenter.getItemData(position);
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.queryPlayer(newText);
                return false;
            }
        });
        //事件统计 点击搜索框
        mSearchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                EventUpdate.onClickMsgContactSearchEvent(getActivity());
                return false;
            }
        });
        mRlChangeClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                //事件统计 点击聊聊搜索框
                EventUpdate.onClickMsgContactSearchEvent(getActivity());
                if (mLlChangeClass.getVisibility() == View.GONE) {
                    openChangeClassWindow();
                } else {
                    closeChangeClassWindow();
                }
            }
        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsFragment.this.getActivity().finish();
            }
        });
        mChangeClassBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                closeChangeClassWindow();
            }
        });

        mChangeClassAdapter.addItemClickListener(new ChangeClassAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int position) {
                mCurrentItemClassSelected = position;
                presenter.changeClass(mCurrentItemClassSelected);
            }
        });
        mTvSureChangeClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeClass(mCurrentItemClassSelected);
            }
        });
        mContactsList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftInput();
                return false;
            }

        });
        /*当键盘收起时光标隐藏*/
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(doMonitorSoftKeyboard(rootView, new OnSoftKeyBoardListener() {
            @Override
            public void hasShow(boolean isSoftVisible) {
                if (!isSoftVisible) {
                    mSearchView.clearFocus();
                }

            }
        }));
    }


    /***
     * 判断软件盘是否弹出
     * @param v
     * @param listener
     * 备注：在不用的时候记得移除OnGlobalLayoutListener
     * */
    public ViewTreeObserver.OnGlobalLayoutListener doMonitorSoftKeyboard(final View v, final OnSoftKeyBoardListener listener) {
        final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                // 获取屏幕的可见范围保存在矩形r中
                v.getWindowVisibleDisplayFrame(r);
                int screenHeight = v.getRootView().getHeight();
                //软件盘高度 = 屏幕真实高度 - 屏幕可见范围的高度
                int heightDifference = screenHeight - r.bottom;
                boolean isSoftVisible = heightDifference > (screenHeight / 3);
                if (listener != null) {
                    listener.hasShow(isSoftVisible);
                }
            }
        };
        v.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        return layoutListener;
    }

    interface OnSoftKeyBoardListener {
        void hasShow(boolean isSoftVisible);
    }

    private void openChangeClassWindow() {
        mImgChangeClass.setImageDrawable(ContextCompat.getDrawable(ContactsFragment.this.getContext(), R.drawable.selector_close_change_class_window));
        presenter.showChangeClassPopupWindow();
    }

    private void closeChangeClassWindow() {
        mImgChangeClass.setImageDrawable(ContextCompat.getDrawable(ContactsFragment.this.getContext(), R.drawable.selector_open_change_class_window));
        hideChangeClassWindow();
    }


    @Override
    public void showItemClickResult(ContactsPlayerBean memberInfo) {
        //事件统计 点击通讯录中的头像
        EventUpdate.onClickMsgContactImageEvent(getActivity());
        if (memberInfo.getId() == Constants.imId) {
            // 不能给自己发消息
            Toast.makeText(getActivity(), "不能给自己发消息", Toast.LENGTH_SHORT).show();
            return;
        }

        DbMember member = new DbMember();
        member.setImId(memberInfo.getId());
        member.setName(memberInfo.getName());
        member.setAvatar(memberInfo.getAvatar());
        member.setGroupId(memberInfo.getClassId());
        member.setGroupName(memberInfo.getClassName());
        EventBus.getDefault().post(member);
        targetMemberInfo=memberInfo;
        //getActivity().finish();
    }

    @Override
    public void showQueryResultList(List<ContactsPlayerBean> data) {
        showContractsList(data);
    }


    @Override
    public void showContractsList(List<ContactsPlayerBean> data) {
        closeChangeClassWindow();
        mContactsAdapter.refresh(data);
    }

    @Override
    public void showChangeClassWindow(List<ClassBean> data, int currentClassPosition) {
        if (data != null && data.size() > 0) {
            mChangeClassAdapter.refresh(data, currentClassPosition);
            TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            mShowAction.setDuration(200);

            mShowAction.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    Log.i(TAG, "onAnimationStart: ");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.i(TAG, "onAnimationEnd: ");
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            changeClassList.startAnimation(mShowAction);

            mLlChangeClass.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void hideChangeClassWindow() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(200);

        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG, "onAnimationEnd: ");
                mLlChangeClass.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        changeClassList.startAnimation(mHiddenAction);

    }

    @Override
    public void changeCurrentClassName(String className) {
        mTvCurrentClassName.setText(className);
    }

    @Override
    public void showLoadingView() {
        mRootView.showLoadingView();
    }

    @Override
    public void hideLoadingView() {
        mRootView.finish();
    }

    @Override
    public void showDataErrorView(String errorMessage) {
        mRootView.showOtherErrorView(errorMessage);
    }


    @Override
    public void showNoDataView() {
        mRootView.showOtherErrorView("通讯录为空");
    }

    @Override
    public void showNetError() {
        mRootView.showNetErrorView();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this.getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void hideSoftInput() {
        InputMethodUtil.closeInputMethod(ContactsFragment.this.getContext(), mImgBack);
    }


}
