package ru.jakovRus.windows.deletionWindows;

import ru.jakovRus.entity.Group;
import ru.jakovRus.service.GroupService;

/**
 * @author Jakov Rogov
 */
public class DeletionGroupWindow extends DeletionWindow {

    public DeletionGroupWindow(Group group) {
        addListeners(group);
    }

    protected void addListeners(Group group) {
        addClickListenerForOkBtn(group);
        addClickListenerForCancelBtn();
    }

    protected void addClickListenerForCancelBtn() {
        cancelBtn.addClickListener(e -> close());
    }

    protected void addClickListenerForOkBtn(Group group) {
        okBtn.addClickListener(e -> {
            GroupService service = GroupService.getInstance();
            service.delete(group);
            addAndClose();
        });
    }
}
