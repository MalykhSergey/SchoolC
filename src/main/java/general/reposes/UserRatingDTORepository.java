package general.reposes;

import general.entities.SchoolClass;
import general.entities.Teacher;
import general.entities.UserRatingDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRatingDTORepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<UserRatingDTO> findAllUsersRatingByTeacherAndClass(Teacher teacher, SchoolClass schoolClass) {
        List<Object[]> resultList = entityManager
                .createNativeQuery("SELECT id, name FROM users WHERE school_class_id = ?1")
                .setParameter(1, schoolClass.getId()).getResultList();
        List<UserRatingDTO> userRatingDTOList = new ArrayList<>(resultList.size());
        for (Object[] result : resultList) {
            UserRatingDTO userRatingDTO = new UserRatingDTO((String) result[1]);
            List<Object[]> answers = entityManager
                    .createNativeQuery("SELECT answers.task_id, answers.rating FROM users INNER JOIN" +
                            " answers on answers.student_id = users.id WHERE student_id = ?1 AND teacher_id =?2")
                    .setParameter(1, result[0])
                    .setParameter(2, teacher.getId())
                    .getResultList();
            userRatingDTO.setAnswers(answers);
            userRatingDTOList.add(userRatingDTO);
        }
        return userRatingDTOList;
    }
}
