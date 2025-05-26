package at.htlsaalfelden.UNSERsplit.NoLib.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import at.htlsaalfelden.UNSERsplit.NoLib.Observable;
import at.htlsaalfelden.UNSERsplit.R;

public class LayoutSwitcher extends ConstraintLayout {
    private Observable<Boolean> leftSelected;

    private final int idLeft;
    private final int idRight;
    private final String valLeft;
    private final String valRight;

    private boolean hasLayouted = false;

    public LayoutSwitcher(Context context) {
        this(context, null);
    }

    public LayoutSwitcher(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LayoutSwitcher(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LayoutSwitcher(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        leftSelected = new Observable<>(true);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LayoutSwitcher,
                defStyleAttr, defStyleRes);

        idLeft = a.getResourceId(R.styleable.LayoutSwitcher_textLeft, -1);
        idRight = a.getResourceId(R.styleable.LayoutSwitcher_textRight, -1);

        valLeft = a.getString(R.styleable.LayoutSwitcher_textLeft);
        valRight = a.getString(R.styleable.LayoutSwitcher_textRight);

        inflate(context, R.layout.layout_switcher, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        TextView txtLeft = findViewById(R.id.txtLeft);
        TextView txtRight = findViewById(R.id.txtRight);

        if(idLeft != -1) {
            txtLeft.setText(idLeft);
        } else {
            txtLeft.setText(valLeft);
        }

        if(idRight != -1) {
            txtRight.setText(idRight);
        } else {
            txtRight.setText(valRight);
        }

        txtLeft.setOnClickListener(v -> {
            leftSelected.set(true);
        });

        txtRight.setOnClickListener(v -> {
            leftSelected.set(false);
        });

        addListener();
    }

    public Observable<Boolean> getState() {
        return leftSelected;
    }

    public void setStateVariable(Observable<Boolean> leftSelected) {
        boolean oldVal = this.leftSelected.get();
        this.leftSelected = leftSelected;
        this.leftSelected.set(oldVal);

        addListener();
    }

    private void addListener() {
        leftSelected.addInstantListener((o,v) -> {
            View underscore_left = findViewById(R.id.undescore_left);
            View underscore_right = findViewById(R.id.underscore_right);

            if(v) {
                underscore_left.setVisibility(View.VISIBLE);
                underscore_right.setVisibility(View.INVISIBLE);
            } else {
                underscore_left.setVisibility(View.INVISIBLE);
                underscore_right.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(hasLayouted) {
            return;
        }

        TextView txtLeft = findViewById(R.id.txtLeft);
        TextView txtRight = findViewById(R.id.txtRight);

        int maxWidth = Math.max(txtLeft.getWidth(), txtRight.getWidth());

        txtLeft.setWidth(maxWidth);
        txtRight.setWidth(maxWidth);
        hasLayouted = true;
    }
}
