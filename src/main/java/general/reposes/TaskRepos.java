package general.reposes;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import general.entities.SchoolClass;
import general.entities.Task;

import java.util.List;

public interface TaskRepos extends CrudRepository<Task, Long> {
    List<Task> findTasksBySchoolClassOrderByTimeStamp(SchoolClass schoolClass);

    @Query(value = "SELECT * FROM tasks WHERE school_class_id = ?1", nativeQuery = true)
    List<Task> findTasksBySchoolClassId(Long schoolClassId);

    Task findTaskById(Long id);

    @Modifying
    @Query(value = "INSERT INTO task_answers VALUES (?1,?2)", nativeQuery = true)
    void addAnswerToTask(Long taskId, Long answerId);
}
