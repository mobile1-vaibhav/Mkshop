package com.mobiles.mkshop.fragments;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mobiles.mkshop.R;
import com.mobiles.mkshop.adapters.CustomAdapter;
import com.mobiles.mkshop.application.Client;
import com.mobiles.mkshop.application.MkShop;
import com.mobiles.mkshop.pojos.BrandModelList;
import com.mobiles.mkshop.pojos.ProductType;
import com.mobiles.mkshop.pojos.Sales;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.mobiles.mkshop.application.MkShop.toast;


public class SaleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    public static String TAG = "Sales";

    MaterialDialog materialDialog;
    private RadioGroup radiogroup;
    private TextView brand, accessoryType, modelNo, imeitextview;
    EditText quantity, price, other, customerName, imei, mobile;
    Button submit;
    List<BrandModelList> salesListx, modelSalesList, productTypeList;
    List<BrandModelList> salesList;
    List<String> brandList, modelList, accessoryTypeList;


    String stringBrand = null, stringProductType = ProductType.Mobile.name(), stringAccessory = null, stringModel = null;


    public static SaleFragment newInstance(String brand, String model) {
        SaleFragment fragment = new SaleFragment();
        Bundle args = new Bundle();
        args.putString("brand", brand);
        args.putString("model", model);
        fragment.setArguments(args);

        return fragment;
    }

    public SaleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stringBrand = getArguments().getString("brand");
            stringModel = getArguments().getString("model");
        }
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MkShop.SCRREN = "SaleFragment";
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_sale, container, false);

        materialDialog = new MaterialDialog.Builder(getActivity())
                .progress(true, 0)
                .cancelable(false)
                .build();
        radiogroup = (RadioGroup) v.findViewById(R.id.radiogroup);
        brand = (TextView) v.findViewById(R.id.brandtext);
        accessoryType = (TextView) v.findViewById(R.id.accessoryType);
        modelNo = (TextView) v.findViewById(R.id.modeltext);
        quantity = (EditText) v.findViewById(R.id.quantityEdit);
        price = (EditText) v.findViewById(R.id.priceEdit);
        other = (EditText) v.findViewById(R.id.otheredit);
        submit = (Button) v.findViewById(R.id.submit);
        imeitextview = (TextView) v.findViewById(R.id.imeitextview);
        customerName = (EditText) v.findViewById(R.id.customerName);
        mobile = (EditText) v.findViewById(R.id.mobile);
        imei = (EditText) v.findViewById(R.id.imei);


        if (getArguments() != null) {
            brand.setText(stringBrand);
            modelNo.setText(stringModel);
        }

        brandList = new ArrayList<>();
        productTypeList = new ArrayList<>();
        accessoryTypeList = new ArrayList<>();
        modelSalesList = new ArrayList<>();
        modelList = new ArrayList<>();


                salesList = BrandModelList.listAll(BrandModelList.class);

                try {
                    productTypeList = Lists.newArrayList(Iterables.filter(salesList, new Predicate<BrandModelList>() {
                        @Override
                        public boolean apply(BrandModelList input) {
                            return (input.getType().equalsIgnoreCase(stringProductType));
                        }
                    }));
                }
                catch (Exception e)
                {
                    Log.e("Err",e.getMessage());
                }


                Set<String> brands = new HashSet();
                for (int i = 0; i < productTypeList.size(); i++) {
                    brands.add(productTypeList.get(i).getBrand());
                }
                brandList.addAll(brands);



        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                final List<String> brandList = new ArrayList();
