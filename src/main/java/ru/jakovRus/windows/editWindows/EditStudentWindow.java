package ru.jakovRus.windows.editWindows;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import ru.jakovRus.entity.Student;
import ru.jakovRus.windows.addWindows.AddStudentWindow;

/**
 * @author Jakov Rogov
 */
public class EditStudentWindow extends AddStudentWindow {
    public EditStudentWindow() {
        setCaption("Редактирование студента");
    }

    @Override
    public void setStudent(Student student) {
        this.student = student;

        name.setValue(student.getName());
        surname.setValue(student.getSurname());
        patronymic.setValue(student.getPatronymic());
        birthdate.setValue(student.getBirthdate());
        numberOfGroup.setValue(student.getNumberOfGroup());

        BeanFieldGroup.bindFieldsUnbuffered(this.student, this);
    }
}
