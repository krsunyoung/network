package com.bit2016.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {

	private static final String SERVER_IP = "192.168.1.21";
	private static final int SERVER_PORT = 5020;

	public static void main(String[] args) {
		Socket socket = null;
		try {
			// 1.소켓 생성 클라이언트라 서버소켓이 필요없음
			socket = new Socket();

			//1-1 . socket Buffer size 확인
			int receiveBufferSize = socket.getReceiveBufferSize();
			int sendBufferSize = socket.getSendBufferSize();
			System.out.println( receiveBufferSize+","+ sendBufferSize);
			
			//1-2 socket buffer size 변경
			socket.setReceiveBufferSize(10 * 1024);
			socket.setSendBufferSize(10*1024);
			receiveBufferSize = socket.getReceiveBufferSize();
			sendBufferSize = socket.getSendBufferSize();
			
			System.out.println( receiveBufferSize+","+ sendBufferSize);
			
			
			//1-3 SO_NODELAY(Nagle Algorithm off)
			socket.setTcpNoDelay(true);//ack를 받기때문에 생기는데 이제 받지 않고 그냥 send만 !!!!!
			
			//1-4 SO_TIMEOUT
			socket.setSoTimeout(1); //바로 에러남  nodelay때문에 에러가 나지 않지만 지워지면 에러남.
			
			// 2. 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[Client] connected ");
			
			//3.IOStream 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			//4.쓰기
			String data = "Hello World\n";
			os.write(data.getBytes("UTF-8"));
			
			//5. 읽기
			byte[] buffer = new byte[256];
			int readByteCount = is.read(buffer);
			if(readByteCount == -1){
				System.out.println("[client] closed by server");
				return;
			}
			
			data = new String(buffer, 0, readByteCount,"UTF-8");
			System.out.println("[Client] received : "+data);
			
		} catch (SocketTimeoutException ex ){
			System.out.println("time out:");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			
//			try {
//				if (socket != null & socket.isClosed() == false) {
//					socket.close();
//				}
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
			//jvm이 소켓 안닫혀 있는것을 자동으로 닫혀줄수도 있지만 소켓 오류남
			//아니면 서버에 catch 하나를 추가
			
		}
	}

}
