package creatingnew.kz.findgooglebook;

import android.content.Context;
import android.graphics.Bitmap;
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

import java.util.ArrayList;

/**
 * Created by Алишер on 09.06.2016.
 */
public class BooksAdapter extends BaseAdapter {

    Context context;
    JSONArray books;
    LayoutInflater inflater;
    ImageLoader mImageLoader;
    RequestQueue mRequestQueue;
    ArrayList list;

    public BooksAdapter(JSONArray books, Context context) {
        this.books = books;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        return books.length();
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

        ViewHolder viewHolder = null;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.row_book_item,null);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            viewHolder.subtitleTextView = (TextView) convertView.findViewById(R.id.subtitleTextView);
            viewHolder.coverNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.coverNetworkImageView);
            viewHolder.authorsTextView = (TextView) convertView.findViewById(R.id.authorsTextView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            JSONObject book = books.getJSONObject(position);
            JSONObject volumeInfo = book.getJSONObject("volumeInfo");
            String title = volumeInfo.getString("title");
            String subtitle = volumeInfo.getString("subtitle");
            String authors = volumeInfo.getString("authors");

            viewHolder.titleTextView.setText(title);
            viewHolder.subtitleTextView.setText(subtitle);
            viewHolder.authorsTextView.setText(authors);

            JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
            String thumbnailLinks = imageLinks.getString("thumbnail");


            viewHolder.coverNetworkImageView.setImageUrl(thumbnailLinks,mImageLoader);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder{
        TextView titleTextView;
        TextView subtitleTextView;
        NetworkImageView coverNetworkImageView;
        TextView authorsTextView;
    }
}
