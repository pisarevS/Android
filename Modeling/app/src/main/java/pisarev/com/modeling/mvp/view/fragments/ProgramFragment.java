package pisarev.com.modeling.mvp.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import pisarev.com.modeling.R;
import pisarev.com.modeling.mvp.model.Const;
import pisarev.com.modeling.mvp.model.SQLiteData;

public class ProgramFragment extends Fragment {

    private EditText editText;
    private SQLiteData sqLiteData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_program, container, false);
        editText = rootView.findViewById(R.id.editText);
        sqLiteData = new SQLiteData(getContext(), SQLiteData.DATABASE_PROGRAM);
        editText.setText(sqLiteData.getProgramText().get(SQLiteData.KEY_PROGRAM));
        return rootView;
    }

    public void setText(String text) {
        editText.setText(text);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Const.TEG, "onDestroy()  ProgramFragment");
        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put(SQLiteData.KEY_PROGRAM, editText.getText().toString());
        sqLiteData.deleteProgramText();
        sqLiteData.setProgramText(stringStringMap);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(Const.TEG, "onPause()  ProgramFragment");
        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put(SQLiteData.KEY_PROGRAM, editText.getText().toString());
        sqLiteData.deleteProgramText();
        sqLiteData.setProgramText(stringStringMap);
    }
}
