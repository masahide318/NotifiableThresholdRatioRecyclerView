package com.masahide.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class NotifiableThresholdRatioRecyclerView extends RecyclerView {
    public NotifiableThresholdRatioRecyclerView(Context context) {
        this(context, null);
    }

    public NotifiableThresholdRatioRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotifiableThresholdRatioRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        OnScrollListener scrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                notifyOverDisplayThresholdRatio();
                notifyLowerHideThresholdRatio();
            }
        };
        addOnScrollListener(scrollListener);
    }

    enum Status {
        DISPLAY,
        HIDE,
    }

    private SparseArray<Status> notifyDisplayMap = new SparseArray<>();

    public void notifyOverDisplayThresholdRatio() {
        List<NotifiableViewHolder> notifiableViewHolders = extractNotifiableViewHolder();
        for (NotifiableViewHolder notifiableViewHolder : notifiableViewHolders) {
            if (canNotifyOverDisplayThresholdRatio(notifiableViewHolder)) {
                notifiableViewHolder.onDisplay();
                notifyDisplayMap.put(notifiableViewHolder.hashCode(), Status.DISPLAY);
            }
        }
    }

    /**
     * It is judged whether or not to notify viewHolder which exceeded the threshold
     */
    public boolean canNotifyOverDisplayThresholdRatio(NotifiableViewHolder notifiableViewHolder) {
        return calculateDisplayedRatio(notifiableViewHolder.getView()) >= notifiableViewHolder.notifyDisplayThresholdRatio &&
                notifyDisplayMap.get(notifiableViewHolder.hashCode()) != Status.DISPLAY;
    }

    public void notifyLowerHideThresholdRatio() {
        List<NotifiableViewHolder> notifiableViewHolders = extractNotifiableViewHolder();
        for (NotifiableViewHolder notifiableViewHolder : notifiableViewHolders) {
            if (canNotifyLowerHideThresholdRatio(notifiableViewHolder)) {
                notifiableViewHolder.onHide();
                notifyDisplayMap.put(notifiableViewHolder.hashCode(), Status.HIDE);
            }
        }
    }

    /**
     * It is judged whether or not to notify viewHolder which fall short the threshold
     */
    public boolean canNotifyLowerHideThresholdRatio(NotifiableViewHolder notifiableViewHolder) {
        return calculateDisplayedRatio(notifiableViewHolder.getView()) < notifiableViewHolder.notifyHideThresholdRatio &&
                notifyDisplayMap.get(notifiableViewHolder.hashCode()) == Status.DISPLAY;
    }

    /**
     * Calculate how much area the targetView is displayed
     */
    public float calculateDisplayedRatio(View targetView) {
        int topPosition = (targetView.getTop() < 0) ? 0 : targetView.getTop();
        int bottomPosition = (targetView.getBottom() > getHeight()) ? getHeight() : targetView.getBottom();
        return (bottomPosition - topPosition) / (float) targetView.getHeight();
    }

    /**
     * Extract ViewHolder from the currently displayed items
     */
    public List<NotifiableViewHolder> extractNotifiableViewHolder() {
        final int firstVisibleItemPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        final int lastVisibleItemPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        final List<NotifiableViewHolder> result = new ArrayList<>();
        for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
            final ViewHolder holder = findViewHolderForAdapterPosition(i);
            if (holder instanceof NotifiableViewHolder) {
                result.add((NotifiableViewHolder) holder);
            }
        }
        return result;
    }


    public abstract static class NotifiableViewHolder extends RecyclerView.ViewHolder {

        public NotifiableViewHolder(View itemView) {
            super(itemView);
        }

        private float notifyDisplayThresholdRatio = 0.5f;
        private float notifyHideThresholdRatio = 0.5f;

        View getView() {
            return itemView;
        }

        public float getNotifyDisplayThresholdRatio() {
            return notifyDisplayThresholdRatio;
        }

        public void setNotifyDisplayThresholdRatio(float notifyDisplayThresholdRatio) {
            this.notifyDisplayThresholdRatio = notifyDisplayThresholdRatio;
        }

        public float getNotifyHideThresholdRatio() {
            return notifyHideThresholdRatio;
        }

        public void setNotifyHideThresholdRatio(float notifyHideThresholdRatio) {
            this.notifyHideThresholdRatio = notifyHideThresholdRatio;
        }

        public abstract void onDisplay();

        public abstract void onHide();
    }
}