//                brandList.add("sony");
//                brandList.add("micromax");
//                brandList.add("lenovo");
//                brandList.add("htc");


                final Dialog view = new Dialog(getActivity(), android.R.style.Theme_Holo_Light_NoActionBar);
                view.setContentView(R.layout.dialog_layout);

                TextView title = (TextView) view.findViewById(R.id.dialogtitle);
                title.setText("Brand");
                ListView listView = (ListView) view.findViewById(R.id.dialoglist);
                CustomAdapter customAdapter = new CustomAdapter(getActivity(), brandList);
                listView.setAdapter(customAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {
                        stringBrand = brandList.get(position);
                        view.dismiss();
                        brand.setText(stringBrand);
                        modelSalesList = Lists.newArrayList(Iterables.filter(productTypeList, new Predicate<BrandModelList>() {
                            @Override
                            public boolean apply(BrandModelList input) {

                                if (stringAccessory == null)
                                    return (input.getBrand().equalsIgnoreCase(stringBrand));
                                else
                                    return (input.getBrand().equalsIgnoreCase(stringBrand) && input.getAccessoryType().equalsIgnoreCase(stringAccessory));


                            }
                        }));

                        modelList = new ArrayList<String>();
                        for (int i = 0; i < modelSalesList.size(); i++) {
                            modelList.add(modelSalesList.get(i).getModelNo());
                        }
                        modelList.add("other");

                    }
                });

                view.show();
            }
        });


        accessoryType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                final List<String> accessoriesList = new ArrayList();
