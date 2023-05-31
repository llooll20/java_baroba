import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Calendar;
public class TimetableView extends JPanel {
    public JTable timetable;
    int MonthOfToday; //현재 월 저장 변수
    int todayWeekOfDay; // 요일 주 저장 변수
    int cnt = 22;

    int number;
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
        MonthOfToday = c.get(Calendar.MONTH) + 1; // 현재 월        MonthOfToday =
        int numberOfWeeks = c.getActualMaximum(Calendar.WEEK_OF_MONTH); // MAX주

        JLabel lblTitle = new JLabel(MonthOfToday + "월 " + todayWeekOfDay + "째주");//"?"월 "?"째주 - 
        lblTitle.setFont(new Font(DesignConstants.HANGUL_FONT, Font.BOLD, 25));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(DesignConstants.SIGNATURE_COLOR));
        titlePanel.add(lblTitle, BorderLayout.CENTER);//TimetableView 정가운데 JLabel 생성 - 
        JButton lWeekBut;
        lWeekBut = new JButton("저번주");
        JButton nWeekBut;
        nWeekBut = new JButton("다음주");
        lWeekBut.addActionListener(new ActionListener(){//lWeekBut 클릭 이벤트 발생 시 - 
            public void actionPerformed(ActionEvent e) {
                todayWeekOfDay -= 1; //주 요일 감소
                formattedFirstDayOfWeek -= 7;
                cnt --;
                System.out.println(cnt);
                if (todayWeekOfDay == 0) { //주가 0이면 이전 달로 이동
                    if (cnt == 18){
                        MonthOfToday = 4;
                        todayWeekOfDay = 4;
                        number = todayWeekOfDay;
                    }
                    if (cnt == 14){
                        MonthOfToday = 3;
                        todayWeekOfDay = 5;
                        number = todayWeekOfDay;
                    }
                    if (cnt == 9){
                        MonthOfToday = 2;
                        todayWeekOfDay = 4;
                        number = todayWeekOfDay;
                    }
                    if (cnt == 5){
                        MonthOfToday = 1;
                        todayWeekOfDay = 4;
                        number = todayWeekOfDay;

                    }
                    if (cnt == 1){
                        MonthOfToday = 12;
                        todayWeekOfDay = 5;
                        number = todayWeekOfDay;
                        cnt = 52;
                    }
                    if (cnt == 47){
                        MonthOfToday = 11;
                        todayWeekOfDay = 4;
                        number = todayWeekOfDay;
                    }
                    if (cnt == 43){
                        MonthOfToday = 10;
                        todayWeekOfDay = 4;
                        number = todayWeekOfDay;
                    }
                    if (cnt == 39){
                        MonthOfToday = 9;
                        todayWeekOfDay = 4;
                        number = todayWeekOfDay;
                    }
                    if (cnt == 35){
                        MonthOfToday = 8;
                        todayWeekOfDay = 5;
                        number = todayWeekOfDay;
                    }
                    if (cnt == 30){
                        MonthOfToday = 7;
                        todayWeekOfDay = 4;
                        number = todayWeekOfDay;
                    }
                    if (cnt == 26){
                        MonthOfToday = 6;
                        todayWeekOfDay = 5;
                        number = todayWeekOfDay;
                    }
                    if (cnt == 22){
                        MonthOfToday = 5;
                        todayWeekOfDay = 5;
                        number = todayWeekOfDay;
                    }
                }
                if (MonthOfToday == 0) {
                    MonthOfToday = 12;
                }
                lblTitle.setText(MonthOfToday + "월 " + todayWeekOfDay + "째주");//TitlePanel 다시 새기기 - 
                drawTimetable();
            }
        });
        nWeekBut.addActionListener(new ActionListener(){//nWeekBut 클릭 이벤트 발생 시 - 
            public void actionPerformed(ActionEvent e) {
                todayWeekOfDay += 1; //주 요일 증가
                formattedFirstDayOfWeek += 7;
                cnt++;
                System.out.println(number);
                if (MonthOfToday == 4) { //주가 0이면 이전 달로 이동
                    if (todayWeekOfDay == 5) {
                        MonthOfToday = 5;
                        todayWeekOfDay = 1;
                    }
                }
                if (MonthOfToday == 3) {
                    if (todayWeekOfDay == 6){
                        MonthOfToday = 4;
                        todayWeekOfDay = 1;
                    }
                }
                if (MonthOfToday == 2) {
                    if (todayWeekOfDay == 5){
                        MonthOfToday = 3;
                        todayWeekOfDay = 1;
                    }
                }
                if (MonthOfToday == 1) {
                    if (todayWeekOfDay == 5){
                        MonthOfToday = 2;
                        todayWeekOfDay = 1;
                    }
                }
                if (MonthOfToday == 12) {
                    if (todayWeekOfDay == 6){
                        MonthOfToday = 1;
                        todayWeekOfDay = 1;
                        cnt = 0;
                    }
                }
                if (MonthOfToday == 11) {
                    if (todayWeekOfDay == 6){
                        MonthOfToday = 12;
                        todayWeekOfDay = 1;
                    }
                }
                if (MonthOfToday == 10) {
                    if (todayWeekOfDay == 5){
                        MonthOfToday = 11;
                        todayWeekOfDay = 1;
                    }
                }
                if (MonthOfToday == 9) {
                    if (todayWeekOfDay == 5){
                        MonthOfToday = 10;
                        todayWeekOfDay = 1;
                    }
                }
                if (MonthOfToday == 8) {
                    if (todayWeekOfDay == 6){
                        MonthOfToday = 9;
                        todayWeekOfDay = 1;
                    }
                }
                if (MonthOfToday == 7) {
                    if (todayWeekOfDay == 5){
                        MonthOfToday = 8;
                        todayWeekOfDay = 1;
                    }
                }
                if (MonthOfToday == 6) {
                    if (todayWeekOfDay == 6){
                        MonthOfToday = 7;
                        todayWeekOfDay = 1;
                    }
                }
                if (MonthOfToday == 5) {
                    if (todayWeekOfDay == 6){
                        MonthOfToday = 6;
                        todayWeekOfDay = 1;
                    }
                }

               
            }
        });
        titlePanel.add(lWeekBut, BorderLayout.WEST);
        titlePanel.add(nWeekBut, BorderLayout.EAST);
        add(titlePanel, BorderLayout.NORTH); // 메인 패널(this) 상단에 titlePanel 추가
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
    public void drawTimetable() {//문제 발생 - 
        formattedFirstDayOfWeek = Get_Monday_of_Week(MonthOfToday-1, todayWeekOfDay);//현재 MonthOfToday, todayWeekOfDay의 월요일 날짜 반환 - 
        for (int i = 0; i < 7; i++){
            //letthisweek();
            int FDOW = (formattedFirstDayOfWeek+i); //주의 첫요일 + i, 0~7이므로 월화수목금토일까지 돌아감
            boolean result = isExist(String.valueOf(FDOW)); //파일이 존재하면 1, 없으면 0

            if (result) {//존재하면 년월을 제외하고 뒷부분을 fileTime에 담고, fileTime 시간만큼 fillTimetableCell을 통해 시간표 차지
                File directory = new File("MemoData/");
                File[] files = directory.listFiles();
                String fileTime = null;
                for(File f : files){
                    if(f.getName().substring(0,8).equals(String.valueOf(FDOW))){
                        fileTime = f.getName().substring(8, 19);
                    }
                }
                JOptionPane.showMessageDialog(null, "drawTimetable\ni : " + i + ", fileTime : " + fileTime);
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
    private boolean isExist(String DOW){ //파일이 존재하는지 확인하는 메소드, DOW는 formattedFirstDayOfWeek과 같음
        File directory = new File("MemoData/");
        
        File[] files = directory.listFiles();
        boolean isFileExist = false;
        for(File f : files){
            if(f.getName().substring(0,8).equals(DOW))isFileExist = true;
        }
        return isFileExist;
    }//날짜와 일치하는 파일 존재하는지 확인
    private int Get_Monday_of_Week(int Month, int DayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Month);
        calendar.set(Calendar.WEEK_OF_MONTH, DayOfWeek);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int monday_year = calendar.get(Calendar.YEAR) * 10000;
        int monday_month = (calendar.get(Calendar.MONTH) + 1) * 100;
        int monday_day = calendar.get(Calendar.DATE);
        return monday_year + monday_month + monday_day;
    }

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
