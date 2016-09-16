package com.example.zsq.newstop;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.squareup.picasso.RequestCreator;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by zsq on 16-8-29.
 */
public class CollectionFragment extends Fragment {
    private MaterialListView materialListView;
    private ArrayList<News> newses;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView selectIcon;
    private ImageView collectionImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newses=MyCollectionLB.get(getActivity()).getCollectionNewses();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_collection, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh_collection);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDate();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        collectionImageView = (ImageView) v.findViewById(R.id.collection_imageview);
        selectIcon = (ImageView) v.findViewById(R.id.image_select_icon);
        selectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
//                Uri uri = Uri.parse("geo:38.899533,77.036476");
//                Intent i=new Intent(Intent.ACTION_VIEW,uri);
//                startActivity(i);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 2);
            }
        });
        materialListView = (MaterialListView) v.findViewById(R.id.collecion_list);
        for(int i=0;i<newses.size();i++) {
            News news = newses.get(i);
            String title=new String();
            title += ("" + news.getTitle());
            if(title.length()>25){
                title = title.substring(0, 24);
                title += ".....";
            }
            Card card = new Card.Builder(getActivity())
                    .setTag(news.getUrl())
                    .setDismissible()
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.card_list_item)
                    .setDescription(title)
                    .setDescriptionGravity(Gravity.START)
                    .setDescriptionResourceColor(R.color.black_button)
                    .setDrawable(news.getPic_1())
                    .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                        @Override
                        public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                            requestCreator.fit();
                        }
                    })
                    .addAction(R.id.left_text, new TextViewAction(getActivity())
                            .setText(news.getDate())
                            .setTextResourceColor(R.color.gray))
                    .addAction(R.id.right_text, new TextViewAction(getActivity())
                            .setText(news.getAutorName())
                            .setTextResourceColor(R.color.orange_button))
                    .endConfig()
                    .build();
            materialListView.getAdapter().add(card);

        }
        materialListView.getAdapter().notifyDataSetChanged();
        materialListView.scrollToPosition(0);
        materialListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, int position) {
                String url = card.getTag().toString();
                String title = MyCollectionLB.get(getActivity()).getCollectionNewses().get(position).getTitle();
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL_EXTRA, url);
                intent.putExtra(WebViewActivity.TITLE_EXTRA, title);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {

            }
        });
        materialListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull Card card, int position) {
                MyCollectionLB.get(getActivity()).getCollectionNewses().remove(position);
                ((HomeFragment.HomeFragmentCallBack)getActivity()).snackBar("删除成功");
            }
        });
        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                ContentResolver cr = getActivity().getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    collectionImageView.setImageBitmap(bitmap);
                    collectionImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                collectionImageView.setImageBitmap(bitmap);
                collectionImageView.setScaleType(ImageView.ScaleType.CENTER);
            }
        }
    }

    private void refreshDate() {
        newses=MyCollectionLB.get(getActivity()).getCollectionNewses();
        materialListView.getAdapter().clear();
        for(int i=0;i<newses.size();i++) {
            News news = newses.get(i);
            String title=new String();
            title += ("" + news.getTitle());
            if(title.length()>25){
                title = title.substring(0, 24);
                title += ".....";
            }
            Card card = new Card.Builder(getActivity())
                    .setTag(news.getUrl())
                    .setDismissible()
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.card_list_item)
                    .setDescription(title)
                    .setDescriptionGravity(Gravity.START)
                    .setDescriptionResourceColor(R.color.black_button)
                    .setDrawable(news.getPic_1())
                    .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                        @Override
                        public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                            requestCreator.fit();
                        }
                    })
                    .addAction(R.id.left_text, new TextViewAction(getActivity())
                            .setText(news.getDate())
                            .setTextResourceColor(R.color.gray))
                    .addAction(R.id.right_text, new TextViewAction(getActivity())
                            .setText(news.getAutorName())
                            .setTextResourceColor(R.color.orange_button))
                    .endConfig()
                    .build();
            materialListView.getAdapter().add(card);
            materialListView.scrollToPosition(0);
        }
    }
}
