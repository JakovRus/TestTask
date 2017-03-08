package ru.jakovRus.windows.entityWindows.studentsWindows;

import javax.annotation.Nullable;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;


/**
 * @author Jakov Rogov
 */
public class StudentsWrapperWindow extends Window {
    public StudentsWrapperWindow(UI myUI, @Nullable String numberOfGroup) {
        super("Список студентов");
        setResizable(false);
        center();
        setWidth("660px");
        setContent(numberOfGroup == null ? new AllStudentsWindow(myUI) : new StudentsByGroupWindow(myUI, numberOfGroup));
    }
}
