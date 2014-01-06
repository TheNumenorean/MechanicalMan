package mechanicalman;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 
 * @author Greg Volger, Francesco Macagno
 * 
 */
public class GridPanel extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8370673685752478211L;

	private MechanicalMan robot;

	private Image chairImg, robotSittingArmsDownImg, robotSittingArmsUpImg, robotStandingArmsUpImg,
			robotStandingArmsDownImg, robotCrashedImg, mod;

	private final static int panelHeight = 590, panelWidth = 590;

	private int widthPixels;
	private int heightPixels;

	private BoundedGrid grid;

	private boolean imagesInitd;

	public GridPanel() {
		setBackground(Color.white);
	}

	private void initImages() {
		try {
			Debug.println("Loading images...");
			chairImg = getImage("img/chair.png").getScaledInstance(widthPixels, heightPixels,
					Image.SCALE_SMOOTH);
			robotSittingArmsDownImg = getImage("img/robotSittingArmsDown.png").getScaledInstance(
					widthPixels, heightPixels, Image.SCALE_SMOOTH);
			robotSittingArmsUpImg = getImage("img/robotSittingArmsUp.png").getScaledInstance(
					widthPixels, heightPixels, Image.SCALE_SMOOTH);
			robotStandingArmsUpImg = getImage("img/robotStandingArmsUp.png").getScaledInstance(
					widthPixels, heightPixels, Image.SCALE_SMOOTH);
			robotStandingArmsDownImg = getImage("img/robotStandingArmsDown.png").getScaledInstance(
					widthPixels, heightPixels, Image.SCALE_SMOOTH);
			robotCrashedImg = getImage("img/robotCrashed.png").getScaledInstance(widthPixels,
					heightPixels, Image.SCALE_SMOOTH);
			mod = getImage("img/core.png").getScaledInstance(widthPixels, heightPixels,
					Image.SCALE_SMOOTH);

			Debug.println("Finished loading images");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Image getImage(String file) throws IOException {
		try {
			return ImageIO.read(new File(file));
		} catch (IOException e) {

			if (!file.startsWith("/"))
				file = '/' + file;

			InputStream is = getClass().getResourceAsStream(file);

			if (is == null)
				throw new IOException("Cannot find file specified! - " + file);

			return ImageIO.read(is);

		}

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Compute the pixles the robot moves each step
		grid = robot.getGrid();
		widthPixels = panelWidth / grid.getWidth();
		heightPixels = panelHeight / grid.getHeight();

		if (!imagesInitd) {
			initImages();
			imagesInitd = true;
		}

		g.setColor(Color.GRAY);

		for (int y = 0; y <= grid.getHeight(); y++) {

			int newY = y * heightPixels;
			if (y % 5 == 0)
				g.setColor(Color.BLACK);
			g.drawLine(0, newY, grid.getWidth() * widthPixels, newY);
			if (y % 5 == 0)
				g.setColor(Color.GRAY);

		}
		for (int x = 0; x <= grid.getWidth(); x++) {

			int newX = x * widthPixels;
			if (x % 5 == 0)
				g.setColor(Color.BLACK);
			g.drawLine(newX, 0, newX, grid.getHeight() * heightPixels);
			if (x % 5 == 0)
				g.setColor(Color.GRAY);
		}

		int rx = robot.location().getX();
		int ry = robot.location().getY();

		g.setColor(robot.statusColor);
		g.fillRect(ry * heightPixels + 1, rx * widthPixels + 1, widthPixels - 1, heightPixels - 1);
		g.setColor(Color.BLACK);

		// Draw the chair
		int cx = robot.getChairLoc().getX();
		int cy = robot.getChairLoc().getY();
		paintImage(chairImg, g, cy, cx, robot.getChairDir());

		// Draw the door - if there is one
		if (grid.hasDoor()) {
			Location doorLocation = grid.getDoor().location();
			int dx = doorLocation.getX();
			int dy = doorLocation.getY();
			Direction doorFacing = grid.getDoor().facing();
			if (doorFacing.equals(Direction.NORTH)) {
				g.fillRect(dy * widthPixels, dx * heightPixels, widthPixels, 5);
			} else if (doorFacing.equals(Direction.WEST)) {
				g.fillRect(dy * widthPixels, dx * heightPixels, 5, heightPixels);
			} else if (doorFacing.equals(Direction.SOUTH)) {
				g.fillRect(dy * widthPixels, (dx + 1) * heightPixels - 5, widthPixels, 5);
			} else if (doorFacing.equals(Direction.EAST)) {
				g.fillRect((dy + 1) * widthPixels - 5, dx * heightPixels, 5, heightPixels);
			}
		}

		// Draw the robot
		boolean armsUp = robot.areArmsUp();

		if (robot.crashed())
			paintImage(robotCrashedImg, g, ry, rx, robot.dir());
		else {
			if (robot.standingUp()) {
				if (armsUp)
					paintImage(robot.mod ? mod : robotStandingArmsUpImg, g, ry, rx, robot.dir());
				else
					paintImage(robotStandingArmsDownImg, g, ry, rx, robot.dir());
			} else {
				if (armsUp)
					paintImage(robotSittingArmsUpImg, g, ry, rx, robot.dir());
				else
					paintImage(robotSittingArmsDownImg, g, ry, rx, robot.dir());
			}
		}

	}

	private void paintImage(Image pic, Graphics g, int ry, int rx, Direction d) {

		AffineTransform trans = new AffineTransform();

		int transX = ry * heightPixels + heightPixels / 2 - pic.getWidth(null) / 2;
		int transY = rx * widthPixels + widthPixels / 2 - pic.getHeight(null) / 2;

		trans.translate(transX, transY);
		if (d.equals(Direction.WEST)) {
			trans.rotate(Math.PI / 2);
			trans.scale(1, -1);
		} else
			trans.rotate(d.getRadians(), pic.getWidth(null) / 2, pic.getHeight(null) / 2);

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(pic, trans, null);

	}

	@Override
	public void update(Observable o, Object arg) {
		robot = (MechanicalMan) o;
		repaint();
	}
}