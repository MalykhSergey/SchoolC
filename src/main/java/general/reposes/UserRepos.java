package general.reposes;

import general.entities.School;
import general.entities.Student;
import general.entities.User;
import general.entities.UserRatingDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepos extends CrudRepository<User, Long> {
    User findUserByNameAndSchool(String name, School school);

    User findUserByName(String name);

    @Query(value = "SELECT * FROM users WHERE school_class_id = ?1", nativeQuery = true)
    List<Student> findAllStudentsBySchoolClass(Long schoolClassId);

    @Modifying
    @Query(value = "INSERT INTO teacher_classes VALUES (?1,?2)", nativeQuery = true)
    void addClassForTeacher(Long teacherId, Long classId);

    @Modifying
    @Query(value = "INSERT INTO schools_teachers VALUES (?2,?1)", nativeQuery = true)
    void setSchoolForTeacher(Long teacherId, Long schoolId);

    @Modifying
    @Query(value = "UPDATE users SET school_class_id = ?1 WHERE id = ?2",nativeQuery = true)
    void updateClassForUser(Long classId, Long userId);

    @Modifying
    @Query(value = "UPDATE users SET school_id = ?1 WHERE id = ?2",nativeQuery = true)
    void updateSchoolForUser(Long schoolId, Long userId);

    @Modifying
    @Query(value = "UPDATE class_students SET school_class_id = ?1 WHERE students_id=?2", nativeQuery = true)
    void updateClassForStudentInJoinTable(Long classId, Long studentId);
}
