// http://forum.java.sun.com/thread.jspa?threadID=260100&messageID=1124265
// posted by noah.w

package org.gamenet.swing;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class DraggableJListExample extends JFrame {
	private static final long serialVersionUID = -3444198636392409577L;

	Vector<String> con = new Vector<String>();
	JList list = new DraggableJList<String>(con);

	public DraggableJListExample() {
		setBounds(1, 1, 600, 400);
		getContentPane().setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				dispose();
				System.exit(0);
			}
		});

		for (int i = 0; i < 100; i++)
			con.add(" a " + i + " entry");

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 40, 10, 40));
		panel.setLayout(new BorderLayout());
		JScrollPane js = new JScrollPane(panel);
		getContentPane().add("Center", js);

		panel.add("Center", list);
		setVisible(true);
	}

	public static void main(String[] args) throws InterruptedException {
		new DraggableJListExample();
	}
}