package general.services;

import general.entities.School;
import general.entities.SchoolClass;
import general.entities.Teacher;
import general.reposes.SchoolClassRepos;
import general.utils.Result;
import general.utils.StringLengthConstants;
import general.utils.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SchoolClassService {

    private final SchoolClassRepos schoolClassRepos;

    @Autowired
    public SchoolClassService(SchoolClassRepos schoolClassRepos) {
        this.schoolClassRepos = schoolClassRepos;
    }

    private static boolean isClassInTeacherSet(Teacher teacher, SchoolClass schoolClass) {
        return teacher.getSchoolClassSet().stream().anyMatch(teacherClass -> teacherClass.fastEqualsById(schoolClass));
    }

    @Transactional
    public Result createSchoolClass(String className, int classNumber, School school, UserDetailsExtended userDetailsExtended) {
        if (school == null) return Result.InvalidSchoolName;
        if (className.length() > StringLengthConstants.ClassName.getMaxLength()) return Result.TooLongClassName;
        if (className.length() < StringLengthConstants.ClassName.getMinLength()) return Result.TooShortClassName;
        if (classNumber > 11 || classNumber < 1) return Result.InvalidClassNumber;
        if (schoolClassRepos.findSchoolClassByNameAndClassNumberAndSchool(className, classNumber, school) != null)
            return Result.ClassIsExists;
        SchoolClass schoolClass = new SchoolClass(className, classNumber, school);
        schoolClassRepos.save(schoolClass);
        return Result.Ok;
    }

    @Transactional
    public void createNewSchoolClass(SchoolClass schoolClass) {
        schoolClassRepos.save(schoolClass);
    }

    public SchoolClass getClassByNameAndNumberAndSchool(String name, int number, School school) {
        return schoolClassRepos.findSchoolClassByNameAndClassNumberAndSchool(name, number, school);
    }

    public SchoolClass getClassById(Long id) {
        return schoolClassRepos.findSchoolClassById(id);
    }

    public List<SchoolClass> getAllClassesBySchool(School school) {
        return schoolClassRepos.findAllBySchoolOrderByClassNumberAscName(school);
    }

    @Transactional
    public Result addClassForTeacher(Teacher teacher, SchoolClass schoolClass) {
        if (teacher == null) return Result.InvalidName;
        if (schoolClass == null) return Result.InvalidClassName;
        if (!teacher.getSchool().getId().equals(schoolClass.getSchool().getId())) return Result.InvalidName;
        if (isClassInTeacherSet(teacher, schoolClass)) return Result.TeacherIsLinked;
        schoolClassRepos.addClassForTeacher(teacher.getId(), schoolClass.getId());
        return Result.Ok;
    }
}
