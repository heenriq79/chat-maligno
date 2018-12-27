import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class CuidadoraDeUsuario extends Thread
{
    protected Socket  conexao;
    protected Salas   salas;
    protected Usuario usuario;

    public CuidadoraDeUsuario(Socket conexao, Salas salas) throws Exception
    {
        // validar se conexao e/ou salas ==null, lan�ando exce��o
        if(conexao == null || salas == null)
            throw new Exception("Parametros nulos!");

        this.conexao = conexao;
        this.salas = salas;
    }

    int i = 0;
    @Override
    public void run()
    {
        try
        {
            instanciarUsuario();

            Coisa recebido = null;
            
            do
            {
                recebido = this.usuario.recebe();
                
                if(recebido instanceof Mensagem)
                {
                    Mensagem msg = (Mensagem)recebido;
                    String nomeUsuario = this.usuario.getNome();
                    msg.setRemetente(this.usuario.getNome());
                    String nomeDestinatario = msg.getDestinatario();
                    
                    synchronized(this.usuario.getSala())
                    {
                        if(nomeDestinatario.equals("TODOS"))
                        {
                            ArrayList<String> lista = this.usuario.getSala().getListaUsuariosNaSala();
                            
                            for(int i = 0; i < lista.size(); i++)
                                this.usuario.getSala().getUsuario(lista.get(i)).envia(msg);
                        }
                        else
                        {
                            this.usuario.getSala().getUsuario(nomeDestinatario).envia(msg);
                            if (!nomeUsuario.equals(nomeDestinatario))
                                this.usuario.envia(msg);
                        }                        
                            
                    }
                }
                else
                if(recebido instanceof AvisoDeEntradaNaSala)
                {
                     
                }
                else
                if(recebido instanceof AvisoDeSaidaDaSala)
                {
                     
                }
               // receber mensagens, avisos de entrada na e de saida da sala
                // se for mensagem, pega nela o destinatario, acha o destinatario na sala e manda para ele a mensagem
            }
            while (!(recebido instanceof PedidoDeSaidaDaSala));
            
            
            synchronized(this.usuario.getSala())
            {
               this.usuario.envia(new PedidoDeSaidaDaSala());
               
                for(int j = 0; j < this.usuario.getSala().getCapacidadeUsada(); j++)
                {
                    String nome = this.usuario.getNome();
                    
                    this.usuario.getSala().getUsuario(j).envia(new AvisoDeSaidaDaSala(nome));
             
                }
                
                Sala salaUsuario = this.usuario.getSala();
                this.usuario.getSala().removerUsuario(this.usuario);
                 
                this.usuario.envia(new ListaDeUsuariosNaSala(salaUsuario.getListaUsuariosNaSala()));
                
                this.usuario.fechaTudo();
            }
            
            
            // remover this.usuario da sala
            // mandar new AvisoDeSaidaDaSala(this.usuario.getNome()) para todos da sala
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }
    
    protected void instanciarUsuario() throws Exception
    {
	// synchronized é pra nao chamar o mesmo metodo ao mesmo tempo por duas threads ou mais

        // declarar e instanciar OOS e OIS
        ObjectOutputStream transmissor = new ObjectOutputStream(this.conexao.getOutputStream());
        ObjectInputStream receptor = new ObjectInputStream(this.conexao.getInputStream());
            
        // interagir com o usr via OOS e OIS ate descobrir o nome da sala em que ele deseja entrar, eventualmente, informando sala cheia
        //transmissor.writeObject(new Salas().getStringSalas());
        //transmissor.flush();

        
        
        
        // ESCOLHA DE SALA
	Sala sala;
        while(true)
        {
            Object obj = receptor.readObject();
            if(obj instanceof EscolhaDeSala)
            {
                String salaEscolhida = ((EscolhaDeSala)obj).getNomeDaSala();
                
                synchronized(salas)
                {
                    if(!salas.existeSala(salaEscolhida))
                    {
                        transmissor.writeObject(new AvisoDeSalaInvalida());
                        transmissor.flush();
                    }
                    else
                    {
                        sala = salas.getSala(salaEscolhida);
                        if (sala.isCheia())
                        {
                            transmissor.writeObject(new AvisoDeSalaCheia());
                            transmissor.flush();
                        }
                        else
                        {
                            transmissor.writeObject(new AvisoDeSalaEscolhidaComSucesso());
                            transmissor.flush();
                            break;
                        }
                    }
                }
            }   
        }

        // RECEBER NOME DE USUÁRIO
        String nomeUsuarioDesejado;
        while(true)
        {
            Object obj = receptor.readObject();
            
            if(obj instanceof EscolhaDeNome)
            {
                nomeUsuarioDesejado = ((EscolhaDeNome)obj).getNome();
                
                if(sala.existeUsuario(nomeUsuarioDesejado))
                {
                    transmissor.writeObject(new AvisoDeNomeJaExistente());
                    transmissor.flush();
                }
                else
                {
                    // instanciar o Usuario, fornecendo, conexao, transmissor, OOS, OIS, nome e sala
                    this.usuario = new Usuario(this.conexao, transmissor, receptor, nomeUsuarioDesejado, sala); 
                    sala.adicionarUsuario(this.usuario);
                    transmissor.writeObject(new AvisoDeNomeEscolhidoComSucesso());
                    transmissor.flush();
                    break;
                }
            }
        }  
        
        
        // ENVIAR LISTA DE USUÁRIOS NA SALA PARA COLOCAR NO LISTBOX
       
                           
        synchronized(sala)
        {
            ArrayList<String> listaUsuariosSala;
            listaUsuariosSala = sala.getListaUsuariosNaSala(); // retorna uma lista de strings para passar no transmissor e pegar no programa depois
            this.usuario.envia(new ListaDeUsuariosNaSala(listaUsuariosSala));
        }
       

        // fazer varias vezes this.usuario.envia(new AvisoDeEntradaDaSala(i)), onde i é o nome de algum usuario que ja estava na sala
        //for (int i = 0; i < sala.getCapacidadeUsada(); i++)
        //{
        //    synchronized(sala)
        //    {
        //        this.usuario.envia(new AvisoDeEntradaNaSala(sala.getUsuario(i).getNome()));
        //    }   
        //}

        // fazer varias vezes i.envia(new AvisoDeEntradaDaSala(usuario.getNome()), onde i � o nome de algum usuario que ja estava na sala
        
        
        for (int i = 0; i < sala.getCapacidadeUsada(); i++)
        {
            synchronized(sala)
            {
                String nome = this.usuario.getNome();
                sala.getUsuario(i).envia(new AvisoDeEntradaNaSala(nome)); 
            }
        }
        
        
    }
}