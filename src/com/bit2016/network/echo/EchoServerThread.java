package com.bit2016.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerThread extends Thread {
	private Socket socket;
	public EchoServerThread(Socket socket){
		this.socket = socket;
	}
	@Override
	public void run() {
		try{
			InetSocketAddress isa = (InetSocketAddress)socket.getRemoteSocketAddress();
			System.out.println
			( "[server# "+getId()+"] connected by Client["+ 
			isa.getAddress().getHostAddress()+":"+isa.getPort()+"]");
			
			//4. IOStream 생성(받아오기)
			BufferedReader br = 
					new BufferedReader( new InputStreamReader( socket.getInputStream(), "UTF-8" ) );
			PrintWriter pw = 
					new PrintWriter( new OutputStreamWriter( socket.getOutputStream(), "UTF-8" ), true );
			while( true ) {
				String data = br.readLine();
				if( data == null ) { // -1보다 낫다. 
					EchoServer02.log( "closed by client" );
					break;
				}
				
				EchoServer02.log( "received:" + data );
				pw.println( data );
			}
		} catch( SocketException ex ){
			EchoServer02.log( "abnormal closed by client" );
		} catch( IOException ex ) {
			EchoServer02.log( "error:" + ex );
		} finally {
			try {
				if(socket != null && socket.isClosed() ==false){
				socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
