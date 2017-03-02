package io.micro.adt.ui.hosts;

import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import io.micro.adt.R;
import io.micro.adt.db.DataProvider;

/**
 * 添加新Item弹窗
 *
 * @author act262@gmail.com
 */
public class NewHostsItemDialog extends DialogFragment implements View.OnClickListener {

    public static NewHostsItemDialog newInstance() {
        Bundle args = new Bundle();
        NewHostsItemDialog fragment = new NewHostsItemDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private EditText hostNameEditor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.dialog_new_hosts_item, null);
        hostNameEditor = (EditText) inflate.findViewById(R.id.et_host_name);
        inflate.findViewById(R.id.btn_ok).setOnClickListener(this);
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                confirmNew();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    private void confirmNew() {
        String name = hostNameEditor.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "不能为空", Toast.LENGTH_SHORT).show();
        }

        //TODO: 重复方案名提示

        ContentResolver contentResolver = getActivity().getContentResolver();
        ContentValues contentValues = new ContentValues(1);
        contentValues.put("title", name);
        contentResolver.insert(DataProvider.HOSTS, contentValues);

        dismiss();
    }
}
