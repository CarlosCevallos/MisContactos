package aynimake.com.miscontactos.entity;

import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd;

import aynimake.com.miscontactos.util.DatabaseHelper;

/**
 * Created by Toshiba on 31/03/2015.
 */
public class ContactoProvider extends OrmLiteSimpleContentProvider<DatabaseHelper> {

    @Override
    protected Class<DatabaseHelper> getHelperClass() {
        return DatabaseHelper.class;
    }

    @Override
    public boolean onCreate() {
        MatcherController controller = new MatcherController();
        controller.add(Contacto.class, MimeTypeVnd.SubType.DIRECTORY, "", ContactoContract.CONTENT_URI_PATTERN_MANY);
        controller.add(Contacto.class, MimeTypeVnd.SubType.ITEM, "", ContactoContract.CONTENT_URI_PATTERN_ONE);
        setMatcherController(controller);

        return true;
    }
}
