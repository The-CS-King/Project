package com.example.santosh.starting;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    // Viewpager variable
    private ViewPager myPage;
    // Layout in xml for first slide,second,third etc
    private int []layouts={R.layout.first_slide,R.layout.second_slide,R.layout.third_slide};

    //creating refernce of pagerAdapter class made by us as MpagerAdapter
   private MpagerAdapter mpagerAdapter;

    // to count the dots in linear layout
    private LinearLayout dots_Layout;

    // dot as image
    private ImageView[] dots;

    //two button on screen as skip and start
    private Button bnNext,bnSkip;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //checking whether app have run previously or not
        if(new PreferenceManager(this).checkPreference())
        {
            loadHome();
        }

//show transparent top of the app

        if(Build.VERSION.SDK_INT>=19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        setContentView(R.layout.activity_welcome);
        myPage=(ViewPager)findViewById(R.id.viewPager);
        mpagerAdapter=new MpagerAdapter(layouts,this);
        myPage.setAdapter(mpagerAdapter);
        dots_Layout=(LinearLayout)findViewById(R.id.dotsLayout);
        bnNext=(Button)findViewById(R.id.bnnext);
        bnSkip=(Button)findViewById(R.id.bnskip);
        bnSkip.setOnClickListener(this);
        bnNext.setOnClickListener(this);
        createDots(0);
        myPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override

            //to change next to start and hide skip when on last slide
            public void onPageSelected(int position) {
                createDots(position);
                if(position==layouts.length-1)
                {
                    bnNext.setText("Start");
                    bnSkip.setVisibility(View.INVISIBLE);
                }
                else
                {
                    bnNext.setText("Next");
                    bnSkip.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void createDots(int current_Possition)
    {
            if(dots_Layout!=null)
            dots_Layout.removeAllViews();


        dots=new ImageView[layouts.length];

        for(int i=0;i<layouts.length;i++)
        {
            dots[i]=new ImageView(this);
            if(i==current_Possition)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dot));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.inactive_dot));
            }
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            dots_Layout.addView(dots[i],params);


        }

    }

    @Override
    //definition for button click on skip and next
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bnnext:
                    loadNextSlide();
                    break;
            case R.id.bnskip:
                    loadHome();
                    new PreferenceManager(this).writePreference();
                    break;
        }
    }

    // method for home page or main activity
    private  void loadHome()
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
//method to load next slide
    private void loadNextSlide()
    {
        int next_slide=myPage.getCurrentItem()+1;
        if(next_slide<layouts.length)
        {
            myPage.setCurrentItem(next_slide);
        }
        else
        {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }
}
