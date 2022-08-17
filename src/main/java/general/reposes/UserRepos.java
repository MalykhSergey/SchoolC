package general.reposes;

import general.entities.School;
import general.entities.Teacher;
import general.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepos extends CrudRepository<User, Long> {
    User findUserByName(String name);
    Teacher findTeacherByName(String name);

    User findUserByNameAndSchool(String name, School school);

    @Query(value = "update users set school_class_id = ?2 where id=?1", nativeQuery = true)
    @Modifying
    void setClassForStudent(Long student, Long schoolClass);

    @Query(value = "SELECT * FROM users WHERE role = 2 and school_id = ?1", nativeQuery = true)
    List<Teacher> findTeachersBySchoolId(Long id);

    @Query(value = "SELECT name FROM users WHERE id in(SELECT teacher_id FROM teacher_classes WHERE class_id = ?1)", nativeQuery = true)
    List<String> findNamesOfTeachersByClassId(Long classId);

}
