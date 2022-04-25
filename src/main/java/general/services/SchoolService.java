package general.services;

import general.utils.CheckDataBoolAnswer;
import general.entities.School;
import general.reposes.SchoolRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class SchoolService {
    private SchoolRepos schoolRepos;

    @Autowired
    public SchoolService(SchoolRepos schoolRepos) {
        this.schoolRepos = schoolRepos;
    }

    @Transactional
    public void createSchool(String name) {
        School school = new School(name);
        school.setName(name);
        schoolRepos.save(school);
    }

    public CheckDataBoolAnswer checkSchoolName(String name, Model model) {
        if (name == null) {
            return new CheckDataBoolAnswer(false, "Введите имя!");
        }
        if (schoolRepos.findSchoolByName(name) != null) {
            return new CheckDataBoolAnswer(false, "Такая школа уже существует");
        }
        if (name.length() < 6) {
            return new CheckDataBoolAnswer(false, "Введите полное название");
        }
        return new CheckDataBoolAnswer(true, null);
    }
}
