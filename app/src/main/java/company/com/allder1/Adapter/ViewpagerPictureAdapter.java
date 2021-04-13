package company.com.allder1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import company.com.allder1.model.Picturesdatum;


public class ViewpagerPictureAdapter extends PagerAdapter {
    Context context;
    ArrayList<Picturesdatum> listlinkimage;

    public ViewpagerPictureAdapter(Context context, ArrayList<Picturesdatum> listlinkimage) {
        this.context = context;
        this.listlinkimage = listlinkimage;
    }

    @Override
    public int getCount() {
        return listlinkimage.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (listlinkimage.get(position).getPicture().isEmpty()) {

        } else {
            String base64 = listlinkimage.get(position).getPicture();
            String base64Image = base64.split(",")[1];
            byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
            Glide.with(context).load(imageByteArray)
                    .centerCrop()
                    .into(imageView);
            container.addView(imageView);
        }

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
