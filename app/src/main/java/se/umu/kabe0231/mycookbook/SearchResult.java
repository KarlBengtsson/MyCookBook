package se.umu.kabe0231.mycookbook;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchResult extends AppCompatActivity {
    Toolbar myToolbar;
    LinearLayout linear;
    TextView text;
    View emptyView;

    //Todo läs på lite om android search funktion. Ta med i rapporten varför du inte valde att göra detta. Läs i kursbok
    //Todo Uppdatera så att det faktiskt visar sökresultat, Om inget resultat, visa inget resultat hittades
    //Todo se till så att tillstånd sparas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        linear = (LinearLayout) findViewById(R.id.LinearLayout);
        String data = getIntent().getExtras().getString("SearchResult",null);
        TextView myToolbarText = (TextView) findViewById(R.id.toolbarTitle);
        myToolbarText.setText("SökResultat");

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        text = new TextView(this);
        emptyView = new View(this);
        int dividerHeight = (int) (getResources().getDisplayMetrics().density * 1);
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dividerHeight));
        emptyView.setBackgroundColor(Color.parseColor("#000000"));
        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        text.setText(data);
        text.setTextSize(40);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setPadding(2, 2, 2, 2);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dosomething
            }
        });
        linear.addView(text);
        linear.addView(emptyView);
    }
}
