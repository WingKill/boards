package Board;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Board {
    private Scanner scan;
    private ArrayList<Post> list;
    private PrintWriter writer;

    public Board(OutputStream out) {
        scan = new Scanner(System.in);
        list = new ArrayList<>();
        writer = new PrintWriter(out, true);
    }

    public synchronized void showMenu() {
    	writer.println("---------------------------------");
        writer.println("1 목록 보기 2 글 등록 3 내용 보기 4 글 삭제 0 종료 >> ");
    }

    public synchronized void showList() {
        writer.println("=================================");
        writer.println("번호" + "\t" + "제목" + "\t" + "작성자" + "\t" + "작성일");

        for (Post p : list) {
            String title = truncateTitle(p.getTitle(), 5); // limit title to 10 characters
            writer.println((list.indexOf(p) + 1) + "\t" + title + "\t" + p.getAuthor() + "\t" + p.getDate());
        }

        
        showMenu();
    }
    
    String truncateTitle(String title, int maxLength) {
        if (title.length() > maxLength) {
            title = title.substring(0, maxLength-3) + "...";
        } else {
            title = String.format("%-" + maxLength + "s", title);
        }
        return title;
    }

    public synchronized void addList(String title, String author, String contents) {
        Post post = new Post();
        
        post.setIndex(list.indexOf(post));
        post.setTitle(title);
        post.setAuthor(author);
        post.setParagraph(contents);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy, hh:mm aaa");
        String date = sdf.format(new Date());
        post.setDate(date);

        list.add(post);
        writer.println(author + "의 글이 추가되었습니다.");
        
        showMenu();
    }

    public synchronized void searchList(int num) {
        if (num <= 0 || num > list.size()) {
            writer.println("해당하는 번호에 대한 글이 없습니다.");
            showMenu();
            return;
        }

        Post search = list.get(num - 1);
        writer.println("=================================");
        writer.println(num + "\t" + search.getTitle() + "\t" + search.getAuthor() + "\t" + search.getDate());
        writer.println("---------------------------------");
        writer.println(search.getParagraph());
        
        showMenu();
    }

    public synchronized void delList(int num) {
        if (num <= 0 || num > list.size()) {
            writer.println("해당하는 번호에 대한 글이 없습니다.");
            showMenu();
            return;
        }

        Post del = list.get(num - 1);
        list.remove(del);
        writer.println(num + "번 글이 삭제되었습니다.");
        
        showMenu();
    }
    
    public void showListInServer(){
        System.out.println("=================================");
        System.out.println("번호" + "\t" + "제목" + "\t" + "작성자" + "\t" + "작성일");

        for (Post p : list) {
        	String title = truncateTitle(p.getTitle(), 5);
           System.out.println((list.indexOf(p) + 1) + "\t" + title + "\t" + p.getAuthor() + "\t" + p.getDate());
        }
     }
}