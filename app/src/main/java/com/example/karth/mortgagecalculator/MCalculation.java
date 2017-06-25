package com.example.karth.mortgagecalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class MCalculation extends Fragment {
    private static SQLiteDatabase myMortgageCalculator;
    //private ViewPager viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View calView = inflater.inflate(R.layout.calculation, container, false);
        Spinner spPropType = (Spinner) calView.findViewById(R.id.ddPropertyType);
        AutoCompleteTextView autoCities = (AutoCompleteTextView) calView.findViewById(R.id.autocompleteCity);
        Spinner spState = (Spinner) calView.findViewById(R.id.ddState);

        myMortgageCalculator = getActivity().openOrCreateDatabase("MortgageCalculator", getActivity().MODE_PRIVATE, null);
        //FloatingActionButton fab = (FloatingActionButton) calView.findViewById(R.id.fabSave);

        FloatingActionButton fEdit = (FloatingActionButton) getActivity().findViewById(R.id.fabEdit);
        FloatingActionButton fDelete = (FloatingActionButton) getActivity().findViewById(R.id.fabDelete);
        FloatingActionButton fSave = (FloatingActionButton) getActivity().findViewById(R.id.fabSave);

        //deleteDefaluts("position",getActivity());
        //*********edit**************//
        String postionDB = getDefaults("position", getActivity());
        if ((postionDB != null) && !(postionDB.equals("")))//edit screen
        {
            Log.i("edit", "in edit loop: ");
            EditText strAddr = (EditText) calView.findViewById(R.id.inStreetAddress);
            EditText zip = (EditText) calView.findViewById(R.id.inZipcode);
            EditText propPrice = (EditText) calView.findViewById(R.id.inPropertyPrice);
            EditText downPay = (EditText) calView.findViewById(R.id.inDownPayment);
            EditText apr = (EditText) calView.findViewById(R.id.inAPR);
            EditText terms = (EditText) calView.findViewById(R.id.inTerms);
            EditText out = (EditText) calView.findViewById(R.id.output);

            String[] params = new String[]{postionDB};
            Cursor c = myMortgageCalculator.rawQuery("SELECT * FROM Mortgage WHERE id=?", params);
            if (c != null) {
                c.moveToFirst();
                final String propTypeDB = c.getString(c.getColumnIndex("propertyType"));
                final String streetAddDB = c.getString(c.getColumnIndex("streetAddress"));
                final String cityDB = c.getString(c.getColumnIndex("city"));
                String stateDB = c.getString(c.getColumnIndex("state"));
                String zipDB = c.getString(c.getColumnIndex("zipCode"));
                final String propPriceDB = c.getString(c.getColumnIndex("propertyPrice"));
                final String downPaymentDB = c.getString(c.getColumnIndex("downPayment"));
                final String arpDB = c.getString(c.getColumnIndex("apr"));
                String termDB = c.getString(c.getColumnIndex("term"));
                final String monthlyPaymentDB = c.getString(c.getColumnIndex("monthlyPayment"));
                String fullAddDB = c.getString(c.getColumnIndex("fulladdress"));

                //setting the values from db on edit
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.property_type, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spPropType.setAdapter(adapter);
                if (!propTypeDB.equals(null)) {
                    int spinnerPosition = adapter.getPosition(propTypeDB);
                    spPropType.setSelection(spinnerPosition);
                }
                strAddr.setText(streetAddDB);
                autoCities.setText(cityDB);
                ArrayAdapter<CharSequence> adapterState = ArrayAdapter.createFromResource(getActivity(), R.array.us_states, android.R.layout.simple_spinner_item);
                adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spState.setAdapter(adapterState);
                if (!stateDB.equals(null)) {
                    int spinnerPosition = adapterState.getPosition(stateDB);
                    spState.setSelection(spinnerPosition);
                }
                zip.setText(zipDB);
                propPrice.setText(propPriceDB);
                downPay.setText(downPaymentDB);
                apr.setText(arpDB);
                terms.setText(termDB);
                //out.setText(monthlyPaymentDB);

                c.close();
            }


            //deleting the position so that it doesnt open the next time
            deleteDefaluts("postion", getActivity());
            setDefaults("EditDelete", postionDB, getContext());
            //*********edit************//

            if (fSave != null && fEdit != null && fDelete != null) {
                fEdit.setVisibility(View.VISIBLE);
                fDelete.setVisibility(View.VISIBLE);
                fSave.setVisibility(View.GONE);
            }
        } else {//default screen
            //**************Property Info****************//
            if (fSave != null && fEdit != null && fDelete != null) {
                fEdit.setVisibility(View.GONE);
                fDelete.setVisibility(View.GONE);
                //
                // .setVisibility(View.VISIBLE);
            }
            //assigning dropdown values to spinner
            ArrayAdapter<CharSequence> propType = ArrayAdapter.createFromResource(getActivity(), R.array.property_type, android.R.layout.simple_spinner_item);
            propType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPropType.setAdapter(propType);
            //assigning values to autocomplete for cities
            String[] citiesList = getResources().getStringArray(R.array.us_cities);
            ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, citiesList);
            autoCities.setAdapter(cityAdapter);
            //assigning dropdown values to spinner
            ArrayAdapter<CharSequence> state = ArrayAdapter.createFromResource(getActivity(), R.array.us_states, android.R.layout.simple_spinner_item);
            state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spState.setAdapter(state);

        }

        //making the layout invisible
        LinearLayout propInfoLayout = (LinearLayout) calView.findViewById(R.id.innerPropInfoLayout);
        propInfoLayout.setVisibility(View.GONE);
        //*******************************Loan Info******************//
        LinearLayout loanInfoLayout = (LinearLayout) calView.findViewById(R.id.innerLoanInfoLayout);
        //loanInfoLayout.setVisibility(View.GONE);
        LinearLayout calLayout = (LinearLayout)calView.findViewById(R.id.calculate);
        if (calLayout != null) {
            EditText out = (EditText) calView.findViewById(R.id.output);
            if (out != null) {
                if (!(out.getText().toString().equals(""))) {
                    calLayout.setVisibility(View.VISIBLE);
                } else {
                    calLayout.setVisibility(View.GONE);
                }
            }
        }
        return calView;
    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static void deleteDefaluts(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        String postionDB = getDefaults("position", context);
        if ((postionDB != null) && !(postionDB.equals("")))//edit screen
        {
            Log.e("Error", "Deleting Shared Preferences");
        } else {
            Log.i("Success", "Deleting Shared Preferences ");
        }
    }
}

