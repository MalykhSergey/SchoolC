package general.controllers;

import general.controllers.dto.SchoolDTO;
import general.services.SchoolService;
import general.utils.Result;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class SchoolControllerTester {
    SchoolService schoolService = Mockito.mock(SchoolService.class);
    Model model = new ConcurrentModel();
    SchoolDTO schoolDTO = Mockito.mock(SchoolDTO.class);
    SchoolController schoolController = new SchoolController(schoolService);

    @Test
    public void testAddSchoolPostOnCompleted() {
        Mockito.when(schoolService.checkSchoolName(any())).thenReturn(Result.Ok);
        schoolController.addSchoolPost(schoolDTO,model);
        assertEquals(model.getAttribute("completed"), "Школа успешно добавлена");
    }

    @Test
    public void testAddSchoolPostOnSchoolError(){
        Mockito.when(schoolService.checkSchoolName(any())).thenReturn(Result.InvalidSchoolName);
        schoolController.addSchoolPost(schoolDTO,model);
        assertEquals(model.getAttribute("error"), Result.InvalidSchoolName.getError());
    }
}
