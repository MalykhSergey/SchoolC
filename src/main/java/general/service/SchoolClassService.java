package general.service;

import general.entity.Role;
import general.entity.School;
import general.entity.SchoolClass;
import general.entity.Teacher;
import general.repository.SchoolClassRepository;
import general.util.Result;
import general.util.StringLengthConstants;
import general.util.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    @Autowired
    public SchoolClassService(SchoolClassRepository schoolClassRepository) {
        this.schoolClassRepository = schoolClassRepository;
    }

    private static boolean isClassInTeacherSet(Teacher teacher, SchoolClass schoolClass) {
        return teacher.getSchoolClassSet().stream().anyMatch(teacherClass -> teacherClass.fastEqualsById(schoolClass));
    }

    @Transactional
    public Result createSchoolClass(String className, int classNumber, School school) {
        if (school == null) return Result.InvalidSchoolName;
        if (className.length() > StringLengthConstants.ClassName.getMaxLength()) return Result.TooLongClassName;
        if (className.length() < StringLengthConstants.ClassName.getMinLength()) return Result.TooShortClassName;
        if (classNumber > 11 || classNumber < 1) return Result.InvalidClassNumber;
        if (schoolClassRepository.findSchoolClassByNameAndClassNumberAndSchool(className, classNumber, school) != null)
            return Result.ClassIsExists;
        SchoolClass schoolClass = new SchoolClass(className, classNumber, school);
        schoolClassRepository.save(schoolClass);
        return Result.Ok;
    }

    @Transactional
    public void createNewSchoolClass(SchoolClass schoolClass) {
        schoolClassRepository.save(schoolClass);
    }

    public SchoolClass getClassByNameAndNumberAndSchool(String name, int number, School school) {
        return schoolClassRepository.findSchoolClassByNameAndClassNumberAndSchool(name, number, school);
    }

    public SchoolClass getClassById(Long id) {
        return schoolClassRepository.findSchoolClassById(id);
    }

    public List<SchoolClass> getAllClassesBySchool(School school) {
        return schoolClassRepository.findAllBySchoolOrderByClassNumberAscName(school);
    }

    @Transactional
    public Result addClassForTeacher(Teacher teacher, SchoolClass schoolClass, UserDetailsExtended userDetailsExtended) {
        if (teacher == null) return Result.InvalidName;
        if (userDetailsExtended.getUser().getRole() == Role.Operator)
            if (!userDetailsExtended.getUser().getSchool().getId().equals(teacher.getSchool().getId()))
                return Result.InvalidName;
        if (schoolClass == null) return Result.InvalidClassName;
        if (!teacher.getSchool().getId().equals(schoolClass.getSchool().getId())) return Result.InvalidName;
        if (isClassInTeacherSet(teacher, schoolClass)) return Result.TeacherIsLinked;
        schoolClassRepository.addClassForTeacher(teacher.getId(), schoolClass.getId());
        return Result.Ok;
    }
}
