package org.gamenet.swing;

import java.awt.GridBagConstraints;

public class GridBagConstraintsWidthRemainderFillWidth extends GridBagConstraints
{
	private static final long serialVersionUID = 8196595180410718413L;

	public GridBagConstraintsWidthRemainderFillWidth()
    {
        super();
        this.anchor = GridBagConstraints.NORTHWEST; 
        this.gridwidth = GridBagConstraints.REMAINDER; 
        this.fill = GridBagConstraints.HORIZONTAL;
    }
}
