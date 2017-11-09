package com.yanxiu.gphone.faceshowadmin_android.customView;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.Comments;

import java.util.ArrayList;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/15 11:39.
 * Function :
 */
public class ClassCircleCommentLayout extends RelativeLayout {

    public interface onItemClickListener{
        void onItemClick(Comments comments, int position);
    }

    private Context mContext;
    private ListView mListView;
    private CommentAdapter adapter;
    private onItemClickListener mItemClickListener;

    public ClassCircleCommentLayout(Context context) {
        this(context,null);
    }

    public ClassCircleCommentLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClassCircleCommentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext=context;
        LayoutInflater.from(context).inflate(R.layout.layout_class_circle_comment,this);
        mListView= (ListView) findViewById(R.id.un_listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Comments comments =adapter.getDataFromPosition(position);
                if (mItemClickListener!=null){
                    mItemClickListener.onItemClick(comments,position);
                }
            }
        });
    }

    public void setItemClickListener(onItemClickListener itemClickListener){
        mItemClickListener=itemClickListener;
    }

    /**
     * 屏蔽listview点击事件的位置在这个自定义listview内部，ontouch
     *
     * 打开后即可拥有回复功能
     * */
    public void setData(ArrayList<Comments> list){
        if (list==null||list.size()==0){
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        adapter=new CommentAdapter(mContext,list);
        mListView.setAdapter(adapter);
    }

    private class CommentAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Comments> mData=new ArrayList<>();

        CommentAdapter(Context context, ArrayList<Comments> list){
            this.mContext=context;
            mData.clear();
            mData.addAll(list);
        }

        Comments getDataFromPosition(int position){
            return mData.get(position);
        }

        @Override
        public int getCount() {
            return mData!=null?mData.size():0;
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Comments comment=mData.get(position);
            convertView= LayoutInflater.from(mContext).inflate(R.layout.adapter_classcircle_comment_item,null);
            TextView textView= (TextView) convertView.findViewById(R.id.tv_comment);
            String text="";
            if (comment.level.equals("1")){
                if (comment.publisher!=null) {
                    text="<b>"+comment.publisher.realName+"</b>: "+comment.content;
//                    text = String.format(mContext.getString(R.string.comment_to_master), comment.publisher.realName, comment.content);
                }
            }else {
                if (comment.publisher != null) {
                    text="<b>"+comment.publisher.realName+"</b>对"+"<b>"+comment.toUser.realName+"</b>: "+comment.content;
//                    text = String.format(mContext.getString(R.string.comment_to_user), comment.publisher.realName, comment.toUser.realName, comment.content);
                }
            }
            textView.setText(Html.fromHtml(text));
            return convertView;
        }

    }
}
