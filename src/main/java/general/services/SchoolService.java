package general.services;

import general.entities.School;
import general.entities.Teacher;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import general.utils.Result;
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
    public void createSchool(String name) {
        School school = new School(name);
        school.setName(name);
        schoolRepos.save(school);
    }

    public School getSchoolByName(String name) {
        return schoolRepos.findSchoolByName(name);
    }

    public School getSchoolById(Long id) {
        return schoolRepos.findSchoolById(id);
    }

    public List<Teacher> getTeachersBySchool(School school) {
        return userRepos.findTeachersBySchoolId(school.getId());
    }

    @Transactional
    public void startNewYearForSchoolId(Long schoolId) {
        schoolClassRepos.deleteBySchoolIdAndClassNumber(schoolId, 11);
        schoolClassRepos.incrementClassNumbersBySchoolId(schoolId);
    }

    public Result checkSchoolName(String name) {
        if (name == null) {
            return Result.NameIsNull;
        }
        if (schoolRepos.findSchoolByName(name) != null) {
            return Result.SchoolIsExists;
        }
        if (name.length() < 6) {
            return Result.RequiredFullName;
        }
        return Result.Ok;
    }
}
