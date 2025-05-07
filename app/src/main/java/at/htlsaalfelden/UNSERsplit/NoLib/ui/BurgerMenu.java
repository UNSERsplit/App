package at.htlsaalfelden.UNSERsplit.NoLib.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import at.htlsaalfelden.UNSERsplit.NoLib.Observable;
import at.htlsaalfelden.UNSERsplit.R;

public class BurgerMenu extends LinearLayout {
    private ImageButton btnToggle;
    private LinearLayout content;
    private Observable<Boolean> open;

    public BurgerMenu(Context context) {
        this(context, null);
    }

    public BurgerMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BurgerMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BurgerMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);


        inflate(context, R.layout.burger_menu, this);

        content = new LinearLayout(context, attrs, defStyleAttr, defStyleRes);
        content.setOrientation(VERTICAL);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


        open = new Observable<>(false);
        btnToggle = findViewById(R.id.open_menu);

        LinearLayout a = (LinearLayout) getChildAt(0);

        a.removeViewAt(1);
        a.addView(content);

        open.addInstantListener((o,v) -> {
            if(v) {
                content.setVisibility(VISIBLE);
            } else {
                content.setVisibility(GONE);
            }
        });

        btnToggle.setOnClickListener((v)->{
            open.set(!open.get());
        });

        while (getChildCount() > 1) {
            View v = getChildAt(1);
            removeViewAt(1);
            content.addView(v);
        }
    }

    public LinearLayout getContent() {
        return content;
    }

    public Observable<Boolean> getState() {
        return open;
    }
}
