package it.porting.android_is.network;


import it.porting.android_is.firebaseArchive.bean.RequestBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitSingleton {
    private Network apiService;
    private static RetrofitSingleton instance = null;

    private RetrofitSingleton() {
        createConnection();
    }


    public static RetrofitSingleton getInstance() {
        if (instance == null) {
            instance = new RetrofitSingleton();
        }
        return instance;
    }


    /**
     * NEW RETROFIT API IMPLEMENTATION
     */

    public void createConnection() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://172.19.130.70:3000")
                .addConverterFactory(GsonConverterFactory.create()).build();
        apiService = retrofit.create(Network.class);

    }


    public void performCreatePDF(RequestBean requestBean, Callback<Void> userCallback) {
        Call<Void> response = apiService.createPDF(requestBean);
        response.enqueue(userCallback);
    }

    public void performCreateApprovedExcel(Callback<Void> userCallback){
        Call<Void> response = apiService.createApprovedExcel();
        response.enqueue(userCallback);
    }

    public void performCreateRefusedExcel(Callback<Void> userCallback){
        Call<Void> response = apiService.createRefusedExcel();
        response.enqueue(userCallback);
    }



}

