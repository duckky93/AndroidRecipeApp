package com.android.enclave.androidrecipeapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.enclave.androidrecipeapp.R;
import com.android.enclave.androidrecipeapp.entities.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ADD_NEW = 0;
    private static final int STEP = 1;

    private Context mContext;
    private List<Step> steps;
    private StepListener listener;

    public StepAdapter(Context mContext) {
        this.mContext = mContext;
        steps = new ArrayList<>();
    }

    public void setListener(StepListener listener) {
        this.listener = listener;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public void addStep(Step step) {
        this.steps.add(step);
        notifyDataSetChanged();
    }

    public void updateStep(Step step, int position) {
        this.steps.set(position, step);
        notifyItemChanged(position);
    }

    public void deleteStep(int position) {
        this.steps.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == steps.size()) {
            return ADD_NEW;
        }
        return STEP;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case ADD_NEW:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_add_new, viewGroup, false);
                return new AddNewViewHolder(view);
            case STEP:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_step, viewGroup, false);
                return new StepViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof StepViewHolder) {
            onBindViewHolder((StepViewHolder) viewHolder, i);
        } else if (viewHolder instanceof AddNewViewHolder) {
            onBindViewHolder((AddNewViewHolder) viewHolder, i);
        }
    }

    public void onBindViewHolder(@NonNull StepViewHolder viewHolder, final int i) {
        final Step step = steps.get(i);
        viewHolder.tvStepName.setText(step.getStepName());
        viewHolder.tvStepDescription.setText(step.getStepDescription());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onStepListenerClickListener(step, i);
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onStepLongClickListener(step, i);
                }
                return false;
            }
        });
    }

    public void onBindViewHolder(@NonNull AddNewViewHolder viewHolder, int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onAddNewStepListener();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size() + 1;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_step_name)
        TextView tvStepName;

        @BindView(R.id.tv_step_description)
        TextView tvStepDescription;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class AddNewViewHolder extends RecyclerView.ViewHolder {

        public AddNewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface StepListener {
        void onAddNewStepListener();

        void onStepListenerClickListener(Step step, int position);

        void onStepLongClickListener(Step step, int position);
    }
}
