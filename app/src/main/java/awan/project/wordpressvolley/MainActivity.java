package awan.project.wordpressvolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String TAG="WPAPP";
    public List<Posts> mPosts;
    public Button btnGetPost;
    public RecyclerView recyclerView;
    public PostAdapter postAdapter;
    public int page=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView= (RecyclerView) findViewById(R.id.recyclerHome);
        postAdapter=new PostAdapter(mPosts,this,false,false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getPost();
    }

    public void getPost() {
        mPosts = new ArrayList<Posts>();
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, Config.base_url+"wp-json/wp/v2/posts/?page="+page, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d(TAG, response.toString() + "Size: "+response.length());
                        for(int i=0;i<response.length();i++){
                            Posts post=new Posts();
                            try {
                                Log.d(TAG,"Object at " + i+ response.get(i));
                                JSONObject obj=response.getJSONObject(i);
                                post.setId(obj.getInt("id"));
                                post.setCreatedAt(obj.getString("date"));
                                post.setPostURL(obj.getString("link"));
                                JSONObject titleObj=obj.getJSONObject("title");
                                post.setTitle(titleObj.getString("rendered"));
                                mPosts.add(post);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        setData(mPosts);

                    }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        ) ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getRequest);


    }

    public void setData(List<Posts> posts){
        recyclerView.setAdapter(postAdapter);
        postAdapter.setData(mPosts);
        postAdapter.notifyDataSetChanged();

    }
}
