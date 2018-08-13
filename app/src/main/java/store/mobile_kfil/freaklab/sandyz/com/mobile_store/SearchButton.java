package store.mobile_kfil.freaklab.sandyz.com.mobile_store;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import store.mobile_kfil.freaklab.sandyz.com.mobile_store.database.DatabaseOpenHelper;


public class SearchButton extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    String queryCode, result;
    private ArrayList<Beans> mylList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.code_list);




        Bundle bundle = getIntent().getExtras();
        //get the search key word

        DatabaseOpenHelper mydata = new DatabaseOpenHelper(this);

        assert bundle != null;
        String code = bundle.getString("Code");




       mylList= mydata.mouldLine(code);
       if (mylList.size()==0){
           mylList=mydata.codeSearch(code);
       }

        // add data into list
        Toast.makeText(getApplicationContext(), String.valueOf(mylList.size())+" Results Found for "+code, Toast.LENGTH_SHORT).show();
        adapter = new RecyclerAdapter(mylList, getApplicationContext());
        //animate the whole adapter

        ScaleInAnimationAdapter slideInRightAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        slideInRightAnimationAdapter.setFirstOnly(false);
        slideInRightAnimationAdapter.setDuration(500);


        //inttialize the recyler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        //add animator to the view
        LandingAnimator animator = new LandingAnimator(new OvershootInterpolator(1f));


        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(animator);

        //set the adapter
        recyclerView.setAdapter(new SlideInBottomAnimationAdapter(slideInRightAnimationAdapter));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final ArrayList<Beans> beansArrayList = new ArrayList<>();
                //campare array list
                for (Beans beans : mylList) {
                    result = beans.getDescp().toLowerCase(Locale.getDefault());
                    queryCode = beans.getCode().toLowerCase();

                    if (result.contains(newText) || queryCode.contains(newText)) {

                        beansArrayList.add(beans);
                    }
                    adapter.filter(beansArrayList);
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return id == R.id.search || super.onOptionsItemSelected(item);
    }



}


