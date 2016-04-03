package controlInterface;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Nemo Li
 * 
 */

public class ClickListener implements MouseListener {

	int id;
	MasterController controller;
	public static boolean activated = true;
	
	/**
	 * @param id
	 * @param controller
	 * 
	 * initialize each mouse listener to a specific panel with id
	 */
	public ClickListener(int id, MasterController controller){
		this.id = id;
		this.controller = controller;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent click) {
		if (!activated){
			return;
		}
		
		if (click.getButton() == MouseEvent.BUTTON1)
			controller.leftClicked(id);
		else
			controller.rightClicked(id);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
