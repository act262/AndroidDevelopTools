package io.micro.adt.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import io.micro.adt.R;

/**
 * 终端,用于输入各种测试命令
 *
 * @author act262@gmail.com
 */
public class TerminalFragment extends DialogFragment {

    private EditText inputEditText;
    private TextView outputTextView;

    public static TerminalFragment newInstance() {
        Bundle args = new Bundle();
        TerminalFragment fragment = new TerminalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.Theme_AppCompat_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_terminal, null);
        inputEditText = (EditText) inflate.findViewById(R.id.et_input);
        outputTextView = (TextView) inflate.findViewById(R.id.tv_output);
//        outputTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        inputEditText.setOnEditorActionListener(new EditorAction());

        return inflate;
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void exec(String cmd) throws IOException {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(process.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        // exec shell with root
        os.writeBytes(cmd + " \n");
        os.writeBytes("exit\n");
        os.flush();

        String line;
        // print execute result
        while ((line = reader.readLine()) != null) {
            outputTextView.append(line);
            outputTextView.append("\n");
        }

        // print error
        while ((line = errorReader.readLine()) != null) {
            outputTextView.append(line);
            outputTextView.append("\n");
        }
    }

    private class EditorAction implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_GO == actionId || EditorInfo.IME_ACTION_DONE == actionId) {

                String cmd = inputEditText.getText().toString();
                if (TextUtils.isEmpty(cmd)) {
                    showToast("do not input invalid command");
                    return true;
                }

                try {
                    outputTextView.setText("");
                    exec(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                    outputTextView.setText(e.getMessage());
                }
            }
            return false;
        }
    }
}
