import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AlarmProgramGUI3 {
    private static final String ALARM_FILE_PATH = "alarms.txt";

    private static final String[] TIME_FORMATS = {"HH:mm", "hh:mm a"};

    public static void main(String[] args) {
        JFrame frame = new JFrame("알림 설정");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel calendarPanel = createCalendarPanel();
        frame.add(calendarPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public static JPanel createCalendarPanel() {
        JPanel calendarPanel = new JPanel();
        calendarPanel.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new GridLayout(1, 7));
        String[] dayLabels = {"일", "월", "화", "수", "목", "금", "토"};
        for (String day : dayLabels) {
            JLabel label = new JLabel(day, JLabel.CENTER);
            label.setForeground(Color.RED);
            headerPanel.add(label);
        }
        calendarPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel daysPanel = new JPanel(new GridLayout(0, 7));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i < firstDayOfWeek; i++) {
            daysPanel.add(new JLabel(""));
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ActionListener dayButtonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                String selectedDate = button.getText();
                showSetAlarmDialog(selectedDate);
            }
        };

        for (int i = 1; i <= daysInMonth; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.addActionListener(dayButtonListener);
            daysPanel.add(button);
        }

        calendarPanel.add(daysPanel, BorderLayout.CENTER);
        return calendarPanel;
    }

    public static void showSetAlarmDialog(final String selectedDate) {
        JDialog setAlarmDialog = new JDialog();
        setAlarmDialog.setTitle("알림 설정");
        setAlarmDialog.setSize(400, 250);
        setAlarmDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setAlarmDialog.setLayout(new BorderLayout());

        JLabel selectedDateLabel = new JLabel("선택한 날짜: " + selectedDate);
        selectedDateLabel.setHorizontalAlignment(JLabel.CENTER);
        setAlarmDialog.add(selectedDateLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel yearLabel = new JLabel("년:");
        JComboBox<String> yearBox = new JComboBox<>(getYearArray());
        panel.add(yearLabel);
        panel.add(yearBox);

        JLabel monthLabel = new JLabel("월:");
        JComboBox<String> monthBox = new JComboBox<>(getMonthArray());
        panel.add(monthLabel);
        panel.add(monthBox);

        JLabel dayLabel = new JLabel("일:");
        JComboBox<String> dayBox = new JComboBox<>(getDayArray());
        panel.add(dayLabel);
        panel.add(dayBox);

        JLabel hourLabel = new JLabel("시:");
        JComboBox<String> hourBox = new JComboBox<>(getHourArray());
        panel.add(hourLabel);
        panel.add(hourBox);

        JLabel minuteLabel = new JLabel("분:");
        JComboBox<String> minuteBox = new JComboBox<>(getMinuteArray());
        panel.add(minuteLabel);
        panel.add(minuteBox);

        setAlarmDialog.add(panel, BorderLayout.CENTER);

        JButton setButton = new JButton("알림 설정");
        setAlarmDialog.add(setButton, BorderLayout.SOUTH);

        setButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedYear = (String) yearBox.getSelectedItem();
                String selectedMonth = (String) monthBox.getSelectedItem();
                String selectedDay = (String) dayBox.getSelectedItem();
                String selectedHour = (String) hourBox.getSelectedItem();
                String selectedMinute = (String) minuteBox.getSelectedItem();

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String inputDateTime = selectedYear + "-" + selectedMonth + "-" + selectedDay + " " + selectedHour + ":" + selectedMinute;

                    Date targetDate = dateFormat.parse(inputDateTime);
                    Date currentDate = new Date();

                    long delay = targetDate.getTime() - currentDate.getTime();

                    if (delay < 0) {
                        JOptionPane.showMessageDialog(setAlarmDialog, "과거의 시간을 설정할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(setAlarmDialog, "알림이 설정되었습니다.");
                        setAlarmDialog.dispose();

                        saveAlarm(selectedDate, inputDateTime);

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                JOptionPane.showMessageDialog(null, "알림! 지정한 시간입니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                                timer.cancel();
                            }
                        }, delay);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(setAlarmDialog, "올바른 날짜와 시간 형식을 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setAlarmDialog.setVisible(true);
    }

    private static String[] getYearArray() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[10];
        for (int i = 0; i < 10; i++) {
            years[i] = Integer.toString(currentYear + i);
        }
        return years;
    }

    private static String[] getMonthArray() {
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = Integer.toString(i + 1);
        }
        return months;
    }

    private static String[] getDayArray() {
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = Integer.toString(i + 1);
        }
        return days;
    }

    private static String[] getHourArray() {
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = Integer.toString(i);
        }
        return hours;
    }

    private static String[] getMinuteArray() {
        String[] minutes = new String[60];
        for (int i = 0; i < 60; i++) {
            minutes[i] = Integer.toString(i);
        }
        return minutes;
    }

    private static void saveAlarm(String selectedDate, String inputDateTime) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(ALARM_FILE_PATH, true));
            writer.write(selectedDate + " " + inputDateTime);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
