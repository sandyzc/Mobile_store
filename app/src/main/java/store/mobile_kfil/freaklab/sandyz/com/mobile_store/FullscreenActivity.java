package store.mobile_kfil.freaklab.sandyz.com.mobile_store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class FullscreenActivity extends AppCompatActivity {


    String code;
    EditText searchData;
    Button searc_Button;
    TextView version_info;

    private static final String TAG = "Main Screen";




    private int version = BuildConfig.VERSION_CODE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        //displaying app version
        version_info = findViewById(R.id.versi);
        String version_name = BuildConfig.VERSION_NAME;
        version_info.setText("V " + version_name);

        searchData = findViewById(R.id.query_data);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        searc_Button = findViewById(R.id.searchButton);
        searc_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchData.getText().length() <= 0) {
                    searchData.setError("Enter KeyWOrd");
                } else {

                    Intent letsSearch = new Intent(FullscreenActivity.this, SearchButton.class);
                    Bundle data = new Bundle();
                    code = searchData.getText().toString();
                    data.putString("Code", code);
                    letsSearch.putExtras(data);
                    startActivity(letsSearch);
                }
            }
        });
        searc_Button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent upload_data = new Intent(FullscreenActivity.this, Excel.class);
                startActivity(upload_data);
                return false;
            }
        });


    }



}
