package com.nstudio.travelreminder.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import com.nstudio.travelreminder.R;

import java.util.Objects;

public class ImageChooserDialog extends Dialog {

    private Context context;
    private OnMethodSelectListener selectListener;

    public ImageChooserDialog(@NonNull Context context, OnMethodSelectListener selectListener) {
        super(context, R.style.DialogAnimBottom);
        this.context = context;
        this.selectListener = selectListener;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Objects.requireNonNull(getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.window_image_choose);
        Objects.requireNonNull(getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.window_bg));

        findViewById(R.id.view_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooserDialog.this.dismiss();
                selectListener.onMethodSelect(0);
            }
        });

        findViewById(R.id.view_storage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooserDialog.this.dismiss();
                selectListener.onMethodSelect(1);
            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



    }

    public interface OnMethodSelectListener {
        void onMethodSelect(int m);
    }

}
