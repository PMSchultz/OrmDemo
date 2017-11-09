package edu.cnm.deepdive.demo.ormdemo.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import edu.cnm.deepdive.demo.ormdemo.R;
import edu.cnm.deepdive.demo.ormdemo.fragments.StudentDetailFragment;
import edu.cnm.deepdive.demo.ormdemo.helpers.OrmHelper;

/**
 * An activity representing a single Student detail screen. This activity is only used narrow width
 * devices. On tablet-size devices, item details are presented side-by-side with a list of items in
 * a {@link StudentListActivity}.
 */
public class StudentDetailActivity
    extends AppCompatActivity
    implements OrmHelper.OrmInteraction {

  private static final String LOG_TAG = "STUDENT_DETAIL_ACTIVITY";

  private OrmHelper helper = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student_detail);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

//    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//    fab.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show();
//      }
//    });
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
    if (savedInstanceState == null) {
      Bundle arguments = new Bundle();
      arguments.putInt(StudentDetailFragment.STUDENT_ID,
          getIntent().getIntExtra(StudentDetailFragment.STUDENT_ID, 0));
      StudentDetailFragment fragment = new StudentDetailFragment();
      fragment.setArguments(arguments);
      getSupportFragmentManager().beginTransaction()
          .add(R.id.student_detail_container, fragment)
          .commit();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
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

}
