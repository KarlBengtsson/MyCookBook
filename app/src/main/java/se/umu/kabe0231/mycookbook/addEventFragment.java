package se.umu.kabe0231.mycookbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Fragment class to add a comment to a recept.
 * Implements dialog fragment and returns two strings,
 * One containing the date of the comment and one
 * containing the comment which the user inputs themselves.
 */
public class addEventFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText Event;
    private Date date = new Date();

    public interface addEventDialogListener {
        void onFinishEditDialog(String a, String b);
    }

    public addEventFragment() {
    }

    public static addEventFragment newInstance(String title) {
        addEventFragment frag = new addEventFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_event, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        Event = (EditText) view.findViewById(R.id.editTextKommentar);
        Event.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Event.setOnEditorActionListener(this);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            addEventDialogListener listener = (addEventDialogListener) getActivity();
            String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
            listener.onFinishEditDialog(modifiedDate, Event.getText().toString());
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }
}

