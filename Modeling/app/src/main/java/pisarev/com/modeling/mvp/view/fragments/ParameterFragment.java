package pisarev.com.modeling.mvp.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import pisarev.com.modeling.R;
import pisarev.com.modeling.mvp.model.Const;
import pisarev.com.modeling.mvp.model.SQLiteData;

public class ParameterFragment extends Fragment {

    private EditText editText;
    private SQLiteData sqLiteData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.fragment_parameter, container, false );
        editText = rootView.findViewById( R.id.editText );
        sqLiteData=new SQLiteData( getContext(),SQLiteData.DATABASE_PARAMETER );
        editText.setText( sqLiteData.getProgramText().get( SQLiteData.KEY_PROGRAM )  );
        editText.setOnCreateContextMenuListener(this);
        return rootView;
    }

    public void setText(String text) {
        editText.setText( text );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d( Const.TEG,"onDestroy()  ParameterFragment" );
        Map<String,String> stringStringMap=new HashMap<>(  );
        stringStringMap.put( SQLiteData.KEY_PROGRAM,editText.getText().toString() );
        sqLiteData.deleteProgramText();
        sqLiteData.setProgramText(stringStringMap);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d( Const.TEG,"onPause()  ParameterFragment" );
        Map<String,String> stringStringMap=new HashMap<>(  );
        stringStringMap.put( SQLiteData.KEY_PROGRAM,editText.getText().toString() );
        sqLiteData.deleteProgramText();
        sqLiteData.setProgramText(stringStringMap);
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu( menu, v, menuInfo );
        if (v.getId()==R.id.editText)
        {
            menu.add(0, 1, 0, "Copy");
            menu.add(0, 2, 0, "Paste");
            menu.add(0, 3, 0, "Select All");
            menu.add(0, 3, 0, "Delete All");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
        }
        return super.onContextItemSelected( item );
    }*/
}
