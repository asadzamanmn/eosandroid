package com.eos.blockchain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CustomAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;

    public CustomAdapter(Context context, List<String> listDataHeader,
                         HashMap<String, List<String>> listChildData) {
        this.context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }


    @Override
    public int getChildrenCount(int groupPosition) {

        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        String headerText = (String)  getGroup(i);
        String[] split = headerText.split(",");

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.group_layout,null);
        }

        TextView mTitle = (TextView)view.findViewById(R.id.mTitle);
        mTitle.setText(" Block Number : "+split[0]+"\n"+" Block Time : "+split[1]+"\n"+"Input Transactions : 0");

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        String childText = (String)  getChild(i,i1);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_layout,null);
        }

        TextView mTitle = (TextView)view.findViewById(R.id.mChildText);
        mTitle.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}