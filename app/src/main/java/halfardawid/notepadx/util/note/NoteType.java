/*
 * Copyright (c) 2017 anno Domini.
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

package halfardawid.notepadx.util.note;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public final class NoteType {

    public static final String TYPE = "TYPE";
    public static final String GET_NEW_INTENT = "getNewIntent";
    public static final String NAME_TYPE = "NAME_TYPE";

    public final Class type;
    public final String name;

    public NoteType(Context con, Class tp) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
        type = tp;
        name = (con != null) ? get_name_type(con) : "No context provided";
        //kind of temporary fail-safe... or rather fail->instant
        get_new_intent_method();
        get_type();
    }

    public Intent get_editor_intent(Context con) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (Intent) get_new_intent_method().invoke(null, con);
    }

    public String get_type() throws NoSuchFieldException, IllegalAccessException {
        return (String) type.getDeclaredField(TYPE).get(null);
    }

    private Method get_new_intent_method() throws NoSuchMethodException {
        return type.getDeclaredMethod(GET_NEW_INTENT, Context.class);
    }

    public String get_name_type(Context con) throws IllegalAccessException, NoSuchFieldException {
        return con.getString(get_name_type_field(type).getInt(null));
    }

    private Field get_name_type_field(Class tp) throws NoSuchFieldException {
        return tp.getDeclaredField(NAME_TYPE);
    }

    public boolean is(String type) throws NoSuchFieldException, IllegalAccessException {
        return type.equals(get_type());
    }

    public Note build(UUID uuid, String data, String title) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (Note) type.getConstructor(UUID.class,String.class,String.class).newInstance(uuid,data,title);
    }
}
