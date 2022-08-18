package general.service;

import general.entity.School;
import general.entity.Teacher;
import general.repository.SchoolClassRepository;
import general.repository.SchoolRepository;
import general.repository.UserRepository;
import general.util.Result;
import general.util.StringLengthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SchoolService {
    private final SchoolRepository schoolRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final UserRepository userRepository;

    @Autowired
    public SchoolService(SchoolRepository schoolRepository, SchoolClassRepository schoolClassRepository, UserRepository userRepository) {
        this.schoolRepository = schoolRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Result createSchool(String name) {
        Result result = checkSchoolName(name);
        if (result != Result.Ok) return result;
        School school = new School(name);
        school.setName(name);
        schoolRepository.save(school);
        return Result.Ok;
    }

    public School getSchoolByName(String name) {
        return schoolRepository.findSchoolByName(name);
    }


    public List<Teacher> getTeachersBySchool(School school) {
        return userRepository.findTeachersBySchoolId(school.getId());
    }

    @Transactional
    public void startNewYearForSchoolId(Long schoolId) {
        schoolClassRepository.deleteBySchoolIdAndClassNumber(schoolId, 11);
        schoolClassRepository.incrementClassNumbersBySchoolId(schoolId);
    }

    private Result checkSchoolName(String name) {
        if (name == null) return Result.NameIsNull;
        if (name.length() < StringLengthConstants.SchoolName.getMinLength()) return Result.TooShortSchoolName;
        if (name.length() > StringLengthConstants.SchoolName.getMaxLength()) return Result.TooLongSchoolName;
        if (schoolRepository.findSchoolByName(name) != null) return Result.SchoolIsExists;
        return Result.Ok;
    }
}