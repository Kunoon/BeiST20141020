package com.bei.smartravel.chen;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
/**
 * 实现与贝竹Server的非阻塞网络通信
 * @author YongKun
 */
public class NSocket {
	private String sip;
	private int port;
	// 与服务器通信的信道
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
	 * 连接指定网络服务器
	 * @param msTimeout   // 连接超时等待
	 * @return boolean
	 */
	public boolean connect(int msTimeout){
		try{
			// 根据主机名和端口号创建套接字地址
			InetSocketAddress addr = new InetSocketAddress(sip, port);
			// 打开与服务器通信的信道
			client = SocketChannel.open();
	        // 设置为非阻塞模式
	        client.configureBlocking(false);   
	        // 打开并注册选择器到信道
	        selector = Selector.open();  
	        client.register(selector, SelectionKey.OP_CONNECT);   
	        // 连接   
	        client.connect(addr);
	        
	        int num = selector.select(msTimeout);
	        if (0 == num)
	        	return false;
	        
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();   
            
            while (iter.hasNext()) {
                SelectionKey key = (SelectionKey) iter.next();
                //iter.remove();
                // 测试此键的通道是否已完成其套接字连接操作
                if (key.isConnectable()) {
                	// 判断此通道上是否正在进行连接操作
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
	 * 断开与服务器的连接
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
	 * 向服务器发送数据请求
	 * @param cont
	 * @return	实际写入的字节的数目
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
	 * 接受服务器下发数据
	 * @param msTimeout   // 通信超时等待
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
