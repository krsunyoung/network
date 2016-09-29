package com.bit2016.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ChatClientRead extends Thread {
	private Socket socket;

	public ChatClientRead(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			while (true) {
				String data = br.readLine();
				System.out.println(data);
				if (data == null ) {
					break;
				}

			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {

			if (socket != null && socket.isClosed() == false) {

				socket.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
