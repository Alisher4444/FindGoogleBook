package creatingnew.kz.findgooglebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DescriptionAdapter extends BaseAdapter {

    private static final String TAG = "NEWADAPTER";
    Context context;
    JSONArray desBook;
    LayoutInflater mInflater;
    RequestQueue mRequestQueue;
    ImageLoader mImageLoader;

    public DescriptionAdapter(JSONArray desBook, Context context){
        this.desBook=desBook;
        this.context=context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRequestQueue = Volley.newRequestQueue(context);

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });

    }

    @Override
    public int getCount() {
        return desBook.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderDes viewHolderDes = null;

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.activity_description,null);
            viewHolderDes = new ViewHolderDes();
            viewHolderDes.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            viewHolderDes.subtitleTextView = (TextView) convertView.findViewById(R.id.subtitleTextView);
            viewHolderDes.coverNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.coverNetworkImageView);
            viewHolderDes.authorsTextView = (TextView) convertView.findViewById(R.id.authorsTextView);
            viewHolderDes.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
            convertView.setTag(viewHolderDes);



        }else{
            viewHolderDes = (ViewHolderDes) convertView.getTag();
        }

        try {
            JSONObject desBooks = desBook.getJSONObject(position);
            JSONObject volumeInfo = desBooks.getJSONObject("volumeInfo");
            String title = volumeInfo.getString("title");
            String subtitle = volumeInfo.getString("subtitle");
            String authors = volumeInfo.getString("authors");
            String description = volumeInfo.getString("description");

            viewHolderDes.descriptionTextView.setText(description);
            viewHolderDes.titleTextView.setText(title);
            viewHolderDes.subtitleTextView.setText(subtitle);
            viewHolderDes.authorsTextView.setText(authors);

            JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
            String thumbnailLinks = imageLinks.getString("thumbnail");


            viewHolderDes.coverNetworkImageView.setImageUrl(thumbnailLinks,mImageLoader);
        } catch (JSONException e) {
            e.printStackTrace();

        }


        return convertView;
    }

    private class ViewHolderDes{
        TextView descriptionTextView;
        TextView titleTextView;
        TextView subtitleTextView;
        NetworkImageView coverNetworkImageView;
        TextView authorsTextView;
    }
}

