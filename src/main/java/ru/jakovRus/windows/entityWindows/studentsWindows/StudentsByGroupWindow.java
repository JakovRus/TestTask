package ru.jakovRus.windows.entityWindows.studentsWindows;

import com.vaadin.ui.UI;
import ru.jakovRus.entity.Student;
import ru.jakovRus.windows.addWindows.AddStudentByGroupWindow;
import ru.jakovRus.windows.deletionWindows.DeletionStudentWindow;
import ru.jakovRus.windows.editWindows.EditStudentWindow;


/**
 * @author Jakov Rogov
 */
public class StudentsByGroupWindow extends AllStudentsWindow {
    private String numberOfGroup;

    public StudentsByGroupWindow(UI myUI, String numberOfGroup) {
        super(myUI);
        this.numberOfGroup = numberOfGroup;

        groupFilter.setVisible(false);
        clearGroupBtn.setVisible(false);

        grid.setColumns("surname", "name", "patronymic", "birthdate");
        grid.getColumn("name").setHeaderCaption("Имя");
        grid.getColumn("surname").setHeaderCaption("Фамилия");
        grid.getColumn("patronymic").setHeaderCaption("Отчество");
        grid.getColumn("birthdate").setHeaderCaption("Дата рождения");
        updateGrid(null, numberOfGroup);

        addComponents(grid, buttons);
    }

    @Override
    protected void addTextChangeListenerForSurnameFilter() {
        surnameFilter.addTextChangeListener(e -> {
            updateGrid(e.getText(), numberOfGroup);
        });
    }

    @Override
    protected void addClickListenerForClearSurnameBtn() {
        clearSurnameBtn.addClickListener(e -> {
            surnameFilter.clear();
            updateGrid(null, numberOfGroup);
        });
    }

    @Override
    protected void addListenerForAddBtn() {
        addBtn.addClickListener(e -> {
            AddStudentByGroupWindow modal = new AddStudentByGroupWindow();
            Student student = new Student();
            student.setNumberOfGroup(numberOfGroup);
            modal.setStudent(student);

            modal.addCreatedListener(event -> {
                updateGrid(null, numberOfGroup);
                surnameFilter.clear();
                groupFilter.clear();
            });
            myUI.addWindow(modal);
        });
    }

    @Override
    protected void addListenerForEditBtn() {
        editBtn.addClickListener(e -> {
            EditStudentWindow modal = new EditStudentWindow();
            modal.setStudent(selectedStudent);

            modal.addCreatedListener(event -> {
                updateGrid(surnameFilter.getValue(), numberOfGroup);
            });
            myUI.addWindow(modal);
        });
    }

    @Override
    protected void addListenerForDeleteBtn() {
        deleteBtn.addClickListener(e -> {
            DeletionStudentWindow modal = new DeletionStudentWindow(selectedStudent);

            modal.addCreatedListener(event -> {
                updateGrid(surnameFilter.getValue(), numberOfGroup);
            });
            myUI.addWindow(modal);
        });
    }

    @Override
    protected void addItemClickListenerForGrid() {
        grid.addItemClickListener(e -> {
            if (e.isDoubleClick()) {
                EditStudentWindow modal = new EditStudentWindow();
                modal.setStudent(selectedStudent);

                modal.addCreatedListener(internalEvent -> {
                    updateGrid(surnameFilter.getValue(), numberOfGroup);
                });
                myUI.addWindow(modal);
            }
        });
    }
}
