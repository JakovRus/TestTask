package ru.jakovRus.windows.deletionWindows;

import ru.jakovRus.entity.Student;
import ru.jakovRus.service.StudentService;

/**
 * @author Jakov Rogov
 */
public class DeletionStudentWindow extends DeletionWindow {
    public DeletionStudentWindow(Student student) {
        addListeners(student);
    }

    protected void addListeners(Student student) {
        addClickListenerForOkBtn(student);
        addClickListenerForCancelBtn();
    }

    protected void addClickListenerForCancelBtn() {
        cancelBtn.addClickListener(e -> close());
    }

    protected void addClickListenerForOkBtn(Student student) {
        okBtn.addClickListener(e -> {
            StudentService service = StudentService.getInstance();
            service.delete(student);
            addAndClose();
        });
    }
}
