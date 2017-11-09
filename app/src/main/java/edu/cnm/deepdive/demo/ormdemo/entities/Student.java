package edu.cnm.deepdive.demo.ormdemo.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@DatabaseTable(tableName = "STUDENT")
public class Student {

  @DatabaseField(columnName = "STUDENT_ID", generatedId = true)
  private int id;

  @DatabaseField(columnName = "CREATED", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
      format = "yyyy-MM-dd HH:mm:ss", canBeNull = false, readOnly = true)
  private Timestamp created;

  @DatabaseField(columnName = "NAME", canBeNull = false)
  private String name;

  @ForeignCollectionField
  public ForeignCollection<Absence> absences;

  public int getId() {
    return id;
  }

  public Timestamp getCreated() {
    return created;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ForeignCollection<Absence> getAbsences() {
    return absences;
  }

  @Override
  public String toString() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("name", name);
    map.put("created", created);
    return map.toString();
  }

}









