package general.repository;

import general.entity.SchoolClass;
import general.entity.Task;
import general.entity.Teacher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findTasksBySchoolClassOrderByTimeStamp(SchoolClass schoolClass);

    Task findTaskBySchoolClassAndName(SchoolClass schoolClass, String name);

    @Query(value = """
            SELECT
            	tasks.id ,
            	tasks.body ,
            	tasks.name,
            	tasks.time_stamp,
            	tasks.school_class_id,
            	tasks.teacher_id
            FROM
            	tasks
            LEFT OUTER JOIN answers ON
            	task_id = tasks.id
            WHERE
            	answers.id IS NULL
            	AND school_class_id = ?1
            	AND time_stamp > now()
            ORDER BY
            	tasks.id""", nativeQuery = true)
    List<Task> findOnlyNonAnsweredActualTasksByClassId(Long classId);

    @Query(value = """
            SELECT
            	tasks.id ,
            	tasks.body ,
            	tasks.name,
            	tasks.time_stamp,
            	tasks.school_class_id,
            	tasks.teacher_id
            FROM
            	tasks
            LEFT OUTER JOIN answers ON
            	task_id = tasks.id
            WHERE
            	answers.id IS NULL
            	AND school_class_id = ?1
            	AND time_stamp > now()
            	AND tasks.teacher_id = ?2
            ORDER BY
            	tasks.id""", nativeQuery = true)
    List<Task> findOnlyNonAnsweredActualTasksByClassIdAndTeacherId(Long classId, Long teacherId);

    @Query(value = """
            SELECT
            	tasks.id ,
            	tasks.body ,
            	tasks.name,
            	tasks.time_stamp,
            	tasks.school_class_id,
            	tasks.teacher_id
            FROM
            	tasks
            LEFT OUTER JOIN answers ON
            	task_id = tasks.id
            WHERE
            	answers.id IS NULL
            	AND school_class_id = ?1
            	AND time_stamp < now()
            	AND tasks.teacher_id = ?2
            ORDER BY
            	tasks.id""", nativeQuery = true)
    List<Task> findOnlyNonAnsweredOldTasksByClassIdAndTeacherId(Long classId, Long teacherId);

    @Query(value = """
            SELECT
            	tasks.id ,
            	tasks.body ,
            	tasks.name,
            	tasks.time_stamp,
            	tasks.school_class_id,
            	tasks.teacher_id
            FROM
            	tasks
            LEFT OUTER JOIN answers ON
            	task_id = tasks.id
            WHERE
            	answers.id IS NULL
            	AND school_class_id = ?1
            	AND time_stamp < now()
            ORDER BY
            	tasks.id""", nativeQuery = true)
    List<Task> findOnlyNonAnsweredOldTasksByClassId(Long classId);

    List<Task> findAllByTeacherAndSchoolClassOrderByTimeStamp(Teacher teacher, SchoolClass schoolClass);


    Task findTaskById(Long id);
}
