package com.example.pc700demo;

public class PrintCommand {
	
	/**
	 * 初始化打印机
	 * @return
	 */
	public static byte[] getCmdEsc() {
		return new byte[] { 0x1B, 0x40 };
	}
	
	/**
	 * 换行
	 * @return
	 */
	public static byte[] getCmdLF() {
		return new byte[] { 0x0A };
	}
	
	/**
	 * 字体大小， n=1 两倍字体大小 n=0 还原字体大小
	 * @param n
	 * 打印机在接收命令之后将根据n值选择不同字号打印；默认为n=0选择24*24点阵汉字打印 
 	 * 当n=0选择24*24点阵汉字打印 
 	 * 当n=1选择48*48点阵汉字打印 
 	 * 当n= 选择16*16点阵汉字打印
	 * @return
	 */
	public static byte[] getCmdTS(byte n) {
		return new byte[] { 0x1B, 0x38, n };
	}
	
	/**
	 * 允许/禁止粗体打印
	 * @param n
	 * n＝1，允许粗体打印；n＝0，禁止粗体打印。
	 * @return
	 */
	public static byte[] getCmdN(byte n) {
		return new byte[] {0x1B, 0x01, n};
	}
	
	/**
	 * 允许/禁止反白打印
	 * @param n=1，允许反白打打印；n=0，禁止反白打打印
	 * @return
	 */
	public static byte[] getCmdI(byte n) {
		return new byte[] { 0x1B, 0x69, n };
	}
	
	/**
	 * 设置右限
	 * @param n
	 * 右限指打印纸右侧不打印的字符数，每个字符的宽度12+字间距（热敏）计算。
	 * n 的值应在 0 到该型号打印机的行宽之内。默认值n=0，即没有右限。
	 * 该命令设置的是绝对位置，不受字符放大命令 ESC U 和 ESC W 的影响。
	 * 该命令设置之后，只要达到右限位置，打印机会自动加入回车换行
	 * @return
	 */
	public static byte[] getCmdQ(byte n) {
		return new byte[] { 0x1B, 0x51, n };
	}
	
	/**
	 * 设置左限
	 * @param n
	 * 左限指打印纸左侧不打印的字符数，每个字符数的宽度按12+字间距计算，
	 * n的数值应在0到该型号打印机的行宽之内。默认值n＝0，即没有左限.
	 * 该命令设置的是绝对位置，不受字符放大信命令以ＥＳＣ Ｕ　和ＥＳＣ W的影响
	 * @return
	 */
	public static byte[] getCmd1(byte n) {
		return new byte[] { 0x1B, 0x6C, n };
	}
	
	/**
	 * 允许/禁止下划线打印
	 * @param n
	 * n＝1，允许下划线打印；N＝0，禁止下划线打印，上电或初始化后N＝0。
	 * 允许下划线打印之后的所有字符、汉字包括空格都将打印出下划线。
	 * @return
	 */
	public static byte[] getCmd_(byte n) {
		return new byte[] { 0x1B, 0x2D, n };
	}
}
