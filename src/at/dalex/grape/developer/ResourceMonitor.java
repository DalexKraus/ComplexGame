package at.dalex.grape.developer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;

import at.dalex.grape.toolbox.MemoryManager;

import javax.swing.JLabel;

@Deprecated //Will be removed in the next update
			//Thus no further documentation needed.
public class ResourceMonitor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel vbos;
	private JLabel vaos;
	private JLabel textures;
	private JLabel verts;
	private JLabel drawCalls;
	private JProgressBar ramUsage;
	private JLabel usedRam;
	private JLabel maxRam;

	public ResourceMonitor() {
		super("Resource Monitor");
		
		setBounds(100, 100, 450, 290);
		setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel ramUsagePanel = new JPanel();
		ramUsagePanel.setBorder(new TitledBorder(null, "RAM-Usage", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ramUsagePanel.setBounds(10, 148, 414, 96);
		contentPane.add(ramUsagePanel);
		ramUsagePanel.setLayout(null);

		ramUsage = new JProgressBar();
		ramUsage.setValue(32);
		ramUsage.setStringPainted(true);
		ramUsage.setBounds(10, 21, 394, 20);
		ramUsagePanel.add(ramUsage);
		
		usedRam = new JLabel("Used RAM:");
		usedRam.setBounds(10, 52, 180, 14);
		ramUsagePanel.add(usedRam);
		
		maxRam = new JLabel("Max RAM:");
		maxRam.setBounds(10, 70, 180, 14);
		ramUsagePanel.add(maxRam);

		JPanel glResourcesPanel = new JPanel();
		glResourcesPanel.setBorder(new TitledBorder(null, "OpenGL Resources", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		glResourcesPanel.setBounds(10, 11, 225, 126);
		contentPane.add(glResourcesPanel);
		glResourcesPanel.setLayout(null);

		vbos = new JLabel("VBOs:");
		vbos.setBounds(10, 31, 205, 14);
		glResourcesPanel.add(vbos);

		vaos = new JLabel("VAOs:");
		vaos.setBounds(10, 44, 205, 14);
		glResourcesPanel.add(vaos);

		textures = new JLabel("Textures:");
		textures.setBounds(10, 56, 205, 14);
		glResourcesPanel.add(textures);

		verts = new JLabel("Vertices:");
		verts.setBounds(10, 87, 205, 14);
		glResourcesPanel.add(verts);

		drawCalls = new JLabel("Draw calls:");
		drawCalls.setBounds(10, 101, 205, 14);
		glResourcesPanel.add(drawCalls);

		JPanel alResourcsPanel = new JPanel();
		alResourcsPanel.setBorder(new TitledBorder(null, "OpenAL Resources", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		alResourcsPanel.setBounds(245, 11, 179, 126);
		contentPane.add(alResourcsPanel);

		setVisible(true);
	}
	
	public void update() {
		vbos.setText("VBOs:            " + MemoryManager.createdVBOs.size());
		vaos.setText("VAOs:            " + MemoryManager.createdVAOs.size());
		textures.setText("Textures:	      " + MemoryManager.createdTextures.size());
		verts.setText("Vertices:       " + MemoryManager.verticesAmount);
		drawCalls.setText("Draw Calls:   " + MemoryManager.drawCallsAmount);
		
		long RAM_USED = Runtime.getRuntime().totalMemory();
		long RAM_TOTAL = Runtime.getRuntime().maxMemory();

		int ramUsagePrecent = (int) (((RAM_USED * 1.0) / RAM_TOTAL) * 100);
		ramUsage.setValue(ramUsagePrecent);
		
		usedRam.setText("Used Memory: " + (RAM_USED / 1024 / 1024) + " MB");
		maxRam.setText("Max Memory: " + (RAM_TOTAL / 1024 / 1024) + " MB");
	}
}
