package aynimake.com.miscontactos.util;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Toshiba on 23/02/2015.
 *
 * Temporada 2
 * Tutorial Android | Cap. 11 | ORMLite y DatabaseHelper
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    public static void main(String[] args) throws IOException, SQLException {
        writeConfigFile("ormlite_config.txt");
    }

}
