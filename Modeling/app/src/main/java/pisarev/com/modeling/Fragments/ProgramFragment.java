package pisarev.com.modeling.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import pisarev.com.modeling.Activity.MainActivity;
import pisarev.com.modeling.MyCollection;
import pisarev.com.modeling.R;

public class ProgramFragment extends Fragment {

    private static EditText editText1;

    public ProgramFragment (){
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments( args );
        if(args.getBoolean( MainActivity.PROGRAM )){
            if(MyCollection.getProgramArrayList()!=null){
                for(int i=0;i<MyCollection.getProgramArrayList().size();i++){
                    editText1.append( MyCollection.getProgramArrayList().get( i ) );
                    editText1.append( "\n" );
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_program, container, false);
        editText1=(EditText)rootView.findViewById( R.id.editText1 );
        return rootView;
    }


}
