package schaatstijden;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import javax.swing.JToggleButton;
import java.awt.Choice;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.BoxLayout;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.regex.Pattern;
import java.awt.TextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

public class GUI {

	private JFrame frame;
	private JTextField begindatum;
	private JTextField einddatum;
	private Choice afstand;
	private JButton btnImporteerTijden;
	private JCheckBox chckbxTussenvoegsel;
	private JLabel lblError;
	private TextArea textArea;
	private Schaatstijden schaatstijden;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		schaatstijden = new Schaatstijden(this);
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textArea = new TextArea();
		textArea.setBounds(27, 182, 380, 369);
		frame.getContentPane().add(textArea);
		
		afstand = new Choice();
		afstand.setBounds(163, 10, 156, 20);
		afstand.addItem("500 meter");
		afstand.addItem("1000 meter");
		afstand.addItem("1500 meter");
		afstand.addItem("3000 meter");
		afstand.addItem("5000 meter");
		afstand.addItem("10000 meter");	
		frame.getContentPane().add(afstand);
		
		begindatum = new JTextField();
		begindatum.setText("jjjj-mm-dd");
		begindatum.setBounds(163, 42, 156, 20);
		frame.getContentPane().add(begindatum);
		begindatum.setColumns(10);
		
		einddatum = new JTextField();
		einddatum.setText("jjjj-mm-dd");
		einddatum.setBounds(163, 73, 156, 20);
		frame.getContentPane().add(einddatum);
		einddatum.setColumns(10);
		
		btnImporteerTijden = new JButton("Importeer Tijden");
		btnImporteerTijden.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				importeerTijden();
			}
		});
		btnImporteerTijden.setBounds(163, 128, 156, 23);
		frame.getContentPane().add(btnImporteerTijden);
		
		JLabel lblAfstand = new JLabel("Afstand");
		lblAfstand.setBounds(48, 16, 46, 14);
		frame.getContentPane().add(lblAfstand);
		
		JLabel lblBegindatum = new JLabel("Begindatum");
		lblBegindatum.setBounds(48, 45, 106, 14);
		frame.getContentPane().add(lblBegindatum);
		
		JLabel lblEinddatum = new JLabel("Einddatum");
		lblEinddatum.setBounds(48, 76, 106, 14);
		frame.getContentPane().add(lblEinddatum);
		
		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setBounds(27, 162, 380, 14);
		frame.getContentPane().add(lblError);
		
		chckbxTussenvoegsel = new JCheckBox("Tussenvoegsel");
		chckbxTussenvoegsel.setBounds(163, 100, 156, 23);
		frame.getContentPane().add(chckbxTussenvoegsel);
	}
	
	public void setFieldsEnabled(boolean set){
		afstand.setEnabled(set);
		begindatum.setEnabled(set);
		einddatum.setEnabled(set);
		chckbxTussenvoegsel.setEnabled(set);
		btnImporteerTijden.setEnabled(set);
		textArea.setEnabled(set);
	}
	
	public void showMessage(String message){
		lblError.setText(message);
		lblError.repaint();
	}

	public void importeerTijden(){
		lblError.setText("");
		System.out.println("importing");
		setFieldsEnabled(false);
		if(!Pattern.matches(Constants.DATUM_FORMAT, begindatum.getText())){
			lblError.setText("Begindatum moet format jjjj-mm-dd hebben");
			setFieldsEnabled(true);
		} else if(!Pattern.matches(Constants.DATUM_FORMAT, einddatum.getText())){
			lblError.setText("Einddatum moet format jjjj-mm-dd hebben");
			setFieldsEnabled(true);
		} else {
			schaatstijden.setAfstand(Integer.parseInt(afstand.getSelectedItem().substring(0, afstand.getSelectedItem().length()-6)));
			schaatstijden.setBegindatum(begindatum.getText());
			schaatstijden.setEinddatum(einddatum.getText());
			schaatstijden.setTussenvoegsel(chckbxTussenvoegsel.isSelected());
			System.out.println("variabels gezet");
			BufferedReader br = new BufferedReader(new StringReader(textArea.getText()));
			System.out.println("reader gemaakt");
			schaatstijden.importDeelnemers(br);
			schaatstijden.importAll();
			schaatstijden.printAll();
		}
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
	}
}
