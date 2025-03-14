package at.htlsaalfelden.UNSERsplit.ui.home;

import androidx.annotation.Nullable;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.model.Group;

public class GroupAdapter extends BaseAdapter {

    private Context context;
    private List<Group> groups;

    private LayoutInflater layoutInflater;

    public GroupAdapter(@NonNull Context context, @NonNull List<Group> groups) {
        this.context = context;
        this.groups = groups;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Group getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(layoutInflater, position, convertView, parent, R.layout.layout_group);
    }

    private @NonNull View createViewFromResource(@NonNull LayoutInflater inflater, int position,
                                                                    @Nullable View convertView, @NonNull ViewGroup parent, int resource) {
        final View view;

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        TextView groupName = view.findViewById(R.id.txtViewGroupName);
        TextView groupBalance = view.findViewById(R.id.txtViewGroupBalance);
        TextView groupUsers = view.findViewById(R.id.txtViewGroupMembers);

        final Group item = getItem(position);
        groupName.setText(item.getName());

        //wenn der Gruppenname länger als 10 Zeichen ist wird er abgeschnitten
        if (groupName.getText().length() > 10){
            groupName.setText(groupName.getText().subSequence(0, 10) + "...");
        }

        groupBalance.setText("+10");
        groupUsers.setText("7 Mitglieder");

        return view;
    }
}
