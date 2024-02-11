package Socket;

import java.io.IOException;
import java.net.Socket;

public class MySocketClient {
	
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 2400); // 소켓 서버에 접속 
			System.out.println("서버에 접속합니다"); // 접속 확인용
			
			ListeningThread t1 = new ListeningThread(socket); // 서버에서 보낸 메세지 읽는 Thread
			WritingThread t2 = new WritingThread(socket); // 서버로 메세지 보내는 Thread

			t1.start(); // ListeningThread Start
			t2.start(); // WritingThread Start
            
		} catch (IOException e) {
			//e.printStackTrace(); 
		}
	}
}