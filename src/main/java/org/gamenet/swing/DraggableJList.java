// http://forum.java.sun.com/thread.jspa?threadID=260100&messageID=1124265
// posted by noah.w

package org.gamenet.swing;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.border.MatteBorder;

public class DraggableJList<E> extends JList {
	private static final long serialVersionUID = -5739484013154330826L;
	
	int from;

	public DraggableJList(final Vector<E> con) {
		super(con);
		
		setBorder(new MatteBorder(1, 1, 1, 1, Color.orange));
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent m) {
				from = getSelectedIndex();
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent m) {
				int to = getSelectedIndex();
				if (to == from)
					return;
				E s = con.remove(from);
				con.add(to, s);
				from = to;
			}
		});
	}
}
