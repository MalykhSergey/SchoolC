package general.reposes;

import general.entities.School;
import general.entities.SchoolClass;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SchoolClassRepos extends CrudRepository<SchoolClass, Long> {
    SchoolClass findSchoolClassByName(String Name);

    List<SchoolClass> findSchoolClassBySchool(School school);

    SchoolClass findSchoolClassById(Long id);

    @Query(value = "SELECT name FROM classes where id = ?1", nativeQuery = true)
    String findSchoolClassNameById(Long id);

    @Modifying
    @Query(value = "INSERT INTO schools_classes  VALUES (?2,?1)", nativeQuery = true)
    void addClassToSchool(Long classId, Long schoolId);

    @Modifying
    @Query(value = "INSERT INTO class_students  VALUES (?1,?2)", nativeQuery = true)
    void addStudentToClass(Long classId, Long studentId);
}
