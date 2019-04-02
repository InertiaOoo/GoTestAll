package com.ooo.deemo.gotestall;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private List<TestLog> testLogList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TestLog testLog = testLogList.get(i);
     viewHolder.textView1.setText(testLog.getID());
     viewHolder.textView2.setText(testLog.getLogmsg());
    }


    @Override
    public int getItemCount() {
        return testLogList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.tv_listitem);
            textView2 = itemView.findViewById(R.id.tv_listitem2);

        }
    }

    public RecycleAdapter( List<TestLog> testLogList){
        this.testLogList = testLogList;
    }
}
