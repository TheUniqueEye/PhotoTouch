package edu.ucsb.cs.cs185.JingYan.phototouch;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;


/**
 * Created by EYE on 23/02/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Uri uri;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private ArrayList<Uri> uris = new ArrayList<>();
    private Bitmap bitmap;
    boolean ifListView = true;
    private ImageView imageView;


    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public ImageAdapter(Context c, Bitmap b) {
        mContext = c;
        bitmap = b;
    }

    public int getCount() {
        return bitmaps.size();
    }

    public Object getItem(int position) {
        return uris.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void addBitmapToList(Bitmap b, Uri u) {
        bitmaps.add(b);
        uris.add(u);
    }

    public void clearStorage() {
        bitmaps.clear();
        uris.clear();
    }

    public void setListMode(Boolean b) {
        ifListView = b;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            imageView = new ImageView(mContext);
            if (ifListView) {
                // List View
                imageView.setLayoutParams(new GridView.LayoutParams(1200, 400));
            } else {
                // Grid View
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(bitmaps.get(position));

        return imageView;
    }

}