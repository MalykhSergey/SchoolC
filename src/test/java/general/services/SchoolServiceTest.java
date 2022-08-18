package general.services;

import general.entities.School;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import general.utils.Result;
import general.utils.StringLengthConstants;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

class SchoolServiceTest {
    SchoolRepos schoolRepos = Mockito.mock(SchoolRepos.class);
    SchoolClassRepos schoolClassRepos = Mockito.mock(SchoolClassRepos.class);
    UserRepos userRepos = Mockito.mock(UserRepos.class);
    SchoolService schoolService = new SchoolService(schoolRepos, schoolClassRepos, userRepos);

    @Test
    void createSchool() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(StringLengthConstants.SchoolName.getMinLength());
        assertEquals(schoolService.createSchool(stringBuilder.toString()), Result.Ok);
        stringBuilder.setLength(StringLengthConstants.SchoolName.getMaxLength());
        assertEquals(schoolService.createSchool(stringBuilder.toString()), Result.Ok);
        stringBuilder.setLength(StringLengthConstants.SchoolName.getMinLength()-1);
        assertEquals(schoolService.createSchool(stringBuilder.toString()), Result.TooShortSchoolName);
        stringBuilder.setLength(StringLengthConstants.SchoolName.getMaxLength()+1);
        assertEquals(schoolService.createSchool(stringBuilder.toString()), Result.TooLongSchoolName);
        stringBuilder.setLength(StringLengthConstants.SchoolName.getMaxLength());
        assertEquals(schoolService.createSchool(null), Result.NameIsNull);
        Mockito.when(schoolRepos.findSchoolByName(anyString())).thenReturn(new School("school"));
        assertEquals(schoolService.createSchool(stringBuilder.toString()), Result.SchoolIsExists);
    }
}