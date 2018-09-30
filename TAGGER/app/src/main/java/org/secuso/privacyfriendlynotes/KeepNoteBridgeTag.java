package org.secuso.privacyfriendlynotes;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


public class KeepNoteBridgeTag extends AppCompatActivity {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments

    TakePhotoTagFragment takePhotoTagFragment;
    AddNoteTagFragment addNoteTagFragment;
    RecorderTagFragment recorderTagFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keepnotebridgetag);
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position,false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);


    }




    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        addNoteTagFragment =new AddNoteTagFragment();
        takePhotoTagFragment =new TakePhotoTagFragment();
        recorderTagFragment =new RecorderTagFragment();


        adapter.addFragment(addNoteTagFragment,"文字標記");
        adapter.addFragment(takePhotoTagFragment,"辨識標記");
        adapter.addFragment(recorderTagFragment,"語音標記");

        viewPager.setAdapter(adapter);
    }

}