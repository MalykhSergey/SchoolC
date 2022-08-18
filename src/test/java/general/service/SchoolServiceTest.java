package general.service;

import general.entity.School;
import general.repository.SchoolClassRepository;
import general.repository.SchoolRepository;
import general.repository.UserRepository;
import general.util.Result;
import general.util.StringLengthConstants;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

class SchoolServiceTest {
    SchoolRepository schoolRepository = Mockito.mock(SchoolRepository.class);
    SchoolClassRepository schoolClassRepository = Mockito.mock(SchoolClassRepository.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    SchoolService schoolService = new SchoolService(schoolRepository, schoolClassRepository, userRepository);

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
        Mockito.when(schoolRepository.findSchoolByName(anyString())).thenReturn(new School("school"));
        assertEquals(schoolService.createSchool(stringBuilder.toString()), Result.SchoolIsExists);
    }
}