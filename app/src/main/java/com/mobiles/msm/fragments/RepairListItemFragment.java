package com.mobiles.msm.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobiles.msm.R;
import com.mobiles.msm.activities.NavigationMenuActivity;
import com.mobiles.msm.application.Client;
import com.mobiles.msm.application.MyApplication;
import com.mobiles.msm.application.Myenum;
import com.mobiles.msm.pojos.models.ServiceCenterEntity;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RepairListItemFragment extends Fragment {


    public static String TAG = "serviceItem";
    private TextView brand, modelNo, status;
    EditText price, other, jobNo, problem, resolution;
    Button submit;
    String stringStatus;
    int index;
    int id;
    ServiceCenterEntity service;


    public static RepairListItemFragment newInstance() {
        RepairListItemFragment fragment = new RepairListItemFragment();
        return fragment;
    }

    public RepairListItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        MyApplication.SCRREN = "RepairListItemFragment";
        service = Myenum.INSTANCE.getServiceCenterEntity();
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_repair_list_item, container, false);
        initView(v);


        try {
            problem.setText(service.getProblem());
            brand.setText(service.getBrand().replace("\\n", ""));
            modelNo.setText(service.getModel());
            price.setText("" + service.getPrice());
            jobNo.setText(service.getJobNo());
            if (service.getResolution() != null && !service.getResolution().equalsIgnoreCase("null"))
                resolution.setText(service.getResolution());
        } catch (NullPointerException e) {
            this.onDestroy();
        }
        jobNo.setEnabled(false);


        status.setText(service.getStatus());
        stringStatus = service.getStatus();
        setIndex(service.getStatus());


        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final List<String> statusOfParts = Arrays.asList(getResources().getStringArray(R.array.items));
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final ArrayAdapter<String> aa1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, statusOfParts);
                builder.setSingleChoiceItems(aa1, index, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        dialog.dismiss();
                        String stringStatus = statusOfParts.get(item);
                        if (stringStatus.equalsIgnoreCase("Returned")) {
                            price.setVisibility(View.GONE);
                            price.setText("0");
                        } else {
                            price.setVisibility(View.VISIBLE);
                        }
                        status.setText(stringStatus);
                        setIndex(stringStatus);
                    }

                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (price.getText().length() == 0)
                    service.setPrice(0);
                else
                    service.setPrice(Integer.parseInt(price.getText().toString()));
                service.setStatus(stringStatus);
                service.setProblem(problem.getText().toString());
                service.setDeliveryDate("");
                service.setUsername(MyApplication.Username);
                if (resolution.getText().length() > 0)
                    service.setResolution(resolution.getText().toString());
                else service.setResolution("");

                SendData();
            }
        });


        return v;
    }

    private void initView(ViewGroup v) {
        brand = (TextView) v.findViewById(R.id.brandtext);
        status = (TextView) v.findViewById(R.id.status);
        modelNo = (TextView) v.findViewById(R.id.modeltext);
        price = (EditText) v.findViewById(R.id.priceEdit);
        other = (EditText) v.findViewById(R.id.otheredit);
        jobNo = (EditText) v.findViewById(R.id.jobnoedit);
        problem = (EditText) v.findViewById(R.id.problemedit);
        submit = (Button) v.findViewById(R.id.submit);
        resolution = (EditText) v.findViewById(R.id.resolutionEdit);

    }

    private int setIndex(String status) {
        if (status.equalsIgnoreCase("Pending")) {
            index = 0;
        } else if (status.equalsIgnoreCase("Processing")) {
            index = 1;
        } else if (status.equalsIgnoreCase("Pna")) {

            index = 2;
        } else if (status.equalsIgnoreCase("Done")) {
            index = 3;
        } else if (status.equalsIgnoreCase("Return")) {
            index = 4;
        } else if (status.equalsIgnoreCase("Delivered")) {
            index = 5;
        } else if (status.equalsIgnoreCase("Returned")) {
            index = 6;
        }
        return index;

    }

    private void SendData() {

        final ProgressDialog dialog;
        dialog = NavigationMenuActivity.materialDialog;
        dialog.show();


        Client.INSTANCE.updateService(MyApplication.AUTH, service).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                MyApplication.toast(getActivity(), "success");
                int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < backStackEntryCount; i++) {
                    getFragmentManager().popBackStack();
                }
                Fragment fragment = RequestRepair.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                MyApplication.toast(getActivity(), t.getMessage());

            }
        });


    }

}
