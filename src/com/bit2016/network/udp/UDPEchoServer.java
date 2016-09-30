package com.bit2016.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPEchoServer {
	
	public static final int PORT = 5055;
	private static final int BUFFER_SIZE = 1024;
	
	public static void main(String[] args) {
		DatagramSocket socket = null;
		try {
			//1.socket생성
			socket = new DatagramSocket(PORT); 
			while(true){
			//2. 데이터수신 [ 대기상태 ] 
			DatagramPacket receivePacket = 
					new DatagramPacket(new byte[ BUFFER_SIZE],BUFFER_SIZE );
			
			System.out.println("[sever] 대기중");
			socket.receive(receivePacket); //blocking
			System.out.println("[sever] 수신");
			
			
			String message = 
					new String(receivePacket.getData(),0,receivePacket.getLength(),"UTF-8");
			System.out.println("[server] received : "+ message);
			
			//3. 데이터 송신 [ echo ]
			byte[ ] sendData = message.getBytes();
			DatagramPacket sendPacket = 
					new DatagramPacket( sendData, sendData.length, 
							receivePacket.getAddress(),receivePacket.getPort());
			socket.send(sendPacket); 
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();			
		}finally {
			if(socket != null && socket.isClosed() ==false){
				socket.close();
			}
		}

	}

}
