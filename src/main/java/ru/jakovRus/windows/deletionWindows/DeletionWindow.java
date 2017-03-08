package ru.jakovRus.windows.deletionWindows;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.jakovRus.windows.abstractWindows.AbstractModalWindow;

/**
 * @author Jakov Rogov
 */
public class DeletionWindow extends AbstractModalWindow {
    protected Button okBtn = new Button("OK");
    protected Button cancelBtn = new Button("Отменить");
    protected Label message = new Label("Вы действительно хотите удалить запись?");

    public DeletionWindow() {
        super("Удаление записи");
        center();
        setWidth("335px");
        setResizable(false);
        setModal(true);

        message.addStyleName("message");
        okBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        okBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        HorizontalLayout buttons = new HorizontalLayout(okBtn, cancelBtn);
        buttons.setSpacing(true);
        buttons.addStyleName("deletion-buttons-layout");

        VerticalLayout layout = new VerticalLayout(message, buttons);

        setContent(layout);
    }
}
