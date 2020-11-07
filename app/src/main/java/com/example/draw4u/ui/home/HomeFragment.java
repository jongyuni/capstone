package com.example.draw4u.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.draw4u.DiaryDayView;
import com.example.draw4u.R;

public class HomeFragment extends Fragment implements CalendarView.OnDateChangeListener {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.activity_cal_dia_view, container, false);
        final CalendarView calendarview = root.findViewById(R.id.calendarView);
        calendarview.setOnDateChangeListener(this);

        return root;
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        String sel_date = "";

        if(dayOfMonth < 10){
            sel_date = "" + year + "-" + (month + 1) + "" + "-0" + dayOfMonth;
        }else{
            sel_date = "" + year + "-" + (month + 1) + "" + "-" + dayOfMonth;
        }//일기 이름 설정. Ex) 2020-10-01, 2020-10-10

        Intent intent = new Intent(getActivity(), DiaryDayView.class);
        intent.putExtra("fname",sel_date);
        startActivity(intent);

    }
}