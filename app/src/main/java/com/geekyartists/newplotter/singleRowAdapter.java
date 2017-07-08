package com.geekyartists.newplotter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Abhishek Panwar on 6/7/2017.
 */

public class singleRowAdapter extends RecyclerView.Adapter<singleRowAdapter.singleRowViewHolder> {

    ArrayList<singleRow> list;

    private LayoutInflater inflater;
    Context context;

    singleRowAdapter(Context c){
        list = new ArrayList<singleRow>();
        this.context =c;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public singleRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singleattributerow,parent,false);
        singleRowViewHolder ViewHolder = new singleRowViewHolder(view,context);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(singleRowViewHolder holder, int position) {

        singleRow current = list.get(position);
        holder.title.setText(current.title);
        holder.value1.setText(current.value1);
        holder.value2.setText(current.value2);
        holder.value3.setText(current.value3);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class singleRowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView title;
        TextView value1;
        TextView value2;
        TextView value3;
        ImageView addButton;
        Context context;

        int previousselected;


        public singleRowViewHolder(final View itemView,Context c) {
            super(itemView);
            this.context = c;
            title = (TextView)itemView.findViewById(R.id.title);
            value1 = (TextView)itemView.findViewById(R.id.value1);
            value2 = (TextView)itemView.findViewById(R.id.value2);
            value3 = (TextView)itemView.findViewById(R.id.value3);
            addButton = (ImageView)itemView.findViewById(R.id.add_value);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    singleRow current = list.get(getAdapterPosition());
                    if(context instanceof MainActivity){
                        ((MainActivity)context).AlertBoxAddValue(current,getAdapterPosition());
                    }
                }
                });

        }


        @Override
        public void onClick(View v) {


            if(MainActivity.actionModeState){
                MainActivity.actionMode.finish();

                MainActivity.recyclerView.getLayoutManager().findViewByPosition(previousselected).setSelected(false);

                if(context instanceof MainActivity){
                    ((MainActivity)context).ContextualToolbar(getAdapterPosition());
                    MainActivity.actionModeState = true;
                }
                if(v.isSelected())
                    v.setSelected(false);
                else
                    v.setSelected(true);

                previousselected = getAdapterPosition();

            }
            else {
                if (MainActivity.outerArr.get(getAdapterPosition()).size() == 0)
                    Toast.makeText(v.getContext(), "Please add some Entries first.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent;
                    intent = new Intent(v.getContext(), GraphActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    v.getContext().startActivity(intent);

                }
            }

        }

        @Override
        public boolean onLongClick(View v) {

            MainActivity.actionModeState = true;

            if(context instanceof MainActivity){
                ((MainActivity)context).ContextualToolbar(getAdapterPosition());
                v.setSelected(true);
            }

            return true;
        }


    }
}
