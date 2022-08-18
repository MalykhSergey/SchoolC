package general.services;

import general.entities.School;
import general.entities.Teacher;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import general.utils.Result;
import general.utils.StringLengthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SchoolService {
    private final SchoolRepos schoolRepos;
    private final SchoolClassRepos schoolClassRepos;
    private final UserRepos userRepos;

    @Autowired
    public SchoolService(SchoolRepos schoolRepos, SchoolClassRepos schoolClassRepos, UserRepos userRepos) {
        this.schoolRepos = schoolRepos;
        this.schoolClassRepos = schoolClassRepos;
        this.userRepos = userRepos;
    }

    @Transactional
    public Result createSchool(String name) {
        Result result = checkSchoolName(name);
        if (result != Result.Ok) return result;
        School school = new School(name);
        school.setName(name);
        schoolRepos.save(school);
        return Result.Ok;
    }

    public School getSchoolByName(String name) {
        return schoolRepos.findSchoolByName(name);
    }


    public List<Teacher> getTeachersBySchool(School school) {
        return userRepos.findTeachersBySchoolId(school.getId());
    }

    @Transactional
    public void startNewYearForSchoolId(Long schoolId) {
        schoolClassRepos.deleteBySchoolIdAndClassNumber(schoolId, 11);
        schoolClassRepos.incrementClassNumbersBySchoolId(schoolId);
    }

    private Result checkSchoolName(String name) {
        if (name == null) return Result.NameIsNull;
        if (name.length() < StringLengthConstants.SchoolName.getMinLength()) return Result.TooShortSchoolName;
        if (name.length() > StringLengthConstants.SchoolName.getMaxLength()) return Result.TooLongSchoolName;
        if (schoolRepos.findSchoolByName(name) != null) return Result.SchoolIsExists;
        return Result.Ok;
    }
}