package udacity.com.tamtommovie;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by omaraltamimi on 3/8/18.
 */

public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

    private int height;
    boolean scrolling = false;

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, BottomNavigationView child, int
            layoutDirection) {
        height = child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       BottomNavigationView child, @NonNull
                                               View directTargetChild, @NonNull View target,
                                       int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BottomNavigationView child, View
            dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, BottomNavigationView child,
                                          View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }


    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull
            BottomNavigationView child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed,
                               @ViewCompat.NestedScrollType int type) {
        if (dyConsumed > 0) {
            if (!scrolling && child.getTranslationY() == 0)
                slideDown(child);
        } else if (dyConsumed < 0) {
            if (!scrolling && child.getTranslationY() > 0)
                slideUp(child);
        }
    }

    private void slideUp(BottomNavigationView child) {
        scrolling = true;
        child.animate().translationY(0).setDuration(50).setListener(new Animator.AnimatorListener
                () {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                scrolling = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void slideDown(BottomNavigationView child) {
        scrolling = true;
        child.animate().translationY(height).setDuration(200).setListener(new Animator
                .AnimatorListener() {


            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                scrolling = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
