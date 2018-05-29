package midascash.indonesia.optima.prima.midascash.formula;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import midascash.indonesia.optima.prima.midascash.R;

public class calculatordialog {
    DecimalFormat formatter = new DecimalFormat("###,###,###.##");
    TextView counted,tempcount;
    Context context;
    ColorStateList color;

    public calculatordialog(){

    }

    public calculatordialog(Context a,TextView b,ColorStateList color){
        counted=b;
        context=a;
        this.color=color;

    }
    public void showcalculatordialog(){
        Button one,two,three,four,five,seven,six,eight,nine,zero,dots;
        Button clear,backspace,equals,divide,times,minus,plus;
        TextView total,current;
        final String[] mustcount = {""};


        AlertDialog.Builder dialogcalc = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                .setTitle("Calculator");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View v = inflater.inflate(R.layout.layour_calculator,null);

        current = v.findViewById(R.id.txtInput);
        current.setText("0");

        zero = v.findViewById(R.id.btnZero);

        zero.setBackgroundTintList(color);

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().equals("0")){

                }else {
                    current.setText(current.getText().toString()+"0");
                }
            }
        });

        one = v.findViewById(R.id.btnOne);

        one.setBackgroundTintList(color);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().equals("0")){
                    current.setText("1");
                }
                else {
                    current.setText(current.getText().toString() + "1");
                }
            }
        });

        two = v.findViewById(R.id.btnTwo);

        two.setBackgroundTintList(color);

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().equals("0")){
                    current.setText("2");
                }
                else {
                    current.setText(current.getText().toString() + "2");
                }
            }
        });

        three = v.findViewById(R.id.btnThree);

        three.setBackgroundTintList(color);

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().equals("0")){
                    current.setText("3");
                }
                else {
                    current.setText(current.getText().toString() + "3");
                }
            }
        });

        four = v.findViewById(R.id.btnFour);

        four.setBackgroundTintList(color);

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().equals("0")){
                    current.setText("4");
                }
                else {
                    current.setText(current.getText().toString() + "4");
                }
            }
        });

        five = v.findViewById(R.id.btnFive);

        five.setBackgroundTintList(color);

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().equals("0")){
                current.setText("5");
            }
            else {
                current.setText(current.getText().toString() + "5");
            }
            }
        });

        six = v.findViewById(R.id.btnSix);

        six.setBackgroundTintList(color);

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().equals("0")){
                    current.setText("5");
                }
                else {
                    current.setText(current.getText().toString() + "5");
                }
            }
        });

        seven = v.findViewById(R.id.btnSeven);

        seven.setBackgroundTintList(color);

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().equals("0")){
                    current.setText("7");
                }
                else {
                    current.setText(current.getText().toString() + "7");
                }
            }
        });

        eight = v.findViewById(R.id.btnEight);

        eight.setBackgroundTintList(color);

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().equals("0")){
                    current.setText("8");
                }
                else {
                    current.setText(current.getText().toString() + "8");
                }
            }
        });

        nine = v.findViewById(R.id.btnNine);

        nine.setBackgroundTintList(color);

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().equals("0")){
                    current.setText("9");
                }
                else {
                    current.setText(current.getText().toString() + "9");
                }
            }
        });

        dots = v.findViewById(R.id.btnDecimal);

        dots.setBackgroundTintList(color);

        dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().toString().contains(".") && current.getText().toString().contains("+") ){
                    String s = current.getText().toString();
                    int counter = 0;
                    for( int i=0; i<s.length(); i++ ) {
                        if( s.charAt(i) == '.' ) {
                            counter++;
                        }
                    }
                    if(counter>=2){
                        current.setText(current.getText().toString());
                    }
                    else if(current.getText().toString().substring(current.getText().toString().length()-1).equals("+")){

                    }else{
                        current.setText(current.getText().toString()+".");
                    }
                }else if(current.getText().toString().contains(".") && current.getText().toString().contains("-") ){
                    String s = current.getText().toString();
                    int counter = 0;
                    for( int i=0; i<s.length(); i++ ) {
                        if( s.charAt(i) == '.' ) {
                            counter++;
                        }
                    }
                    if(counter>=2){
                        current.setText(current.getText().toString());
                    }
                    else if(current.getText().toString().substring(current.getText().toString().length()-1).equals("-")){

                    }else {
                        current.setText(current.getText().toString()+".");
                    }
                }else if(current.getText().toString().contains(".") && current.getText().toString().contains("/") ){
                    String s = current.getText().toString();
                    int counter = 0;
                    for( int i=0; i<s.length(); i++ ) {
                        if( s.charAt(i) == '.' ) {
                            counter++;
                        }
                    }
                    if(counter>=2){
                        current.setText(current.getText().toString());
                    }
                    else if(current.getText().toString().substring(current.getText().toString().length()-1).equals("/")){

                    }else {
                        current.setText(current.getText().toString()+".");
                    }
                }else if(current.getText().toString().contains(".") && current.getText().toString().contains("x") ){
                    String s = current.getText().toString();
                    int counter = 0;
                    for( int i=0; i<s.length(); i++ ) {
                        if( s.charAt(i) == '.' ) {
                            counter++;
                        }
                    }
                    if(counter>=2){
                        current.setText(current.getText().toString());
                    }
                    else if(current.getText().toString().substring(current.getText().toString().length()-1).equals("x")){

                    }else {
                        current.setText(current.getText().toString()+".");
                    }
                }
                else if(current.getText().toString().contains(".")){

                }else {
                        current.setText(current.getText().toString() + ".");

                }
            }
        });

        clear = v.findViewById(R.id.btnClear);

        clear.setBackgroundTintList(color);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setText("0");
            }
        });
        backspace = v.findViewById(R.id.btnBack);

        backspace.setBackgroundTintList(color);

        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().toString().equals("0")){
                    current.setText("0");
                }
                else {
                    current.setText(current.getText().toString().substring(0,current.getText().toString().length()-1));
                }

            }
        });



        equals = v.findViewById(R.id.btnEquals);

        equals.setBackgroundTintList(color);

        equals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().toString().substring(current.getText().toString().length()-1).equals("+")){

                }
                else if (current.getText().toString().substring(current.getText().toString().length()-1).equals("-")){

                }
                else if (current.getText().toString().substring(current.getText().toString().length()-1).equals("/")){

                }
                else if (current.getText().toString().substring(current.getText().toString().length()-1).equals("x")){

                }
                else {
                    resolveresult(current);
                }
            }
        });

        plus = v.findViewById(R.id.btnAdd);

        plus.setBackgroundTintList(color);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().toString().substring(current.getText().toString().length()-1).equals("+") || current.getText().toString().substring(current.getText().toString().length()-1).equals("-") || current.getText().toString().substring(current.getText().toString().length()-1).equals("x") || current.getText().toString().substring(current.getText().toString().length()-1).equals("/")){

                }
                else if(!mustcount[0].equals("")){
                    resolveresult(current);
                    mustcount[0]="+";
                    current.setText(current.getText().toString()+"+");
                }
                else {
                    current.setText(current.getText().toString()+"+");
                    mustcount[0] = "+";
                }
            }
        });

        minus = v.findViewById(R.id.btnSubtract);

        minus.setBackgroundTintList(color);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().toString().substring(current.getText().toString().length()-1).equals("+") || current.getText().toString().substring(current.getText().toString().length()-1).equals("-") || current.getText().toString().substring(current.getText().toString().length()-1).equals("x") || current.getText().toString().substring(current.getText().toString().length()-1).equals("/")){

                }
                else if(!mustcount[0].equals("")){
                    resolveresult(current);
                    mustcount[0]="-";
                    current.setText(current.getText().toString()+"-");
                }
                else {
                    current.setText(current.getText().toString()+"-");
                    mustcount[0] = "-";
                }
            }
        });

        divide = v.findViewById(R.id.btnDivide);

        divide.setBackgroundTintList(color);

        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().toString().substring(current.getText().toString().length()-1).equals("+") || current.getText().toString().substring(current.getText().toString().length()-1).equals("-") || current.getText().toString().substring(current.getText().toString().length()-1).equals("x") || current.getText().toString().substring(current.getText().toString().length()-1).equals("/")){

                }
                else if(!mustcount[0].equals("")){
                    resolveresult(current);
                    mustcount[0]="/";
                    current.setText(current.getText().toString()+"/");
                }
                else {
                    current.setText(current.getText().toString()+"/");
                    mustcount[0] = "/";
                }
            }
        });

        times = v.findViewById(R.id.btnMultiply);

        times.setBackgroundTintList(color);

        times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().toString().substring(current.getText().toString().length()-1).equals("+") || current.getText().toString().substring(current.getText().toString().length()-1).equals("-") || current.getText().toString().substring(current.getText().toString().length()-1).equals("x") || current.getText().toString().substring(current.getText().toString().length()-1).equals("/")){

                }
                else if(!mustcount[0].equals("")){
                    resolveresult(current);
                    mustcount[0]="x";
                    current.setText(current.getText().toString()+"x");
                }
                else {
                    current.setText(current.getText().toString()+"x");
                    mustcount[0] = "x";
                }
            }
        });




        current.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialogcalc.setView(v);
        dialogcalc.setPositiveButton("Save",null);
        dialogcalc.setNegativeButton("Cancel",null);

        AlertDialog calc = dialogcalc.create();

        calc.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) calc).getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mustcount[0].equals("")){
                            resolveresult(current);
                            counted.setText(formatter.format(Double.parseDouble(current.getText().toString())));
                        }
                        else {
                            resolveresult(current);
                            counted.setText(formatter.format(Double.parseDouble(current.getText().toString())));
                        }
                            dialog.dismiss();

                    }
                });
            }
        });

        calc.show();

    }

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public void resolveresult(TextView a){
            String string = a.getText().toString();
            try {
                if (string.contains("+")) {
                    String[] parts = string.split("\\+");
                    String part1 = parts[0];
                    String part2 = parts[1];
                    Double part3 = Double.parseDouble(part1);
                    Double part4 = Double.parseDouble(part2);

                    a.setText(String.valueOf(part3 + part4));

                } else if (string.contains("-")) {
                    String[] parts = string.split("-");
                    String part1 = parts[0];
                    String part2 = parts[1];
                    Double part3 = Double.parseDouble(part1);
                    Double part4 = Double.parseDouble(part2);

                    a.setText(String.valueOf(part3 - part4));
                } else if (string.contains("x") || string.contains("X")) {
                    String[] parts = string.split("x");
                    String part1 = parts[0];
                    String part2 = parts[1];
                    Double part3 = Double.parseDouble(part1);
                    Double part4 = Double.parseDouble(part2);

                    a.setText(String.valueOf(part3 * part4));
                } else if (string.contains("/")) {
                    String[] parts = string.split("/");
                    String part1 = parts[0];
                    String part2 = parts[1];
                    Double part3 = Double.parseDouble(part1);
                    Double part4 = Double.parseDouble(part2);

                    a.setText(String.valueOf(part3 / part4));
                } else {

                }

                String[] add = string.split("\\+");
                String[] substract = string.split("-");
                String[] times = string.split("x");
                String[] devide = string.split("/");
            }catch (Exception e){
                Toast.makeText(context,"Out Of Range : "+e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }

    }
}
