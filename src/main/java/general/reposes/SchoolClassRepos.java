package general.reposes;

import general.entities.School;
import general.entities.SchoolClass;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SchoolClassRepos extends CrudRepository<SchoolClass, Long> {
    SchoolClass findSchoolClassByNameAndClassNumberAndSchool(String name,int number, School school);

    List<SchoolClass> findAllBySchoolOrderByClassNumber(School school);

    SchoolClass findSchoolClassById(Long id);

    void deleteBySchoolIdAndClassNumber(Long schoolId, int classNumber);

    @Modifying
    @Query(value = "UPDATE classes SET class_number = class_number + 1 WHERE school_id = ?1", nativeQuery = true)
    void incrementClassNumbersBySchoolId(Long schoolId);
}
