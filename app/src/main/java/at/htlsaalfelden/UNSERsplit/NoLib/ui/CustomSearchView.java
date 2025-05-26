package at.htlsaalfelden.UNSERsplit.NoLib.ui;

import static at.htlsaalfelden.UNSERsplit.NoLib.ReflectionUtils.get;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import at.htlsaalfelden.UNSERsplit.NoLib.ReflectionUtils;
import at.htlsaalfelden.UNSERsplit.R;

public class CustomSearchView<T> extends SearchView {
    /***
     * Implements all logic
     */
    private SearchViewListener<T> mCallback;
    private int mSuggestionLayout = 0;
    private String[] colNames;
    private int[] colContentIds;
    private SimpleCursorAdapter simpleCursorAdapter;

    private List<T> data;

    public CustomSearchView(@NonNull Context context) {
        super(context);
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0,0);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr,0);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs,defStyleAttr,defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomSearchView,
                0, 0);

        mSuggestionLayout = a.getResourceId(R.styleable.CustomSearchView_suggestionLayout, 0);
    }

    public void setSuggestionLayout(@LayoutRes int mSuggestionLayout) {
        this.mSuggestionLayout = mSuggestionLayout;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        expandAndUnfocus();
        addSuggestionListener();
    }

    public void setListener(SearchViewListener<T> mCallback) {
        this.mCallback = mCallback;

        colContentIds = mCallback.getResourceIds();
        colNames = new String[colContentIds.length];
        for (int i = 0; i < colContentIds.length; i++) {
            colNames[i] = colContentIds[i] + "";
        }

        addSuggestionAdapter();
    }


    private void expandAndUnfocus() {
        // expand without acquiring the focus
        Activity callingActivity = ReflectionUtils.getActivity(getContext());
        if(callingActivity == null) {
            return; // assume rendering inside android studio
        }
        View currentFocus = callingActivity.getCurrentFocus();

        this.setIconified(false); // expand

        if(currentFocus != null) {
            currentFocus.requestFocus();
        } else {
            this.clearFocus();
        }
    }

    private void addSuggestionListener() {
        this.setOnSuggestionListener(new SuggestionListener());
        this.setOnQueryTextListener(new QueryTextChangeListener());
    }

    private void addSuggestionAdapter() {
        simpleCursorAdapter = new SimpleCursorAdapter(
                getContext(),
                mSuggestionLayout,
                null,
                colNames,
                colContentIds,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        this.setSuggestionsAdapter(simpleCursorAdapter);
    }

    private class SuggestionListener implements SearchView.OnSuggestionListener {

        @Override
        public boolean onSuggestionSelect(int position) {
            return false;
        }

        @Override
        public boolean onSuggestionClick(int position) {
            T rawData = data.get(position);

            mCallback.onClickSuggestion(rawData, position);
            return true;
        }
    }

    private class QueryTextChangeListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            String[] names = new String[colNames.length+1];
            names[0] = "_id";

            data = new ArrayList<>();

            System.arraycopy(colNames, 0, names, 1, colNames.length);

            MatrixCursor cursor = new MatrixCursor(names);

            ListView suggestionsView = getListViewUnsafe(CustomSearchView.this);

            int change = 0;

            if(suggestionsView != null) {
                suggestionsView.setDivider(new ColorDrawable(0xff828282));
                suggestionsView.setDividerHeight(7);

                ListPopupWindow popupWindow = getListPopupWindowUnsafe(CustomSearchView.this);

                change = suggestionsView.getLayoutParams().width - getLayoutParams().width;

                suggestionsView.getLayoutParams().width = getLayoutParams().width;
                popupWindow.setWidth(getLayoutParams().width);
                popupWindow.setHorizontalOffset(-change);
            }

            int finalChange = change;
            SearchContext<T> searchContext = new SearchContext<>() {
                @Override
                public void setValue(T rawData, int id, Object... contents) {
                    assert colNames.length == contents.length;
                    Object[] objects = new Object[colNames.length+1];

                    objects[0] = id;

                    System.arraycopy(contents, 0, objects, 1, colNames.length);

                    data.add(rawData);

                    cursor.addRow(objects);
                }

                @Override
                public void update() {
                    simpleCursorAdapter.changeCursor(cursor);
                    simpleCursorAdapter.notifyDataSetChanged();

                    for(Object o : data) {
                        System.out.println(o);
                    }

                    ListView suggestionsView = getListViewUnsafe(CustomSearchView.this);

                    if(suggestionsView != null) {
                        suggestionsView.setDivider(new ColorDrawable(0xff828282));
                        suggestionsView.setDividerHeight(7);

                        ListPopupWindow popupWindow = getListPopupWindowUnsafe(CustomSearchView.this);
                        suggestionsView.getLayoutParams().width = getLayoutParams().width;
                        popupWindow.setWidth(getLayoutParams().width);
                        popupWindow.setHorizontalOffset(-finalChange);
                    }
                }
            };

            mCallback.updateSuggestions(newText, searchContext);

            return false;
        }
    }

    /**
     * Interface to implement custom functionality
     * @param <T>
     */
    public static interface SearchViewListener<T> {
        /**
         * Get all Ids for the views inside the suggestion layout
         * @return all R.id.* <strong>in order</strong>
         */
        int[] getResourceIds();

        /**
         * Called when the user changed the query, update the suggestions with {@link SearchContext}
         * @param query the new query
         * @param context a {@link SearchContext} implementation to communicate back
         */
        void updateSuggestions(String query, SearchContext<T> context);

        /**
         * Called when the user has clicked a suggestion
         * @param data the raw data Passed to {@link SearchContext}
         * @param position the position in the list of suggestions
         */
        void onClickSuggestion(T data, int position);
    }

    /**
     * Interface to communicate back to {@link CustomSearchView}
     * @param <T>
     */
    public static interface SearchContext<T> {
        /**
         * Call to add a suggestion <strong>does not update the ui</strong>
         * @param rawData pass raw Data here to retrieve when clicking
         * @param id unique id to identify this suggestion
         * @param contents Array of anything {@link SimpleCursorAdapter} can handle
         */
        void setValue(T rawData, int id, Object ...contents);

        /**
         * update the ui
         */
        void update();
    }

    private static ListView getListViewUnsafe(SearchView searchView) {
        Object searchAutoComplete = get(searchView, "mSearchSrcTextView", SearchView.class);

        //showMembers(searchAutoComplete);

        ListPopupWindow popupWindow = get(searchAutoComplete, "mPopup", AutoCompleteTextView.class);
        return get(popupWindow, "mDropDownList");
    }

    private static ListPopupWindow getListPopupWindowUnsafe(SearchView searchView) {
        Object searchAutoComplete = get(searchView, "mSearchSrcTextView", SearchView.class);

        //showMembers(searchAutoComplete);

        return get(searchAutoComplete, "mPopup", AutoCompleteTextView.class);
    }
}
