package com.bit2016.network.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer2 {

	private static final int PORT = 5020; // main 안에 하면 오류남.

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			// 1. 서버소켓 생성
			serverSocket = new ServerSocket();

			// 2. 바인딩 (binding 소켓에 소켓주소(IP+port) 을 바인딩한다.)
			InetAddress inetAddress = InetAddress.getLocalHost();
			String hostAddress = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(hostAddress, PORT));
			// "192.168.1.25" 이것을 hostAddress를 구해서 바꿔주는것으로 변경
			System.out.println("[server] binding " + hostAddress + ":" + PORT);

			// 3.accept(클라이언트로부터 연결요청을 기다린다.)
			Socket socket = serverSocket.accept(); // block 된다.
		
			// 연결한 다음 클라이언트 주소를 알고 싶을때 !
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			// InetAddress inetRemoteHostAddress=
			// inetSocketAddress.getHostName();
			
			String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
			int remoteHostPort = inetSocketAddress.getPort();
			System.out.println("[server] connected by client " + remoteHostAddress + ":" + remoteHostPort);

			try {
				// 4. IOStream 받아오기 [ 데이터통신 ]
//				InputStream inputStream = socket.getInputStream();
//				OutputStream outputStream = socket.getOutputStream();
//				
				BufferedReader br = 
					new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
				//BufferedReader도 보조스트림. inputStream 이 byte 기반 보조스트림.
				PrintWriter pw = 
					new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);
				//네트워크에서 소스에서 읽어들여오는것. 
				
				while (true) { //무한루프 종료할때까지 계속 읽고 쓰고 반복
					// 5. 데이터 읽기
					String data = br.readLine();
					if(data == null){
						System.out.println("[server] closed by client");
						break;
					}
					System.out.println("[server] received :"+ data);
					
					// 6.쓰기
					pw.println(data);
					// pw.print(data +"\n"); 같은의미
					
				}
			} catch (IOException ex) {
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

