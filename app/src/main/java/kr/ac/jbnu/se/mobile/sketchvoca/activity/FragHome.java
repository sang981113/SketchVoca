package kr.ac.jbnu.se.mobile.sketchvoca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.ac.jbnu.se.mobile.sketchvoca.R;

public class FragHome extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.frag_home, container, false);

        Button SketchBUtton = (Button) view.findViewById(R.id.stekch);
        //SketchBUtton.setVisibility(View.VISIBLE);
        SketchBUtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //프래그먼트 처리
                getChildFragmentManager().beginTransaction().replace(R.id.Child_Paint_frag, new PaintFragment()).commit();
                SketchBUtton.setVisibility(View.GONE);

            }
        });
       return  view;
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }


}
