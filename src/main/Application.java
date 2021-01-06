package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import content.Content;
import exception.TagException;
import utils.TagUtils;

/**
 * Main class of the application
 * 
 * @author Nicolas Amiot
 */
public class Application {
	
	/**
	 * Constant windows name
	 */
	public final static String WINDOWS_NAME = "Generator";

	/**
	 * Main method :
	 * <br>0 argument : launch the client app
	 * <br>2 arguments : launch the generator with the project and datafile informed
	 * 
	 * @param args
	 * @throws Exception if the arguments length is incorrect
	 */
	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			Generator generator = Generator.getInstance();
			File root = new File(generator.getSource());
			String[] projects = root.list((file, name) -> new File(file, name).isDirectory());
			String[] datafiles = root.list((file, name) -> new File(file, name).isFile() && name.endsWith(".json"));
			launch(projects, datafiles);
		}
		else if(args.length == 2) {
			execute(args[0], args[1]);
		}
		else {
			throw new TagException("Invalid argument size");
		}
	}
	
	/**
	 * Initialize the app client
	 * 
	 * @param projects
	 * @param datafiles
	 */
	private static void launch(String[] projects, String[] datafiles) {
        JFrame frame = new JFrame(WINDOWS_NAME);
        frame.getContentPane().add(createPanel(frame, projects, datafiles), BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
	/**
	 * Positions the different elements of the ihm
	 * 
	 * @param frame
	 * @param projects
	 * @param datafiles
	 * @return
	 */
    private static Component createPanel(JFrame frame, String[] projects, String[] datafiles) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        JComboBox<String> projectList = new JComboBox<>(projects);
        JLabel labelProjet = new JLabel("Project :");
        labelProjet.setBounds(20, 10, 85, 25);
        panel.add(labelProjet);
        projectList.setBounds(80, 10, 280, 25);
        panel.add(projectList);

        JComboBox<String> datafileList = new JComboBox<>(datafiles);
        JLabel labelDatafile = new JLabel("Datafile :");
        labelDatafile.setBounds(20, 45, 85, 25);
        panel.add(labelDatafile);
        datafileList.setBounds(80, 45, 280, 25);
        panel.add(datafileList);
        
        JTextArea infos = new JTextArea();
        infos.setAutoscrolls(true);
        infos.setEnabled(false);
        infos.setDisabledTextColor(Color.DARK_GRAY);
        infos.setText(
    		"Information:\n\n"
    		+ "You can configure the project in resources/conf.properties.\n"
    		+ "The generation is based on the tags contained in the files.\n\n"
    		+ "Choose the project folder that contains the files to generate.\n"
    		+ "Then choose the file containing the variables for the project.\n"
    		+ "To finish, click on the buttom for launch the generation."
		);
        JScrollPane scrollPane = new JScrollPane(infos);
        scrollPane.setBounds(20, 80, 345, 135);
        panel.add(scrollPane);
        
        JButton buttonLancer = new JButton("Start processing");
        buttonLancer.setBounds(90, 225, 200, 25);
        buttonLancer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    execute((String) projectList.getSelectedItem(), (String) datafileList.getSelectedItem());
                    JOptionPane.showMessageDialog(frame, "Generation was completed without error", WINDOWS_NAME + " - Generation finished", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex, WINDOWS_NAME + " - Generation error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                frame.dispose();
            }
        });
        panel.add(buttonLancer);

        return panel;
    }
    
    /**
     * Launch the generation
     * 
     * @param project
     * @param datafile
     * @throws Exception
     */
    private static void execute(String project, String datafile) throws Exception {
    	Generator generator = Generator.getInstance();
		File root = new File(TagUtils.join('/', generator.getSource(), project));
		generator.setProject(project);
		generator.setDatafile(datafile);
		generator.readVariables();
		for(String filename : root.list())
		{
			generator.setFilename(filename);
			List<Content> contents = generator.readContent();
			generator.writeFile(contents);
		}
    }

}