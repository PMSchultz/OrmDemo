package edu.cnm.deepdive.demo.ormdemo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import edu.cnm.deepdive.demo.ormdemo.R;
import edu.cnm.deepdive.demo.ormdemo.activities.StudentDetailActivity;
import edu.cnm.deepdive.demo.ormdemo.activities.StudentListActivity;
import edu.cnm.deepdive.demo.ormdemo.entities.Absence;
import edu.cnm.deepdive.demo.ormdemo.entities.Student;
import edu.cnm.deepdive.demo.ormdemo.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.List;

/**
 * A fragment representing a single Student detail screen. This fragment is either contained in a
 * {@link StudentListActivity} in two-pane mode (on tablets) or a {@link StudentDetailActivity} on
 * handsets.
 */
public class StudentDetailFragment extends Fragment {

  public static final String STUDENT_ID = "student_id";

  private int studentId;
  private Student student;
  private OrmHelper helper;
  private View rootView;
  private ListView absenceList;
  private ArrayAdapter<Absence> absenceAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments().containsKey(STUDENT_ID)) {
      studentId = getArguments().getInt(STUDENT_ID);
    } else {
      studentId = 0;
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_student_detail, container, false);
    absenceList = rootView.findViewById(R.id.absence_list);
    absenceAdapter = new ArrayAdapter<>(getContext(), R.layout.absence_list_item);
    absenceList.setAdapter(absenceAdapter);
    return rootView;
  }

  @Override
  public void onStart() {
    super.onStart();
    helper = ((OrmHelper.OrmInteraction) getActivity()).getHelper();
    if (studentId > 0) {
      try {
        Dao<Student, Integer> studentDao = helper.getStudentDao();
        Dao<Absence, Integer> absenceDao = helper.getAbsenceDao();
        student = studentDao.queryForId(getArguments().getInt(STUDENT_ID));
        ((TextView) getActivity().findViewById(R.id.student_id)).setText(Integer.toString(student.getId()));
        ((EditText) getActivity().findViewById(R.id.student_name)).setText(student.getName());
        ((TextView) getActivity().findViewById(R.id.student_created)).setText(student.getCreated().toString());
        QueryBuilder<Absence, Integer> builder = absenceDao.queryBuilder();
        builder.where().eq("STUDENT_ID", student.getId());
        builder.orderBy("DATE", false);
        List<Absence> absences = absenceDao.query(builder.prepare());
        absenceAdapter.addAll(absences);
        absenceAdapter.notifyDataSetChanged();
        absenceList.invalidateViews();
        rootView.forceLayout();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }  else {
      student = null;
    }
  }

}
