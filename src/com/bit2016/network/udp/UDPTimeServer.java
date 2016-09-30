package com.bit2016.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPTimeServer {
	private static final int PORT = 8088;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {

		DatagramSocket socket = null;

		try {
			// 소켓생성
			socket = new DatagramSocket(PORT);
			while (true) {
				DatagramPacket receivePacket = 
						new DatagramPacket(new byte[ BUFFER_SIZE],BUFFER_SIZE );
				
				System.out.println("[sever] 대기중");
				socket.receive(receivePacket); //blocking
				System.out.println("[sever] 수신");
				
				
//				String message = 
//						new String(receivePacket.getData(),0,receivePacket.getLength(),"UTF-8");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
				String message = format.format(new Date());
				System.out.println(message);
				
				// 3. 데이터 송신
				
				byte[] sendData = message.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(),
						receivePacket.getPort());
				socket.send(sendPacket);
				
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
	}

}
