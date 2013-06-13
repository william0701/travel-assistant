import javax.swing.*;
import java.awt.event.*;
public class h
{
	public static void main(String args[])
	{
		new gui();
	}
}
class gui implements ActionListener
{
	JFrame jf=new JFrame("Ä£ÄâÎ¢ÐÅ");
	JButton coding=new JButton("´«ËÍ");
	//JButton decoding=new JButton("Decoding");
	JTextField testinput=new JTextField();
	JTextField codesoutput=new JTextField();
	gui()
	{
		jf.setSize(530,280);
		jf.setVisible(true);
		jf.setLayout(null);
		testinput.setSize(350, 40);
		testinput.setLocation(20,10);
		codesoutput.setSize(350,200);
		codesoutput.setLocation(20,60);
		//codesinput.setSize(350, 40);
		//codesinput.setLocation(20,130);
		//testoutput.setSize(350, 40);
		//testoutput.setLocation(20,180);
		coding.setSize(100,40);
		//decoding.setSize(100,40);
		coding.setLocation(400,10);
		//decoding.setLocation(400,130);
		coding.addActionListener(this);
		//decoding.addActionListener(this);
		jf.add(testinput);
		//jf.add(testoutput);
		//jf.add(codesinput);
		//jf.add(codesoutput);
		jf.add(coding);
		//jf.add(decoding);
	}
	public void actionPerformed(ActionEvent e)
	{
		String s;
		String rst;
		s=testinput.getText();
		rst=HelloWeChat.LetsChat(s);
		codesoutput.setText(rst);
	}
}