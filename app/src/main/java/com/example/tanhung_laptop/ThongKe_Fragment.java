package com.example.tanhung_laptop;

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
import com.example.tanhung_laptop.User.BatDau_activity;

import java.util.ArrayList;


public class ThongKe_Fragment extends Fragment {
    View view;
    GridView gridView_SanPham;
    TextView soluongtonkho;
    ArrayList<LAPTOP> laptopArrayList;
    LaptopAdminAdapter adapter;
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
        soluongtonkho = view.findViewById(R.id.soluongtonkho);
        gridView_SanPham = (GridView) view.findViewById(R.id.gridviewQLSanPham);
        laptopArrayList = new ArrayList<>();
        adapter = new LaptopAdminAdapter(ThongKe_Fragment.this, R.layout.product_sanpham_admin, laptopArrayList);
        gridView_SanPham.setAdapter(adapter);
        gridView_SanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ThongKe_Fragment.class);


                intent.putExtra("id",i);

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
        Cursor cursor1 = BatDau_activity.database.GetData("SELECT SUM ( SOLUONG ) FROM LAPTOP ");
        cursor1.moveToNext();
        soluongtonkho.setText(String.valueOf(cursor1.getInt(0) + " Sản phẩm "));


        Cursor cursor = BatDau_activity.database.GetData("SELECT * FROM LAPTOP WHERE SOLUONG < 50 ");
        laptopArrayList.clear();
        while (cursor.moveToNext())
        {
            laptopArrayList.add(new LAPTOP(
                    cursor.getInt(0),
                    cursor.getBlob(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getInt(7)
            ));
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_content, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.menu_delete_item:
                LAPTOP laptop = LaptopAdminAdapter.laptopList.get(info.position);
                BatDau_activity.database.DELETE_SANPHAM(
                        laptop.getIDLT()
                );

                Toast.makeText(getActivity(),"Xóa thành công",Toast.LENGTH_LONG).show();
                GetData();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}