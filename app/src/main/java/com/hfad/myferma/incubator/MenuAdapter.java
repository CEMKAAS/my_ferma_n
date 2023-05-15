package com.hfad.myferma.incubator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MenuAdapter  extends FragmentStateAdapter {
    public MenuAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeIncubatorFragment();
            case 1:
                return new ArchiveIncubatorFragment();
            default:
                return new HomeIncubatorFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
