package general.controllers;

import general.controllers.forms.SchoolForm;
import general.services.SchoolService;
import general.utils.ResultOfInputDataChecking;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class SchoolControllerTester {
    SchoolService schoolService = Mockito.mock(SchoolService.class);
    Model model = new ConcurrentModel();
    SchoolForm schoolForm = Mockito.mock(SchoolForm.class);
    SchoolController schoolController = new SchoolController(schoolService);

    @Test
    public void testAddSchoolPostOnCompleted() {
        Mockito.when(schoolService.checkSchoolName(any())).thenReturn(new ResultOfInputDataChecking(true,""));
        schoolController.addSchoolPost(schoolForm,model);
        assertEquals(model.getAttribute("completed"), "Школа успешно добавлена");
    }

    @Test
    public void testAddSchoolPostOnSchoolError(){
        Mockito.when(schoolService.checkSchoolName(any())).thenReturn(new ResultOfInputDataChecking(false,"Error!"));
        schoolController.addSchoolPost(schoolForm,model);
        assertEquals(model.getAttribute("error"), "Error!");
    }
}
