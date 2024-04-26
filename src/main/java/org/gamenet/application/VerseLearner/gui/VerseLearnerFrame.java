package org.gamenet.application.VerseLearner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gamenet.application.VerseLearner.VerseLearner;
import org.gamenet.application.VerseLearner.data.Verse;
import org.gamenet.swing.DraggableJList;
import org.gamenet.swing.GridBagConstraintsOneShareWidthOneShareHeight;
import org.gamenet.swing.GridBagConstraintsWidthRemainder;
import org.gamenet.swing.GridBagConstraintsWidthRemainderOneShareHeight;

public class VerseLearnerFrame extends JFrame {
	private static final long serialVersionUID = -3726495478154449465L;

    final private JPanel modeVerseTextFromReferencesPanel = new JPanel();
    
    final private JScrollPane verseOutputJScrollPane = new JScrollPane();
    final private JTextArea verseOutputTextComponent = new JTextArea(1, 40);
	final private JTextArea verseInputTextComponent = new JTextArea(1, 40);
	final private JLabel verseReferenceLabel = new JLabel();
	final private JTextField verseReferenceInput = new JTextField(20);
    final private JTextArea answerLabel = new JTextArea(1, 40);
    final private JTextArea checkAnswerLabel = new JTextArea(1, 40);
	final private JLabel hintLabel = new JLabel();
	final private JButton showAnswerButton = new JButton("Answer");
	final private JButton hideAnswerButton = new JButton("Hide Answer");
	final private JButton verseReferenceButton = new JButton("Switch to this verse");
	final private JButton reportUnhelpfulHintButton = new JButton("Report Unhelpful Hint");
    final private JButton clearPreviousVersesButton = new JButton("Clear completed verse display.");

    final private JPanel modeVerseOrderPanel = new JPanel();

    final private JPanel modeBookOrderPanel = new JPanel();
    
    private VerseLearner verseLearnerApplication;
    
	public VerseLearnerFrame(VerseLearner verseLearner) {
		super("VerseLearner");
		
		this.verseLearnerApplication = verseLearner;

		initialize();
	}
	
	public void initialize() {
		this.setSize(640, 480);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

        setUpMenuUI();

        this.getContentPane().setLayout(new BorderLayout()); 
        this.getContentPane().add(modeVerseTextFromReferencesPanel, BorderLayout.CENTER);
		
        initializeModeVerseTextFromReferenceUI();
        initializeModeVerseOrderUI();
        initializeModeBookOrderUI();
	}

