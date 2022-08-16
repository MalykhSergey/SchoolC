package general.reposes;

import general.entities.SchoolClass;
import general.entities.Task;
import general.entities.Teacher;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepos extends CrudRepository<Task, Long> {
    List<Task> findTasksBySchoolClassOrderByTimeStamp(SchoolClass schoolClass);

    List<Task> findAllByTeacherAndSchoolClassOrderByTimeStamp(Teacher teacher, SchoolClass schoolClass);


    Task findTaskById(Long id);
}
