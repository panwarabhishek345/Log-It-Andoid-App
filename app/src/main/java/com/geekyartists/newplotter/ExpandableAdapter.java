package com.geekyartists.newplotter;

/**
 * Created by Abhishek Panwar on 6/13/2017.
 */

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    HashMap<String, List<String>> _listDataChild;


    public ExpandableAdapter(Context context, List<String> listDataHeader,
                             HashMap<String, List<String>> listChildData ) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
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
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

            if(isLastChild){
                if (convertView == null) {
                    LayoutInflater infalInflater = (LayoutInflater) this._context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = infalInflater.inflate(R.layout.list_item_add, null);
                }

                TextView txtListChild = (TextView) convertView
                        .findViewById(R.id.lblListItemAdd);
                txtListChild.setTypeface(null, Typeface.BOLD);


                txtListChild.setText(childText);
            }
            else{
                if (convertView == null) {
                    LayoutInflater infalInflater = (LayoutInflater) this._context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = infalInflater.inflate(R.layout.list_item, null);
                }

                TextView txtListChild = (TextView) convertView
                        .findViewById(R.id.lblListItem);
                txtListChild.setText(childText);
            }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
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
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
       if(groupPosition == 0){
           if (convertView == null) {
               LayoutInflater infalInflater = (LayoutInflater) this._context
                       .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               convertView = infalInflater.inflate(R.layout.list_group_profile, null);
               TextView lblListHeader = (TextView) convertView
                       .findViewById(R.id.lblListHeader);
               lblListHeader.setTypeface(null, Typeface.BOLD);
               lblListHeader.setText(headerTitle);

           }
       }
       else {
           if (convertView == null) {
               LayoutInflater infalInflater = (LayoutInflater) this._context
                       .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               convertView = infalInflater.inflate(R.layout.list_group, null);
           }

           int[] myImageList = new int[]{R.drawable.ic_user, R.drawable.settingsicon,R.drawable.helpicon,R.drawable.abouticon,R.drawable.ic_menu_share};


           TextView lblListHeader = (TextView) convertView
                   .findViewById(R.id.lblListHeader);

           ImageView iconImage = (ImageView)convertView.findViewById(R.id.icon_nav_list);
           iconImage.setImageResource(myImageList[groupPosition]);
           lblListHeader.setText(headerTitle);

       }

        return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
