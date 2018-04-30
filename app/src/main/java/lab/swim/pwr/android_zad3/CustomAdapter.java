package lab.swim.pwr.android_zad3;


import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context ctx;

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView mCategoryTextView;
        private TextView mDurationTextView;

        ViewHolder(View v) {
            super(v);
            mTitleTextView = v.findViewById(R.id.title_text_view);
            mCategoryTextView = v.findViewById(R.id.category_text_view);
            mDurationTextView= v.findViewById(R.id.duration_text_view);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.walks_row, parent, false);

        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {
        Walk movie = WalksKeeper.getInstance().getWalksList().get(position);
        holder.mTitleTextView.setText(movie.getName());
        holder.mCategoryTextView.setText(movie.getNumOfSteps());
        holder.mDurationTextView.setText(movie.getDuration());
        }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Walk getItemAtPosition(int position) {
        return WalksKeeper.getInstance().getWalksList().get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int getItemCount() {
        return WalksKeeper.getInstance().getWalksList().size();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void removeItem(int id) {
        WalksKeeper.getInstance().getWalksList().remove(id);
        this.notifyItemRemoved(id);
    }
}
