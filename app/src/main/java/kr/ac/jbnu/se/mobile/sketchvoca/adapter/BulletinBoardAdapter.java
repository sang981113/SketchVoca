package kr.ac.jbnu.se.mobile.sketchvoca.adapter;

import android.app.Activity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.ac.jbnu.se.mobile.sketchvoca.R;
import kr.ac.jbnu.se.mobile.sketchvoca.WordInfo;

public class BulletinBoardAdapter extends RecyclerView.Adapter<BulletinBoardAdapter.ViewHolder> {
    private ArrayList<WordInfo> mDataSet;
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public ViewHolder(CardView view) {
            super(view);
            // Define click listener for the ViewHolder's View

            cardView = view;
        }
    }

    public BulletinBoardAdapter(ArrayList<WordInfo> dataSet) {
        mDataSet = dataSet;
    }

    public BulletinBoardAdapter(Activity activity, ArrayList<WordInfo> myDataSet) {
        mDataSet = myDataSet;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bulletincard, viewGroup, false);
        final ViewHolder ViewHolder = new ViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        CardView cardView = viewHolder.cardView;
        TextView engWordTextView = cardView.findViewById(R.id.engWordBulletinTextView);
        TextView wordMeaning = cardView.findViewById(R.id.wordMeaningBulletinTextView);

        engWordTextView.setText(mDataSet.get(position).getEngWord());
        wordMeaning.setText(mDataSet.get(position).getWordMeaning());
        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        String wordImagePath = mDataSet.get(position).getWordImagePath();
        if(Patterns.WEB_URL.matcher(wordImagePath).matches()){
            contentsLayout.removeAllViews();//없으면 recyclerView에서 View가 중복생성됨
            ImageView wordImageView = new ImageView(activity);
            wordImageView.setLayoutParams(layoutParams);
            contentsLayout.addView(wordImageView);
            Glide.with(activity).load(wordImagePath).override(800).into(wordImageView);
        } else{
            ImageView defaultImageView = cardView.findViewById(R.id.defaultImageView);
            defaultImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
