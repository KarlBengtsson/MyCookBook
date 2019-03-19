package se.umu.kabe0231.mycookbook;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class viewEventsFragment extends DialogFragment {
    //implements TextView.OnEditorActionListener

    ArrayList<String> eventList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    ImageButton okButton;
    LinearLayout linear;
    LinearLayout MainLinear;
    TextView text;
    TextView text1;
    View emptyView;


    public viewEventsFragment() {
    }

    public static viewEventsFragment newInstance(String title) {
        viewEventsFragment frag = new viewEventsFragment();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventList = getArguments().getStringArrayList("events");
        dateList = getArguments().getStringArrayList("dates");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_events, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainLinear = (LinearLayout) view.findViewById(R.id.linearMain);

        //Button to dismiss the fragment
        okButton = (ImageButton) view.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Display earlier coments in a List
        for (int i = 0; i < dateList.size(); i++) {
            //define Horizontal Linear Layout
            linear = new LinearLayout(getActivity());
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linear.setOrientation(LinearLayout.HORIZONTAL);
            linear.setLayoutParams(layout);

            text = new TextView(getContext());
            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            text.setText(dateList.get(i) + ":   ");
            text.setPadding(0, 10, 0, 10);
            text.setAutoSizeTextTypeUniformWithConfiguration(12, 20, 1, TypedValue.COMPLEX_UNIT_DIP);
            text.setTypeface(null, Typeface.BOLD);
            //text.setGravity(Gravity.RIGHT);

            text1 = new TextView(getContext());
            text1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            text1.setText(eventList.get(i));
            text1.setPadding(0, 10, 0, 10);
            text1.setAutoSizeTextTypeUniformWithConfiguration(1, 20, 1, TypedValue.COMPLEX_UNIT_DIP);
            linear.addView(text);
            linear.addView(text1);

            int dividerHeight = (int) (getResources().getDisplayMetrics().density * 1);
            emptyView = new View(getActivity());
            emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dividerHeight));
            emptyView.setBackgroundColor(Color.parseColor("#000000"));
            MainLinear.addView(emptyView);
            MainLinear.addView(linear);

        }

    }

    /*@Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            addEventDialogListener listener = (addEventDialogListener) getActivity();
            listener.onFinishEditDialog("Tack");
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }*/
}



