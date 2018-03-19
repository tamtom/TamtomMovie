package udacity.com.tamtommovie.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by omaraltamimi on 3/3/18.
 */

public abstract class BaseFragment extends Fragment {
    public final String TAG = this.getClass().getSimpleName();
    Unbinder unbinder;
    private View mRootView;

    @LayoutRes
    public abstract int getLayoutResId();

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        int layoutId = getLayoutResId();
        try {
            mRootView = inflater.inflate(layoutId, container, false);
            unbinder = ButterKnife.bind(this, mRootView);

            return mRootView;
        } catch (Exception ex) {
            Log.e(TAG, "onCreateView: ", ex);
        }

        return null;
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }
}
