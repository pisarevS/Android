package pisarev.com.modeling.mvp.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import javax.inject.Inject;

import pisarev.com.modeling.Const;
import pisarev.com.modeling.R;
import pisarev.com.modeling.application.App;
import pisarev.com.modeling.mvp.model.MyData;

public class ProgramFragment extends Fragment  {

    private static EditText editText;
    @Inject
    MyData data;

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments( args );
        if (args != null) {
            editText.setText(args.getString( Const.PROGRAM ));
        }
        if(args.getBoolean( Const.TOUCH_FAB )){
            data.setProgram( editText.getText().toString()  );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_program, container, false);
        editText =rootView.findViewById( R.id.editText );
        App.getComponent().inject(this  );
        if(data.getProgram()!=""){
            editText.setText( data.getProgram() );
        }
        return rootView;
    }

    public static void setText(String text){
        editText.setText( text );
    }

    public static String getText(){
        return editText.getText().toString();
    }



}
