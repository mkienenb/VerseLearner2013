package org.gamenet.swing;

import java.awt.GridBagConstraints;

public class GridBagConstraintsWidthRemainderOneShareHeight extends GridBagConstraints
{
	private static final long serialVersionUID = -7715902478545147246L;

	public GridBagConstraintsWidthRemainderOneShareHeight()
    {
        super();
        this.gridwidth = GridBagConstraints.REMAINDER; 
        this.fill = GridBagConstraints.BOTH;
        this.weighty = 1.0; 
    }
}
