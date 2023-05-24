
// originally uploaded to http://blog.naver.com/azure0777

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; //반응형
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.*;

class CalendarDataManager{ // 6*7배열에 나타낼 달력 값을 구하는 class
    static final int CAL_WIDTH = 7; // 달력의 행, 일주일이므로 7
    final static int CAL_HEIGHT = 6; // 달력의 열, 가장 많은 경우까지 가정하여 6으로 정의
    int calDates[][] = new int[CAL_HEIGHT][CAL_WIDTH]; // 달력 칸수 (날짜)
    int calYear; //달력의 년
    int calMonth; // 달력의 달
    int calDayOfMon; //달력의 날짜
    final int calLastDateOfMonth[]={31,28,31,30,31,30,31,31,30,31,30,31}; //달력의 마지막날
    int calLastDate; //달력의 마지막날짜
    Calendar today = Calendar.getInstance(); // getinstance가 캘린더 클래스의 현재 날짜, 시간 정보가 포함된 캘린더 객체를 반환
    Calendar cal;

    public CalendarDataManager(){
        setToday();
    }
    public void setToday(){ // 오늘 날짜를 기준으로 달력을 설정
        calYear = today.get(Calendar.YEAR);
        calMonth = today.get(Calendar.MONTH);
        calDayOfMon = today.get(Calendar.DAY_OF_MONTH);
        makeCalData(today);
    } 
    private void makeCalData(Calendar cal){
        // 1일의 위치와 마지막 날짜를 구함
        int calStartingPos = (cal.get(Calendar.DAY_OF_WEEK)+7-(cal.get(Calendar.DAY_OF_MONTH))%7)%7; //달 시작 위치를 설정
        if(calMonth == 1) calLastDate = calLastDateOfMonth[calMonth] + leapCheck(calYear); //
        else calLastDate = calLastDateOfMonth[calMonth];
        // 달력 배열 초기화
        for(int i = 0 ; i<CAL_HEIGHT ; i++){
            for(int j = 0 ; j<CAL_WIDTH ; j++){
                calDates[i][j] = 0;
            }
        }
        // 달력 배열에 값 채워넣기
        for(int i = 0, num = 1, k = 0 ; i<CAL_HEIGHT ; i++){ //다음날짜 일일이 설정해줌
            if(i == 0) k = calStartingPos;
            else k = 0;
            for(int j = k ; j<CAL_WIDTH ; j++){
                if(num <= calLastDate) calDates[i][j]=num++;
            }
        }
    }
    private int leapCheck(int year){ // 윤년인지 확인하는 함수
        if(year%4 == 0 && year%100 != 0 || year%400 == 0) return 1;
        else return 0;
    }
    public void moveMonth(int mon){ // 현재달로 부터 n달 전후를 받아 달력 배열을 만드는 함수(1년은 +12, -12달로 이동 가능)
        calMonth += mon;
        if(calMonth>11) while(calMonth>11){
            calYear++;
            calMonth -= 12;
        } else if (calMonth<0) while(calMonth<0){
            calYear--;
            calMonth += 12;
        }
        cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);
        makeCalData(cal);
    }
}

public class MemoCalendar extends CalendarDataManager{ // CalendarDataManager의 GUI + 메모기능 + 시계
    // 창 구성요소와 배치도
    JFrame mainFrame; //달력 프로그램 틀
    //프로그램 아이콘
    JPanel calOpPanel; //달력 위쪽 패널
    JButton todayBut; //오늘 날짜 버튼
    JLabel todayLab; // 현재 날짜 텍스트
    JButton lYearBut; // << 버튼,1 년 전 달 넘어가는 버튼
    JButton lMonBut; // < 버튼, 1달 전 달 넘어가는 버튼
    JLabel curMMYYYYLab; // 오늘 날짜 버튼 옆 년월일 텍스트
    JButton nMonBut; // > 버튼, 1달 앞 달 넘어가는 버튼
    JButton nYearBut; // >> 버튼, 1년 앞 달 넘어가는 버튼
    ListenForCalOpButtons lForCalOpButtons = new ListenForCalOpButtons(); //caloppanel의 이벤트를 처리하는 리스너 객체

    JPanel calPanel; // 달력 부분 패널
    JButton weekDaysName[]; // 일주일의 요일을 나타내는 버튼?
    JButton dateButs[][] = new JButton[6][7]; // 날짜 칸을 버튼으로 만듬
    listenForDateButs lForDateButs = new listenForDateButs(); //달력 날짜 버튼의 이벤트를 처리하는 리스너 객체

    JPanel infoPanel; // 오른쪽 위 시계 패널
    JLabel infoClock; // 오른쪽 시계 텍스트

