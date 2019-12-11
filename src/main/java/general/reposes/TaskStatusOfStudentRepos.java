package general.reposes;

import general.entities.Student;
import general.entities.Task;
import general.entities.TaskStatusOfStudent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskStatusOfStudentRepos extends CrudRepository<TaskStatusOfStudent, Long> {
    public TaskStatusOfStudent findTaskStatusOfStudentByStudentAndTask(Student student, Task task);
    public List<TaskStatusOfStudentRepos> findTaskStatusOfStudentsByStudent(Student student);

    Iterable<TaskStatusOfStudent> findAllByStudent(Student student);
}
