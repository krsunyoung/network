package com.bit2016.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPTimeClient {
	private static String SERVER_IP = "192.168.1.21";
	private static int PORT = 8088;
	private static int BUFFER_SIZE = 1024;

	public static void main(String[] args) {

		DatagramSocket socket = null;

		try {

			// socket 생성
			socket = new DatagramSocket();

				// 사용자 입력 받음.
				String message = "";
				
				// String message = "안녕자바 네트워킹 프로그램";
				//  메세지 전송
				byte[] sendData = message.getBytes("UTF-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						new InetSocketAddress(SERVER_IP, PORT));

				socket.send(sendPacket);

				// 메세지 수신
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);

				socket.receive(receivePacket);

				message = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");

				System.out.println("지금 시간은" + message);

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
