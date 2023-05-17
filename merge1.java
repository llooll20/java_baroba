import java.io.*;

public class merge1 {
    public static void main(String[ ] args) throws IOException{
        String path1 = System.getProperty("user.dir")+"/databox"; //폴더 경로
	File Folder = new File(path1);

	// 해당 디렉토리가 없다면 디렉토리를 생성.
	if (!Folder.exists()) {
		try{
		    Folder.mkdir(); //폴더 생성합니다. ("새폴더"만 생성)
		    System.out.println("폴더가 생성완료.");
	        } 
	        catch(Exception e){
		    e.getStackTrace();
		}        
         }else {
		System.out.println("폴더가 이미 존재합니다..");
//
        String path2 = System.getProperty("user.dir") + "/databox/";
            // System.getProperty("user.dir")을 통해 
            // 현재 working dir까지 가져오고 그 뒤에 경로는 따로 추가해 줌
        System.out.println(path2);
        //String content =path;
        BufferedReader br = new BufferedReader(new FileReader(path2 + "MKML.txt"));
        String txtName1 = "MKML3";
        PrintWriter pw = new PrintWriter(new FileWriter(path2 + txtName1 +".txt"));
        pw.write(path2);
        pw.write("new text");
        	// new FileReader("D:\\eclipse-workspace\\JavaStudy\\src\\FILEIO\\input.txt")
        	// 이런식으로 전체 경로를 다 써줘도 됨

        String str;
        while(true) {
            str = br.readLine();	// BufferedReader을 이용해 한 줄 씩 불러오기
            if(str == null) break;
            pw.println(str);		// PrintWriter을 이용해 한 줄 씩 출력하기
        }

        br.close();
        pw.close();
    }
    }
}
