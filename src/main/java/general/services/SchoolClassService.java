package general.services;

import general.entities.School;
import general.entities.SchoolClass;
import general.reposes.SchoolClassRepos;
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
        return schoolClassRepos.findAllBySchoolOrderByClassNumber(school);
    }

    public boolean isClassExistsInSchool(String name, int number, School school) {
        return schoolClassRepos.findSchoolClassByNameAndClassNumberAndSchool(name,number, school) != null;
    }


    public boolean checkClassName(String name) {
        return name.length() < 20;
    }

}
