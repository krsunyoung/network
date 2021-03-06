package com.bit2016.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {

	private static final int PORT = 5020; // main 안에 하면 오류남.
	
	public static void main(String[] args) {
	
		ServerSocket serverSocket = null;
		
		try {
			// 1. 서버소켓 생성
			serverSocket = new ServerSocket();

			// 2. 바인딩 (binding 소켓에 소켓주소(IP+port) 을 바인딩한다.)
			InetAddress inetAddress = InetAddress.getLocalHost(); //호스트에  있는 큰덩어리(IP, address 등등을가져옴)
			String hostAddress = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(hostAddress, PORT));
			// "192.168.1.25" 이것을 hostAddress를 구해서 바꿔주는것으로 변경
			System.out.println("[server] binding " + hostAddress + ":" + PORT);

			// 3.accept(클라이언트로부터 연결요청을 기다린다.)
			Socket socket = serverSocket.accept(); // block 된다.
			// 연결한 다음 클라이언트 주소를 알고 싶을때 !
			InetSocketAddress inetSocketAddress = 
					(InetSocketAddress) socket.getRemoteSocketAddress(); 
			
			// InetAddress inetRemoteHostAddress= InetSocketAddress.getHostName();
		
			String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
			int remoteHostPort = inetSocketAddress.getPort();
			System.out.println
			("[server] connected by client " + remoteHostAddress + ":" + remoteHostPort);

			try {
				// 4. IOStream 받아오기 [ 데이터통신 ]
				//Input 클라이언트가 서버한테 주는것 output 서버가 클라이언트한테 주는것 
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				
				while (true) { //무한루프 종료할때까지 계속 읽고 쓰고 반복
					// 5. 데이터 읽기
					// inputStreamReder를 쓰면 byte크기를 안정해도된다.
					byte[] buffer = new byte[256];
					int readByteConunt = inputStream.read(buffer); //block

					// socket에 관련해서 try catch 를 해준다.
					if (readByteConunt == -1) {
						// 정상종료 ( 상대편 remote socket 이 close()를 불러서 정상적으로 소켓을
						// 닫았다. )
						System.out.println("[server] close by client");
						break;
					}
					String data = new String(buffer, 0, readByteConunt, "UTF-8");
					System.out.println("[server] received : " + data);

					// 6.쓰기
					outputStream.write(data.getBytes("UTF-8"));
				}
			}catch (SocketException ex){ //클라이언트 finally 비정상종료 안만들었때 아래 내용이 뜬다.  
				System.out.println("[server] abnormal closed by");
			}
			catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					// 7. 자원정리 (소켓 닫기)
					socket.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
