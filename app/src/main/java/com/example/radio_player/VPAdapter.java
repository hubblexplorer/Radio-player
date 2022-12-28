package com.example.radio_player;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

//Esta classe trata de manipular os fragmentos contituintes do programa
public class VPAdapter extends FragmentStateAdapter {


    public VPAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @NonNull
    @Override
    //Esta função trata de criar os vários fragmentos quando o programa corre
    public Fragment createFragment(int position) {
       switch (position){
           case 0:
               return new fragment1();
           case 1:
               return new fragment2();
       }
       return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
