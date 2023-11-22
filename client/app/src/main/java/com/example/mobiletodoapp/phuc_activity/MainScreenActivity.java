package com.example.mobiletodoapp.phuc_activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.ViewPagerAdapter;
import com.example.mobiletodoapp.databinding.ActivityMainScreenBinding;
import com.example.mobiletodoapp.phuc_activity.Fragment.HomeFragment;
import com.example.mobiletodoapp.phuc_activity.Fragment.SettingFragment;
import com.example.mobiletodoapp.trung_activity.CalendarUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDate;

public class MainScreenActivity extends AppCompatActivity {
    ActivityMainScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CalendarUtils.selectedDate = LocalDate.now();
//        }
//        HomeFragment homeFragment = new HomeFragment();
//        SettingFragment settingFragment = new SettingFragment();
//
//        ViewPager2 viewPager2 = binding.viewPager;
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
//        viewPager2.setAdapter(viewPagerAdapter);
//        viewPager2.setUserInputEnabled(false);
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//
//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                switch (position){
//                    case 0:
//                        bottomNavigationView.setSelectedItemId(R.id.menu_home);
//                        break;
//                    case 1:
//                        bottomNavigationView.setSelectedItemId(R.id.menu_fab);
//                        break;
//                    case 2:
//                        bottomNavigationView.setSelectedItemId(R.id.menu_setting);
//                        break;
//                }
//            }
//        });
//
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if(item.getItemId() == R.id.menu_home){
//                    viewPager2.setCurrentItem(0);
//                }
//                if(item.getItemId() == R.id.menu_setting){
//                    viewPager2.setCurrentItem(2);
//                }
//                return true;
//            }
//        });
    }


}