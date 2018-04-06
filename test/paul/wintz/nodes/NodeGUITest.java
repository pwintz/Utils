package paul.wintz.nodes;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import paul.wintz.utils.logging.Lg;

@SuppressWarnings("serial")
public class NodeGUITest  extends JFrame {
	protected static final String TAG = Lg.makeTAG(NodeGUITest.class);

	private final JPanel contentPane = createContentPane();

	public NodeGUITest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		setVisible(true);
		add(new DraggableComponent());
	}

	private JPanel createContentPane() {
		JPanel pane = new JPanel();
		pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		pane.setLayout(new CardLayout(0, 0));
		setContentPane(pane);
		return pane;
	}

	private static class DraggableComponent extends JComponent {
		private boolean isDraggable = true;
		private Point anchorPoint;
		private Cursor draggingCursor =  Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
		
		public DraggableComponent() {
			addDragListener();
			setOpaque(true);
			setBackground(new Color(0, 240, 250));
			setSize(new Dimension(20, 20));
			setLocation(20,  20);
		}
		
		private void addDragListener() {
			
			addMouseMotionListener(new MouseAdapter() {

				@Override
				public void mouseMoved(MouseEvent e) {
					anchorPoint = e.getPoint();
					setCursor(draggingCursor);
				}
				
				@Override
				public void mouseDragged(MouseEvent e) {
					
					int anchorY = anchorPoint.y;
					
					Point parentOnScreen = getParent().getLocationOnScreen();
					Point mouseOnScreen = e.getLocationOnScreen();
					Point position = new Point(
							mouseOnScreen.x - parentOnScreen.x - anchorPoint.x,
							mouseOnScreen.y - parentOnScreen.y - anchorPoint.y);
					setLocation(position);
					
					getParent().setComponentZOrder(DraggableComponent.this,  0);
					repaint();
			
				}
				
			});
			
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (isOpaque()) {
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String... args) {
		new NodeGUITest();
	}
}
