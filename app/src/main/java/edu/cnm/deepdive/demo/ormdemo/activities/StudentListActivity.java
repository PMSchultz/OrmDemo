package edu.cnm.deepdive.demo.ormdemo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.j256.ormlite.android.apptools.OpenHelperManager;

import edu.cnm.deepdive.demo.ormdemo.R;
import edu.cnm.deepdive.demo.ormdemo.fragments.StudentDetailFragment;
import edu.cnm.deepdive.demo.ormdemo.entities.Student;
import edu.cnm.deepdive.demo.ormdemo.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.List;

/**
 * An activity representing a list of Students. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link StudentDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two vertical panes.
 */
public class StudentListActivity
    extends AppCompatActivity
    implements OrmHelper.OrmInteraction {

  private static final String LOG_TAG = "STUDENT_LIST_ACTIVITY";

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
   */
  private boolean mTwoPane;
  private OrmHelper helper = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getHelper().getWritableDatabase().close();
    setContentView(R.layout.activity_student_list);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

//    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//    fab.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show();
//      }
//    });
//
    View recyclerView = findViewById(R.id.student_list);
    assert recyclerView != null;
    setupRecyclerView((RecyclerView) recyclerView);

    if (findViewById(R.id.student_detail_container) != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      mTwoPane = true;
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    getHelper();
  }

  @Override
  protected void onStop() {
    releaseHelper();
    super.onStop();
  }

  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    try {
      recyclerView.setAdapter(
          new SimpleItemRecyclerViewAdapter(getHelper().getStudentDao().queryForAll()));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public synchronized OrmHelper getHelper() {
    if (helper == null) {
      helper = OpenHelperManager.getHelper(this, OrmHelper.class);
    }
    return helper;
  }

  public synchronized void releaseHelper() {
    if (helper != null) {
      OpenHelperManager.releaseHelper();
      helper = null;
    }
  }

  public class SimpleItemRecyclerViewAdapter
      extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final List<Student> mValues;

    public SimpleItemRecyclerViewAdapter(List<Student> items) {
      mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.student_list_content, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
      holder.mItem = mValues.get(position);
      holder.mStudentView.setText(mValues.get(position).getName());
      holder.mCreatedView.setText(mValues.get(position).getCreated().toString());

      holder.mView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(StudentDetailFragment.STUDENT_ID, holder.mItem.getId());
            StudentDetailFragment fragment = new StudentDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.student_detail_container, fragment)
                .commit();
          } else {
            Context context = v.getContext();
            Intent intent = new Intent(context, StudentDetailActivity.class);
            intent.putExtra(StudentDetailFragment.STUDENT_ID, holder.mItem.getId());
            context.startActivity(intent);
          }
        }
      });
    }

    @Override
    public int getItemCount() {
      return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

      public final View mView;
      public final TextView mStudentView;
      public final TextView mCreatedView;
      public Student mItem;

      public ViewHolder(View view) {
        super(view);
        mView = view;
        mStudentView = (TextView) view.findViewById(R.id.student_name);
        mCreatedView = (TextView) view.findViewById(R.id.student_created);
      }

      @Override
      public String toString() {
        return super.toString() + " '" + mStudentView.getText() + "'";
      }
    }
  }

}
