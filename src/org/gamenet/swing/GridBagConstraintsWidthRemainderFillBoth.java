package org.gamenet.swing;

import java.awt.GridBagConstraints;

public class GridBagConstraintsWidthRemainderFillBoth extends GridBagConstraints
{
	private static final long serialVersionUID = 1124225572839290858L;

	public GridBagConstraintsWidthRemainderFillBoth()
    {
        super();
        this.anchor = GridBagConstraints.NORTHWEST; 
        this.gridwidth = GridBagConstraints.REMAINDER; 
        this.fill = GridBagConstraints.BOTH;
    }
}
