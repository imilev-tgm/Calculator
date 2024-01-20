package com.example.calculator;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText input1, input2;
    private TextView output;
    private Button buttonCalculate, buttonMS, buttonMR;
    private Spinner calculationTypeSpinner;

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_KEY = "calculator_shared_prefs";
    private static final String CALCULATION_TYPE_KEY = "calculation_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        output = findViewById(R.id.output);
        calculationTypeSpinner = findViewById(R.id.calculationTypeSpinner);

        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonMS = findViewById(R.id.buttonMS);
        buttonMR = findViewById(R.id.buttonMR);

        Toolbar toolbar = findViewById(R.id.header);
        setSupportActionBar(toolbar);

        String[] calculationTypes = {"+", "-", "*", "/"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, calculationTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calculationTypeSpinner.setAdapter(dataAdapter);


        calculationTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedOperatorId = calculationTypeSpinner.getSelectedItemPosition();
                // Now you can use the selected calculation type
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the situation where no calculation type is selected
            }
        });

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String operator = calculationTypeSpinner.getSelectedItem().toString();
                try {
                    double value1 = Double.parseDouble(input1.getText().toString());
                    double value2 = Double.parseDouble(input2.getText().toString());
                    double result = calculate(value1, value2, operator);
                    output.setText(String.valueOf(result));
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid input! Please enter valid numbers.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        buttonMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Save current calculation type
                String selectedOperator = calculationTypeSpinner.getSelectedItem().toString();
                sharedPreferences.edit().putString(CALCULATION_TYPE_KEY, selectedOperator).apply();
                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });


        buttonMR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Load saved calculation type
                String savedOperator = sharedPreferences.getString(CALCULATION_TYPE_KEY, "+");
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) calculationTypeSpinner.getAdapter();
                int savedOperatorId = adapter.getPosition(savedOperator);
                calculationTypeSpinner.setSelection(savedOperatorId);
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


    // Called when the options menu is being created
    // Inflate the menu resource into the provided Menu object
    // Force the system to display the icons in the options menu
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }


    // Called whenever an item in the options menu is selected
    // Handle the selection of the "Reset" and "Info" items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.reset) {
            resetFields();
            return true;
        } else if (id == R.id.info) {
            showInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Called when a menu is opened
    // Forces the system to display the icons in the options menu
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened", e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    // Reset the input fields and the output field
    private void resetFields() {
        input1.setText("");
        input2.setText("");
        output.setText("");
    }

    // Display a toast message with information about the author and the version of the app
    private void showInfo() {
        String message = "Author: Ivan Milev\nVersion: 1.0";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }




}
