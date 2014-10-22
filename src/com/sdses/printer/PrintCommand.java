package com.sdses.printer;

public class PrintCommand {
	
	/**
	 * 初始化打印机
	 * @return
	 */
	public static byte[] getCmdEsc() {
		return new byte[] {0X1B, 0X40};
	}
	
	/**
	 * 设置要打印条形码水平对齐位置。
	 * n=1 靠左对齐；
	 * n=2 居中对齐； （默认值）
	 * n=3 靠右对齐；
	 * @param n
	 * @return
	 */
	public static byte[] getCmdGSp(byte n) {
		return new byte[] {0X1D, 0X70, n};
	}
	
	/**
	 * 回车
	 * @return
	 */
	public static byte[] getCmdCR() {
		return new byte[] {0X0D};
	}
	
	/**
	 * 空格
	 * @return
	 */
	public static byte[] getCmdNUL() {
		return new byte[] {0X00};
	}
	
	/**
	 * 换行
	 * @return
	 */
	public static byte[] getCmdLF() {
		return new byte[] {0X0A};
	}
	
	/**
	 * 打印机向前进给 n 点行。
	 * 。n 的值应在 1到 255范围之内。这个命令不发出回车换行。
	 * 它不会影响后面换行命令。 
	 * 如果你需要产生立即进给走纸而不要回车， 就可以使用 ESC J 命令。
	 * @param n
	 * @return
	 */
	public static byte[] getCmdJ(byte n) {
		return new byte[] {0X1B, 0X4A, n};
	}
	
	/**
	 * 为后面的换行命令设置 n点行间距。即设置两行之间的间距为 n 点。
	 * n 的值应当在 0 到 255之间。
	 * 在使用“ESC K”命令进行点阵图形打印时，通常设置 n=0，文本打印方式时通常设 n=3。
	 * @param n
	 * @return
	 */
	public static byte[] getCmd1(byte n) {
		return new byte[] {0X1B, 0X31, n};
	}
	
	/**
	 * 设置左限为 n个字节（8 点/字节） ，n 的值应当在打印机的行宽之内（n<384)。
	 * 默认值 n=0，即没有左限。
	 * 该命令设置的是绝对位置，不受字符命令 ESC U 和 ESC W 的影响。
	 * @param n
	 * @return
	 */
	public static byte[] getCmdL(byte n) {
		return new byte[] {0X1B, 0X6C, n};
	}
	
	/**
	 * 设置右限为 n 个字节（8点/字节） ，n 的数值应当在该型号打印机的行宽之内。
	 * 如果已经设置了左限，n的值应该大于左限值。
	 * 默认值 n=0，即没有右限。
	 * 该命令设置的是绝对位置，不受字符放大命令 ESC U 和 ESC W 的影响。
	 * 该命令设置之后，只要达到右限位置，打印机便会自动加入换行符。
	 * @param n
	 * @return
	 */
	public static byte[] getCmdQ(byte n) {
		return new byte[] {0X1B, 0X51, n};
	}
	
	/**
	 * 在该命令输入之后的字符、图形和汉字将以正常宽度 n 倍打印，n的数值应该在 0 或 1，n=0 正常打印；
	 * n=1:倍宽打印； 。这个命令应当在一行的开始发出。
	 * @param n
	 * @return
	 */
	public static byte[] getCmdU(byte n) {
		return new byte[] {0X1B, 0X55, n};
	}
	
	/**
	 * 在该命令输入之后的字符、图形和汉字将以正常高度 n 倍打印，n的数值应该在 0 或 1，n=0 正常打印；
	 * n=1:倍高打印；这个命令应当在一行的开始发出。
	 * @param n
	 * @return
	 */
	public static byte[] getCmdV(byte n) {
		return new byte[] {0X1B, 0X56, n};
	}
	
	/**
	 * ：在该命令输入之后的字符、图形和汉字将以正常高度和宽度的 n倍打印，n的数值应为 0或 1。
	 * n=0 正常打印；n=1:放大 2 倍
	 * @param n
	 * @return
	 */
	public static byte[] getCmdW(byte n) {
		return new byte[] {0X1B, 0X57, n};
	}
	
	/**
	 * n=1，允许下划线打印;n=0，禁止下划线打印。
	 * 允许下划线打印之后的所有字符都将打印出下划线，除非遇到禁止下划线的打印命令。
	 * 此指令对汉字同样有效。
	 * @param n
	 * @return
	 */
	public static byte[] getCmd_(byte n) {
		return new byte[] {0X1B, 0X2D, n};
	}
	
	/**
	 * n=1，允许上划线打印;n=0，禁止上划线打印。
	 * 允许上划线打印之后的所有字符都将打印上划线。此指令对汉字同样有效。
	 * @param n
	 * @return
	 */
	public static byte[] getCmd__(byte n) {
		return new byte[] {0X1B, 0X2B, n};
	}
	
	/**
	 * 如果 n=1，允许反白打印;n=0，禁止反白打印。
	 * 反白打印时在黑色背景下打印白字，就像照相的底片一样。
	 * 正常打印是在白色背景下打印黑字，默认为禁止反白打印。
	 * @param n
	 * @return
	 */
	public static byte[] getCmdI(byte n) {
		return new byte[] {0X1B, 0X69, n};
	}
	
	/**
	 * 当 n=1，反向打印方式允许；n=0反向打印方式禁止。默认值 n=1。
	 * 反向打印不但支持字符方式，也支持图形方式和汉字方式。
	 * 在反向打印图形时，请注意图形单元的打印顺序。
	 * @param n
	 * @return
	 */
	public static byte[] getCmdC(byte n) {
		return new byte[] {0X1B, 0X63, n};
	}
}
