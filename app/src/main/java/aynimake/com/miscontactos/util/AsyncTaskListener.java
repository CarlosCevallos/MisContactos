package aynimake.com.miscontactos.util;

/**
 * Created by Toshiba on 04/03/2015.
 */
public interface AsyncTaskListener<T> {
    void  processResult(T result);
}
