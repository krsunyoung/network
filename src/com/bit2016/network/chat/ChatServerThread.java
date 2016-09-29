package com.bit2016.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class ChatServerThread extends Thread {
	private Socket socket;
	private String name;

	private List<PrintWriter> listPrintWriter;

	public ChatServerThread(Socket socket, List<PrintWriter> listPrintWriter) {
		this.socket = socket;
		this.listPrintWriter = listPrintWriter;
	}

	@Override
	public void run() {

		try {

			// 1. print remote socket address
			InetSocketAddress remoteAdderes = (InetSocketAddress) socket.getRemoteSocketAddress();
			ChatServer.consoleLog("connected by client[" + remoteAdderes.getAddress().getHostAddress() + ":"
					+ remoteAdderes.getPort() + "]");

			// 2.Create Stream(From Basic Stream )
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			
			
			// 4. processing...
			// String data = null;
		
			while (true) {
				String line = br.readLine();
				if (line == null) {
					// doQuit();
					break;
				}
				String[] tokens = line.split(":");
				if ("JOIN".equalsIgnoreCase(tokens[0])) {
					doJoin(tokens[1], pw); // name 을 넘겨받는것.
				}  else if ("QUIT".equalsIgnoreCase(tokens[0])) {
					doQuit(pw);
				} else {
					doMessage(name +">>"+line);
				}

			}

		} catch (UnsupportedEncodingException e) {
			ChatServer.consoleLog("error:" + e);
		} catch (IOException e) {
			ChatServer.consoleLog("error:" + e);
		} finally {
			try {
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				ChatServer.consoleLog("error:" + e);

			}
		}

	}

	private void doJoin(String name, PrintWriter printWriter) {
		// 1.save nickname
		this.name = name;

		// 2.broadcasting
		String message = name + "님이 입장하였습니다.";

		broadcastMessage(message);

		// 3.printWriter을 저장시킨다 (공유하고 있는)
		addPrintWriter(printWriter);

		// 4.ack
		printWriter.println("JOIN:OK");
	}
	private void doMessage(String message) {

		broadcastMessage(message);

		System.out.println( message);
		
	}
	private void doQuit(PrintWriter printWriter) {

		// 2.broadcasting
		String message = name + "님이 퇴장하였습니다.";
		broadcastMessage(message);

		deletePrintWriter(printWriter);

		printWriter.println("QUIT:getout");
	}

	private void addPrintWriter(PrintWriter printWriter) {
		synchronized (listPrintWriter) {
			listPrintWriter.add(printWriter);
		}

	}

	private void deletePrintWriter(PrintWriter printWriter) {
		synchronized (listPrintWriter) {
			listPrintWriter.remove(printWriter);
		}

	}

	private void broadcastMessage(String message) {
		synchronized (listPrintWriter) {
			for (PrintWriter printWriter : listPrintWriter) {
				printWriter.println(message);
			}
		}

	}

}
