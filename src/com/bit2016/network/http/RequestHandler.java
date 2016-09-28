package com.bit2016.network.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

				// Header만 읽음, 첫번째 라인만 가져올려고 사용했는데 null값이라서 두번째 라인으로 넘어간다?
				// request에 첫번째라인 저장됨?
				if ("".equals(line)) {
					break;
				}

				if (request == null) {
					request = line; // 첫줄을 출력할수 있도록 넣어준것 원래는 안넣는다.
				}
			}

			consoleLog(request);

			String[] tokens = request.split(" ");
			if ("GET".equals(tokens[0]) == true) { // 첫번째는 명령어. 나머지는 잘못된 요청이다.
													// bad request로 ....
				reponseStaticResource(outputStream, tokens[1], tokens[2]);
				// Static 정적인 리소스들이라 url 요청한 파일을 보내준다.(계속 index.html파일만 보내)
				// 정적인 파일데이터를 보내준다? program 동적인 리소스라 한다.

			} else { // POST, DELETE, PUT, Etc 명령들은 400 Bad Request
				response400Error(outputStream, tokens[2]);
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

	public void reponseStaticResource(OutputStream outputStream, String url, String protocol) throws IOException {
		// readallbytes 에 try catch 를 해줘야 하는데 throws IOException 을 써주면서
		// IOException을 무시하겠다고 해주면 try catch 를 무시하게끔 해준다. 안써도 된다!!

		System.out.println(url + ":" + protocol);

		if ("/".equals(url) == true) {
			// welcome(default) file 사이트에 들어왔을때 바뀌게 해주는것. default.html/ jsp
			// ....등등
			url = "/index.html";
		}

		File file = new File("./webapp" + url);
		if (file.exists() == false) {

			response404Error(outputStream, protocol);
			return;
		}

		// nio
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath()); // 파일에 컨텐츠
																	// 타입을 리턴해
																	// 준다

		// 응답(response)
		outputStream.write((protocol + " 200 OK\r\n").getBytes("UTF-8")); // 응답해더의
																			// 첫줄,
																			// 왜
																			// 이렇게
																			// 하냐고
																			// 묻지말것
		// \r은 캐리지 리턴으로 그 줄의 맨앞으로 가라는 의미
		outputStream.write(("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes("UTF-8")); // 콘텐츠타입
																										// 지정
																										// 이미지거나
																										// CSS파일이면
																										// 그것에
																										// 맞게
																										// bytes
																										// 타입을
																										// 바꿔준다.
		outputStream.write("\r\n".getBytes()); // 개행해주면 브라우저에 빈 개행이 있으면 헤더의
												// 마지막줄임
		outputStream.write(body);
		// 안적어도 되는 이유가 ..... html이 정해져있어서 어디에 정해져 있느냐... 그것이 문제로다...

	}

	public void response404Error(OutputStream outputStream, String protocol) throws IOException {

		//System.out.println("404 error 응답할것  [과제]");
		File file = new File("./webapp/error/404.html");
		if (file.exists() == false) {

			response404Error(outputStream, protocol);
			return;
		}

		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());

		outputStream.write((protocol + " 200 OK\r\n").getBytes("UTF-8"));
		outputStream.write(("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes("UTF-8"));
		outputStream.write("\r\n".getBytes());
		outputStream.write(body);
	}

	public void response400Error(OutputStream outputStream, String protocol) throws IOException {
		// HTTP/1.1 400 Bad Request
		// Content=TYPE : text/html; charset=uft-8
		// html(./webapp/error/400.html)
	//	System.out.println("400 error 응답할것  [과제]");
		
		File file = new File("./webapp/error/400.html");
		if (file.exists() == false) {

			response404Error(outputStream, protocol);
			return;
		}

		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());

		outputStream.write((protocol + " 200 OK\r\n").getBytes("UTF-8"));
		outputStream.write(("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes("UTF-8"));
		outputStream.write("\r\n".getBytes());
		outputStream.write(body);
	}

	public void consoleLog(String message) {
		// sysout을 계속 쓰지 않아도 consolelog 매서드를 만들어서 입력하면 바로 메시지가 나오게 된다.

		System.out.println("[RequestHandler#" + getId() + "] " + message);
	}
}
