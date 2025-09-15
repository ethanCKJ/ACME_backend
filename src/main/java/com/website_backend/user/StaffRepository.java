package com.website_backend.user;

import com.website_backend.user.dto.SignupStaffDetails;
import com.website_backend.user.dto.StaffProfile;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StaffRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public StaffRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  /**
   * Save staff profile information. Does not save staff login details.
   * @param staffInfo
   */
  public void saveStaffProfile(SignupStaffDetails staffInfo){
    try {
      int staffId =  namedParameterJdbcTemplate.queryForObject("SELECT id FROM acme_db.users WHERE username=:username ", Map.of("username",staffInfo.username()), Integer.class);
      namedParameterJdbcTemplate.update("INSERT INTO acme_db.staff(id, staff_name) VALUES (:id, :staff_name)",
          Map.of("id", staffId,
                 "staff_name", staffInfo.staffName()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * Loads staff profile information (staffName) from database
   * @param staffId
   * @return
   */
  public StaffProfile getStaffProfile(int staffId) {
    StaffProfile result;
    try {
      result = namedParameterJdbcTemplate.queryForObject("SELECT staff_name FROM acme_db.staff WHERE id=:id",Map.of("id", staffId), StaffProfile.class);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
    return result;
  }
}
