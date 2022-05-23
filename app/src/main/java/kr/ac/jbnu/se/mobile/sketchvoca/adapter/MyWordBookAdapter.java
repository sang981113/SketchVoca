package kr.ac.jbnu.se.mobile.sketchvoca.adapter;

import android.app.Activity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.ac.jbnu.se.mobile.sketchvoca.R;
import kr.ac.jbnu.se.mobile.sketchvoca.WordInfo;

public class MyWordBookAdapter extends RecyclerView.Adapter<MyWordBookAdapter.ViewHolder> {
    private ArrayList<WordInfo> mDataSet;
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    public MyWordBookAdapter(Activity activity, ArrayList<WordInfo> myDataSet) {
        mDataSet = myDataSet;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wordcard, viewGroup, false);
        final ViewHolder ViewHolder = new ViewHolder(cardView);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        CardView cardView = viewHolder.cardView;

        ImageView wordImageView = cardView.findViewById(R.id.wordImageView);
        TextView engWordTextView = cardView.findViewById(R.id.engWordTextView);
        TextView wordMeaning = cardView.findViewById(R.id.wordMeaningTextView);

        engWordTextView.setText(mDataSet.get(position).getEngWord());
        wordMeaning.setText(mDataSet.get(position).getWordMeaning());

        String wordImagePath = mDataSet.get(position).getWordImagePath();
        if(Patterns.WEB_URL.matcher(wordImagePath).matches()){
            Glide.with(activity).load(wordImagePath).override(400).into(wordImageView);
        }

        engWordTextView.setText(mDataSet.get(position).getEngWord());
        wordMeaning.setText(mDataSet.get(position).getWordMeaning());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
