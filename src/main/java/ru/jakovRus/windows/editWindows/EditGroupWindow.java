package ru.jakovRus.windows.editWindows;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import ru.jakovRus.entity.Group;
import ru.jakovRus.windows.addWindows.AddGroupWindow;

/**
 * @author Jakov Rogov
 */
public class EditGroupWindow extends AddGroupWindow {
    public EditGroupWindow() {
        super();
        setCaption("Редактирование группы");
    }

    @Override
    public void setGroup(Group group) {
        this.group = group;
        numberOfGroup.setValue(group.getNumberOfGroup());
        nameOfFaculty.setValue(group.getNameOfFaculty());

        BeanFieldGroup.bindFieldsUnbuffered(this.group, this);
    }
}
