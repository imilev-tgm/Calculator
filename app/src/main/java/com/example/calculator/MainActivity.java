package com.example.calculator;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText input1, input2;
    private TextView output;
    private RadioGroup operatorGroup;
    private Button buttonCalculate, buttonMS, buttonMR;

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_KEY = "calculator_shared_prefs";
    private static final String CALCULATION_TYPE_KEY = "calculation_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        output = findViewById(R.id.output);
        operatorGroup = findViewById(R.id.operatorGroup);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonMS = findViewById(R.id.buttonMS);
        buttonMR = findViewById(R.id.buttonMR);

        // Deactivate RadioButtons
        deactivateRadioButtons();

        // Set listeners
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected operator
                int selectedOperatorId = operatorGroup.getCheckedRadioButtonId();
                String operator = "";

                if (selectedOperatorId == R.id.buttonPlus) {
                    operator = "+";
                } else if (selectedOperatorId == R.id.buttonMinus) {
                    operator = "-";
                } else if (selectedOperatorId == R.id.buttonMultiply) {
                    operator = "*";
                } else if (selectedOperatorId == R.id.buttonDivide) {
                    operator = "/";
                }

                // Get the input values
                double value1 = Double.parseDouble(input1.getText().toString());
                double value2 = Double.parseDouble(input2.getText().toString());

                // Perform calculation
                double result = calculate(value1, value2, operator);

                // Update output field
                output.setText(String.valueOf(result));
                if (result < 0) {
                    output.setTextColor(Color.RED);
                } else {
                    output.setTextColor(Color.BLACK);
                }
            }
        });

        output.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // Clear output field
                output.setText("");
                return false;
            }
        });

        buttonMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Save current calculation type to SharedPreferences
                int selectedOperatorId = operatorGroup.getCheckedRadioButtonId();
                sharedPreferences.edit().putInt(CALCULATION_TYPE_KEY, selectedOperatorId).apply();
                Toast.makeText(MainActivity.this, "Gespeichert", Toast.LENGTH_SHORT).show();
            }
        });

        buttonMR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Restore calculation type from SharedPreferences
                int savedOperatorId = sharedPreferences.getInt(CALCULATION_TYPE_KEY, R.id.buttonPlus);
                RadioButton savedOperatorButton = findViewById(savedOperatorId);
                savedOperatorButton.setChecked(true);
            }
        });

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
    }

    private double calculate(double value1, double value2, String operator) {
        double result = 0;

        switch (operator) {
            case "+":
                result = value1 + value2;
                break;
            case "-":
                result = value1 - value2;
                break;
            case "*":
                result = value1 * value2;
                break;
            case "/":
                result = value1 / value2;
                break;
        }

        return result;
    }

    @Override
    protected void onStart() {
        super.onStart();
        activateRadioButtons();
    }

    private void activateRadioButtons() {
        for (int i = 0; i < operatorGroup.getChildCount(); i++) {
            operatorGroup.getChildAt(i).setEnabled(true);
        }
    }

    private void deactivateRadioButtons() {
        for (int i = 0; i < operatorGroup.getChildCount(); i++) {
            operatorGroup.getChildAt(i).setEnabled(false);
        }
    }
}
