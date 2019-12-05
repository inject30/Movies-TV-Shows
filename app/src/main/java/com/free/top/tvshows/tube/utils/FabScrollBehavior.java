package com.free.top.tvshows.tube.utils;

import android.content.Context;
import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;


public class FabScrollBehavior extends FloatingActionButton.Behavior {

    public FabScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton fab, @NonNull View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        if (dependency instanceof AppBarLayout) {
            AppBarLayout appBarLayout = (AppBarLayout) dependency;
            if ((appBarLayout.getHeight() - appBarLayout.getBottom()) <= 300 && fab.getVisibility() != View.VISIBLE)
                fab.show();
            else if ((appBarLayout.getHeight() - appBarLayout.getBottom()) > 300 && fab.getVisibility() == View.VISIBLE)
                fab.hide();
        }
        return true;
    }
}