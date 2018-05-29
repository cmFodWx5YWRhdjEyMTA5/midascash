package midascash.indonesia.optima.prima.midascash.fragment_transaction;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.formula.calculatordialog;

public class fragment_transfer extends Fragment {

    LayoutInflater inflater;
    LinearLayout contain;

    calculatordialog calculatorchoice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);
        contain = view.findViewById(R.id.llincome);

        inflater = getActivity().getLayoutInflater();
        View child = inflater.inflate(R.layout.layout_transactionstransfer, null);
        EditText editfrom = child.findViewById(R.id.input_value);
        editfrom.setSelected(false);

        ImageView calc = child.findViewById(R.id.transcalc);

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TypedValue typedValue = new TypedValue();
                getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
                int color = typedValue.data;

                int[][] states = new int[][] {
                        new int[] { android.R.attr.state_enabled}, // enabled
                        new int[] {-android.R.attr.state_enabled}, // disabled
                        new int[] {-android.R.attr.state_checked}, // unchecked
                        new int[] { android.R.attr.state_pressed}  // pressed
                };

                int[] colors = new int[] {
                        color,
                        Color.WHITE,
                        Color.GREEN,
                        Color.BLUE
                };

                ColorStateList myList = new ColorStateList(states, colors);

                calculatorchoice = new calculatordialog(getActivity(),editfrom,myList);
                calculatorchoice.showcalculatordialog();
            }
        });


        TextInputLayout editformcat = child.findViewById(R.id.input_valuecatch);
        editformcat.setSelected(false);


        contain.addView(child);

        // Inflate the layout for this fragment
        return view;
    }

}
