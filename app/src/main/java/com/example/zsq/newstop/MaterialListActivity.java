package com.example.zsq.newstop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.squareup.picasso.RequestCreator;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.SlideInLeftAnimationAdapter;

import static com.dexafree.materialList.listeners.RecyclerItemClickListener.*;

/**
 * Created by zsq on 16-8-27.
 */
public class MaterialListActivity extends AppCompatActivity {
    private MaterialListView materialListView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matreial_list);
        materialListView = (MaterialListView) findViewById(R.id.material_list);
        materialListView.setItemAnimator(new SlideInLeftAnimator());
        materialListView.getItemAnimator().setAddDuration(300);
        materialListView.getItemAnimator().setRemoveDuration(300);
        materialListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(@NonNull Card card, int position) {
                Toast.makeText(getApplicationContext(),"点击了这个card",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {

            }
        });
        Card card = new Card.Builder(this)
                .setTag("ImgaeButton")
                .setDismissible()
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_basic_image_buttons_card_layout)
                .setTitle("标题")
                .setTitleGravity(Gravity.END)
                .setDescription("描述")
                .setDescriptionGravity(Gravity.END)
                .setDrawable(R.drawable.xueshan_60)
                .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                    @Override
                    public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                        requestCreator.fit();
                    }
                })
                .endConfig()
                .build();
        materialListView.getAdapter().add(card);
        materialListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull Card card, int position) {

            }
        });
    }
}
