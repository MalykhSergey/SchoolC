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

}
