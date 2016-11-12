package com.se215h12.hci_stock.widgets;

import android.accessibilityservice.AccessibilityService;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.se215h12.hci_stock.R;

/**
 * Created by TungHo on 11/07/2016.
 */
public class DialogStockMenu extends Dialog {

    public DialogStockMenu(Context context, int themeResId) {
        super(context, themeResId);
        initLayout(context);
    }

    public DialogStockMenu(Context context) {
        super(context);
        initLayout(context);
    }

    protected DialogStockMenu(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initLayout(context);
    }

    private void initLayout(Context context) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        this.setContentView(R.layout.dialog_stock_menu);

    }

    @Override
    public void show(){

        super.show();
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height =ViewGroup.LayoutParams.MATCH_PARENT;

    }

    public void setPosition(int x, int y){
        // set position
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.gravity = Gravity.TOP;
        lp.x = x;
        lp.y = y;

    }
}
