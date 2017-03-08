package ru.jakovRus.windows.entityWindows;


import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import ru.jakovRus.entity.Group;
import ru.jakovRus.service.GroupService;
import ru.jakovRus.service.StudentService;
import ru.jakovRus.windows.abstractWindows.AbstractWindow;
import ru.jakovRus.windows.addWindows.AddGroupWindow;
import ru.jakovRus.windows.deletionWindows.DeletionGroupWindow;
import ru.jakovRus.windows.editWindows.EditGroupWindow;
import ru.jakovRus.windows.entityWindows.studentsWindows.StudentsWrapperWindow;

import java.util.List;

/**
 * @author Jakov Rogov
 */
public class GroupsWindow extends AbstractWindow<Group> {
    private Group selectedGroup;
    private Button allStudentsBtn = new Button("Все студенты");

    public GroupsWindow(UI myUI) {
        this.myUI = myUI;
        service = GroupService.getInstance();

        addListeners(myUI);

        grid.setColumns("numberOfGroup", "nameOfFaculty");
        grid.getColumn("numberOfGroup").setHeaderCaption("Номер группы");
        grid.getColumn("nameOfFaculty").setHeaderCaption("Название факультета");
        grid.addStyleName("groups-grid");
        updateGrid(null, null);

        buttons.addComponent(allStudentsBtn);
        buttons.addStyleName("buttons-group-layout");

        addComponents(grid, buttons);
    }

    protected void addListeners(UI myUI) {
        addClickListenerForAllStudentBtn(myUI);
        addItemClickListenerForGrid(myUI);

        addClickListenerForAddBtn();
        addClickListenerForDeleteBtn();
        addClickListenerForEditBtn();
        addSelectionListenerForGrid();
    }

    protected void addItemClickListenerForGrid(UI myUI) {
        grid.addItemClickListener(e -> {
            if (e.isDoubleClick()) {
                myUI.addWindow(new StudentsWrapperWindow(this.myUI, selectedGroup.getNumberOfGroup()));
            }
        });
    }

    protected void addSelectionListenerForGrid() {
        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                deleteBtn.setVisible(false);
                editBtn.setVisible(false);
            } else {
                selectedGroup = (Group) e.getSelected().iterator().next();
                deleteBtn.setVisible(true);
                editBtn.setVisible(true);
            }
        });
    }

    protected void addClickListenerForEditBtn() {
        editBtn.addClickListener(e -> {
            EditGroupWindow modal = new EditGroupWindow();
            modal.setGroup(selectedGroup);
            modal.addCreatedListener(event -> {
                updateGrid(null, null);
            });
            this.myUI.addWindow(modal);
        });
    }

    protected void addClickListenerForDeleteBtn() {
        deleteBtn.addClickListener(e -> {
            if (!StudentService.getInstance().findAll(null, selectedGroup.getNumberOfGroup()).isEmpty()) {
                Notification.show("Невозможно удалить группу, содержащую студентов", Notification.Type.ERROR_MESSAGE);
                return;
            }

            DeletionGroupWindow modal = new DeletionGroupWindow(selectedGroup);
            modal.addCreatedListener(event -> {
                updateGrid(null, null);
            });
            this.myUI.addWindow(modal);
        });
    }

    protected void addClickListenerForAddBtn() {
        addBtn.addClickListener(e -> {
            AddGroupWindow modal = new AddGroupWindow();
            modal.setGroup(new Group());
            modal.addCreatedListener(event -> updateGrid(null, null));

            this.myUI.addWindow(modal);
        });
    }

    protected void addClickListenerForAllStudentBtn(UI myUI) {
        allStudentsBtn.addClickListener(e -> {
            myUI.addWindow(new StudentsWrapperWindow(this.myUI, null));
        });
    }

    protected void updateGrid(String facultyFilter, String groupFilter) {
        List<Group> entities = service.findAll(facultyFilter, groupFilter);
        grid.setContainerDataSource(new BeanItemContainer<>(Group.class, entities));
    }
}
