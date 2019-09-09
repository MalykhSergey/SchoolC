package general.reposes;

import general.entities.Teacher;
import org.springframework.data.repository.CrudRepository;

public interface TeacherRepos extends CrudRepository<Teacher, Long> {
    Teacher findTeacherByName(String Name);
}
