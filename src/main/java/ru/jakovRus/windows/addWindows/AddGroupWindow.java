package ru.jakovRus.windows.addWindows;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.jakovRus.entity.Group;
import ru.jakovRus.service.GroupService;
import ru.jakovRus.util.EntityNotUniqueException;
import ru.jakovRus.windows.abstractWindows.AbstractModalWindow;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author Jakov Rogov
 */
public class AddGroupWindow extends AbstractModalWindow {
    protected TextField numberOfGroup = new TextField("Номер группы *");
    protected TextField nameOfFaculty = new TextField("Название факультета *");
    protected Label message = new Label("* - поля, обязательные для заполнения");

    protected Button okBtn = new Button("OK");
    protected Button cancelBtn = new Button("Отменить");

    protected Group group;
    protected GroupService service = GroupService.getInstance();

    public AddGroupWindow() {
        super("Добавление группы");
        center();
        setWidth("400px");
        setModal(true);
        setResizable(false);

        okBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        okBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        HorizontalLayout buttons = new HorizontalLayout(okBtn, cancelBtn);
        buttons.setSpacing(true);
        buttons.addStyleName("add-buttons-layout");

        FormLayout form = new FormLayout(numberOfGroup, nameOfFaculty, message, buttons);
        form.addStyleName("group-form-layout");

        addFocusTraversal(numberOfGroup, nameOfFaculty, ShortcutAction.KeyCode.ENTER);
        addListeners();
        addValidators();

        setContent(form);
    }

    protected void addClickListenerForOkBtn() {
        okBtn.addClickListener(e -> {
            if (!validateFields()) {
                return;
            }

            trimFields();
            try {
                service.save(group);
            } catch (EntityNotUniqueException error) {
                Notification.show("Группа с данным номером уже существует", Notification.Type.ERROR_MESSAGE);
                return;
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

    public void setGroup(Group group) {
        this.group = group;
        this.group.setNameOfFaculty("");
        this.group.setNumberOfGroup("");

        BeanFieldGroup.bindFieldsUnbuffered(this.group, this);
    }

    protected void addValidators() {
        addCustomValidator(numberOfGroup, "Номер группы должен содержать от 1 до 25 символов", 25);
        addCustomValidator(nameOfFaculty, "Название факультета должно содержать от 1 до 50 символов", 50);
    }

    protected boolean validateFields() {
        if (!validate(numberOfGroup)) {
            if (numberOfGroup.getValue().trim().isEmpty()) {
                numberOfGroup.setValue("");
            }
            return false;
        }

        if (!validate(nameOfFaculty)) {
            if (nameOfFaculty.getValue().trim().isEmpty()) {
                nameOfFaculty.setValue("");
            }
            return false;
        }

        return true;
    }

    protected void trimFields() {
        group.setNumberOfGroup(group.getNumberOfGroup().trim());
        group.setNameOfFaculty(group.getNameOfFaculty().trim());
    }
}