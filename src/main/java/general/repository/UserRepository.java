package general.repository;

import general.entity.School;
import general.entity.Student;
import general.entity.Teacher;
import general.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByName(String name);
    Student findStudentByName(String name);
    Teacher findTeacherByName(String name);

    User findUserByNameAndSchool(String name, School school);
    Student findStudentByNameAndSchool(String name, School school);

    @Query(value = "update users set school_class_id = ?2 where id=?1", nativeQuery = true)
    @Modifying
    void setClassForStudent(Long student, Long schoolClass);

    @Query(value = "SELECT * FROM users WHERE role = 2 and school_id = ?1", nativeQuery = true)
    List<Teacher> findTeachersBySchoolId(Long id);

    @Query(value = "SELECT name FROM users WHERE id in(SELECT teacher_id FROM teacher_classes WHERE class_id = ?1)", nativeQuery = true)
    List<String> findNamesOfTeachersByClassId(Long classId);

}
