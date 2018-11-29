package org.secuso.privacyfriendlynotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.secuso.privacyfriendlynotes.code_old.DbContract;
import org.secuso.privacyfriendlynotes.code_old.MainActivity;


public class KeepNoteBridge extends AppCompatActivity {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments

    TakePhotoFragment takePhotoFragment;
    AddNoteFragment addNoteFragment;
    RecorderFragment recorderFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keepnotebridge);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("著作權聲明");//设置标题
        builder.setIcon(R.mipmap.ic_launcher);//设置图标
        builder.setMessage("\n" +
                "一、著作權聲明\n" +
                "\n" +
                "1.智慧財產法院網站上刊載之所有內容，除著作權法規定不得為著作權之標的（如法律、命令、公務員撰擬之講稿、新聞稿等--請參考著作權法第9條規定）外，其他包括文字敘述、攝影、圖片、錄音、影像及其他資訊，均受著作權法保護。\n" +
                "\n" +
                "2.上述不得為著作權標的者，任何人均得自由利用，歡迎各界廣為利用。\n" +
                "\n" +
                "3.本院網站資訊內容受著作權法保護者，除有合理使用情形外，應取得該著作財產權人同意或授權後，方得利用。\n" +
                "\n" +
                "4.上述〝合理使用情形〞，說明如下：\n" +
                "\n" +
                "(1)本網站上所刊載以本院名義公開發表之著作，即著作人為本院者，在合理範圍內，得重製、公開播送或公開傳輸，利用時，並請註明出處。\n" +
                "\n" +
                "(2)本院網站上之資訊，可為個人或家庭非營利之目的而重製。\n" +
                "\n" +
                "(3)為報導、評論、教學、研究或其他正當目的，在合理範圍內，得引用本院網站上之資訊，引用時，並請註明出處。\n" +
                "\n" +
                "(4)其他合理使用情形，請參考著作權法第四十四條至第六十五條之規定。\n" +
                "\n" +
                "5.除了合於著作權法第八十條之一非移除或變更權利管理電子資訊， 否則無法合法利用著作；或者因為錄製或傳輸系統轉換時，技術上必須要移除或變更的情況之外，本院網站所標示之權利管理電子資訊，未經許可，不得移除或變更。\n" +
                "\n" +
                "二、連結至本院網站\n" +
                "\n" +
                "一般而言，任何網站連結至本院網站，毋須經過本院同意，然而連結須明白標示本院名稱，若是連結會誤導使用者，本院將不允許此連結行為。\n" +
                "\n" +
                "三、本院網站之相關連結\n" +
                "\n" +
                "為供網路使用者便利，本院網站僅提供相關網站之連結，對利用人涉及該網站內容之使用行為，本院不負責任。\n" +
                "\n" +
                "四、免責聲明\n" +
                "\n" +
                "如欲使用他人之筆記，請經過他人之同意，才得以分享、轉載。");//设置内容
        builder.setPositiveButton("我同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(KeepNoteBridge.this, "點了同意" , Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("我不同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent iii = new Intent(getApplication(), MainActivity.class);

                startActivity(iii);
                KeepNoteBridge.this.finish();
            }
        });

        //用creat()方法创建dialog, show()方法展示出来
        AlertDialog dialog = builder.create();
        dialog.show();

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);

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
        addNoteFragment=new AddNoteFragment();
        takePhotoFragment=new TakePhotoFragment();
        recorderFragment=new RecorderFragment();


        adapter.addFragment(addNoteFragment,"文字");
        adapter.addFragment(takePhotoFragment,"拍照");
        adapter.addFragment(recorderFragment,"錄音");
        viewPager.setAdapter(adapter);
    }

}