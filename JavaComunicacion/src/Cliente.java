import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;


public class Cliente {

	public static void main(String[] args) {
                MarcoCliente mimarco = new MarcoCliente();
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}


class MarcoCliente extends JFrame{	
	public MarcoCliente(){		
		setBounds(600,300,280,350);
                
		LaminaMarcoCliente milamina = new LaminaMarcoCliente();		
		add(milamina);
		
		setVisible(true);
		}		
}


class LaminaMarcoCliente extends JPanel implements Runnable{
    
    	private JTextField campo1;
        private JTextField nick;
        private JTextField ip;
	private JButton bEnviaTexto;
        private JTextArea campoChat;
	
	public LaminaMarcoCliente(){     
                
                nick = new JTextField(5);
                add(nick);
            
		JLabel texto = new JLabel("-CHAT-");		
		add(texto);
                
                ip = new JTextField(8);
                add(ip);
                
                campoChat = new JTextArea(12,24);
                add(campoChat);               
	
		campo1 = new JTextField(20);             
		add(campo1);		
	
                bEnviaTexto = new JButton("Enviar");
		EnviaTexto eEnviaTexto = new EnviaTexto(); 
                bEnviaTexto.addActionListener(eEnviaTexto);
		add(bEnviaTexto);	
                
                Thread mihilo = new Thread(this);
                mihilo.start();
	}
                

        @Override
        public void run() {
                try{
                    ServerSocket serverE = new ServerSocket(4000);
                    Socket miSocketE;
                    PaqueteDatos paqueteEntrante;
                    
                    while(true){
                        miSocketE = serverE.accept();
                        
                        ObjectInputStream flujoEntrante = new ObjectInputStream(miSocketE.getInputStream());
                        paqueteEntrante = (PaqueteDatos) flujoEntrante.readObject();
                        
                        campoChat.append("\n" + paqueteEntrante.getNick() + ": " + paqueteEntrante.getMensaje());
                    }
                    
                } catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
        }
	
        private class EnviaTexto implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                campoChat.append("\n" + campo1.getText());
                
                try {
                    Socket miSocketS = new Socket("200.115.25.74", 9999);
                    
                    PaqueteDatos paqueteEnvio = new PaqueteDatos();
                    paqueteEnvio.setNick(nick.getText());
                    paqueteEnvio.setIp(ip.getText());
                    paqueteEnvio.setMensaje(campo1.getText());
                    
                    ObjectOutputStream paqueteSalida = new ObjectOutputStream(miSocketS.getOutputStream());
                    paqueteSalida.writeObject(paqueteEnvio);
                    
                    miSocketS.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }            
        }		
}


class PaqueteDatos implements Serializable{
        private String nick, ip, mensaje;

        public String getNick() {
            return nick;
        }
        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getIp() {
            return ip;
        }
        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getMensaje() {
            return mensaje;
        }
        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
        
}