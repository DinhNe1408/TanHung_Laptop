package com.example.tanhung_laptop.Admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tanhung_laptop.Adapter.LaptopAdminAdapter;
import com.example.tanhung_laptop.Models.LAPTOP;
import com.example.tanhung_laptop.R;
import com.example.tanhung_laptop.Retrofit.API;
import com.example.tanhung_laptop.Retrofit.RetrofitClient;
import com.example.tanhung_laptop.Retrofit.Utils;
import com.example.tanhung_laptop.User.BatDau_activity;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ThongKe_Fragment extends Fragment {
    View view;
    GridView gridView_SanPham;
    TextView soluongtonkho;
    ArrayList<LAPTOP> laptopArrayList;
    LaptopAdminAdapter adapter;
    API api;
    CompositeDisposable compositeDisposable;

    public ThongKe_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doanh_thu_, container, false);
        compositeDisposable = new CompositeDisposable();
        api = RetrofitClient.getInstance(Utils.BASE_URL).create(API.class);

        soluongtonkho = view.findViewById(R.id.soluongtonkho);
        gridView_SanPham = (GridView) view.findViewById(R.id.gridviewQLSanPham);
        laptopArrayList = new ArrayList<>();
        adapter = new LaptopAdminAdapter(ThongKe_Fragment.this, R.layout.product_sanpham_admin, laptopArrayList);
        gridView_SanPham.setAdapter(adapter);
        gridView_SanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), SuaSanPham_Activity.class);


                intent.putExtra("id", i);

                startActivity(intent);

            }
        });
        registerForContextMenu(gridView_SanPham);

        GetData();
        return view;
    }

    @Override
    public void onStart() {
        GetData();
        super.onStart();
    }

    private void GetData() {

        compositeDisposable.add(api.laysllt()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        integerModel -> {
                            if (integerModel.isSuccess()) {
                                soluongtonkho.setText(integerModel.getResult() + " S???n ph???m ");
                            }
                        }, throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }));

        compositeDisposable.add(api.layltduoi50()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        laptopModel -> {
                            laptopArrayList.clear();
                            if (laptopModel.isSuccess()) {

                                for (int i= 0;i<laptopModel.getResult().size();i++){
                                    laptopArrayList.add(laptopModel.getResult().get(i));
                                }
                                Toast.makeText(getContext(), "Th??nh c??ng", Toast.LENGTH_LONG).show();
                            }
                            adapter.notifyDataSetChanged();
                        }, throwable -> {

                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }));
    }



    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}