    JPanel memoPanel; // 메모 부분 패널
    JLabel selectedDate; //
    JTextArea memoArea; //메모를 작성하는 텍스트 부분
    JScrollPane memoAreaSP; // memoarea를 스크롤 할수 있게하는 팬 객체
    JPanel memoSubPanel; // 저장 삭제 초기화 버튼 패널
    JButton saveBut; // 저장 버튼
    JButton delBut; // 삭제 버튼ㅌ
    JButton clearBut; // 초기화 버튼

    JPanel frameBottomPanel; // 하단 패널, 메세지 나옴
    JLabel bottomInfo = new JLabel(""); // 시작할때 나오는 문구
    //상수, 메세지
    final String WEEK_DAY_NAME[] = { "일", "월", "화", "수", "목", "금", "토" };
    final String title = "바로봐 1.0";
    final String SaveButMsg1 = "를 MemoData폴더에 저장하였습니다.";
    final String SaveButMsg2 = "메모를 먼저 작성해 주세요.";
    final String SaveButMsg3 = "<html><font color=red>ERROR : 파일 쓰기 실패</html>";
    final String DelButMsg1 = "메모를 삭제하였습니다.";
    final String DelButMsg2 = "작성되지 않았거나 이미 삭제된 memo입니다.";
    final String DelButMsg3 = "<html><font color=red>ERROR : 파일 삭제 실패</html>";
    final String ClrButMsg1 = "입력된 메모를 비웠습니다.";

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new MemoCalendar();
            }
        });
    }
    public MemoCalendar(){ //구성요소 순으로 정렬되어 있음. 각 판넬 사이에 빈줄로 구별

        mainFrame = new JFrame(title); //
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //프레임이 닫히면 종료
        mainFrame.setSize(1400,800);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        try{
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//LookAndFeel Windows 스타일 적용
            SwingUtilities.updateComponentTreeUI(mainFrame) ;
        }catch(Exception e){
            bottomInfo.setText("ERROR : LookAndFeel setting failed");
        }

        calOpPanel = new JPanel();
        todayBut = new JButton("오늘");
        todayBut.setToolTipText("오늘");
        todayBut.addActionListener(lForCalOpButtons);
        todayLab = new JLabel(today.get(Calendar.YEAR)+"년"+today.get(Calendar.MONTH)+"월"+today.get(Calendar.DAY_OF_MONTH)+"일");
        lYearBut = new JButton("작년");
        lYearBut.setToolTipText("Previous Year");
        lYearBut.addActionListener(lForCalOpButtons);
        lMonBut = new JButton("저번달");
        lMonBut.setToolTipText("Previous Month");
        lMonBut.addActionListener(lForCalOpButtons);
        curMMYYYYLab = new JLabel("<html><table width=100><tr><th><font size=4>"+calYear+"년"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+"월"+"</th></tr></table></html>");
        nMonBut = new JButton("다음달");
        nMonBut.setToolTipText("Next Month");
        nMonBut.addActionListener(lForCalOpButtons);
        nYearBut = new JButton("내년");
        nYearBut.setToolTipText("Next Year");
        nYearBut.addActionListener(lForCalOpButtons);
        calOpPanel.setLayout(new GridBagLayout());
        GridBagConstraints calOpGC = new GridBagConstraints();
        calOpGC.gridx = 1;
        calOpGC.gridy = 1;
        calOpGC.gridwidth = 2;
        calOpGC.gridheight = 1;
        calOpGC.weightx = 1;
        calOpGC.weighty = 1;
        calOpGC.insets = new Insets(5,5,0,0);
        calOpGC.anchor = GridBagConstraints.WEST;
        calOpGC.fill = GridBagConstraints.NONE;
        calOpPanel.add(todayBut,calOpGC);
        calOpGC.gridwidth = 3;
        calOpGC.gridx = 2;
        calOpGC.gridy = 1;
        calOpPanel.add(todayLab,calOpGC);
        calOpGC.anchor = GridBagConstraints.CENTER;
        calOpGC.gridwidth = 1;
        calOpGC.gridx = 1;
        calOpGC.gridy = 2;
        calOpPanel.add(lYearBut,calOpGC);
        calOpGC.gridwidth = 1;
        calOpGC.gridx = 2;
        calOpGC.gridy = 2;
        calOpPanel.add(lMonBut,calOpGC);
        calOpGC.gridwidth = 2;
        calOpGC.gridx = 3;
        calOpGC.gridy = 2;
        calOpPanel.add(curMMYYYYLab,calOpGC);
        calOpGC.gridwidth = 1;
        calOpGC.gridx = 5;
        calOpGC.gridy = 2;
        calOpPanel.add(nMonBut,calOpGC);
        calOpGC.gridwidth = 1;
        calOpGC.gridx = 6;
        calOpGC.gridy = 2;
        calOpPanel.add(nYearBut,calOpGC);

        calPanel=new JPanel();
        weekDaysName = new JButton[7];
        for(int i=0 ; i<CAL_WIDTH ; i++){
            weekDaysName[i]=new JButton(WEEK_DAY_NAME[i]);
            weekDaysName[i].setBorderPainted(false);
            weekDaysName[i].setContentAreaFilled(false);
            weekDaysName[i].setForeground(Color.WHITE);
            if(i == 0) weekDaysName[i].setBackground(new Color(200, 50, 50));
            else if (i == 6) weekDaysName[i].setBackground(new Color(50, 100, 200));
            else weekDaysName[i].setBackground(new Color(150, 150, 150));
            weekDaysName[i].setOpaque(true);
            weekDaysName[i].setFocusPainted(false);
            calPanel.add(weekDaysName[i]);
        }
        for(int i=0 ; i<CAL_HEIGHT ; i++){
            for(int j=0 ; j<CAL_WIDTH ; j++){
                dateButs[i][j]=new JButton();
                dateButs[i][j].setBorderPainted(false);
                dateButs[i][j].setContentAreaFilled(false);
                dateButs[i][j].setBackground(Color.WHITE);
                dateButs[i][j].setOpaque(true);
                dateButs[i][j].addActionListener(lForDateButs);
                calPanel.add(dateButs[i][j]);
            }
        }
        calPanel.setLayout(new GridLayout(0,7,2,2));
        calPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        showCal(); // 달력을 표시

        infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoClock = new JLabel("", SwingConstants.RIGHT);
        infoClock.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(infoClock, BorderLayout.NORTH);
        selectedDate = new JLabel("<Html><font size=3>"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.DAY_OF_MONTH)+"/"+today.get(Calendar.YEAR)+"&nbsp;(Today)</html>", SwingConstants.LEFT);
        selectedDate.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        timeTableBut = new JButton("시간표"); //시간표 버튼
        timeTableBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTimetableView();
            }
        });
        
        memoPanel=new JPanel();
        memoPanel.setBorder(BorderFactory.createTitledBorder("메모"));
        memoArea = new JTextArea();
        memoArea.setLineWrap(true);
        memoArea.setWrapStyleWord(true);
        memoAreaSP = new JScrollPane(memoArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        readMemo();

        memoSubPanel=new JPanel();
        saveBut = new JButton("저장");
        saveBut.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                try {
                    File f= new File("MemoData");
                    if(!f.isDirectory()) f.mkdir();

                    String memo = memoArea.getText();
                    if(memo.length()>0){
                        BufferedWriter out = new BufferedWriter(new FileWriter("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt"));
                        String str = memoArea.getText();
                        out.write(str);
                        out.close();
                        bottomInfo.setText(calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt"+SaveButMsg1);
                    }
                    else
                        bottomInfo.setText(SaveButMsg2);
                } catch (IOException e) {
                    bottomInfo.setText(SaveButMsg3);
                }
                showCal();
            }
        });
        delBut = new JButton("삭제");
        delBut.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                memoArea.setText("");
                File f =new File("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt");
                if(f.exists()){
                    f.delete();
                    showCal();
                    bottomInfo.setText(DelButMsg1);
                }
                else
                    bottomInfo.setText(DelButMsg2);
            }
        });
        clearBut = new JButton("초기화");
        clearBut.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                memoArea.setText(null);
                bottomInfo.setText(ClrButMsg1);
            }
        });
        memoSubPanel.add(saveBut);
        memoSubPanel.add(delBut);
        memoSubPanel.add(clearBut);
        memoPanel.setLayout(new BorderLayout());
        memoPanel.add(selectedDate, BorderLayout.NORTH);
        memoPanel.add(memoAreaSP,BorderLayout.CENTER);
        memoPanel.add(memoSubPanel,BorderLayout.SOUTH);

        //calOpPanel, calPanel을  frameSubPanelWest에 배치
        JPanel frameSubPanelWest = new JPanel();
        Dimension calOpPanelSize = calOpPanel.getPreferredSize();
        calOpPanelSize.height = 90;
        calOpPanel.setPreferredSize(calOpPanelSize);
        frameSubPanelWest.setLayout(new BorderLayout());
        frameSubPanelWest.add(calOpPanel,BorderLayout.NORTH);
        frameSubPanelWest.add(calPanel,BorderLayout.CENTER);

        //infoPanel, memoPanel을  frameSubPanelEast에 배치
        JPanel frameSubPanelEast = new JPanel();
        Dimension infoPanelSize=infoPanel.getPreferredSize();
        infoPanelSize.height = 65;
        infoPanel.setPreferredSize(infoPanelSize);
        frameSubPanelEast.setLayout(new BorderLayout());
        frameSubPanelEast.add(infoPanel,BorderLayout.NORTH);
        frameSubPanelEast.add(memoPanel,BorderLayout.CENTER);

        Dimension frameSubPanelWestSize = frameSubPanelWest.getPreferredSize();
        frameSubPanelWestSize.width = 410;
        frameSubPanelWest.setPreferredSize(frameSubPanelWestSize);

        //뒤늦게 추가된 bottom Panel..
        frameBottomPanel = new JPanel();
        frameBottomPanel.add(bottomInfo);

        //frame에 전부 배치
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(frameSubPanelWest, BorderLayout.WEST);
        mainFrame.add(frameSubPanelEast, BorderLayout.CENTER);
        mainFrame.add(frameBottomPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);

        focusToday(); //현재 날짜에 focus를 줌 (mainFrame.setVisible(true) 이후에 배치해야함)


    }
    private void focusToday(){
        if(today.get(Calendar.DAY_OF_WEEK) == 1)
            dateButs[today.get(Calendar.WEEK_OF_MONTH)][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
        else
            dateButs[today.get(Calendar.WEEK_OF_MONTH)-1][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
    }
    private void readMemo(){
        try{
            File f = new File("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt");
            if(f.exists()){
                BufferedReader in = new BufferedReader(new FileReader("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt"));
                String memoAreaText= new String();
                while(true){
                    String tempStr = in.readLine();
                    if(tempStr == null) break;
                    memoAreaText = memoAreaText + tempStr + System.getProperty("line.separator");
                }
                memoArea.setText(memoAreaText);
                in.close();
            }
            else memoArea.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showCal(){
        for(int i=0;i<CAL_HEIGHT;i++){
            for(int j=0;j<CAL_WIDTH;j++){
                String fontColor="black";
                if(j==0) fontColor="red";
                else if(j==6) fontColor="blue";

                File f =new File("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDates[i][j]<10?"0":"")+calDates[i][j]+".txt");
                if(f.exists()){
                    dateButs[i][j].setText("<html><b><font color="+fontColor+">"+calDates[i][j]+"</font></b></html>");
                }
                else dateButs[i][j].setText("<html><font color="+fontColor+">"+calDates[i][j]+"</font></html>");

                JLabel todayMark = new JLabel("<html><font color=green>*</html>");
                dateButs[i][j].removeAll();
                if(calMonth == today.get(Calendar.MONTH) &&
                        calYear == today.get(Calendar.YEAR) &&
                        calDates[i][j] == today.get(Calendar.DAY_OF_MONTH)){
                    dateButs[i][j].add(todayMark);
                    dateButs[i][j].setToolTipText("Today");
                }

                if(calDates[i][j] == 0) dateButs[i][j].setVisible(false);
                else dateButs[i][j].setVisible(true);
            }
        }
    }
    private class ListenForCalOpButtons implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == todayBut){
                setToday();
                lForDateButs.actionPerformed(e);
                focusToday();
            }
            else if(e.getSource() == lYearBut) moveMonth(-12);
            else if(e.getSource() == lMonBut) moveMonth(-1);
            else if(e.getSource() == nMonBut) moveMonth(1);
            else if(e.getSource() == nYearBut) moveMonth(12);

            curMMYYYYLab.setText("<html><table width=100><tr><th><font size=4>"+calYear+"년"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+"월"+"</th></tr></table></html>");
            showCal();
        }
    }
    private class listenForDateButs implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            int k=0,l=0;
            for(int i=0 ; i<CAL_HEIGHT ; i++){
                for(int j=0 ; j<CAL_WIDTH ; j++){
                    if(e.getSource() == dateButs[i][j]){
                        k=i;
                        l=j;
                    }
                }
            }

            if(!(k ==0 && l == 0)) calDayOfMon = calDates[k][l]; //today버튼을 눌렀을때도 이 actionPerformed함수가 실행되기 때문에 넣은 부분

            cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);

            String dDayString = new String();
            int dDay=((int)((cal.getTimeInMillis() - today.getTimeInMillis())/1000/60/60/24));
            if(dDay == 0 && (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR))
                    && (cal.get(Calendar.MONTH) == today.get(Calendar.MONTH))
                    && (cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH))) dDayString = "Today";
            else if(dDay >=0) dDayString = "D-"+(dDay+1);
            else if(dDay < 0) dDayString = "D+"+(dDay)*(-1);

            selectedDate.setText("<Html><font size=3>"+(calMonth+1)+"/"+calDayOfMon+"/"+calYear+"&nbsp;("+dDayString+")</html>");

            readMemo();
        }
    }
    private void showTimetableView()  {
        TimetableView timetableView = new TimetableView();
        JFrame timetableFrame = new JFrame("시간표");
        timetableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        timetableFrame.setSize(600*2, 400*2);
        timetableFrame.setLayout(new BorderLayout());
        timetableFrame.add(timetableView, BorderLayout.CENTER);
        timetableFrame.setVisible(true);
    }
}
