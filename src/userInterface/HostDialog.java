package userInterface;

import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*; //property change stuff
import java.awt.*;
import java.awt.event.*;
/**
 * 
 * @author Oracle
 *
 *	This class is borrowed from the tutorials provided
 *	by Oracle on Java swing dialogs. And I made small
 *	changes to facilitate my use
 *
 *	See:
 *	http://docs.oracle.com/javase/tutorial/displayCode.html?code=
 *	http://docs.oracle.com/javase/tutorial/uiswing/examples/components/
 *	DialogDemoProject/src/components/DialogDemo.java
 */

@SuppressWarnings("serial")
public class HostDialog extends JDialog
                   implements ActionListener,
                              PropertyChangeListener {
	
    private String typedText = null;
    private JTextField textField;

    private JOptionPane optionPane;

    private String enter = "Enter";
    private String cancel = "Cancel";

    /**
     * Returns null if the typed string was invalid;
     * otherwise, returns the string as the user entered it.
     */
    public String getValidatedText() {
        return typedText;
    }

    /** Creates the reusable dialog. */
    public HostDialog(Frame aFrame) {
        super(aFrame, true);
        setTitle("Host");
        textField = new JTextField("localhost");

        //Create an array of the text and components to be displayed.
        Object[] array = {"Enter IP address of host:", textField};

        //Create an array specifying the number of dialog buttons
        //and their text.
        Object[] options = {enter, cancel};

        //Create the JOptionPane.
        optionPane = new JOptionPane(array,
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]);

        //Make this dialog display it.
        setContentPane(optionPane);

        handleWindowClosing();

		addListeners();
    }

	private void addListeners() {
		// Ensure the text field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				textField.requestFocusInWindow();
			}
		});

        //Register an event handler that puts the text into the option pane.
        textField.addActionListener(this);

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
	}

	private void handleWindowClosing() {
		//Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				/*
				 * Instead of directly closing the window, we're going to change
				 * the JOptionPane's value property.
				 */
				optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});
	}

    /** This method handles events for the text field. */
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(enter);
    }

	/** This method reacts to state changes in the option pane. */
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();

		if (isVisible()
				&& (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY
						.equals(prop))) {
			Object value = optionPane.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				// ignore reset
				return;
			}

			// Reset the JOptionPane's value.
			// If you don't do this, then if the user
			// presses the same button next time, no
			// property change event will be fired.
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

			if (enter.equals(value)) {
				typedText = textField.getText();
				clearAndHide();
			} else { // user closed dialog or clicked cancel
				typedText = null;
				clearAndHide();
			}
		}
	}

    /** This method clears the dialog and hides it. */
    public void clearAndHide() {
        textField.setText(null);
        setVisible(false);
    }
}
