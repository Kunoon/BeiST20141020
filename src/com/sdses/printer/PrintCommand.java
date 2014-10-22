package com.sdses.printer;

public class PrintCommand {
	
	/**
	 * ��ʼ����ӡ��
	 * @return
	 */
	public static byte[] getCmdEsc() {
		return new byte[] {0X1B, 0X40};
	}
	
	/**
	 * ����Ҫ��ӡ������ˮƽ����λ�á�
	 * n=1 ������룻
	 * n=2 ���ж��룻 ��Ĭ��ֵ��
	 * n=3 ���Ҷ��룻
	 * @param n
	 * @return
	 */
	public static byte[] getCmdGSp(byte n) {
		return new byte[] {0X1D, 0X70, n};
	}
	
	/**
	 * �س�
	 * @return
	 */
	public static byte[] getCmdCR() {
		return new byte[] {0X0D};
	}
	
	/**
	 * �ո�
	 * @return
	 */
	public static byte[] getCmdNUL() {
		return new byte[] {0X00};
	}
	
	/**
	 * ����
	 * @return
	 */
	public static byte[] getCmdLF() {
		return new byte[] {0X0A};
	}
	
	/**
	 * ��ӡ����ǰ���� n ���С�
	 * ��n ��ֵӦ�� 1�� 255��Χ֮�ڡ������������س����С�
	 * ������Ӱ����滻����� 
	 * �������Ҫ��������������ֽ����Ҫ�س��� �Ϳ���ʹ�� ESC J ���
	 * @param n
	 * @return
	 */
	public static byte[] getCmdJ(byte n) {
		return new byte[] {0X1B, 0X4A, n};
	}
	
	/**
	 * Ϊ����Ļ����������� n���м�ࡣ����������֮��ļ��Ϊ n �㡣
	 * n ��ֵӦ���� 0 �� 255֮�䡣
	 * ��ʹ�á�ESC K��������е���ͼ�δ�ӡʱ��ͨ������ n=0���ı���ӡ��ʽʱͨ���� n=3��
	 * @param n
	 * @return
	 */
	public static byte[] getCmd1(byte n) {
		return new byte[] {0X1B, 0X31, n};
	}
	
	/**
	 * ��������Ϊ n���ֽڣ�8 ��/�ֽڣ� ��n ��ֵӦ���ڴ�ӡ�����п�֮�ڣ�n<384)��
	 * Ĭ��ֵ n=0����û�����ޡ�
	 * ���������õ��Ǿ���λ�ã������ַ����� ESC U �� ESC W ��Ӱ�졣
	 * @param n
	 * @return
	 */
	public static byte[] getCmdL(byte n) {
		return new byte[] {0X1B, 0X6C, n};
	}
	
	/**
	 * ��������Ϊ n ���ֽڣ�8��/�ֽڣ� ��n ����ֵӦ���ڸ��ͺŴ�ӡ�����п�֮�ڡ�
	 * ����Ѿ����������ޣ�n��ֵӦ�ô�������ֵ��
	 * Ĭ��ֵ n=0����û�����ޡ�
	 * ���������õ��Ǿ���λ�ã������ַ��Ŵ����� ESC U �� ESC W ��Ӱ�졣
	 * ����������֮��ֻҪ�ﵽ����λ�ã���ӡ������Զ����뻻�з���
	 * @param n
	 * @return
	 */
	public static byte[] getCmdQ(byte n) {
		return new byte[] {0X1B, 0X51, n};
	}
	
	/**
	 * �ڸ���������֮����ַ���ͼ�κͺ��ֽ���������� n ����ӡ��n����ֵӦ���� 0 �� 1��n=0 ������ӡ��
	 * n=1:�����ӡ�� ���������Ӧ����һ�еĿ�ʼ������
	 * @param n
	 * @return
	 */
	public static byte[] getCmdU(byte n) {
		return new byte[] {0X1B, 0X55, n};
	}
	
	/**
	 * �ڸ���������֮����ַ���ͼ�κͺ��ֽ��������߶� n ����ӡ��n����ֵӦ���� 0 �� 1��n=0 ������ӡ��
	 * n=1:���ߴ�ӡ���������Ӧ����һ�еĿ�ʼ������
	 * @param n
	 * @return
	 */
	public static byte[] getCmdV(byte n) {
		return new byte[] {0X1B, 0X56, n};
	}
	
	/**
	 * ���ڸ���������֮����ַ���ͼ�κͺ��ֽ��������߶ȺͿ�ȵ� n����ӡ��n����ֵӦΪ 0�� 1��
	 * n=0 ������ӡ��n=1:�Ŵ� 2 ��
	 * @param n
	 * @return
	 */
	public static byte[] getCmdW(byte n) {
		return new byte[] {0X1B, 0X57, n};
	}
	
	/**
	 * n=1�������»��ߴ�ӡ;n=0����ֹ�»��ߴ�ӡ��
	 * �����»��ߴ�ӡ֮��������ַ�������ӡ���»��ߣ�����������ֹ�»��ߵĴ�ӡ���
	 * ��ָ��Ժ���ͬ����Ч��
	 * @param n
	 * @return
	 */
	public static byte[] getCmd_(byte n) {
		return new byte[] {0X1B, 0X2D, n};
	}
	
	/**
	 * n=1�������ϻ��ߴ�ӡ;n=0����ֹ�ϻ��ߴ�ӡ��
	 * �����ϻ��ߴ�ӡ֮��������ַ�������ӡ�ϻ��ߡ���ָ��Ժ���ͬ����Ч��
	 * @param n
	 * @return
	 */
	public static byte[] getCmd__(byte n) {
		return new byte[] {0X1B, 0X2B, n};
	}
	
	/**
	 * ��� n=1�������״�ӡ;n=0����ֹ���״�ӡ��
	 * ���״�ӡʱ�ں�ɫ�����´�ӡ���֣���������ĵ�Ƭһ����
	 * ������ӡ���ڰ�ɫ�����´�ӡ���֣�Ĭ��Ϊ��ֹ���״�ӡ��
	 * @param n
	 * @return
	 */
	public static byte[] getCmdI(byte n) {
		return new byte[] {0X1B, 0X69, n};
	}
	
	/**
	 * �� n=1�������ӡ��ʽ����n=0�����ӡ��ʽ��ֹ��Ĭ��ֵ n=1��
	 * �����ӡ����֧���ַ���ʽ��Ҳ֧��ͼ�η�ʽ�ͺ��ַ�ʽ��
	 * �ڷ����ӡͼ��ʱ����ע��ͼ�ε�Ԫ�Ĵ�ӡ˳��
	 * @param n
	 * @return
	 */
	public static byte[] getCmdC(byte n) {
		return new byte[] {0X1B, 0X63, n};
	}
}
