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

/**
 * Fragment to add Ingredient to a recipe, implements dialogFragment
 * returns two strings from dialogFragment, on string with the
 * name of the ingredient and one with the amount. The user inputs both.
 */

public class AddIngredientFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText Ingredient;
    private EditText Amount;

    public interface AddIngredientDialogListener {
        void onFinishEditDialog(String a, String b);
    }

    public AddIngredientFragment() {
    }

    public static AddIngredientFragment newInstance(String title) {
        AddIngredientFragment frag = new AddIngredientFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add__ingredient, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        Ingredient = (EditText) view.findViewById(R.id.editTextIngrediens);
        Amount = (EditText) view.findViewById(R.id.editTextMängd);
        Ingredient.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Amount.setOnEditorActionListener(this);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            AddIngredientDialogListener listener = (AddIngredientDialogListener) getActivity();
            listener.onFinishEditDialog(Ingredient.getText().toString(), Amount.getText().toString());
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }
}
