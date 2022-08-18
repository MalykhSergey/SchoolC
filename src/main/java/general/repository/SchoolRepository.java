package general.repository;

import general.entity.School;
import org.springframework.data.repository.CrudRepository;

public interface SchoolRepository extends CrudRepository<School, Long> {
    School findSchoolByName(String Name);

    School findSchoolById(Long id);

}
