import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AlarmProgramGUI2 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("알림 설정");
        frame.setSize(300, 250);
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
        setAlarmDialog.setSize(300, 150);
        setAlarmDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setAlarmDialog.setLayout(new BorderLayout());

        JLabel selectedDateLabel = new JLabel("선택한 날짜: " + selectedDate);
        selectedDateLabel.setHorizontalAlignment(JLabel.CENTER);
        setAlarmDialog.add(selectedDateLabel, BorderLayout.NORTH);

        JTextField dateTimeField = new JTextField(16);
        setAlarmDialog.add(dateTimeField, BorderLayout.CENTER);

        JButton setButton = new JButton("알림 설정");
        setAlarmDialog.add(setButton, BorderLayout.SOUTH);

        setButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputDateTime = dateTimeField.getText();

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date targetDate = dateFormat.parse(inputDateTime);
                    Date currentDate = new Date();

                    long delay = targetDate.getTime() - currentDate.getTime();

                    if (delay < 0) {
                        JOptionPane.showMessageDialog(setAlarmDialog, "과거의 시간을 설정할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(setAlarmDialog, "알림이 설정되었습니다.");
                        setAlarmDialog.dispose();

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
}