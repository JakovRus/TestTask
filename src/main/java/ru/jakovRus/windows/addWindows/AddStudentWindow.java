package ru.jakovRus.windows.addWindows;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.jakovRus.entity.Group;
import ru.jakovRus.entity.Student;
import ru.jakovRus.service.GroupService;
import ru.jakovRus.service.StudentService;
import ru.jakovRus.util.EntityNotUniqueException;
import ru.jakovRus.windows.abstractWindows.AbstractModalWindow;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * @author Jakov Rogov
 */
public class AddStudentWindow extends AbstractModalWindow {
    protected TextField name = new TextField("Имя *");
    protected TextField surname = new TextField("Фамилия *");
    protected TextField patronymic = new TextField("Отчество");
    protected DateField birthdate = new DateField("Дата рождения *");

    protected ComboBox numberOfGroup = new ComboBox("Номер группы *");

    protected Button okBtn = new Button("OK");
    protected Button cancelBtn = new Button("Отменить");

    protected Label message = new Label("* - поля, обязательные для заполнения");

    protected Student student;
    protected StudentService service = StudentService.getInstance();

    public AddStudentWindow() {
        super("Добавление студента");
        center();
        setModal(true);
        setWidth("360px");
        setResizable(false);
        message.setWidth("190px");

        updateComboBox();

        okBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        okBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        HorizontalLayout buttons = new HorizontalLayout(okBtn, cancelBtn);
        buttons.setSpacing(true);
        buttons.addStyleName("add-buttons-layout");

        FormLayout form = new FormLayout(surname, name, patronymic, birthdate, numberOfGroup, message, buttons);
        form.addStyleName("student-form-layout");

        addFocusTraversal(surname, name, ShortcutAction.KeyCode.ENTER);
        addFocusTraversal(name, patronymic, ShortcutAction.KeyCode.ENTER);
        addFocusTraversal(patronymic, birthdate, ShortcutAction.KeyCode.ENTER);
        addFocusTraversal(birthdate, numberOfGroup, ShortcutAction.KeyCode.ENTER);
        addListeners();
        addValidators();

        setContent(form);
    }

    protected void updateComboBox() {
        GroupService service = GroupService.getInstance();
        List<Group> groups = service.findAll(null, null);
        for (Group group : groups) {
            numberOfGroup.addItem(group.getNumberOfGroup());
        }
    }

    protected void addClickListenerForOkBtn() {
        okBtn.addClickListener(e -> {
            if (!validateFields()) {
                return;
            }

            trimFields();
            try {
                service.save(student);
            } catch (EntityNotUniqueException error) {
                System.err.println("[ERROR] student is not unique");
            }

            addAndClose();
        });
    }

    protected void addClickListenerForCancelBtn() {
        cancelBtn.addClickListener(e -> {
            close();
        });
    }

    protected void addListeners() {
        addClickListenerForOkBtn();
        addClickListenerForCancelBtn();
    }

    public void setStudent(Student student) {
        this.student = student;
        this.student.setName("");
        this.student.setSurname("");
        this.student.setPatronymic("");

        BeanFieldGroup.bindFieldsUnbuffered(this.student, this);
    }

    protected void addValidators() {
        addCustomValidator(surname, "Фамилия должна содержать от 1 до 25 символов", 25);
        addCustomValidator(name, "Имя должно содержать от 1 до 25 символов", 25);
        addCustomValidator(numberOfGroup, "Выберите группу", 25);
        birthdate.addValidator(new NullValidator("Введите дату рождения", false));
        birthdate.setValidationVisible(false);
    }

    protected boolean validateFields() {
        if (!validate(surname)) {
            if (surname.getValue().trim().isEmpty()) {
                surname.setValue("");
            }
            return false;
        }

        if (!validate(name)) {
            if (name.getValue().trim().isEmpty()) {
                name.setValue("");
            }
            return false;
        }

        if (!validate(birthdate)) {
            return false;
        }

        if (!validate(numberOfGroup)) {
            return false;
        }

        return true;
    }

    protected void trimFields() {
        student.setSurname(student.getSurname().trim());
        student.setName(student.getName().trim());
        student.setPatronymic(student.getPatronymic().trim());
    }
}
