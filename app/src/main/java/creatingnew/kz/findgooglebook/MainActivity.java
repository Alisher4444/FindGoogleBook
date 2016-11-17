package creatingnew.kz.findgooglebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView listView;
    private Button searchBtn;
    private EditText bookNameEditText;
    private Button sortBtn;
    private Button scrollBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        searchBtn = (Button)findViewById(R.id.searchBtn);
        bookNameEditText = (EditText)findViewById(R.id.bookNameEditText);
        sortBtn = (Button) findViewById(R.id.sortBtn);
        scrollBtn = (Button)findViewById(R.id.scrollBtn);

        scrollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScrollButtonClick();
            }
        });

        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSortBtnClick();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchButtonClick();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DescriptionActivity.class);
                String mes = bookNameEditText.getText().toString();
                intent.putExtra("description",mes);
                startActivity(intent);

            }
        });
    }

    private void onScrollButtonClick() {
        listView.setSelection(0);
    }

    private void onSortBtnClick() {
        Comparator<BooksAdapter> bb = Collections.reverseOrder();

    }

    private void onSearchButtonClick() {
        //https://www.googleapis.com/books/v1/volumes?q={search terms}

        String bookName = bookNameEditText.getText().toString();
        String url = "https://www.googleapis.com/books/v1/volumes?q="+bookName;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,response.toString());

                try {
                    JSONArray books = response.getJSONArray("items");

                    displayBooks(books);

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

    private void displayBooks(JSONArray books) {
        BooksAdapter adapter = new BooksAdapter(books,this);

        listView.setAdapter(adapter);
    }
}
