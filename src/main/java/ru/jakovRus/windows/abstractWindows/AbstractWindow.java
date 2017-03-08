package ru.jakovRus.windows.abstractWindows;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.jakovRus.entity.Entity;
import ru.jakovRus.service.Service;

/**
 * @author Jakov Rogov
 */
public class AbstractWindow<T extends Entity> extends VerticalLayout {
    protected UI myUI;
    protected Service<T> service;
    protected Grid grid = new Grid();

    protected Button addBtn = new Button("Добавить");
    protected Button deleteBtn = new Button("Удалить");
    protected Button editBtn = new Button("Изменить");

    protected HorizontalLayout buttons = new HorizontalLayout(addBtn, deleteBtn, editBtn);

    public AbstractWindow() {
        deleteBtn.setVisible(false);
        editBtn.setVisible(false);

        addBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        deleteBtn.setClickShortcut(ShortcutAction.KeyCode.DELETE);
        buttons.setSpacing(true);

        addLayoutClickListener();
    }

    protected void addLayoutClickListener() {
        addLayoutClickListener(e -> {
            if (!e.getSource().equals(grid)) {
                grid.deselectAll();
            }
        });
    }
}
