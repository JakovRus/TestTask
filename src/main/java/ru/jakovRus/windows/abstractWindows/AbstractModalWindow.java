package ru.jakovRus.windows.abstractWindows;

import com.vaadin.data.Validator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import ru.jakovRus.util.CustomValidator;

import javax.annotation.Nullable;

/**
 * @author Jakov Rogov
 */
public class AbstractModalWindow extends Window {
    protected Listener entityCreatedListener;

    public AbstractModalWindow(String caption) {
        super(caption);
    }

    public void addCreatedListener(Listener listener) {
        this.entityCreatedListener = listener;
    }

    protected void addAndClose() {
        if (this.entityCreatedListener != null) {
            this.entityCreatedListener.componentEvent(null);
        }
        close();
    }

    protected <T extends AbstractField & FieldEvents.BlurNotifier & FieldEvents.FocusNotifier> void addFocusTraversal
            (T fieldName, @Nullable T focusedFieldName, int keyCode) {
        ShortcutListener shortcut = new ShortcutListener("focus", keyCode, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                if (focusedFieldName != null) focusedFieldName.focus();
            }
        };

        fieldName.addFocusListener(e -> {
            addShortcutListener(shortcut);
        });

        fieldName.addBlurListener(e -> {
            removeShortcutListener(shortcut);
        });
    }

    protected <T extends AbstractField> void addCustomValidator(T field, String message, int maxLength) {
        field.addValidator(new CustomValidator(message, maxLength));
        field.setValidationVisible(false);
    }

    protected <T extends AbstractField> boolean validate(T field) throws Validator.InvalidValueException {
        try {
            field.validate();
        } catch (Validator.InvalidValueException error) {
            field.focus();
            Notification.show(error.getMessage(), Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
