package com.bit2016.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	private static final int PORT = 5010;

	public static void main(String[] args) {
		ServerSocket serversocket = null;
		try {
			serversocket = new ServerSocket();

			InetAddress inetAddress = InetAddress.getLocalHost();
			String hostAddress = inetAddress.getHostAddress();
			serversocket.bind(new InetSocketAddress(hostAddress, PORT));

			Socket socket = serversocket.accept(); // 소켓 받아들일 준비상태!!

			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();

			String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
			int remoteHostPort = inetSocketAddress.getPort();
			System.out.println("[server] connected by client " + remoteHostAddress + ":" + remoteHostPort);

			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			try {
				while (true) {

					String data = br.readLine();
					if (data == null) {
						System.out.println("[server] closed by client");
						break;
					}

					System.out.println("[server] received :" + data);

					pw.println(data);
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {

					socket.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serversocket != null && serversocket.isClosed() == false) {
					serversocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
