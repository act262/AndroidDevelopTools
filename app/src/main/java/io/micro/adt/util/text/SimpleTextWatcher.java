package io.micro.adt.util.text;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Simple TextWatcher Wrapper
 *
 * @author act262@gmail.com
 */
public class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // no-op
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // no-op
    }

    @Override
    public void afterTextChanged(Editable s) {
        // no-op
    }
}
