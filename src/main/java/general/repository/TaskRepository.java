package general.repository;

import general.entity.SchoolClass;
import general.entity.Task;
import general.entity.Teacher;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findTasksBySchoolClassOrderByTimeStamp(SchoolClass schoolClass);

    Task findTaskBySchoolClassAndName(SchoolClass schoolClass, String name);

    List<Task> findAllByTeacherAndSchoolClassOrderByTimeStamp(Teacher teacher, SchoolClass schoolClass);


    Task findTaskById(Long id);
}
