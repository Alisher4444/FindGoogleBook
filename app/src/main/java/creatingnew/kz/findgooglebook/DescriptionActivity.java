package creatingnew.kz.findgooglebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DescriptionActivity extends AppCompatActivity {


    private GridView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description2);

        scrollView = (GridView) findViewById(R.id.scrollView);
        Intent intent = getIntent();

        String bookName2 = intent.getStringExtra("description");

        String url = "https://www.googleapis.com/books/v1/volumes?q="+bookName2;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONArray books = response.getJSONArray("items");

                    displayDesBooks(books);




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG,error.getMessage());
            }
        });

        queue.add(request);

    }

    private void displayDesBooks(JSONArray desBooks){
        DescriptionAdapter descriptionAdapter = new DescriptionAdapter(desBooks,this);
        scrollView.setAdapter(descriptionAdapter);
    }
}
