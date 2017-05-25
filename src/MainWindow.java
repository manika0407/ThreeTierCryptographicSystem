import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import java.math.BigInteger;
import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class MainWindow {
	protected Shell shell;
	private Text plainText;
	private Text cipherText;
	private Text plainText2;
	
   //Launch the application
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	//Open the window
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	//Create contents of the window.
	protected void createContents() {
		shell = new Shell();
		shell.setSize(700, 500);
		shell.setText("Three-tier CryptoSystem");
		Label Systemused=new Label(shell,SWT.NONE);
		Systemused.setBounds(200,40,500,20);
		Systemused.setText("Select out of: DES/RSA/Elgamal");
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(34, 77, 175, 41);
		lblNewLabel.setText("Enter Plain Text :");
		
		plainText = new Text(shell, SWT.BORDER);
		plainText.setBounds(173, 74, 500, 30);
		
		Button DESButton = new Button(shell, SWT.NONE);
		DESButton.setBounds(63, 250, 90, 22);
		DESButton.setText("DES");
		
		DESButton.addListener(SWT.Selection, new Listener(){
		   @Override
		   public void handleEvent(Event event) {
				 String str=plainText.getText();
				 Systemused.setText("");
				 Systemused.setText("DES System");
				 try {
					ArrayList<String> result=new ArrayList<String>();
					result=DES.main(str);
					cipherText.setText(result.get(0));
					plainText2.setText(result.get(1));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
		});

		Button RSAButton = new Button(shell, SWT.NONE);
		RSAButton.setBounds(187, 250, 98, 22);
		RSAButton.setText("RSA");
		RSAButton.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event event) {
				Systemused.setText("RSA System");
				String str=plainText.getText();
				cipherText.setText("");
				plainText2.setText("");
				//BigInteger output;
				ArrayList<BigInteger> result=new ArrayList<BigInteger>();
				try {
					result = RSA.main(str);
					cipherText.setText(String.valueOf(result.get(0)));
					String text2=new String(result.get(1).toByteArray());
					plainText2.setText(text2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		Button ElgamalButton = new Button(shell, SWT.NONE);
		ElgamalButton.setBounds(313, 250, 110, 22);
		ElgamalButton.setText("ElGamal");
		ElgamalButton.addListener(SWT.Selection, new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				Systemused.setText("Elgamal System");
				String str=plainText.getText();
				cipherText.setText("");
				plainText2.setText("");
				ArrayList<BigInteger> result=new ArrayList<BigInteger>();
				try{
					result=ElGamal.ElgamalMain(str);
					cipherText.setText(String.valueOf(result.get(0)));
					String text2=new String(result.get(1).toByteArray());
					plainText2.setText(text2);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(34, 124, 119, 22);
		lblNewLabel_1.setText("Cipher Text :");

		Label lbl=new Label(shell, SWT.NONE);
		lbl.setBounds(34,175,119,22);
		lbl.setText("PlainText Back:");
		
		cipherText = new Text(shell, SWT.BORDER);
		cipherText.setText("");
		cipherText.setBounds(173, 121, 500, 22);

		plainText2 = new Text(shell,SWT.BORDER);
		plainText2.setText("");
		plainText2.setBounds(173, 172, 500,30 );
	}
}