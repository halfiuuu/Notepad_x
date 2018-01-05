/*
 * Copyright (c) 2018 anno Domini.
 *
 * Code below is a part of
 * https://github.com/halfiuuu/Notepad_x
 * available for use under the
 * GNU Affero General Public License v3.0
 * as stated in
 * https://github.com/halfiuuu/Notepad_x/blob/master/LICENSE
 *
 * Created by Dawid Halfar
 * contact possible via halfardawid@gmail.com
 *
 * I'm not sure what else this thing should state... Whatever.
 */

package halfardawid.notepadx.activity.check_list_editor;

/**
 * Created by Dawid on 2018-01-05.
 */

interface CheckListEntryCallbacks {
    boolean alreadyShowingEdit();
    void setShowingEdit(CheckListEntryFragment entry);
    void clearShowing();
}
