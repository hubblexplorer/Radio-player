package com.example.radio_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TabLayout bottomAppBar;
    private ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomAppBar = findViewById(R.id.bottomAPPbar);
        viewPager2 = findViewById(R.id.viewpager);


        VPAdapter adapter = new VPAdapter(this);
        viewPager2.setAdapter(adapter);


        new TabLayoutMediator(bottomAppBar, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String str = "str_tab" + (position + 1);
                String packageName = getPackageName();
                int i = getResources().getIdentifier(str, "string", packageName);
                tab.setText(getResources().getString(i));
                str = "icon_tab" + (position + 1);
                i = getResources().getIdentifier(str, "drawable", packageName);
                tab.setIcon(getResources().getDrawable(i,getTheme()));

            }
        }).attach();

    }
}