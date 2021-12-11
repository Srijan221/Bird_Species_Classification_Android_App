package com.Srijan.BirdSpecies_Classification.ui.home;
/*
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.Srijan.BirdSpecies_Classification.R;

public class Add_Cards extends Fragment {
    Context context;
    CardView cardview;
    RelativeLayout.LayoutParams layoutparams;
    TextView textview;
    RelativeLayout relativeLayout;
    private String[] titles = {"Chapter One",
            "Chapter Two",
            "Chapter Three",
            "Chapter Four",
            "Chapter Five",
            "Chapter Six",
            "Chapter Seven",
            "Chapter Eight"};

    private String[] details = {"Item one details",
            "Item two details", "Item three details",
            "Item four details", "Item five details",
            "Item six details", "Item seven details",
            "Item eight details"};

    private int[] images = {};

    private int[] cardId = {};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final Button button = root.findViewById(R.id.button);
        final RelativeLayout relativeLayout= (RelativeLayout) getActivity().findViewById(R.id.relativelayout1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCardViewProgrammatically();
            }
        });
        return root;

    }

    public void CreateCardViewProgrammatically(){
        relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.relativelayout1);
        context = getActivity().getApplicationContext();
        cardview = new CardView(context);

        layoutparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        cardview.setLayoutParams(layoutparams);

        cardview.setRadius(15);

        cardview.setPadding(25, 25, 25, 25);

        cardview.setCardBackgroundColor(Color.MAGENTA);

        cardview.setMaxCardElevation(30);

        cardview.setMaxCardElevation(6);

        textview = new TextView(context);

        textview.setLayoutParams(layoutparams);

        textview.setText("CardView Programmatically");

        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

        textview.setTextColor(Color.WHITE);

        textview.setPadding(25,25,25,25);

        textview.setGravity(Gravity.CENTER);

        cardview.addView(textview);

        relativeLayout.addView(cardview);

    }

}
*/
