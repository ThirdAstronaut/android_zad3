package lab.swim.pwr.android_zad3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class WalksListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CustomAdapter mCustomAdapter;
/*
    public static void start(Context context, float numOfSteps, String duration, String name) {
        Intent starter = new Intent(context, WalksListActivity.class);
        starter.putExtra();
        context.startActivity(starter);
    }
  */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walks_list);
        mCustomAdapter = new CustomAdapter();
        setRecyclerView();

        ItemTouchHelper.Callback callback = new SwipeHelper(mCustomAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    private void setRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mCustomAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

}