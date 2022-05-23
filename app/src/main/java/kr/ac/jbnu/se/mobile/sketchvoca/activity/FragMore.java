package kr.ac.jbnu.se.mobile.sketchvoca.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.ac.jbnu.se.mobile.sketchvoca.R;

public class FragMore extends Fragment {
    private Button Settingbutton;
    private Button LikedPostbutton;
    private Button MyPostsbutton;
    private Button MyDatabutton;
    private TextView Plus;
    public int SelectedButton = 0;
    private View view;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.frag_more, container, false);
        //getChildFragmentManager().beginTransaction().add(R.id.child_fragment, new child_fragment1()).commit();


        Button MyPostsbutton  = (Button) view.findViewById(R.id.MyPostButton);
       Button Settingbutton = (Button) view.findViewById(R.id.Settingbutton);
       Button MyDatabutton = (Button) view.findViewById(R.id.MyDatabutton);



        Settingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int commit = getChildFragmentManager().beginTransaction().replace(R.id.Child_Frame, new child_fragBGMSetting()).commit();

            }
        });


        MyPostsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int commit = getChildFragmentManager().beginTransaction().replace(R.id.Child_Frame, new child_fragMyPost()).commit();

            }
        });

        MyDatabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int commit = getChildFragmentManager().beginTransaction().replace(R.id.Child_Frame, new child_fragMyMemInfo()).commit();

            }
        });



        return  view;


    }
}
