package com.bit2016.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient {

	private static final String SERVER_IP = "192.168.1.25";
	private static final int SERVER_PORT = 5020;

	public static void main(String[] args) {
		Socket socket = null;
		try {
			// 1.소켓 생성 클라이언트라 서버소켓이 필요없음
			socket = new Socket();

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
