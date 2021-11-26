package com.teamopendata.mindcareapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamopendata.mindcareapp.R;
import com.teamopendata.mindcareapp.common.model.entity.Task;

import java.util.List;

public class TodayTaskAdapter extends RecyclerView.Adapter<TodayTaskAdapter.ViewHolder> {

    private final List<Task> mTasks;

    public TodayTaskAdapter(List<Task> taskList) {
        mTasks = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_today_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mTasks.get(position));
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cb_today_task);
        }

        public void bind(Task task) {
            checkBox.setChecked(task.isCompleted());
            checkBox.setText(task.getContents());
        }
    }
}
