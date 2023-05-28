
import javax.swing.JOptionPane;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AlarmProgramGUI {

    public static void main(String[] args) {
        String memoDataFolderPath = "MemoData";

        // 현재 날짜 및 시간 가져오기
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // YYYYMMDD 형식의 날짜로 변환
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 폴더 경로 생성
        String folderPath = memoDataFolderPath + File.separator + formattedDate;

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("해당 날짜의 메모 파일이 존재하지 않습니다.");
            return;
        }

        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                String fileName = file.getName().replace(".txt", "");
                String[] timeParts = fileName.split("-");

                String startTime = timeParts[0];

                String[] startParts = startTime.split("_");

                int startHour = Integer.parseInt(startParts[0]);
                int startMinute = Integer.parseInt(startParts[1]);

                LocalTime fileStartTime = LocalTime.of(startHour, startMinute);

                if (currentTime.getHour() == fileStartTime.getHour() && currentTime.getMinute() == fileStartTime.getMinute()) {
                    String message = fileName + " 파일이름의 메모가 해당 시간입니다.";
                    JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.INFORMATION_MESSAGE);
                    break;  // 알림 울린 후 반복문 종료
                }
            }
        }
    }
}
