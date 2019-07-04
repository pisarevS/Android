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

    private static EditText editText;
    private final String KEY = "parameterFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.fragment_parameter, container, false );
        editText = rootView.findViewById( R.id.editText );
        if (savedInstanceState != null) {
            editText.setText( savedInstanceState.getString( KEY ) );
        }
        return rootView;
    }

    public static void setText(String text) {
        editText.setText( text );
    }

    public static String getText() {
        return editText.getText().toString();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState( outState );
        outState.putString( KEY, editText.getText().toString() );
    }
}
