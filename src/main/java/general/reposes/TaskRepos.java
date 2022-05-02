package general.reposes;

import general.entities.Teacher;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import general.entities.SchoolClass;
import general.entities.Task;

import java.util.List;

public interface TaskRepos extends CrudRepository<Task, Long> {
    List<Task> findTasksBySchoolClassOrderByTimeStamp(SchoolClass schoolClass);

    List<Task> findAllByTeacherAndSchoolClass(Teacher teacher, SchoolClass schoolClass);


    Task findTaskById(Long id);
}
