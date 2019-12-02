package general.reposes;

import general.entities.SchoolClass;
import org.springframework.data.repository.CrudRepository;

public interface SchoolClassRepos extends CrudRepository<SchoolClass, Long> {
    SchoolClass findSchoolClassByName(String Name);
    SchoolClass findSchoolClassById(Long id);
}
