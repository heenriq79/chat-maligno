//import lista.desordenada.*;
//import lista.ordenada.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;

public class JanelaPrincipal 
{
    JFrame frmChat;
    private JTextField txtEnviar;
    private JTextArea txtAreaMensagens;
    private JList listaUsuarios;
    private boolean conectado=false;
    private Socket conexao;
    private ObjectInputStream receptor;
    private ObjectOutputStream transmissor;
    private DefaultListModel<String> model;
    private JLabel lblConectado;
    private JButton btnConectar;
    private JButton btnEnviar;
    private JScrollPane scrollPane;
	

	public JanelaPrincipal() 
        {
            frmChat = new JFrame();
            frmChat.setResizable(false);
            frmChat.setTitle("Chat");
            frmChat.setBounds(50, 50, 857, 599);
            frmChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frmChat.getContentPane().setLayout(null);
		
            scrollPane = new JScrollPane();
            scrollPane.setBounds(10, 96, 639, 389);
            frmChat.getContentPane().add(scrollPane);
		
            txtAreaMensagens = new JTextArea();
            txtAreaMensagens.setEditable(false);
            scrollPane.setViewportView(txtAreaMensagens);
		
            JScrollPane scrollPane_1 = new JScrollPane();
            scrollPane_1.setBounds(659, 96, 161, 425);
            frmChat.getContentPane().add(scrollPane_1);
		
            // Lista de usuarios
            model = new DefaultListModel<String>();
            listaUsuarios = new JList(model);
            scrollPane_1.setViewportView(listaUsuarios);
		
            // Area para escrever a mensagem que vai ser enviada
            txtEnviar = new JTextField();
            txtEnviar.addActionListener(new ActionListener() 
            {
		public void actionPerformed(ActionEvent arg0) 
                {
			btnEnviar.doClick();
		}
            });
            txtEnviar.setBounds(10, 496, 528, 25);
            frmChat.getContentPane().add(txtEnviar);
            txtEnviar.setColumns(10);
		
            // Botao para enviar mensagem
            btnEnviar = new JButton("Enviar");
            btnEnviar.setEnabled(false);
            btnEnviar.addActionListener(new ActionListener() 
            {
		public void actionPerformed(ActionEvent arg0)
		{
			if (txtEnviar.getText().trim().equals(""))
				return;
			
			enviarMensagem("TODOS", txtEnviar.getText());
			
			txtEnviar.setText("");
		}
            });
		
            btnEnviar.setBounds(544, 496, 105, 25);
            frmChat.getContentPane().add(btnEnviar);
		
		
		
            JLabel lblNewLabel = new JLabel("Usu\u00E1rios");
            lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
            lblNewLabel.setBounds(659, 73, 105, 25);
            frmChat.getContentPane().add(lblNewLabel);
		
            JLabel lblMensagens = new JLabel("Mensagens");
            lblMensagens.setFont(new Font("Tahoma", Font.PLAIN, 14));
            lblMensagens.setBounds(10, 73, 105, 25);
            frmChat.getContentPane().add(lblMensagens);
		
            lblConectado = new JLabel("Voc\u00EA n\u00E3o est\u00E1 conectado a nenhuma sala!");
            lblConectado.setFont(new Font("Tahoma", Font.BOLD, 18));
            lblConectado.setBounds(10, 21, 639, 35);
            frmChat.getContentPane().add(lblConectado);
		
		
            // Botao para conectar a um servidor
            btnConectar = new JButton("Conectar");
            btnConectar.addActionListener(new ActionListener() 
            {
            
		public void actionPerformed(ActionEvent arg0) 
                {
			conectarOuDesconectar();
		}
            });
            btnConectar.setFont(new Font("Tahoma", Font.PLAIN, 18));
            btnConectar.setBounds(659, 21, 161, 32);
            frmChat.getContentPane().add(btnConectar);
		
            JLabel lblNewLabel_1 = new JLabel("Clique duas vezes no nome de algu\u00E9m para enviar uma mensagem particular");
            lblNewLabel_1.setBounds(356, 532, 464, 18);
            frmChat.getContentPane().add(lblNewLabel_1);
            
            // USUARIO FECHOU O PROGRAMA - FECHAR CONEXAO
            frmChat.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    try
                    {
                        if(conectado)
                            transmissor.writeObject(new PedidoDeSaidaDaSala());
                    }
                    catch(IOException erro)
                    {}
                }
            });
		
		// Evento double click da lista de usuarios
		listaUsuarios.addMouseListener(new MouseAdapter() 
                {
		    public void mouseClicked(MouseEvent evt)
		    {
		    	if (!conectado)
		    		return;
		    	
				JList list = (JList)evt.getSource();
		        if (evt.getClickCount() == 2)
		        {
		            // Double-click detectado
		            int index = list.locationToIndex(evt.getPoint());
		            
		            list.setSelectedIndex(index);
		            String usuarioAReceber = (String)list.getSelectedValue();
                            
		            String mensagemAEnviar = JOptionPane.showInputDialog("Digite a mensagem particular a ser enviada para " + usuarioAReceber);
		            enviarMensagem(usuarioAReceber, mensagemAEnviar);
		        }
		    }
		});
            }
	
	
	private void conectarOuDesconectar()
	{
		if (!conectado)
		// PARTE 1 DA CONEXAO - VALIDACAO DE IP E PORTA
		{
			String ipAConectar;
			int porta=0;
			String nomeSala;
			
			for (;;)
			{
				ipAConectar = JOptionPane.showInputDialog("Por favor, digite o IP do servidor que deseja se conectar.");
				
				if (ipAConectar == null)
					return;
				
				if (!ipEhValido(ipAConectar))
				{
					JOptionPane.showMessageDialog(null, "Digite um IP válido.");
					continue;
				}
				break;
			}
			
			for (;;)
			{
				String resp = null;
				try
				{
					resp = JOptionPane.showInputDialog("Por favor, digite a porta que deseja se conectar. (Padrao: 5555)");
					porta = Integer.parseInt(resp);
				}
				catch (Exception e)
				{
					if (resp==null)
						return;
					
					JOptionPane.showMessageDialog(null, "Digite uma porta válida.");
					continue;
				}
				
				if (!portaEhValida(porta))
				{
					JOptionPane.showMessageDialog(null, "Digite uma porta válida.");
					continue;
				}
			
				break;
			}
			
			
			// PARTE 2 DA CONEXAO - CONECTANDO AO SERVIDOR
			try
			{
				this.conexao = new Socket(ipAConectar, porta);
				this.transmissor = new ObjectOutputStream(conexao.getOutputStream());
				this.receptor = new ObjectInputStream(conexao.getInputStream());
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Houve algum erro ao conectar com o servidor.");
				return;
			}
			
                        

			// PARTE 3 DA CONEXAO - ENVIANDO DADOS DO USUARIO
                        
                        Coisa resposta;
			try
			{
			// PASSO I) ENVIAR A SALA QUE O USUARIO QUER CONECTAR
                                           
                            Salas salas = new Salas();
			    String[] nomSls = salas.getStringSalas();
                            
                            String salaSelecionada = (String)JOptionPane.showInputDialog(null, "Salas disponíveis", "Seleção de salas", JOptionPane.QUESTION_MESSAGE, null, nomSls, "Regular");
                            nomeSala = salaSelecionada;

                            if (salaSelecionada == null)
                            {
                                    transmissor.close();
                                    transmissor = null;
                                    receptor.close();
                                    receptor = null;
                                    conexao.close();
                                    conexao = null;
                                    return;
                            }
				
				this.transmissor.writeObject(new EscolhaDeSala(nomeSala));
                                this.transmissor.flush();
				
                                
				// PASSO II) VERIFICAR SE A SALA ESTÁ CHEIA
				
				resposta = (Coisa)this.receptor.readObject();
                                
                                
                                if(resposta instanceof AvisoDeSalaCheia)
                                {
                                    JOptionPane.showMessageDialog(null, "Desculpe, mas esta sala está cheia!");
                                    return;
                                }
                                else
                                    if(resposta instanceof AvisoDeSalaEscolhidaComSucesso)
                                    {}
                                
				String nomeUsuario = "";
                             // PASSO III) ENVIAR NOME DE USUARIO E VERIFICAR JA EXISTE
				for (;;)
				{
                                    nomeUsuario = "";
                                    nomeUsuario = new String("");
                                    
                                        while(nomeUsuario.equals(""))
                                            nomeUsuario = JOptionPane.showInputDialog("Digite seu nome de usuário");
                                                
					
					if (nomeUsuario==null)
					{
						transmissor.close();
						transmissor = null;
						receptor.close();
						receptor = null;
						conexao.close();
						conexao = null;
						return;
					}
					
					transmissor.writeObject(new EscolhaDeNome(nomeUsuario));
					transmissor.flush();
					
                                        
					resposta = (Coisa)this.receptor.readObject();
                                        
					if (resposta instanceof AvisoDeNomeJaExistente)
                                            JOptionPane.showMessageDialog(null, "Este nome já está sendo usado na sala");

                                            
					
					if (resposta instanceof AvisoDeNomeEscolhidoComSucesso){
                                            break;
                                        }    
				}
				
				
				// PASSO IV) ESCREVER OS USUÁRIOS DA SALA NA TELA
                                
				resposta = (Coisa)this.receptor.readObject();
                                
                                if (resposta instanceof ListaDeUsuariosNaSala)
                                {
                                    ArrayList<String> arrayListaUsuarios = ((ListaDeUsuariosNaSala)resposta).getListaUsuariosSala();
                                    for (int i = 0; i < arrayListaUsuarios.size(); i++)
                                        this.model.addElement(arrayListaUsuarios.get(i));
                                }
                                

				// SE O PROGRAMA CHEGOU AT� AQUI, O USU�RIO ESTÁ CONECTADO CORRETAMENTE.
				txtAreaMensagens.setText("");
				this.conectado = true;
				this.lblConectado.setText("Conectado à sala: " + nomeSala);
				btnConectar.setText("Desconectar");
				btnEnviar.setEnabled(true);
                                
                                //this.transmissor.writeObject(new AvisoDeEntradaNaSala(nomeUsuario));
				receberMensagens();
				
				return;
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "A conexão com o servidor foi perdida.");
				this.transmissor = null;
				this.receptor = null;
				this.conexao = null;
			}
		}
		
		// Usuário já está conectado e quer desconectar 
                try
                {
                    this.transmissor.writeObject(new PedidoDeSaidaDaSala());
                }
                catch(Exception erro)
                {}
	}
	
	private void enviarMensagem (String destinatario, String mensagem)
	{
		if (!conectado)
			return;
		
		try
		{
                    this.transmissor.writeObject(new Mensagem(destinatario, "USUARIO", mensagem));
                    this.transmissor.flush();
		}
		catch (Exception e) 
                {}
	}
	
	private void receberMensagens ()
	{ 
		// Criacao de thread para escrever as mensagens recebidas
		Thread receber = new Thread()
		{
			public void run()
			{
                            try
                            {
                                while (conectado)
                                {
                                    Coisa c = (Coisa)receptor.readObject();
                                    tratarComando(c);
                                }    
                                desconectar();
                            }
                            catch (Exception e)
                            {
                                System.out.println("Erro: "+e);
                                try {
                                    desconectar();
                                } catch (Exception ex) {
                                    System.out.println("Erro: "+ex);
                                    Logger.getLogger(JanelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
		};
		
		receber.start();
	}
        
        private void desconectar() throws Exception
        {
            transmissor = null;
            receptor = null;
            conexao = null;			                
            lblConectado.setText("Voc\u00EA n\u00E3o est\u00E1 conectado a nenhuma sala!");
            btnConectar.setText("Conectar");
            model.removeAllElements();
            btnEnviar.setEnabled(false);
            this.txtAreaMensagens.setText("");
        }
	
	private void tratarComando (Coisa c)
	{
		if (c instanceof Mensagem)
		{
                    String msg = ((Mensagem)c).getMensagem();
                    String remetente = ((Mensagem)c).getRemetente();
                    String destina = ((Mensagem)c).getDestinatario();
                    
                    if(!destina.equals("TODOS"))
                    {
                        if(!destina.equals(remetente))
                            this.txtAreaMensagens.append("- mensagem privada de "+ remetente + " para "+ destina + ": " + msg +"\n");
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Não é possível mandar mensagem privada para você mesmo.");
                            return;
                        }
                    }    
                    else
                    this.txtAreaMensagens.append(remetente + ": " + msg +"\n");
         
                    
                    JScrollBar vertical = this.scrollPane.getVerticalScrollBar();
                    vertical.setValue( vertical.getMaximum());
                    return;
		}
		
		if (c instanceof AvisoDeEntradaNaSala)
		{
                    String elemento = ((AvisoDeEntradaNaSala)c).getRemetente();
                    
                    for (int i = 0; i < model.size(); i++)
                        if(model.get(i).equals(elemento)){
                            this.txtAreaMensagens.append("*"+elemento + " entrou na sala*\n");
                            return;
                        }

                    this.model.addElement(elemento);
                    this.txtAreaMensagens.append("*"+elemento + " entrou na sala*\n");
                    return;
		}
		
		if (c instanceof AvisoDeSaidaDaSala)
		{
                    String elemento = ((AvisoDeSaidaDaSala)c).getRemetente();
                    this.model.removeElement(elemento);
                    this.txtAreaMensagens.append("*"+elemento + " saiu na sala*\n");
                    return;
		}
                
                if (c instanceof PedidoDeSaidaDaSala)
                {
                    this.conectado = false;
                    return;
                }
	}
	
	
	
	public static boolean ipEhValido(String text)
	{
		if (text.equals("localhost"))
			return true;
		
	    Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
	    Matcher m = p.matcher(text);
	    return m.find();
	}
	
	public static boolean portaEhValida(int porta)
	{
		if (porta < 0 || porta > 65535)
			return false;
		
		return true;
	}
}
