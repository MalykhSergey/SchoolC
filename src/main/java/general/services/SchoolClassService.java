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

    private final SchoolClassRepos schoolClassRepos;

    @Autowired
    public SchoolClassService(SchoolClassRepos schoolClassRepos) {
        this.schoolClassRepos = schoolClassRepos;
    }

    @Transactional
    public void createNewSchoolClass(String name, School school) {
        SchoolClass schoolClass = new SchoolClass(name, school);
        schoolClassRepos.save(schoolClass);
    }

    public SchoolClass getClassByName(String name) {
        return schoolClassRepos.findSchoolClassByName(name);
    }

    public SchoolClass getClassById(Long id) {
        return schoolClassRepos.findSchoolClassById(id);
    }

    public boolean checkClass(String name) {
        return schoolClassRepos.findSchoolClassByName(name) != null;
    }


    public boolean checkClassName(String name) {
        return name.length() < 6;
    }

}
