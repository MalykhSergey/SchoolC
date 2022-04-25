package general.services;

import general.entities.School;
import general.entities.SchoolClass;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SchoolClassService {

    private UserRepos userRepos;
    private SchoolRepos schoolRepos;
    private SchoolClassRepos schoolClassRepos;

    @Autowired
    public SchoolClassService(UserRepos userRepos, SchoolRepos schoolRepos, SchoolClassRepos schoolClassRepos) {
        this.userRepos = userRepos;
        this.schoolRepos = schoolRepos;
        this.schoolClassRepos = schoolClassRepos;
    }

    @Transactional
    public void createNewSchoolClass(String name, School school) {
        SchoolClass schoolClass = new SchoolClass(name, school);
        schoolClassRepos.save(schoolClass);
        schoolRepos.addClassToSchoolById(school.getId(), schoolClass.getId());
    }

    public boolean checkClass(String name) {
        return schoolClassRepos.findSchoolClassByName(name) != null;
    }


    public boolean checkClassName(String name) {
        return name.length() < 6;
    }

}
