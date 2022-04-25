package general.reposes;

import general.entities.School;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SchoolRepos extends CrudRepository<School, Long> {
    School findSchoolByName(String Name);

    @Modifying
    @Query(value = "INSERT INTO schools_classes VALUES (?1,?2)", nativeQuery = true)
    void addClassToSchoolById(Long schoolId, Long classId);
}
