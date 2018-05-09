package lab.swim.pwr.android_zad3;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Rafał on 2018-03-27.
 */

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    private CustomAdapter mCustomAdapter;

    SwipeHelper(CustomAdapter customAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        mCustomAdapter = customAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mCustomAdapter.removeItem(viewHolder.getAdapterPosition());
    }
}
