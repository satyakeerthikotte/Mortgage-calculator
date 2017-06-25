package com.example.karth.mortgagecalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker maker;
    Context context;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    /* * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
             */
    private GoogleApiClient client;
    private static SQLiteDatabase myMortgageCalculator;
    String errorMessage = "";
    Button button1, button2;
    private GoogleMap.OnInfoWindowClickListener infoButtonListener;
    Bundle b = new Bundle();

    private static ViewPager vPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mapView = inflater.inflate(R.layout.map, container, false);
        context = mapView.getContext();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(context).addApi(AppIndex.API).build();
        vPager = (ViewPager) mapView.findViewById(R.id.container);


        return mapView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        myMortgageCalculator = getActivity().openOrCreateDatabase("MortgageCalculator", getActivity().MODE_PRIVATE, null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final ArrayList<String> addressesfromDb = new ArrayList<String>();
        ArrayList<Integer> idFromDB = new ArrayList<>();
        Cursor c = myMortgageCalculator.rawQuery("SELECT * FROM Mortgage", null);
        if (c != null && c.moveToFirst()) {
            do {
                String unID = c.getString(c.getColumnIndex("id"));
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

                int uID = Integer.parseInt(unID);
                idFromDB.add(uID);

                String wholeAddDB = streetAddDB.toLowerCase() + " " + cityDB.toLowerCase() + " " + stateDB.toLowerCase() + " " + zipDB;
                addressesfromDb.add(wholeAddDB);


            } while (c.moveToNext());

            c.close();
        }

        int counter = 0;
        for (int i = 0; i < addressesfromDb.size(); i++) {
            String loc = addressesfromDb.get(i);

            List<Address> addressList = null;
            Geocoder geocoder = new Geocoder(context);
            try {
                addressList = geocoder.getFromLocationName(loc, 1);
                if (addressList.size() > 0) {
                    android.location.Address address = addressList.get(0);
                    String locality = address.getLocality();
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    setMarkerCustom(locality, latLng);
                    //counter++;
                    int uIDfromList = idFromDB.get(i);
                    mHashMap.put(maker, uIDfromList);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            View v = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);
                            TextView hiddenTV = (TextView) v.findViewById(R.id.hiddenVar);
                            TextView propType = (TextView) v.findViewById(R.id.PropertyType);
                            TextView streetAddress = (TextView) v.findViewById(R.id.StreetAddress);
                            TextView city = (TextView) v.findViewById(R.id.City);
                            TextView loanAmount = (TextView) v.findViewById(R.id.LoanAmount);
                            TextView aPR = (TextView) v.findViewById(R.id.APR);
                            TextView monthlyPayment = (TextView) v.findViewById(R.id.MonthlyPayment);

                            int pos = mHashMap.get(marker);
                            String[] params = new String[]{Integer.toString(pos)};
                            Cursor c = myMortgageCalculator.rawQuery("SELECT * FROM Mortgage WHERE id=?", params);
                            //        Cursor c = myMortgageCalculator.rawQuery("SELECT * FROM Mortgage WHERE fulladdress=?", params);
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
                                propType.setText(propTypeDB);
                                streetAddress.setText(streetAddDB);
                                city.setText(cityDB);
                                long loanAmt = Long.parseLong(propPriceDB) - Long.parseLong(downPaymentDB);
                                loanAmount.setText(Long.toString(loanAmt));
                                aPR.setText(arpDB);
                                monthlyPayment.setText(monthlyPaymentDB);
                                c.close();
                            }
                            return v;
                        }
                    });
                    final ViewPager vp = (ViewPager) getActivity().findViewById(R.id.container);
                    //mMap.setOnInfoWindowClickListener(null);
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            View v = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);
                            int pos = mHashMap.get(marker);
                            Log.i("clicked marker: ", Integer.toString(pos));
                            String postion = Integer.toString(pos);
                            //to got to cal view
                            setDefaults("position", postion, getActivity());
                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
                            /*marker.hideInfoWindow();
                            Fragment calFrag = new MCalculation();
                            FragmentTransaction fragTras = getFragmentManager().beginTransaction();
                            fragTras.replace(R.id.map, calFrag);
                            //fragTras.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragTras.addToBackStack(null);
                            fragTras.commit();*/

                            /*
                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
                            setDefaults("position",postion,getActivity());
*/
                        }
                    });
                } else {
                    Log.e("Wrong Address", "Invalid address in DB");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.125212,-94.551136),3.0f));

    }

    private void setMarkerCustom(String locality, LatLng latLng) {
        MarkerOptions options = new MarkerOptions().position(latLng).title(locality).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        maker = mMap.addMarker(options);
    }


    public void ondelete(Marker marker) {
        LatLng lalt = marker.getPosition();
        marker.remove();
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
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



