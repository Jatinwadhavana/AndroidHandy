package com.androidhandy.pop;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.androidhandy.R;
import com.androidhandy.utils.Utils;

public class PopupWindowCustom {
    private PopupWindow popEditProfile = null;
    private Activity activity = null;
    private View ancherView = null;
    LinearLayout linPopProfileMain;
    FrameLayout frmPopMain;

    private TextView tvSaveToCollection;
    private TextView tvDownload;
    private TextView tvShare;

    public PopupWindowCustom(Activity baseAct, View ancher) {
        this.activity = baseAct;
        this.ancherView = ancher;
        View popUpLayout = LayoutInflater.from(activity).inflate(R.layout.pop_up_more_menu, null);
        popEditProfile = new PopupWindow(popUpLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popEditProfile.setFocusable(true);
        linPopProfileMain = popUpLayout.findViewById(R.id.linPopMain);
        frmPopMain = popUpLayout.findViewById(R.id.frmPopMain);

        CardView.LayoutParams layoutParams = (CardView.LayoutParams) linPopProfileMain.getLayoutParams();
        layoutParams.width = Utils.INSTANCE.getDisWidth(activity) / 2;

        layoutParams.height = Utils.INSTANCE.getDisWidth(activity) / 2;
        linPopProfileMain.setLayoutParams(layoutParams);

        popEditProfile.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        tvSaveToCollection = popUpLayout.findViewById(R.id.tvSaveToCollection);
        tvDownload = popUpLayout.findViewById(R.id.tvDownload);
        tvShare = popUpLayout.findViewById(R.id.tvShare);

        tvSaveToCollection.setOnClickListener(v -> onPopItemSelected.onSaveToCollection());
        tvDownload.setOnClickListener(v -> onPopItemSelected.onDownload());
        tvShare.setOnClickListener(v -> onPopItemSelected.onShare());
    }

    public void setOnPopItemSelected(OnPopItemSelected onPopItemSelected) {
        this.onPopItemSelected = onPopItemSelected;
    }

    private OnPopItemSelected onPopItemSelected;

    public interface OnPopItemSelected {
        void onSaveToCollection();

        void onDownload();

        void onShare();
    }

    public boolean isShowing() {
        return popEditProfile.isShowing();
    }

    public void dismissPop() {
        popEditProfile.dismiss();
    }

    public void show(View ancherView) {
        try {
            if (popEditProfile.isShowing()) {
                popEditProfile.dismiss();
            }
//            popEditProfile.showAtLocation(linRootMain,  Gravity.CENTER_VERTICAL, (int) ancherView.getX(), (int) ancherView.getY());

//            popEditProfile.showAsDropDown(ancherView, Utils.INSTANCE.getDisWidth(activity) / 2 - (int) activity.getResources().getDimension(R.dimen._7sdp), 0);
            frmPopMain.measure(0,0);
            popEditProfile.showAsDropDown(ancherView, 0,Utils.INSTANCE.getDisWidth(activity) / 2 *-1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popEditProfile.setElevation(100F);
            }
            popEditProfile.setFocusable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}