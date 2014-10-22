package com.cmcc.nativepackage;

/**
 * @author 作�? E-mail: WangJC
 * @version 创建时间�?013-10-11 下午4:14:26 <br>
 * 类说�?打印�?
 * <p>so库文件名:CMCC_PRINT_厂家英文名缩写_厂家设备型号缩写.so
 */
public class Printer {

	static {
		System.loadLibrary("CMCC_UNITDEVICE_WEWINS_A8");
	}

	/**
	 * <b><i>public static native int openPrinter(int printerType,String
	 * deviceId,String password);</i></b>
	 * <p>
	 * 连接打印�?br>
	 * password是为了将来可能出现的wifi设备预留的参数�?
	 * 
	 * @param printerType
	 *            打印机类型：<i>1：USB打印�?包括USB智能终端外设�?��机中的打印机)
	 *            2：蓝牙打印机（包括蓝牙智能终端外设一体机中的打印机） 3：串口打印机（包括串口智能终端外设一体机中的打印机）
	 *            4、内置打印机（包括智能终端一体机中的打印机）</i>
	 * @param deviceId
	 *            设备ID�?i>当printerType�?时，deviceId为打印机MAC地址�?
	 *            当printerType�?时，deviceId为USB �?�?转换的串口，�?dev/ttyUSB0�?</i>
	 * @param password
	 *            密码�?i>预留字段</i>
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int openPrinter(int printerType, String deviceId,
			String password);

	/**
	 * <b><i>public static native int closePrinter();</i></b>
	 * <p>
	 * 关闭与打印机的连�?
	 * 
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int closePrinter();

	/**
	 * <b><i>public static native int getPrinterVersion(byte[] version);</i></b>
	 * <p>
	 * 获取各厂商打印机组件的版本信息�?
	 * 
	 * @param version
	 *            版本号：<i>各厂商打印机组件的版本号(三位整数)，数值越大代表版本越高�?</i>
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int getPrinterVersion(byte[] version);

	/**
	 * <b><i>public static native int initialPrinter();</i></b>
	 * <p>
	 * 初始化打印机，清除打印缓冲区中的数据，复位打印机打印参数到打印机缺省参数�?br>
	 * 不是完全恢复到出厂设置，只是将打印指令参数恢复到打印机缺省参数�?<br>
	 * 点距是指打印的内容每个点之间的距离�?<br>
	 * 具体缺省设置包括如下�?
	 * <p>
	 * <li>1.字体宽高缩放比例�?�?br>
	 * <li>2.对齐方式：左对齐�?br>
	 * <li>3.左边距：0个点距；<br>
	 * <li>4.右边�? 0个点距；<br>
	 * <li>5.行间距：8个点距；<br>
	 * <li>6.字符间距�?个点距；<br>
	 * <li>7.打印方向：横打；<br>
	 * <li>8.非粗体打印；<br>
	 * <li>9.非下划线打印�?br>
	 * <li>10.非反白打印�?
	 * 
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int initialPrinter();

	/**
	 * <b><i>public static native int setZoonIn(int widthZoonIn,int
	 * heightZoonIn);</i></b>
	 * <p>
	 * 设置打印机字符串的字符宽高缩放比例�?<br>
	 * 打印机字体宽高缩放比例缺省设置为 1�?br>
	 * 厂商必须支持宽高缩放比为正常字体两�?或以上�?
	 * 
	 * @param widthZoonIn
	 *            字体放大宽度�?i>相比正常字体宽度的�?数，必须是正整数�?/i>
	 * @param heightZoonIn
	 *            字体放大高度�?i>相比正常字体高度的�?数，必须是正整数�?/i>
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int setZoonIn(int widthZoonIn, int heightZoonIn);

	/**
	 * <b><i>public static native int setAlignType(int alignType);</i></b>
	 * <p>
	 * 设置打印机字符串的对齐方式�?<br>
	 * <li>1.打印机缺省设置为左对齐�?
	 * <li>2.仅在�?���?��处理时，该命令才有效�?
	 * <li>3.竖向打印不支持设置对齐方式，默认上对齐�?
	 * 
	 * @param alignType
	 *            对齐方式�?i>0：左对齐�?1：居中对齐； 2：右对齐 </i>
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int setAlignType(int alignType);

	/**
	 * <b><i>public static native int setLeftMargin(int n);</i></b>
	 * <p>
	 * 设置打印机每行字符左边距为n 个点�?br>
	 * <li>1.左边距位置表示打印内容的左侧边缘位置�?
	 * <li>2.点距是指打印的内容每个点之间的距离�?
	 * <li>3.竖向打印不支持设置对齐方式，默认上对齐�?
	 * 
	 * @param n
	 *            左边点距
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int setLeftMargin(int n);

	/**
	 * <b><i>public static native int setRightMargin(int n);</i></b>
	 * <p>
	 * 设置打印机每行字符右边距为n 个点�?br>
	 * <li>1. 右边距位置表示打印内容的右侧边缘位置�?
	 * <li>2. 点距是指打印的内容每个点之间的距离�?
	 * 
	 * @param n
	 *            右边点距
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int setRightMargin(int n);

	/**
	 * <b><i>public static native int setLineSpacingByDotPitch(int n);</i></b>
	 * <p>
	 * 设置打印机字符串的字符行间距�?n 个垂直点�?
	 * <li>1.打印机行间距缺省设置�?�?
	 * <li>2.仅在�?���?��处理时，该命令才有效�?
	 * <li>3. 点距是指打印的内容每个点之间的距离�?
	 * 
	 * @param n
	 *            垂直点距
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int setLineSpacingByDotPitch(int n);

	/**
	 * <b><i>public static native int setWordSpacingByDotPitch(int n);</i></b>
	 * <p>
	 * 设置打印机字符串的字符间距为 n个水平点�?
	 * <li>1.打印机字符间距缺省设置为0�?
	 * <li>2.在�?宽模式下，字符右侧间距是正常值的两�?。当字符被放大时，字符右侧间距被放大同样的�?数�?该函数同时影响英文和汉字字符的设定�?
	 * <li>3. 点距是指打印的内容每个点之间的距离�?
	 * 
	 * @param n
	 *            水平点距�?i>取�?范围�?�?n �?255</i>
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int setWordSpacingByDotPitch(int n);

	/**
	 * <b><i>public static native int setPrintOrientation(int
	 * printOrientation);</i></b>
	 * <p>
	 * 设置打印机字符串的字符间距为 n个水平点�?
	 * <li>1.打印机缺省设置为横打�?
	 * <li>2.设置打印方向后，�?��的打印都按照此格式打印�?
	 * <li>3.竖向不支持设置对齐方式，默认上对齐�?
	 * 
	 * @param printOrientation
	 *            打印方向�?i>0：竖�?1：横�?</i>
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int setPrintOrientation(int printOrientation);

	/**
	 * <b><i>public static native int setBold(int n);</i></b>
	 * <p>
	 * 设置打印机字符串是否粗体打印<br>
	 * 打印机缺省设置为非粗体打印�?
	 * 
	 * @param n
	 *            是否粗体�?i>0：取消粗体打印设置；1：设置粗体打�?/i>
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int setBold(int n);

	/**
	 * <b><i>public static native int setUnderLine(int n);</i></b>
	 * <p>
	 * 设置打印机字符串是否下划线打�?br>
	 * 打印机缺省设置为非下划线打印�?
	 * 
	 * @param n
	 *            是否下划线打印：<i>0：取消下划线打印�?：设置下划线打印</i>
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int setUnderLine(int n);

	/**
	 * <b><i>public static native int setInverse(int n);</i></b>
	 * <p>
	 * 设置打印机字符串是否反白打印<br>
	 * 打印机缺省设置为非反白打印�?
	 * 
	 * @param n
	 *            是否反白打印�?i>0：取消反白打印； 1：设置反白打�?</i>
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int setInverse(int n);
	
	/**
	 * <b><i>public static native int print(String content);</i></b>
	 * <p>
	 * 打印字符�?br>
	 * 当每行数据超出打印纸张宽度时打印机输出自动换行�?
	 * 
	 * @param content
	 *            打印字符串：<i>打印字符串，可包括多行打印数据，使用”\n”表示换行�? </i>
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int print(String content);
	
	/**
	 * <b><i>public static native int printHTML(String content);</i></b>
	 * <p>
	 * 打印HTML格式数据<br>
	 * 支持常用html标签�?
	 * 
	 * @param content
	 *            HTML格式数据
	 * @return <li>0�?Success；状态码�? Fail
	 */
	public static native int printHTML(String content);
	
	public static native int printHTML(byte[] content, int len);

}
