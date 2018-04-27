package com.test.yanxiu.im_ui.contacts;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.test.yanxiu.im_ui.R;
import com.test.yanxiu.im_ui.contacts.view.ContactsFragment;

public class ContactsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constacts);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ContactsFragment contactsFragment = new ContactsFragment();
        transaction.add(R.id.fragment_content, contactsFragment);
        transaction.commit();


    }
}
