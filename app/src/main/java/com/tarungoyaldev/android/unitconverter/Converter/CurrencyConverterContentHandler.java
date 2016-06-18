package com.tarungoyaldev.android.unitconverter.Converter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tarungoyaldev.android.unitconverter.UtilitiesSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Handles currency conversion content. Currency conversion rates are obtained from
 * http://fixer.io
 */
public class CurrencyConverterContentHandler implements ConverterContentHandler {

    private final String title;
    private final Activity activity;
    private final ConversionFragment conversionFragment;
    private final HashMap<String, Double> nameValueMap = new HashMap<>(0);
    private final String url = "http://api.fixer.io/latest";
    private String[] nameValues;

    public CurrencyConverterContentHandler(
            String title, final Activity activity, final ConversionFragment conversionFragment) {
        this.title = title;
        this.activity = activity;
        this.conversionFragment = conversionFragment;
        nameValueMap.put("EUR", 1.0);
        nameValues = new String[1];
        nameValues[0] = "EUR";
        final ProgressDialog progressDialog = new ProgressDialog(activity, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading data!!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String base = response.getString("base");
                            nameValueMap.clear();
                            nameValueMap.put(base, 1.0);
                            JSONObject rates = response.getJSONObject("rates");
                            Iterator<String> rateKeys = rates.keys();
                            while (rateKeys.hasNext()) {
                                String rate = rateKeys.next();
                                nameValueMap.put(rate, rates.getDouble(rate));
                            }
                            nameValues = nameValueMap.keySet().toArray(new String[0]);
                            Arrays.sort(nameValues);
                            System.out.println("Update spinner called");
                            System.out.println(nameValueMap.toString());
                            conversionFragment.updateSpinners();
                        } catch (JSONException e) {
                            nameValueMap.clear();
                            nameValueMap.put("EUR", 1.0);
                            Log.e("CurrencyConverter", e.toString());
                            Toast.makeText(activity,
                                    "Unable to fetch data. Please try again after some time",
                                    Toast.LENGTH_LONG);
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, "Device not online!!!", Toast.LENGTH_LONG).show();
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        UtilitiesSingleton.getUtilitiesSingleton(activity.getApplicationContext())
                .addToRequestQueue(jsonObjectRequest);
        System.out.println(nameValueMap.toString());
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String[] getNamesArray() {
        return nameValues;
    }

    public double getUnitValue(String name) {
        return nameValueMap.get(name);
    }

    public double getUnitValueAt(int pos) {
        return nameValueMap.get(nameValues[pos]);
    }

    @Override
    public double conversionResult(double value, int firstPos, int secondPos) {
        return value * getUnitValueAt(secondPos) / getUnitValueAt(firstPos);
    }
}
