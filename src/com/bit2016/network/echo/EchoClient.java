package com.bit2016.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "192.168.0.50";
	private static final int SERVER_PORT = 5010;

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		Socket socket = null;
		try {
			// 1.소켓 생성 클라이언트라 서버소켓이 필요없음
			socket = new Socket();

			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[Client] connected ");

			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			String data = scan.nextLine();
			os.write(data.getBytes("UTF-8"));

			byte[] buffer = new byte[256];
			int readByteCount = is.read(buffer);
			if (readByteCount == -1) {
				System.out.println("[client] closed by server");
				return;
			}

			data = new String(buffer, 0, readByteCount, "UTF-8");
			System.out.println("[Client] received : " + data);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			
			try {
				socket.close();
				scan.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
