package com.yanxiu.gphone.faceshowadmin_android.interf;

import android.content.Intent;
import android.view.View;

/**
 * listener for recyclerView`s Adapter item click
 * Created by frc on 17-10-27.
 */

public interface RecyclerViewItemClickWithIntentListener {
    void onItemClick(View v, int position, Intent intent);
}