//
//                accessoriesList.add("Earphone");
//                accessoriesList.add("cover");
//                accessoriesList.add("screenguard");
//                accessoriesList.add("battery");

                final Dialog view = new Dialog(getActivity(), android.R.style.Theme_Holo_Light_NoActionBar);
                view.setContentView(R.layout.dialog_layout);

                TextView title = (TextView) view.findViewById(R.id.dialogtitle);
                title.setText("Accessory ");
                ListView listView = (ListView) view.findViewById(R.id.dialoglist);
                CustomAdapter customAdapter = new CustomAdapter(getActivity(), accessoryTypeList);
                listView.setAdapter(customAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {
                        stringAccessory = accessoryTypeList.get(position);
                        view.dismiss();
                        accessoryType.setText(stringAccessory);

                        List<BrandModelList> newArrayList = Lists.newArrayList(Iterables.filter(productTypeList, new Predicate<BrandModelList>() {
                            @Override
                            public boolean apply(BrandModelList input) {
                                return (input.getType().equalsIgnoreCase(stringProductType) && input.getAccessoryType().equalsIgnoreCase(stringAccessory));
                            }
                        }));


                        Set<String> brand = new HashSet();
                        for (int i = 0; i < newArrayList.size(); i++) {
                            brand.add(newArrayList.get(i).getBrand());
                        }
                        brandList.addAll(brand);


                    }
                });

                view.show();
            }
        });

        modelNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringBrand == null) {
                    toast(getActivity(), "please select brand");
                } else {
//                    final List<String> modellist = new ArrayList();
//                    modellist.add("Galaxy y");
//                    modellist.add("galaxy s");
//                    modellist.add("galaxy s2");
//                    modellist.add("galaxy s3");
//                    modellist.add("other");

                    final Dialog view = new Dialog(getActivity(), android.R.style.Theme_Holo_Light_NoActionBar);
                    view.setContentView(R.layout.dialog_layout);

                    TextView title = (TextView) view.findViewById(R.id.dialogtitle);
                    title.setText("Model no");
                    ListView listView = (ListView) view.findViewById(R.id.dialoglist);
                    CustomAdapter customAdapter = new CustomAdapter(getActivity(), modelList);
                    listView.setAdapter(customAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {
                            stringModel = modelList.get(position);
                            view.dismiss();
                            if (stringModel.equalsIgnoreCase("other")) {
                                other.setVisibility(View.VISIBLE);
                            } else {
                                other.setVisibility(View.GONE);
                                other.getText().clear();
                            }


                            modelNo.setText(stringModel);
                        }
                    });

                    view.show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringProductType.equalsIgnoreCase(ProductType.Accessory.name()) && stringAccessory == null) {
                    toast(getActivity(), "please select accessory type");

                } else if (stringBrand == null) {
                    toast(getActivity(), "please select brand");

                } else if (modelNo.getText().length() == 0 || modelNo.getText().toString().equalsIgnoreCase("other") && other.getText().toString().length() == 0) {
                    toast(getActivity(), "please select model");

                } else if (price.getText().length() <= 0) {
                    toast(getActivity(), "please enter price");

                } else if (customerName.getText().length() <= 0) {
                    toast(getActivity(), "please enter customer name");

                } else if (mobile.getText().length() != 10) {
                    toast(getActivity(), "mobile no should be 10 digit");

                } else if (stringProductType.equalsIgnoreCase(ProductType.Mobile.name()) && imei.getText().length() == 0) {
                    toast(getActivity(), "please enter imei");

                } else {
                    if (stringModel.equalsIgnoreCase("other")) {
                        stringModel = other.getText().toString();
                    }


                    new SendData().execute();


                }
            }
        });


        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch (checkedId) {
                    case R.id.radiomobile:
                        accessoryType.setVisibility(View.GONE);
                        stringProductType = ProductType.Mobile.name();
                        accessoryType.setText("");
                        brand.setText("");
                        modelNo.setText("");
                        other.setText("");
                        quantity.getText().clear();
                        price.getText().clear();
                        other.setVisibility(View.GONE);
                        stringModel = null;
                        stringAccessory = null;
                        stringBrand = null;
                        imeitextview.setVisibility(View.VISIBLE);
                        imei.setVisibility(View.VISIBLE);


                        modelSalesList.clear();
                        productTypeList.clear();
                        modelList.clear();
                        accessoryTypeList.clear();
                        brandList.clear();
                        productTypeList = Lists.newArrayList(Iterables.filter(salesList, new Predicate<BrandModelList>() {
                            @Override
                            public boolean apply(BrandModelList input) {
                                return (input.getType().equalsIgnoreCase(stringProductType));
                            }
                        }));

                        Set<String> brandStrings = new HashSet();
                        for (int i = 0; i < productTypeList.size(); i++) {
                            brandStrings.add(productTypeList.get(i).getBrand());
                        }
                        brandList.addAll(brandStrings);


                        break;
                    case R.id.radioAccessory:
                        accessoryType.setVisibility(View.VISIBLE);
                        stringProductType = ProductType.Accessory.name();
                        accessoryType.setText("");
                        brand.setText("");
                        modelNo.setText("");
                        other.setText("");
                        quantity.getText().clear();
                        price.getText().clear();
                        other.setVisibility(View.GONE);
                        stringModel = null;
                        stringAccessory = null;
                        stringBrand = null;
                        imeitextview.setVisibility(View.GONE);
                        imei.setVisibility(View.GONE);


                        modelSalesList.clear();
                        productTypeList.clear();
                        modelList.clear();
                        accessoryTypeList.clear();
                        brandList.clear();
                        productTypeList = Lists.newArrayList(Iterables.filter(salesList, new Predicate<BrandModelList>() {
                            @Override
                            public boolean apply(BrandModelList input) {
                                return (input.getType().equalsIgnoreCase(stringProductType));
                            }
                        }));

                        Set<String> accessoryStrings = new HashSet();
                        for (int i = 0; i < productTypeList.size(); i++) {
                            accessoryStrings.add(productTypeList.get(i).getAccessoryType());
                        }
                        accessoryTypeList.addAll(accessoryStrings);

                }


            }
        });


        return v;
    }


    private class SendData extends AsyncTask<Void, Void, Void> {


        Sales sales;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            materialDialog.show();



            sales = new Sales();
            sales.setType(stringProductType);
            sales.setBrand(stringBrand);
            sales.setModel(stringModel);
            if (stringAccessory == null)
                sales.setAccessoryType("");
            else
                sales.setAccessoryType(stringAccessory);
            sales.setQuantity("1");
            sales.setPrice(price.getText().toString());
            sales.setUsername(MkShop.Username);
            sales.setCustomerName(customerName.getText().toString());
            sales.setMobile(mobile.getText().toString());
            if (imei.getText().length() == 0)
                sales.setImei("");
            else
                sales.setImei(imei.getText().toString());
        }


        @Override
        protected Void doInBackground(Void... params) {




            Client.INSTANCE.sales(MkShop.AUTH, sales, new Callback<String>() {
                @Override
                public void success(String response, Response response2) {

                    MkShop.toast(getActivity(), response);
                    if(materialDialog !=null &&materialDialog.isShowing())
                        materialDialog.dismiss();




                }

                @Override
                public void failure(RetrofitError error) {

                    if(materialDialog !=null &&materialDialog.isShowing())
                        materialDialog.dismiss();


                    if (error.getKind().equals(RetrofitError.Kind.NETWORK))
                        MkShop.toast(getActivity(), "check your internet connection");
                    else
                        MkShop.toast(getActivity(), error.getMessage());

                }
            });

            return null;
        }
    }
}
