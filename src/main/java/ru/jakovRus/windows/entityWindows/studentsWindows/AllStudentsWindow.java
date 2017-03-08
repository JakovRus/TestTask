package ru.jakovRus.windows.entityWindows.studentsWindows;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.jakovRus.entity.Student;
import ru.jakovRus.service.StudentService;
import ru.jakovRus.windows.abstractWindows.AbstractWindow;
import ru.jakovRus.windows.addWindows.AddStudentWindow;
import ru.jakovRus.windows.deletionWindows.DeletionStudentWindow;
import ru.jakovRus.windows.editWindows.EditStudentWindow;

import java.util.List;

/**
 * @author Jakov Rogov
 */
public class AllStudentsWindow extends AbstractWindow<Student> {
    protected Student selectedStudent;

    protected TextField surnameFilter = new TextField();
    protected Button clearSurnameBtn = new Button(FontAwesome.TIMES);

    protected TextField groupFilter = new TextField();
    protected Button clearGroupBtn = new Button(FontAwesome.TIMES);

    public AllStudentsWindow(UI myUI) {
        this.myUI = myUI;
        service = StudentService.getInstance();

        surnameFilter.setInputPrompt("Введите фамилию");
        groupFilter.setInputPrompt("Введите номер группы");
        groupFilter.setWidth("200px");

        CssLayout filterSurnameLayout = new CssLayout();
        filterSurnameLayout.addComponents(surnameFilter, clearSurnameBtn);
        filterSurnameLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filterSurnameLayout.addStyleName("filterLayout");

        CssLayout filterGroupLayout = new CssLayout();
        filterGroupLayout.addComponents(groupFilter, clearGroupBtn);
        filterGroupLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filterGroupLayout.addStyleName("filterLayout");

        HorizontalLayout filter = new HorizontalLayout(filterSurnameLayout, filterGroupLayout);

        grid.setColumns("surname", "name", "patronymic", "birthdate", "numberOfGroup");
        grid.getColumn("name").setHeaderCaption("Имя");
        grid.getColumn("surname").setHeaderCaption("Фамилия");
        grid.getColumn("patronymic").setHeaderCaption("Отчество");
        grid.getColumn("birthdate").setHeaderCaption("Дата рождения");
        grid.getColumn("numberOfGroup").setHeaderCaption("Номер группы");
        grid.setStyleName("students-grid");
        updateGrid(null, null);

        buttons.setStyleName("buttons-student-layout");

        addComponents(filter, grid, buttons);
        addListeners();
    }

    protected void addListeners() {
        addTextChangeListenerForSurnameFilter();
        addClickListenerForClearSurnameBtn();
        addTextChangeListenerForGroupFilter();
        addClickListenerForClearGroupBtn();
        addListenerForAddBtn();
        addListenerForEditBtn();
        addListenerForDeleteBtn();
        addSelectionListenerForGrid();
        addItemClickListenerForGrid();
        addContextClickListener();
    }

    protected void addContextClickListener() {
        addContextClickListener(e -> {
            if (!e.getSource().equals(grid)) {
                grid.deselectAll();
            }
        });
    }

    protected void addSelectionListenerForGrid() {
        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                deleteBtn.setVisible(false);
                editBtn.setVisible(false);
            } else {
                selectedStudent = (Student) e.getSelected().iterator().next();

                deleteBtn.setVisible(true);
                editBtn.setVisible(true);
            }
        });
    }

    protected void addClickListenerForClearGroupBtn() {
        clearGroupBtn.addClickListener(e -> {
            groupFilter.clear();
            updateGrid(surnameFilter.getValue(), null);
        });
    }

    protected void addClickListenerForClearSurnameBtn() {
        clearSurnameBtn.addClickListener(e -> {
            surnameFilter.clear();
            updateGrid(null, groupFilter.getValue());
        });
    }

    protected void addTextChangeListenerForGroupFilter() {
        groupFilter.addTextChangeListener(e -> {
            updateGrid(surnameFilter.getValue(), e.getText());
        });
    }

    protected void addTextChangeListenerForSurnameFilter() {
        surnameFilter.addTextChangeListener(e -> {
            updateGrid(e.getText(), groupFilter.getValue());
        });
    }

    protected void addListenerForAddBtn() {
        addBtn.addClickListener(e -> {
            AddStudentWindow modal = new AddStudentWindow();
            modal.setStudent(new Student());

            modal.addCreatedListener(event -> {
                updateGrid(null, null);
                surnameFilter.clear();
                groupFilter.clear();
            });
            myUI.addWindow(modal);
        });
    }

    protected void addListenerForEditBtn() {
        editBtn.addClickListener(e -> {
            EditStudentWindow modal = new EditStudentWindow();
            modal.setStudent(selectedStudent);

            modal.addCreatedListener(event -> {
                updateGrid(surnameFilter.getValue(), groupFilter.getValue());
            });
            myUI.addWindow(modal);
        });
    }

    protected void addListenerForDeleteBtn() {
        deleteBtn.addClickListener(e -> {
            DeletionStudentWindow modal = new DeletionStudentWindow(selectedStudent);

            modal.addCreatedListener(event -> {
                updateGrid(surnameFilter.getValue(), groupFilter.getValue());
            });
            myUI.addWindow(modal);
        });
    }

    protected void addItemClickListenerForGrid() {
        grid.addItemClickListener(e -> {
            if (e.isDoubleClick()) {
                EditStudentWindow modal = new EditStudentWindow();
                modal.setStudent(selectedStudent);

                modal.addCreatedListener(internalEvent -> {
                    updateGrid(surnameFilter.getValue(), groupFilter.getValue());
                });
                myUI.addWindow(modal);
            }
        });
    }

    protected void updateGrid(String surnameFilter, String groupFilter) {
        List<Student> entities = service.findAll(surnameFilter, groupFilter);
        grid.setContainerDataSource(new BeanItemContainer<>(Student.class, entities));
    }
}