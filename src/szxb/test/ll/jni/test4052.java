//////////////////////////////=============== BeiSmart 3 Start ===============//////////////////////////////
package szxb.test.ll.jni;
/**
 * For XiaoBing
 * @author Yongkun
 *
 */
import android.util.Log;

public class test4052 {

	private static final String TAG = "MyAndroid";

	public static native String stringFromJNI();


	public static native int testopen();

	public static native int testrelease();

	public static native int testioctl(int com);

	public static void myprintf() {
		Log.v(TAG, "ok");
	}

	static {
		System.loadLibrary("test4052-jni");
	}
}
//////////////////////////////=============== BeiSmart 3 End ===============//////////////////////////////
