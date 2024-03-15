package org.gamenet.swing;

import java.awt.GridBagConstraints;

public class GridBagConstraintsOneShareWidthOneShareHeight extends GridBagConstraints
{
	private static final long serialVersionUID = 4475876413239934953L;

	public GridBagConstraintsOneShareWidthOneShareHeight()
    {
        super();
        this.fill = GridBagConstraints.BOTH;
        this.weightx = 1.0; 
        this.weighty = 1.0; 
    }
}
