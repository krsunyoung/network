package com.bit2016.network.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	private Socket socket;

	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			consoleLog("connected from " + inetSocketAddress.getAddress().getHostAddress() + ":"
					+ inetSocketAddress.getPort());

			// get IOStream
			OutputStream outputStream = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

			String request = null;
			while (true) {
				String line = br.readLine(); // br을 한라인씩 읽어준다.
				
				// 브라우저가 연결을 끊으면
				if (line == null) {
					break; // 읽는 작업 끝낸다.
				}

				// Header만 읽음, 첫번째 라인만 가져올려고  사용했는데 null값이라서 두번째 라인으로 넘어간다?
				//request에 첫번째라인 저장됨?
				if ("".equals(line)) { 
					break;
				}

				if (request == null) {
					request = line;
				}
			}
			
			consoleLog(request);

			String[] tokens = request.split(" ");
			if ("GET".equals(tokens[0]) == true) { 
				reponseStaticResource(outputStream, tokens[1], tokens[2]);
				// Static 정적인 리소스들이라 계속 index.html파일만 보내줌?

			} else { //POST, DELETE, PUT, Etc 명령들은 400 Bad Request
				// respones400Error( outputStream, tokens[1]);
			}
			
			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
			// outputStream.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
			// outputStream.write( "Content-Type:text/html;
			// charset=utf-8\r\n".getBytes( "UTF-8" ) );
			// outputStream.write( "\r\n".getBytes() );
			// outputStream.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할
			// 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );

		} catch (Exception ex) {
			consoleLog("error:" + ex);
		} finally {
			// clean-up
			try {
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}

			} catch (IOException ex) {
				consoleLog("error:" + ex);
			}
		}
	}

	public void reponseStaticResource(
		OutputStream outputStream, String url, String protocol) 
		throws IOException {
		System.out.println(url + ":" + protocol);
		// 응답하게끔 입력해줘야함...

		if("/".equals( url ) == true ) {
			//welcome(default) file
			url = "/index.html";
		}
		
		File file = new File("./webapp" + url);
		if (file.exists() == false) {
			
			response404Error( outputStream, protocol);
			return;
		}

		// nio
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType( file.toPath());
		//

		// 응답(response)
		 outputStream.write( (protocol + " 200 OK\r\n").getBytes( "UTF-8" )) ; //응답해더의 첫줄, 왜 이렇게 하냐고 묻지말것
		 outputStream.write( ("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) ); //콘텐츠타입 지정
		 outputStream.write( "\r\n".getBytes() ); //개행해주면 브라우저에 빈 개행이 생기고 헤더의 마지막줄임
		 outputStream.write( body );
	}

	public void response404Error (
			OutputStream outputStream, String protocol)
			throws IOException{
			// HTTP/1.1 404 File Not Found
			// Content=TYPE : text/html; charest=uft-8
			// 이렇게 적어주면 html파일만 가져온다?
			// html(./webapp/error/404.html)
			System.out.println("404 error 응답할것  [과제]");
	}
	
	public void response400Error (
			OutputStream outputStream, String protocol)
			throws IOException{
			// HTTP/1.1 400 Bad Request
			// Content=TYPE : text/html; charest=uft-8
			// 이렇게 적어주면 html파일만 가져온다?
			// html(./webapp/error/400.html)
			System.out.println("400 error 응답할것  [과제]");
	}
	
	public void consoleLog(String message) { 
		// sysout을 계속 쓰지 않아도 consolelog 매서드를 만들어서 입력하면 바로 메시지가 나오게 된다.
												
		System.out.println("[RequestHandler#" + getId() + "] " + message);
	}
}
