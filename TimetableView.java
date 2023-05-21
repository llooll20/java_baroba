package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Calendar;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
public class TimetableView extends JPanel {
    public JTable timetable;
    int MonthOfToday; //현재 월 저장 변수
    int todayWeekOfDay; // 요일 주 저장 변수
    public TimetableView() {
        setLayout(new BorderLayout());
        setTitlePanel();
        setTimetablePanel();
        setVisible(true);
    } // Constructor
    public void setTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setPreferredSize(new Dimension(500, 80));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        Calendar c = Calendar.getInstance(); // 캘린더 인스턴스
        int currentDay = c.get(Calendar.DAY_OF_MONTH); // 현재날짜 가져오기
        todayWeekOfDay = (currentDay  - 1) / 7 + 1; // 요일 주 계싼
        MonthOfToday = c.get(Calendar.MONTH); // 현재 월
        int numberOfWeeks = c.getActualMaximum(Calendar.WEEK_OF_MONTH); // MAX주

        JLabel lblTitle = new JLabel((MonthOfToday + 1) + "월 " + todayWeekOfDay + "째주");
        lblTitle.setFont(new Font(DesignConstants.HANGUL_FONT, Font.BOLD, 25));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(DesignConstants.SIGNATURE_COLOR));
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        JButton lWeekBut;
        lWeekBut = new JButton("저번주");
        JButton nWeekBut;
        nWeekBut = new JButton("다음주");
        class ListenForWeekButtons implements ActionListener { //버튼 클릭 이벤트 처리용 액션클래스
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == lWeekBut) {moveWeek(-1);} //저번주 버튼 클릭시 주 -1
                else if (e.getSource() == nWeekBut) {moveWeek(1);} //다음주 버튼 클릭시 주 +1
            }
        }
        ListenForWeekButtons lForWeekButtons = new ListenForWeekButtons();
        lWeekBut.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                todayWeekOfDay -= 1; //주 요일 감소
                formattedFirstDayOfWeek -= 7;
                if (todayWeekOfDay == 0) { //주가 0이면 이전 달로 이동
                    MonthOfToday -= 1;
                    todayWeekOfDay = numberOfWeeks; // 주를 달의 최대 주로 설정
                }
            }
});
        nWeekBut.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                todayWeekOfDay += 1; //주 요일 증가
                formattedFirstDayOfWeek += 7;
                if (todayWeekOfDay == numberOfWeeks) { //주가 0이면 이전 달로 이동
                    MonthOfToday += 1;
                    todayWeekOfDay = 1; // 주를 달의 첫주로 설정
                }
            }
        });
        titlePanel.add(lWeekBut, BorderLayout.WEST);
        nWeekBut.addActionListener(lForWeekButtons);
        titlePanel.add(nWeekBut, BorderLayout.EAST);
        add(titlePanel, BorderLayout.NORTH); // 메인 패널(this) 상단에 titlePanel 추가
    }
    public int moveWeek(int week){ //임시로 만든 메소드 해결되면 지울것
        Calendar c = Calendar.getInstance();
        int firstDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int todayWeekOfDay = (currentDay + firstDayOfWeek - 2) / 7 + 1;
        int MonthOfToday = c.get(Calendar.MONTH);
        int numberOfWeeks = c.getActualMaximum(Calendar.WEEK_OF_MONTH); // MAX주
        todayWeekOfDay += week;
        if (todayWeekOfDay >= numberOfWeeks) while (todayWeekOfDay >= numberOfWeeks) {
            MonthOfToday++;
            todayWeekOfDay = 1;
        }
        else if (todayWeekOfDay < 0) while (todayWeekOfDay < 0) {
            MonthOfToday--;
            todayWeekOfDay = numberOfWeeks;

        }
        return todayWeekOfDay;
    }
    private void setTimetablePanel() { // 시간표 표 짜기
        String[] columnNames = {"","월","화","수","목","금","토","일"}; // 첫 행 정하기
        DefaultTableModel model = new DefaultTableModel(49, 8); // 시간표 표 정하기
        model.setColumnIdentifiers(columnNames);
        for (int row = 0; row < 48; row++) {
            if (row % 2 == 0) {
                model.setValueAt(row / 2  , row+1, 0);
            } // 표 만들기
        }
        timetable = new JTable(model);
        timetable.setBackground(Color.WHITE);
        timetable.setGridColor(Color.LIGHT_GRAY);
        timetable.setRowHeight(25);
        timetable.setEnabled(false);
        TableColumn dateColumn = timetable.getColumnModel().getColumn(0);
        dateColumn.setPreferredWidth(30);
        setHeaderRenderer();
        //셀간격
        timetable.setIntercellSpacing(new Dimension(0, 2));
        timetable.setDefaultRenderer(Object.class, new customCellRenderer());
        //색상
        timetable.getTableHeader().getColumnModel().getColumn(6).setHeaderRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setBackground(Color.CYAN); // 파란색으로 헤더 색상 변경
                return component;
            }
        });
        // 일요일 헤더 색상 변경
        timetable.getTableHeader().getColumnModel().getColumn(7).setHeaderRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setBackground(Color.PINK); // 빨간색으로 헤더 색상 변경
                return component;
            }
        });
        JScrollPane timetablePanel = new JScrollPane(timetable);
        timetablePanel.setPreferredSize(new Dimension(500*2, 596));
        add(timetablePanel, BorderLayout.CENTER);
        drawTimetable();
    } // setTimetablePanel()
    private void setHeaderRenderer() { //헤더 렌더링
        JTableHeader tableHeader = timetable.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(new Font(DesignConstants.HANGUL_FONT, Font.PLAIN, 10));
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setForeground(Color.DARK_GRAY);
        JLabel headerRenderer = (JLabel)tableHeader.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    } // setHeaderRenderer()
    public JTable getTable() {
        return timetable;
    }
    public static class customCellRenderer extends DefaultTableCellRenderer { //셀 꾸미기
        @Override
        public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            JLabel cell = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            if (value != null) {
                // Row Header
                if (col == 0) {
                    cell.setBackground(Color.WHITE);
                    cell.setHorizontalAlignment(SwingConstants.RIGHT);
                    cell.setVerticalAlignment(SwingConstants.TOP);
                }
                // 실제 시간표 정보를 담고 있는 Cell
                else {
                    cell.setBackground(new Color(DesignConstants.TIMETABLE_CELL_COLOR));
                    cell.setHorizontalAlignment(SwingConstants.LEFT);
                    cell.setVerticalAlignment(SwingConstants.CENTER);
                }
                cell.setFont(new Font(DesignConstants.HANGUL_FONT, Font.PLAIN, 16));
                cell.setForeground(Color.darkGray);
            }
            else
                cell.setBackground(Color.WHITE);
            return cell;
        } // getTableCellRendererComponent()
    } // customCellRenderer Class
    TimetableView timetableView; // 클래스 인스턴스 선언
    int formattedFirstDayOfWeek; //주의 첫번째 요일 선언
    public void drawTimetable() {
        for (int i = 0; i == 7; i++){
            letthisweek();
            int FDOW = (formattedFirstDayOfWeek+i); //주의 첫요일 + i, 0~7이므로 월화수목금토일까지 돌아감
            boolean result = isExist(String.valueOf(FDOW)); //파일이 존재하면 1, 없으면 0
            if (result) {//존재하면 년월을 제외하고 뒷부분을 fileTime에 담고, fileTime 시간만큼 fillTimetableCell을 통해 시간표 차지
                File directory = new File("MemoData/");
                String[] fileNames = directory.list();
                String fileTime = fileNames[0].substring(8);
                fillTimetableCell(String.valueOf(i), fileTime);
            }
        }
    }  //drawTimetable()
    public void fillTimetableCell(String strWeek, String fileTime) {//시간만큼 시간표의 행을 차지하는 메소드
        class TimeModel {
            int hour;
            int minute;
            TimeModel(String time) {
                String[] splitTime = time.split("_"); // _로 시간과 분을 구분하여 추출
                hour = Integer.parseInt(splitTime[0]);
                minute = Integer.parseInt(splitTime[1]);
            } // 생성자
            int isHalf() { //분이 0보다 크면 1, 아니면 0을 반환
                return minute > 0 ? 1 : 0;
            }
        }
        String[] splitTimes = fileTime.split("-");//-로 시작시간과 종료시간을 구분하여 추출
        TimeModel startTime = new TimeModel(splitTimes[0]);
        TimeModel endTime = new TimeModel(splitTimes[1]);
        // 시간표가 추가 될 테이블 행의 시작 인덱스와 종료 인덱스를 계산하여 저장
        int startRowIndex = (startTime.hour - 9) * 2 + startTime.isHalf(); //시작
        int endRowIndex = (endTime.hour - 9) * 2 + endTime.isHalf(); //종료
        JTable table = timetableView.getTable();
        TableColumn column = table.getColumn(strWeek);
        int columnIndex = column.getModelIndex();
        //시작 인덱스부터 종료 인덱스까지 공백으로 시간표 차지
        for (int i = startRowIndex + 2; i <= endRowIndex; ++i)
            table.setValueAt(" ", i, columnIndex);

        //시작 인덱스칸에 메모 내용 차지
        String memoAreaText;
        try {
            BufferedReader in = new BufferedReader(new FileReader("MemoData/"));
            memoAreaText = new String();
            while (true) {
                String tempStr = null;
                try {
                    tempStr = in.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (tempStr == null) break;
                memoAreaText = memoAreaText + tempStr + System.getProperty("line.separator");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        table.setValueAt(memoAreaText, startRowIndex+2,columnIndex);
    }
    public int letthisweek(){//파일이름에서 년월일 추출하는 메소드
        LocalDate currentDate = LocalDate.now(); //오늘날짜 받아오기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); //20230521 이런식의 포맷으로 받아올것
        LocalDate firstDayOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); //오늘날짜 주의 월요일을 받아옴
        int formattedFirstDayOfWeek = Integer.parseInt(firstDayOfWeek.format(formatter)); //오늘날짜 주의 월요일을 위의 포맷으로 받음
        return formattedFirstDayOfWeek;
    }//주의 첫 일 구하기
    private boolean isExist(String DOW){ //파일이 존재하는지 확인하는 메소드, DOW는 formattedFirstDayOfWeek과 같음
        File directory = new File("MemoData/");
        String[] fileNames = directory.list(); //폴더안 파일은 하나밖에 없으므로 리스트로 받아와도됨
        String fileDate = fileNames[0].substring(0,8); // 파일의 년원일부분 추출
        return fileDate == DOW;
    }//날짜와 일치하는 파일 존재하는지 확인

    public static void main(String args[]){ //안돼서 해보는 main 메소드, 일단 남겨두지만 지울것
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new TimetableView();
            }
        });
    }
} // TimetableView Class
 class DesignConstants {
    public static final int SIGNATURE_COLOR = 0x334D61;
    public static final int TIMETABLE_CELL_COLOR = 0xFFF7CB;
    public static final String HANGUL_FONT = "Malgun Gothic";
}