	private void setUpMenuUI()
    {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
 
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenu modeMenu = new JMenu("Activities");
        menuBar.add(modeMenu);

        JMenu optionMenu = new JMenu("Options");
        menuBar.add(optionMenu);

        JMenuItem openMenuItem = new JMenuItem("Open");
        fileMenu.add(openMenuItem);
        
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        fileMenu.add(quitMenuItem);
        
        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	verseLearnerApplication.openFile();
            }
        });
        
        final Component parent = this;
        quitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(parent, "Are you sure you want to quit?");
                if (JOptionPane.YES_OPTION == result || JOptionPane.OK_OPTION == result)
                {
                	verseLearnerApplication.quit();
                }
            }
        });
        
        ButtonGroup modeButtonGroup = new ButtonGroup();
        
        JRadioButtonMenuItem modeFullVerseTextMenuItem = new JRadioButtonMenuItem("Write verses from references");
        modeButtonGroup.add(modeFullVerseTextMenuItem);
        modeMenu.add(modeFullVerseTextMenuItem);

        JRadioButtonMenuItem modeVerseOrderMenuItem = new JRadioButtonMenuItem("Put verses in correct order.");
        modeButtonGroup.add(modeVerseOrderMenuItem);
        modeMenu.add(modeVerseOrderMenuItem);
        
        JRadioButtonMenuItem modeBookOrderMenuItem = new JRadioButtonMenuItem("Put books of the bible in correct order.");
        modeButtonGroup.add(modeBookOrderMenuItem);
        modeMenu.add(modeBookOrderMenuItem);
        
        modeFullVerseTextMenuItem.setSelected(true);

        modeFullVerseTextMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verseLearnerApplication.setWriteVersesFromReferencesMode();
            }
        });

        modeVerseOrderMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verseLearnerApplication.setPutVersesInCorrectOrderMode();
            }
        });

        modeBookOrderMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verseLearnerApplication.setPutBooksOfTheBibleInCorrectOrderMode();
            }
        });

        // optionMenu.addSeparator();
        final JCheckBoxMenuItem allowAnyNumberOfSpacesAfterASentenceCheckbox = new JCheckBoxMenuItem(
        		"Allow any number of spaces after a sentence",
        		verseLearnerApplication.isAllowAnyNumberOfSpacesAfterASentence());
        optionMenu.add(allowAnyNumberOfSpacesAfterASentenceCheckbox);

        final JCheckBoxMenuItem ignoreWhitespaceCheckbox = new JCheckBoxMenuItem(
        		"Ignore whitespace",
        		verseLearnerApplication.isIgnoreWhitespace());
        optionMenu.add(ignoreWhitespaceCheckbox);

        final JCheckBoxMenuItem ignoreCaseCheckbox = new JCheckBoxMenuItem(
        		"Ignore case-sensitivity",
        		verseLearnerApplication.isIgnoreCase());
        optionMenu.add(ignoreCaseCheckbox);

        final JCheckBoxMenuItem ignorePunctuationCheckbox = new JCheckBoxMenuItem(
        		"Ignore punctuation",
        		verseLearnerApplication.isIgnorePunctuation());
        optionMenu.add(ignorePunctuationCheckbox);

        allowAnyNumberOfSpacesAfterASentenceCheckbox.addChangeListener(new ChangeListener() {
            
            public void stateChanged(ChangeEvent e) {
                ToggleButtonModel toggleButtonModel = (ToggleButtonModel)allowAnyNumberOfSpacesAfterASentenceCheckbox.getModel();
                verseLearnerApplication.setAllowAnyNumberOfSpacesAfterASentence(toggleButtonModel.isSelected());
            }
        });

        ignoreWhitespaceCheckbox.addChangeListener(new ChangeListener() {
            
            public void stateChanged(ChangeEvent e) {
                ToggleButtonModel toggleButtonModel = (ToggleButtonModel)ignoreWhitespaceCheckbox.getModel();
                verseLearnerApplication.setIgnoreWhitespace(toggleButtonModel.isSelected());
            }
        });
        
        ignoreCaseCheckbox.addChangeListener(new ChangeListener() {
            
            public void stateChanged(ChangeEvent e) {
                ToggleButtonModel toggleButtonModel = (ToggleButtonModel)ignoreCaseCheckbox.getModel();
                verseLearnerApplication.setIgnoreCase(toggleButtonModel.isSelected());
            }
        });

        ignorePunctuationCheckbox.addChangeListener(new ChangeListener() {
            
            public void stateChanged(ChangeEvent e) {
                ToggleButtonModel toggleButtonModel = (ToggleButtonModel)ignorePunctuationCheckbox.getModel();
                verseLearnerApplication.setIgnorePunctuation(toggleButtonModel.isSelected());
            }
        });
    }

    private void initializeModeBookOrderUI()
    {
        DraggableJList<String> bookJList = new DraggableJList<String>(verseLearnerApplication.getBookNameVector());
        bookJList.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent m) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run()
                    {
                    	verseLearnerApplication.bookDragged();
                    }
                });
            }
        });

        modeBookOrderPanel.setLayout(new GridBagLayout());
        modeBookOrderPanel.add(new JScrollPane(bookJList), new GridBagConstraintsOneShareWidthOneShareHeight());
    }

    private void initializeModeVerseOrderUI()
    {
        final DraggableJList<Verse> bookJList = new DraggableJList<Verse>(verseLearnerApplication.getVerseVector());
        bookJList.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent m) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run()
                    {
                    	verseLearnerApplication.verseDragged();
                    }
                });
            }
        });

        class JLabelOutputRenderer extends JLabel implements ListCellRenderer
        {
			private static final long serialVersionUID = -5503085525279912369L;

			public Component getListCellRendererComponent(JList list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus)
            {
                Verse verse = (Verse)value;
                String output = verse.getText();
                if (verseLearnerApplication.isShowReferencesOnEachVerse())
                {
                    output = verse.getReference() + ": " + output;
                }
                
                this.setText(output);
                
                if (isSelected) {
                    this.setBackground(list.getSelectionBackground());
                    this.setForeground(list.getSelectionForeground());
                }
                else {
                    this.setBackground(list.getBackground());
                    this.setForeground(list.getForeground());
                }
                this.setEnabled(list.isEnabled());
                this.setFont(list.getFont());
                this.setOpaque(true);
                
                return this;
            }
        };

        ListCellRenderer bookJListCellRenderer = new JLabelOutputRenderer();
        bookJList.setCellRenderer(bookJListCellRenderer);
        
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        
        modeVerseOrderPanel.setLayout(new GridBagLayout());
        modeVerseOrderPanel.add(controlsPanel, new GridBagConstraintsOneShareWidthOneShareHeight());

        final JCheckBox showReferencesOnEachVerseCheckbox = new JCheckBox(
        		"Show Reference On Each Verse",
        		verseLearnerApplication.isShowReferencesOnEachVerse());

        controlsPanel.add(new JScrollPane(bookJList));
        controlsPanel.add(showReferencesOnEachVerseCheckbox);
        
        showReferencesOnEachVerseCheckbox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ToggleButtonModel toggleButtonModel = (ToggleButtonModel)showReferencesOnEachVerseCheckbox.getModel();
                verseLearnerApplication.setShowReferencesOnEachVerse(toggleButtonModel.isSelected());

                bookJList.invalidate();
                bookJList.repaint();
            }
        });
        
    }

    private void initializeModeVerseTextFromReferenceUI()
    {
        final JButton checkAnswerButton = new JButton("Check");
        final JButton hintButton = new JButton("Hint");
        final JButton previousVerseButton = new JButton("Go back to previous verse");
        final JButton nextVerseButton = new JButton("Go to next verse");
        
        final JCheckBox includeReferenceInAnswerCheckbox = new JCheckBox(
        		"Include Reference In Answer",
        		verseLearnerApplication.isIncludeReferenceInAnswer());
        
        verseOutputTextComponent.setEditable(false);
        verseOutputTextComponent.setLineWrap(true);
        verseOutputTextComponent.setWrapStyleWord(true);

        JPanel verseOutputInternalPanel = new JPanel();
        verseOutputInternalPanel.setLayout(new FlowLayout());
        verseOutputInternalPanel.add(verseOutputTextComponent);

		JPanel inputTextPanel = new JPanel();
		inputTextPanel.setBorder(new LineBorder(Color.BLACK));
		inputTextPanel.setLayout(new BoxLayout(inputTextPanel, BoxLayout.Y_AXIS));

		verseInputTextComponent.setLineWrap(true);
        verseInputTextComponent.setWrapStyleWord(true);
        inputTextPanel.add(verseInputTextComponent);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

		JPanel buttonRowPanel = new JPanel();
		buttonRowPanel.setLayout(new FlowLayout());
		
		JPanel buttonRow2Panel = new JPanel();
		buttonRow2Panel.setLayout(new FlowLayout());

        JPanel preferencesPanel = new JPanel();
        preferencesPanel.setLayout(new FlowLayout());

		controlPanel.add(buttonRowPanel);
		buttonRowPanel.add(showAnswerButton);
		buttonRowPanel.add(hideAnswerButton);
		buttonRowPanel.add(checkAnswerButton);
		buttonRowPanel.add(hintButton);
		buttonRowPanel.add(reportUnhelpfulHintButton);
		
        clearPreviousVersesButton.setVisible(false);
        
		controlPanel.add(buttonRow2Panel);
        buttonRow2Panel.add(clearPreviousVersesButton);
		buttonRow2Panel.add(previousVerseButton);
		buttonRow2Panel.add(nextVerseButton);

        JScrollPane verseInputPanel = new JScrollPane(inputTextPanel);
        
        verseOutputJScrollPane.setViewportView(verseOutputInternalPanel);
        verseOutputJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        verseOutputJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        verseOutputJScrollPane.setVisible(false);

        JPanel verseReferencePanel = new JPanel();
        verseReferencePanel.setLayout(new FlowLayout());

		verseReferencePanel.add(verseReferenceLabel);
		verseReferencePanel.add(verseReferenceInput);
		verseReferencePanel.add(verseReferenceButton);
		
        modeVerseTextFromReferencesPanel.setLayout(new GridBagLayout());

        answerLabel.setEditable(false);
        answerLabel.setLineWrap(true);
        answerLabel.setWrapStyleWord(true);
        answerLabel.setBackground(null);
        answerLabel.setFont(answerLabel.getFont().deriveFont(Font.BOLD));
        
        checkAnswerLabel.setEditable(false);
        checkAnswerLabel.setLineWrap(true);
        checkAnswerLabel.setWrapStyleWord(true);
        checkAnswerLabel.setBackground(null);
        checkAnswerLabel.setFont(checkAnswerLabel.getFont().deriveFont(Font.BOLD));
        checkAnswerLabel.setForeground(Color.GREEN.darker().darker().darker());
        hintLabel.setForeground(Color.BLUE.darker().darker().darker());
        
//        // Vertically laid out labels.
//        
//        Box labels = Box.createVerticalBox();
//        labels.add(new JLabel("Keep on scrollin'"));
//        labels.add(new JLabel("Keep on scrollin'"));
//        labels.add(new JLabel("Keep on scrollin'"));
//        labels.add(new JLabel("Keep on scrollin'"));
// 
//        // Scroll pane for labels.
//        JScrollPane scroller = new JScrollPane(labels)
//        {
//            public Dimension getMaximumSize()
//            {
//                return getPreferredSize();
//            }
// 
//            public Dimension getMinimumSize()
//            {
//                return getPreferredSize();
//            }
//        };
// 
        
        JPanel helpsPanel = new JPanel();
        helpsPanel.setLayout(new GridBagLayout());
        helpsPanel.add(answerLabel, new GridBagConstraintsWidthRemainderOneShareHeight());
        helpsPanel.add(checkAnswerLabel, new GridBagConstraintsWidthRemainderOneShareHeight());
        helpsPanel.add(hintLabel, new GridBagConstraintsWidthRemainderOneShareHeight());

        modeVerseTextFromReferencesPanel.add(verseOutputJScrollPane, new GridBagConstraintsWidthRemainderOneShareHeight());
        modeVerseTextFromReferencesPanel.add(verseReferencePanel, new GridBagConstraintsWidthRemainder());
        modeVerseTextFromReferencesPanel.add(verseInputPanel, new GridBagConstraintsWidthRemainderOneShareHeight());
        modeVerseTextFromReferencesPanel.add(helpsPanel, new GridBagConstraintsWidthRemainder());
        modeVerseTextFromReferencesPanel.add(controlPanel, new GridBagConstraintsWidthRemainder());

        // main.pack();
        
        clearPreviousVersesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearCompletedVerseList();                
            }
        });

        final Component rootWindow = this;
        verseReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(rootWindow, "Are you sure you want to change verses?");
                if (JOptionPane.YES_OPTION == result || JOptionPane.OK_OPTION == result) {
                	verseLearnerApplication.changeVerses(verseReferenceInput.getText());
                }
            }
        });

		includeReferenceInAnswerCheckbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ToggleButtonModel toggleButtonModel = (ToggleButtonModel)includeReferenceInAnswerCheckbox.getModel();
				verseLearnerApplication.setIncludeReferenceInAnswer(toggleButtonModel.isSelected());

				initializeVerseReference();
			}
		});
        
		hintButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String proposedAnswer = verseInputTextComponent.getText();
				String hint = verseLearnerApplication.requestHint(proposedAnswer);
				
				hintLabel.setText(hint);
				reportUnhelpfulHintButton.setVisible(true);
			}
		});
		
		reportUnhelpfulHintButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String proposedAnswer = verseInputTextComponent.getText();
				verseLearnerApplication.reportUnhelpfulHint(proposedAnswer);
			}
		});
		
		previousVerseButton.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.YES_OPTION;
				if (0 != verseInputTextComponent.getText().length()) {
					result = JOptionPane.showConfirmDialog(rootWindow, "Are you sure you want to change verses?");
				}
				if (JOptionPane.YES_OPTION == result || JOptionPane.OK_OPTION == result) {
					verseLearnerApplication.previousVerse();
				}
			}
		
		});
		
		nextVerseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.YES_OPTION;
				if (0 != verseInputTextComponent.getText().length()) {
					result = JOptionPane.showConfirmDialog(rootWindow, "Are you sure you want to change verses?");
				}
				if (JOptionPane.YES_OPTION == result || JOptionPane.OK_OPTION == result) {
					verseLearnerApplication.nextVerse();
				}
			}
		});
		
		verseInputTextComponent.addKeyListener(new KeyListener() {
			
			private void onKeyEvent(final JButton reportUnhelpfulHintButton) {
				reportUnhelpfulHintButton.setVisible(false);
				verseLearnerApplication.checkVerse(verseInputTextComponent.getText());
			}
		
			public void keyReleased(KeyEvent e) {
				onKeyEvent(reportUnhelpfulHintButton);
			}

			public void keyPressed(KeyEvent e) {
				onKeyEvent(reportUnhelpfulHintButton);
			}
		
			public void keyTyped(KeyEvent e) {
				onKeyEvent(reportUnhelpfulHintButton);
			}
		});
		
		showAnswerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				answerLabel.setVisible(true);
				showAnswerButton.setVisible(false);
				hideAnswerButton.setVisible(true);
				verseLearnerApplication.didShowAnswer();
			}
		});
		
		hideAnswerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				answerLabel.setVisible(false);
				showAnswerButton.setVisible(true);
				hideAnswerButton.setVisible(false);
			}
		});
		
		checkAnswerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String proposedAnswer = verseInputTextComponent.getText();
				String answerSoFar = verseLearnerApplication.checkAnswerSoFar(proposedAnswer);
				checkAnswerLabel.setText(answerSoFar);
			}
		});
    }

    // public methods
    
	public void initializeVerseAnswer(String answer)
	{
		verseInputTextComponent.setText("");
		verseInputTextComponent.setSelectionStart(0);
		verseInputTextComponent.setSelectionEnd(verseInputTextComponent.getText().length());
		checkAnswerLabel.setText("");
		hintLabel.setText("");
		answerLabel.setText(answer);
		answerLabel.setVisible(false);

		showAnswerButton.setVisible(true);
		hideAnswerButton.setVisible(false);
		reportUnhelpfulHintButton.setVisible(false);
		
		initializeVerseReference();
		
		verseInputTextComponent.requestFocusInWindow();
        
        // Window window = SwingUtilities.getWindowAncestor(verseInputTextComponent);
        // window.pack();
	}
	
	public void initializeVerseReference() {
		if (verseLearnerApplication.isIncludeReferenceInAnswer())
		{
			verseReferenceLabel.setText("Verse Reference: ");
			verseReferenceInput.setVisible(true);
			verseReferenceButton.setVisible(true);
			// verseReferenceInput.setText("");
		}
		else
		{
			verseReferenceLabel.setText(verseLearnerApplication.getVerseReference());
			verseReferenceInput.setVisible(false);
			verseReferenceButton.setVisible(false);
		}
	}

	public void addCurrentVerseToCompletedVerseList(String currentVerseText)
    {
        verseOutputJScrollPane.setVisible(true);
        String previousText = verseOutputTextComponent.getText();
        if (0 != previousText.length())
        {
        	currentVerseText = previousText + "\n" + currentVerseText;
        }
        verseOutputTextComponent.setText(currentVerseText);
        verseOutputTextComponent.setSize(new Dimension(
                verseInputTextComponent.getWidth(),
                verseOutputTextComponent.getHeight()));
        clearPreviousVersesButton.setVisible(true);
    }

    public void clearCompletedVerseList()
    {
        verseOutputJScrollPane.setVisible(false);
        verseOutputTextComponent.setText("");
        clearPreviousVersesButton.setVisible(false);
    }

	public void showModePanel(JPanel modePanel) {
        this.getContentPane().removeAll();
        this.getContentPane().add(modePanel, BorderLayout.CENTER);
        this.validate();
        this.repaint();
    }

	public void showWriteVersesFromReferencesMode() {
		showModePanel(modeVerseTextFromReferencesPanel);
    }

    public void showPutVersesInCorrectOrderMode() {
    	showModePanel(modeVerseOrderPanel);
	}

    public void showPutBooksOfTheBibleInCorrectOrderMode() {
    	showModePanel(modeBookOrderPanel);
	}

	public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
	}
}
