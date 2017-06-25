package com.example.karth.mortgagecalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    private boolean isAddsValid = false;
    private SQLiteDatabase myMortgageCalculator;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
        FloatingActionButton fDelete = (FloatingActionButton) findViewById(R.id.fabDelete);
        FloatingActionButton fSave = (FloatingActionButton) findViewById(R.id.fabSave);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //this.deleteDatabase("MortgageCalculator");
        myMortgageCalculator = this.openOrCreateDatabase("MortgageCalculator", MODE_PRIVATE, null);
        myMortgageCalculator.execSQL("CREATE TABLE IF NOT EXISTS Mortgage (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, propertyType VARCHAR(11), streetAddress VARCHAR,city VARCHAR, state VARCHAR,zipCode INT(7), propertyPrice DOUBLE,downPayment DOUBLE, apr FLOAT,term DOUBLE, monthlyPayment INT, fulladdress VARCHAR) ");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //*********************Map**************//
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //*********save*****************//

        if (fSave != null && fEdit != null && fDelete != null) {


            fSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Spinner proprtyTypeDropdown = (Spinner) findViewById(R.id.ddPropertyType);
                    EditText inStreetAddr = (EditText) findViewById(R.id.inStreetAddress);
                    AutoCompleteTextView autoCities = (AutoCompleteTextView) findViewById(R.id.autocompleteCity);
                    Spinner spState = (Spinner) findViewById(R.id.ddState);
                    EditText inZipCode = (EditText) findViewById(R.id.inZipcode);
                    EditText inPropertyPrice = (EditText) findViewById(R.id.inPropertyPrice);
                    EditText inDownPayment = (EditText) findViewById(R.id.inDownPayment);
                    EditText inAPR = (EditText) findViewById(R.id.inAPR);
                    EditText inTerms = (EditText) findViewById(R.id.inTerms);
                    TextView output = (TextView) findViewById(R.id.output);
                    isAddsValid = false;

                    String buttonClickOperation = getFABButtonID(view.getId());
                    if (!(buttonClickOperation.equals(""))) {
                        if ((proprtyTypeDropdown != null) && (inStreetAddr != null) && (autoCities != null) && (spState != null) && (inZipCode != null) && (inPropertyPrice != null) && (inDownPayment != null) && (inAPR != null) && (inTerms != null) && (output != null)) {
                            if (proprtyTypeDropdown.getSelectedItemPosition() != -1) {
                                if (!(inStreetAddr.getText().toString().equals(""))) {
                                    if (!(autoCities.getText().toString().equals(""))) {
                                        if (spState.getSelectedItemPosition() != -1) {
                                            if (!(inZipCode.getText().toString().equals(""))) {
                                                if (!(inPropertyPrice.getText().toString().equals(""))) {
                                                    if (!(inDownPayment.getText().toString().equals(""))) {
                                                        if (!(inAPR.getText().toString().equals(""))) {
                                                            if (!(inTerms.getText().toString().equals(""))) {
                                                                if (!(output.getText().toString().equals(""))) {
                                                                    try {
                                                                        String Addr = inStreetAddr.getText().toString().toLowerCase() + " " +
                                                                                autoCities.getText().toString().toLowerCase() + " " +
                                                                                spState.getSelectedItem().toString().toLowerCase() + " " +
                                                                                inZipCode.getText().toString();

                                                                        //*****edit in db**********//
                                                                        String postionDB = getDefaults("EditDelete", getBaseContext());
                                                                        if (((postionDB == null) || (postionDB.equals(""))) && (buttonClickOperation.equals("Save"))) {//********save in db*****//
                                                                            String saveQuery = "INSERT INTO Mortgage " +
                                                                                    "(propertyType, " +
                                                                                    "streetAddress, " +
                                                                                    "city, " +
                                                                                    "state, " +
                                                                                    "zipCode, " +
                                                                                    "propertyPrice, " +
                                                                                    "downPayment, " +
                                                                                    "apr, " +
                                                                                    "term, " +
                                                                                    "monthlyPayment," +
                                                                                    "fulladdress)" +
                                                                                    "VALUES ('" +
                                                                                    proprtyTypeDropdown.getSelectedItem().toString() + "','" +
                                                                                    inStreetAddr.getText().toString() + "','" +
                                                                                    autoCities.getText().toString() + "','" +
                                                                                    spState.getSelectedItem().toString() + "'," +
                                                                                    Integer.parseInt(inZipCode.getText().toString()) + "," +
                                                                                    Double.parseDouble(inPropertyPrice.getText().toString()) + "," +
                                                                                    Double.parseDouble(inDownPayment.getText().toString()) + "," +
                                                                                    Float.parseFloat(inAPR.getText().toString()) + "," +
                                                                                    Double.parseDouble(inTerms.getText().toString()) + "," +
                                                                                    Integer.parseInt(output.getText().toString()) + ",'" +
                                                                                    Addr + "')";
                                                                            myMortgageCalculator.execSQL(saveQuery);

                                                                        }
                                                                        Snackbar.make(view, "Mortgage saved successfully!", Snackbar.LENGTH_LONG)
                                                                                .setAction("Action", null).show();
                                                                        //refreshing to sync the map
                                                                        finish();
                                                                        startActivity(getIntent());
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                } else {
                                                                    Snackbar.make(view, "Calculate mortgage to proceed with saving!", Snackbar.LENGTH_LONG)
                                                                            .setAction("Action", null).show();
                                                                }
                                                            } else {
                                                                Snackbar.make(view, "Terms field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                                        .setAction("Action", null).show();
                                                            }
                                                        } else {
                                                            Snackbar.make(view, "APR field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                                    .setAction("Action", null).show();
                                                        }
                                                    } else {
                                                        Snackbar.make(view, "Down Payment field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                                .setAction("Action", null).show();
                                                    }
                                                } else {
                                                    Snackbar.make(view, "Property Price field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                }

                                            } else {
                                                Snackbar.make(view, "ZipCode field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                            }
                                        } else {
                                            Snackbar.make(view, "State field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                    } else {
                                        Snackbar.make(view, "City field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                } else {
                                    Snackbar.make(view, "Street Address field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            } else {
                                Snackbar.make(view, "Property Type field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }
                }
            });
            fEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Spinner proprtyTypeDropdown = (Spinner) findViewById(R.id.ddPropertyType);
                    EditText inStreetAddr = (EditText) findViewById(R.id.inStreetAddress);
                    AutoCompleteTextView autoCities = (AutoCompleteTextView) findViewById(R.id.autocompleteCity);
                    Spinner spState = (Spinner) findViewById(R.id.ddState);
                    EditText inZipCode = (EditText) findViewById(R.id.inZipcode);
                    EditText inPropertyPrice = (EditText) findViewById(R.id.inPropertyPrice);
                    EditText inDownPayment = (EditText) findViewById(R.id.inDownPayment);
                    EditText inAPR = (EditText) findViewById(R.id.inAPR);
                    EditText inTerms = (EditText) findViewById(R.id.inTerms);
                    TextView output = (TextView) findViewById(R.id.output);
                    isAddsValid = false;

                    String buttonClickOperation = getFABButtonID(view.getId());
                    if (!(buttonClickOperation.equals(""))) {
                        if ((proprtyTypeDropdown != null) && (inStreetAddr != null) && (autoCities != null) && (spState != null) && (inZipCode != null) && (inPropertyPrice != null) && (inDownPayment != null) && (inAPR != null) && (inTerms != null) && (output != null)) {
                            if (proprtyTypeDropdown.getSelectedItemPosition() != -1) {
                                if (!(inStreetAddr.getText().toString().equals(""))) {
                                    if (!(autoCities.getText().toString().equals(""))) {
                                        if (spState.getSelectedItemPosition() != -1) {
                                            if (!(inZipCode.getText().toString().equals(""))) {
                                                if (!(inPropertyPrice.getText().toString().equals(""))) {
                                                    if (!(inDownPayment.getText().toString().equals(""))) {
                                                        if (!(inAPR.getText().toString().equals(""))) {
                                                            if (!(inTerms.getText().toString().equals(""))) {
                                                                if (!(output.getText().toString().equals(""))) {
                                                                    try {
                                                                        String Addr = inStreetAddr.getText().toString().toLowerCase() + " " +
                                                                                autoCities.getText().toString().toLowerCase() + " " +
                                                                                spState.getSelectedItem().toString().toLowerCase() + " " +
                                                                                inZipCode.getText().toString();

                                                                        //*****edit in db**********//
                                                                        String postionDB = getDefaults("EditDelete", getBaseContext());
                                                                        if ((postionDB != null) && (!(postionDB.equals(""))) && (buttonClickOperation.equals("Edit")))//edit screen
                                                                        {
                                                                            String updateQuery = "UPDATE Mortgage " +
                                                                                    "SET propertyType='" + proprtyTypeDropdown.getSelectedItem().toString() + "'," +
                                                                                    "streetAddress='" + inStreetAddr.getText().toString() + "'," +
                                                                                    "city='" + autoCities.getText().toString() + "'," +
                                                                                    "state='" + spState.getSelectedItem().toString() + "'," +
                                                                                    "zipCode=" + inZipCode.getText().toString() + "," +
                                                                                    "propertyPrice=" + inPropertyPrice.getText().toString() + "," +
                                                                                    "downPayment=" + inDownPayment.getText().toString() + "," +
                                                                                    "apr=" + inAPR.getText().toString() + "," +
                                                                                    "term=" + inTerms.getText().toString() + "," +
                                                                                    "monthlyPayment=" + output.getText().toString() + "," +
                                                                                    "fulladdress='" + Addr + "' " +
                                                                                    "WHERE id=" + postionDB;
                                                                            myMortgageCalculator.execSQL(updateQuery);
                                                                            deleteDefaluts("EditDelete", getBaseContext());
                                                                        }
                                                                        Snackbar.make(view, "Mortgage updated successfully!", Snackbar.LENGTH_LONG)
                                                                                .setAction("Action", null).show();
                                                                        //refreshing to sync the map
                                                                        finish();
                                                                        startActivity(getIntent());
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                } else {
                                                                    Snackbar.make(view, "Calculate mortgage to proceed with saving!", Snackbar.LENGTH_LONG)
                                                                            .setAction("Action", null).show();
                                                                }
                                                            } else {
                                                                Snackbar.make(view, "Terms field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                                        .setAction("Action", null).show();
                                                            }
                                                        } else {
                                                            Snackbar.make(view, "APR field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                                    .setAction("Action", null).show();
                                                        }
                                                    } else {
                                                        Snackbar.make(view, "Down Payment field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                                .setAction("Action", null).show();
                                                    }
                                                } else {
                                                    Snackbar.make(view, "Property Price field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                }

                                            } else {
                                                Snackbar.make(view, "ZipCode field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                            }
                                        } else {
                                            Snackbar.make(view, "State field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                    } else {
                                        Snackbar.make(view, "City field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                } else {
                                    Snackbar.make(view, "Street Address field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            } else {
                                Snackbar.make(view, "Property Type field is mandatory inorder to save!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }
                }
            });

            fDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Spinner proprtyTypeDropdown = (Spinner) findViewById(R.id.ddPropertyType);
                    EditText inStreetAddr = (EditText) findViewById(R.id.inStreetAddress);
                    AutoCompleteTextView autoCities = (AutoCompleteTextView) findViewById(R.id.autocompleteCity);
                    Spinner spState = (Spinner) findViewById(R.id.ddState);
                    EditText inZipCode = (EditText) findViewById(R.id.inZipcode);
                    EditText inPropertyPrice = (EditText) findViewById(R.id.inPropertyPrice);
                    EditText inDownPayment = (EditText) findViewById(R.id.inDownPayment);
                    EditText inAPR = (EditText) findViewById(R.id.inAPR);
                    EditText inTerms = (EditText) findViewById(R.id.inTerms);
                    TextView output = (TextView) findViewById(R.id.output);
                    isAddsValid = false;

                    String buttonClickOperation = getFABButtonID(view.getId());
                    if (!(buttonClickOperation.equals(""))) {
                        if ((proprtyTypeDropdown != null) && (inStreetAddr != null) && (autoCities != null) && (spState != null) && (inZipCode != null) && (inPropertyPrice != null) && (inDownPayment != null) && (inAPR != null) && (inTerms != null) && (output != null)) {

                            try {
                                //*****edit in db**********//
                                String postionDB = getDefaults("EditDelete", getBaseContext());
                                if ((postionDB != null) && (!(postionDB.equals(""))) && (buttonClickOperation.equals("Delete")))//delete
                                {
                                    String deleteQuery = "DELETE FROM Mortgage WHERE id=" + postionDB;
                                    myMortgageCalculator.execSQL(deleteQuery);
                                    deleteDefaluts("EditDelete", getBaseContext());
                                }
                                Snackbar.make(view, "Mortgage deleted! No related records!!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                //refreshing to sync the map
                                finish();
                                startActivity(getIntent());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });


        }
    }


    public String getFABButtonID(int id) {
        switch (id) {
            case R.id.fabSave:
                return "Save";
            case R.id.fabEdit:
                return "Edit";
            case R.id.fabDelete:
                return "Delete";
            default:
                return "";
        }
    }

    //toggles the property info fields on click of the layout
    public void toggle_propertyInfo(View v) {
        LinearLayout propInfoLayout = (LinearLayout) findViewById(R.id.innerPropInfoLayout);
        if (propInfoLayout != null) {
            propInfoLayout.setVisibility(propInfoLayout.isShown()
                    ? View.GONE
                    : View.VISIBLE);
        }
    }

    //toggles the loan info fields on click of the layout
    public void toggle_LoanInfo(View v) {
        LinearLayout loanInfoLayout = (LinearLayout) findViewById(R.id.innerLoanInfoLayout);
        if (loanInfoLayout != null) {
            loanInfoLayout.setVisibility(loanInfoLayout.isShown()
                    ? View.GONE
                    : View.VISIBLE);
        }
        LinearLayout calLayout = (LinearLayout) findViewById(R.id.calculate);
        if (calLayout != null) {
            EditText out = (EditText) findViewById(R.id.output);
            if (out != null) {
                if (!(out.getText().toString().equals(""))) {
                    calLayout.setVisibility(View.VISIBLE);
                } else {
                    calLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    public void btnCalOnClick(View v) {
        EditText inPropertyPrice = (EditText) findViewById(R.id.inPropertyPrice);
        EditText inDownPayment = (EditText) findViewById(R.id.inDownPayment);
        EditText inAPR = (EditText) findViewById(R.id.inAPR);
        EditText inTerms = (EditText) findViewById(R.id.inTerms);
        EditText output = (EditText) findViewById(R.id.output);
        if ((inPropertyPrice != null) && (inDownPayment != null) && (inAPR != null) && (inTerms != null) && (output != null)) {
            if (!(inPropertyPrice.getText().toString().equals(""))) {
                if (!(inDownPayment.getText().toString().equals(""))) {
                    if (!(inAPR.getText().toString().equals(""))) {
                        if (!(inTerms.getText().toString().equals(""))) {
                            float propertyPrice = Float.parseFloat(inPropertyPrice.getText().toString());
                            float downPayment = Float.parseFloat(inDownPayment.getText().toString());
                            float loanAmount = propertyPrice - downPayment;
                            float years = Float.parseFloat(inTerms.getText().toString());
                            float numPayments = years * 12;
                            float rateAnnual = Float.parseFloat(inAPR.getText().toString()) / 100;
                            float rateMonthly = rateAnnual / 12;
                            double dummyPower = Math.pow((1 + rateMonthly), numPayments);
                            double numerator = (rateMonthly * dummyPower) * loanAmount;
                            double denominator = dummyPower - 1;
                            double payment = numerator / denominator;
                            double dummyFloor = Math.floor(payment);
                            int outputMonthlyPayment = (int) dummyFloor;
                            output.setText(Integer.toString(outputMonthlyPayment));
                            Log.i("output", Integer.toString(outputMonthlyPayment));
                            LinearLayout calLayout = (LinearLayout) findViewById(R.id.calculate);
                            if (calLayout != null) {
                                if (!(output.getText().toString().equals(""))) {
                                    calLayout.setVisibility(View.VISIBLE);
                                } else {
                                    calLayout.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            Snackbar.make(v, "Terms field is mandatory!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } else {
                        Snackbar.make(v, "APR field is mandatory!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(v, "Down Payment field is mandatory!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } else {
                Snackbar.make(v, "Property Price field is mandatory!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            finish();
            startActivity(getIntent());
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    //commented as created new classes for each tab view


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        ViewPager mViewPager;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            /*// getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            return PlaceholderFragment.newInstance(position);*/
            switch (position) {
                case 0:
                    MCalculation cal = new MCalculation();
                    return cal;
                case 1:
                    Map map = new Map();
                    return map;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Calculation & Save";
                case 1:
                    return "Google Map";
                /*case 2:
                    return "SECTION 3";*/
            }
            return null;
        }
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
        editor.remove(key);
        editor.commit();
    }

}
