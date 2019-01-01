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

public class ParameterFragment extends Fragment {

    private static EditText editText2;

    public ParameterFragment(){
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments( args );
        if(args.getBoolean( MainActivity.PARAMETER )){
            if(MyCollection.getParameterArrayList()!=null){
                for(int i=0;i<MyCollection.getParameterArrayList().size();i++){
                    editText2.append( MyCollection.getParameterArrayList().get( i ) );
                    editText2.append( "\n" );
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_parameter, container, false);
        editText2=(EditText)rootView.findViewById( R.id.editText2 );
        return rootView;
    }


}
