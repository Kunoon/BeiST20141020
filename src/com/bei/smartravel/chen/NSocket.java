package com.bei.smartravel.chen;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
/**
 * ʵ���뱴��Server�ķ���������ͨ��
 * @author YongKun
 */
public class NSocket {
	private String sip;
	private int port;
	// �������ͨ�ŵ��ŵ�
	private SocketChannel client;
	private Selector selector;
	private static CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
	//private static CharsetDecoder decoder = Charset.forName("GBK").newDecoder();
	private ByteBuffer buffer = ByteBuffer.allocate(16 * 1024);
	
	public NSocket()
	{
		
	}
	
	public NSocket(String ip, int port){
		sip = ip;
		this.port = port;
	}
	
	public void resetAddr(String ip, int port){
		sip = ip;
		this.port = port;
	}
	/**
	 * ����ָ�����������
	 * @param msTimeout   // ���ӳ�ʱ�ȴ�
	 * @return boolean
	 */
	public boolean connect(int msTimeout){
		try{
			// �����������Ͷ˿ںŴ����׽��ֵ�ַ
			InetSocketAddress addr = new InetSocketAddress(sip, port);
			// ���������ͨ�ŵ��ŵ�
			client = SocketChannel.open();
	        // ����Ϊ������ģʽ
	        client.configureBlocking(false);   
	        // �򿪲�ע��ѡ�������ŵ�
	        selector = Selector.open();  
	        client.register(selector, SelectionKey.OP_CONNECT);   
	        // ����   
	        client.connect(addr);
	        
	        int num = selector.select(msTimeout);
	        if (0 == num)
	        	return false;
	        
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();   
            
            while (iter.hasNext()) {
                SelectionKey key = (SelectionKey) iter.next();
                //iter.remove();
                // ���Դ˼���ͨ���Ƿ���������׽������Ӳ���
                if (key.isConnectable()) {
                	// �жϴ�ͨ�����Ƿ����ڽ������Ӳ���
                    if (client.isConnectionPending())
                    	client.finishConnect();
                }
            }
            return true;
		}catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
	/**
	 * �Ͽ��������������
	 */
	public void disConnect(){
		try{
		selector.close();
		client.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * �������������������
	 * @param cont
	 * @return	ʵ��д����ֽڵ���Ŀ
	 */
	public int send(String cont){
		int num = 0;
		try{
			client.register(selector, SelectionKey.OP_WRITE);
			num = selector.select(10000);
	        if (0 == num){
	        	log.logIns.logE("#@#@#@#@#@#@#@#write error@#@#@#@#@#");
	        	return 0;
	        }
			num = client.write(encoder.encode(CharBuffer.wrap(cont)));
			return num;
		}catch (Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * ���ܷ������·�����
	 * @param msTimeout   // ͨ�ų�ʱ�ȴ�
	 * @return	String
	 */
	public String recv(int msTimeout){
		try{
			String data = "";
			int num;
	        do {
	    		buffer.clear();
				client.register(selector, SelectionKey.OP_READ);
				num = selector.select(msTimeout);
		        if (0 == num) {
		        	return "";
		        }
        		num = client.read(buffer);
	        	if(num > 0) {
		        	data += (new String(buffer.array())).substring(0, num);
	        	}
	        }while(num > 0);
	        return data;
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}
	}

}
