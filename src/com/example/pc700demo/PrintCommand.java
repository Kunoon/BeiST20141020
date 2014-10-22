package com.example.pc700demo;

public class PrintCommand {
	
	/**
	 * ��ʼ����ӡ��
	 * @return
	 */
	public static byte[] getCmdEsc() {
		return new byte[] { 0x1B, 0x40 };
	}
	
	/**
	 * ����
	 * @return
	 */
	public static byte[] getCmdLF() {
		return new byte[] { 0x0A };
	}
	
	/**
	 * �����С�� n=1 ���������С n=0 ��ԭ�����С
	 * @param n
	 * ��ӡ���ڽ�������֮�󽫸���nֵѡ��ͬ�ֺŴ�ӡ��Ĭ��Ϊn=0ѡ��24*24�����ִ�ӡ 
 	 * ��n=0ѡ��24*24�����ִ�ӡ 
 	 * ��n=1ѡ��48*48�����ִ�ӡ 
 	 * ��n= ѡ��16*16�����ִ�ӡ
	 * @return
	 */
	public static byte[] getCmdTS(byte n) {
		return new byte[] { 0x1B, 0x38, n };
	}
	
	/**
	 * ����/��ֹ�����ӡ
	 * @param n
	 * n��1����������ӡ��n��0����ֹ�����ӡ��
	 * @return
	 */
	public static byte[] getCmdN(byte n) {
		return new byte[] {0x1B, 0x01, n};
	}
	
	/**
	 * ����/��ֹ���״�ӡ
	 * @param n=1�������״��ӡ��n=0����ֹ���״��ӡ
	 * @return
	 */
	public static byte[] getCmdI(byte n) {
		return new byte[] { 0x1B, 0x69, n };
	}
	
	/**
	 * ��������
	 * @param n
	 * ����ָ��ӡֽ�Ҳ಻��ӡ���ַ�����ÿ���ַ��Ŀ��12+�ּ�ࣨ���������㡣
	 * n ��ֵӦ�� 0 �����ͺŴ�ӡ�����п�֮�ڡ�Ĭ��ֵn=0����û�����ޡ�
	 * ���������õ��Ǿ���λ�ã������ַ��Ŵ����� ESC U �� ESC W ��Ӱ�졣
	 * ����������֮��ֻҪ�ﵽ����λ�ã���ӡ�����Զ�����س�����
	 * @return
	 */
	public static byte[] getCmdQ(byte n) {
		return new byte[] { 0x1B, 0x51, n };
	}
	
	/**
	 * ��������
	 * @param n
	 * ����ָ��ӡֽ��಻��ӡ���ַ�����ÿ���ַ����Ŀ�Ȱ�12+�ּ����㣬
	 * n����ֵӦ��0�����ͺŴ�ӡ�����п�֮�ڡ�Ĭ��ֵn��0����û������.
	 * ���������õ��Ǿ���λ�ã������ַ��Ŵ��������ԣţӣ� �ա��ͣţӣ� W��Ӱ��
	 * @return
	 */
	public static byte[] getCmd1(byte n) {
		return new byte[] { 0x1B, 0x6C, n };
	}
	
	/**
	 * ����/��ֹ�»��ߴ�ӡ
	 * @param n
	 * n��1�������»��ߴ�ӡ��N��0����ֹ�»��ߴ�ӡ���ϵ���ʼ����N��0��
	 * �����»��ߴ�ӡ֮��������ַ������ְ����ո񶼽���ӡ���»��ߡ�
	 * @return
	 */
	public static byte[] getCmd_(byte n) {
		return new byte[] { 0x1B, 0x2D, n };
	}
}
