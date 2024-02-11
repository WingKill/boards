package Socket;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Board.Post;
import Board.Board;

public class MySocketServer extends Thread {
	static Socket socket = null;
	static List<Socket> clients = null;
	boolean idcheck = false;

	public MySocketServer(Socket socket) {
		this.socket = socket; // 유저 socket을 할당
		this.clients = clients;	
	}

	public void run() { // Thread 에서 start() 메소드 사용 시 자동으로 해당 메소드 시작 (Thread별로 개별적 수행)
		try {
			System.out.println("서버 : " + socket.getInetAddress() + " IP의 클라이언트와 연결되었습니다"); // 연결 확인용

			// InputStream - 클라이언트에서 보낸 메세지 읽기
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			// OutputStream - 서버에서 클라이언트로 메세지 보내기
			OutputStream out = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			
			String readValue; // Client에서 보낸 값 저장
			Board board = new Board(out);
			int count = 0;
			
			String author = socket.getInetAddress().toString();

			writer.println("[[게시판 프로그램]]");
			board.showMenu();
			
			while ((readValue = reader.readLine()) != null) { // 클라이언트가 메세지 입력시마다 수행

				out = socket.getOutputStream();
				writer = new PrintWriter(out, true);

				if (readValue.equals("0")) {
					writer.println("종료합니다.");
					System.exit(0);
				} else if (readValue.equals("1")) {
					if (count <= 0) {
						writer.println("등록된 글이 없습니다.");
						board.showMenu();
					} else {
						board.showList();
					}
				} else if (readValue.equals("2")) {

					writer.println("제목 >> ");
					String title = reader.readLine();

					if (!idcheck) {
						writer.println("작성자 >> ");
						String str = reader.readLine();
						author = str;
						idcheck = true;
					} else {
						writer.print("작성자 >> ");
						writer.println(author);
					}

					writer.println("내용 >> ");
					String contents = reader.readLine();

					board.addList(title, author, contents);
					count++;
					board.showListInServer();
				} else if (readValue.equals("3")) {
					if (count <= 0) {
						writer.println("등록된 글이 없습니다.");
						board.showMenu();
					} else {
						writer.println("내용을 볼 글 번호 >> ");
						String str = reader.readLine();
						int searchNum = Integer.parseInt(str);
						board.searchList(searchNum);
					}
				} else if (readValue.equals("4")) {
					if (count <= 0) {
						writer.println("등록된 글이 없습니다.");
						board.showMenu();
					} else {
						writer.println("삭제할 글 번호 >> ");
						String str = reader.readLine();
						int delNum = Integer.parseInt(str);
						board.delList(delNum);
						count--;
						board.showListInServer();
					}
				} else {
					writer.println("선택한 번호의 게시판 기능이 없습니다.");
				}
				
			} // end of while
			


		} catch (Exception e) {
			e.printStackTrace(); // 예외처리
		} finally {
			clients.remove(socket);
			System.out.println("서버 : " + socket.getInetAddress() + " IP의 클라이언트와 연결 종료");
		}
	} // end of run()

	public static void main(String[] args) {
		
		int socketPort = 2400; // 소켓 포트 설정용
		List<Socket> clients = new ArrayList<>();
		try {
			ServerSocket serverSocket = new ServerSocket(socketPort); // 서버 소켓 만들기
			System.out.println("socket : " + socketPort); // 서버 오픈 확인용

			// 소켓 서버가 종료될 때까지 무한루프
			while (true) {
				Socket socketUser = serverSocket.accept(); // 서버에 클라이언트 접속 시
				clients.add(socketUser);
				Thread thd = new MySocketServer(socketUser); // Thread 안에 클라이언트 정보를 담아줌
				thd.start(); // Thread 시작
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}