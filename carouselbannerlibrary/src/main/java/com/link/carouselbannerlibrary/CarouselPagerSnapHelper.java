package com.link.carouselbannerlibrary;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CarouselPagerSnapHelper extends LinearSnapHelper {
    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        int targetPosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
        final View currentView = findSnapView(layoutManager);
        if (targetPosition != RecyclerView.NO_POSITION && currentView != null) {
            int currentPos = layoutManager.getPosition(currentView);
            int first = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            int last = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            currentPos = targetPosition < currentPos ? last : (targetPosition > currentPos ? first : currentPos);
            targetPosition = targetPosition < currentPos ? currentPos - 1 : (targetPosition > currentPos ? currentPos + 1 : currentPos);
        }
        return targetPosition;
    }
}
