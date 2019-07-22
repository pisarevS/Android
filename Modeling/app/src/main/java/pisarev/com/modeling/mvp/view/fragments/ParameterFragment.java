package pisarev.com.modeling.mvp.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import pisarev.com.modeling.R;

public class ParameterFragment extends Fragment {

    private EditText editText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.fragment_parameter, container, false );
        editText = rootView.findViewById( R.id.editText );
        return rootView;
    }

    public void setText(String text) {
        editText.setText( text );
    }

    public String getText() {
        return editText.getText().toString();
    }

}
