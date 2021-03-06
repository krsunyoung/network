package com.bit2016.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ChatClient  {
	private static final String SERVER_IP = "192.168.0.16";
	private static final int SERVER_PORT = 9090;

	public static void main(String[] args)  {
		Scanner scanner = null;
		Socket socket = null;

		try {
			scanner = new Scanner(System.in);
			socket = new Socket();
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			
			System.out.print("JOIN:");
			String line = "JOIN:"+scanner.nextLine();
			pw.println(line);
			
			String join = br.readLine();
			System.out.println(join);
	
			Thread thread = new ChatClientRead(socket);
			thread.start();
			
			while(true){
				String memo = scanner.nextLine();
				pw.println(memo);
				if ("quit".equals(memo)) {
					break;
				} 
				//클라이언트가 먼저 나가서 SeverThread에 있는 quit가 실행되지 않는다.
				
				//String data = br.readLine();
//				if (data == null) {
//					log("closed by server");
//					break;
//				}

			}
			
			
		} catch (SocketException ex) {
			log("abnormal closed by client");
		} catch (IOException ex) {
			log("error" + ex);
		} finally {
			try {
				if (scanner != null) {
					scanner.close();
				}
				if (socket != null && socket.isClosed() == false) {
					
					socket.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void log(String message) {
		System.out.println(message);
	}
}
