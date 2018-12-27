
import java.io.*;
import java.net.*;

public class Usuario
{
    private String             nome;
    private Socket             conexao;
    private ObjectOutputStream transmissor;
    private ObjectInputStream  receptor;
    private Sala               sala;

    public Usuario (Socket conexao,
                    ObjectOutputStream transmissor,
                    ObjectInputStream  receptor,
                    String nome,
                    Sala sala) throws Exception
    {
        // validar parametros
        if(conexao == null || transmissor == null || receptor == null || nome == null || sala == null)
          throw new Exception("Parâmetros inválidos");

        // guardar parametros nos atributos
        this.conexao = conexao;
        this.transmissor = transmissor;
        this.receptor = receptor;
        this.nome = nome;
        this.sala = sala;
    }

    
    //getNome
    public String getNome()
    {
      return this.nome;
    }
    
    public Socket getConexao()
    {
        return this.conexao;
    }
    
    public ObjectOutputStream getTransmissor()
    {
        return this.transmissor;
    }
    
    public ObjectInputStream getReceptor()
    {
        return this.receptor;
    }
    
    public Sala getSala()
    {
        return this.sala;
    }
    

    public void envia (Coisa x) throws Exception
    {
        try
        {
            this.transmissor.writeObject (x);
            this.transmissor.flush();
        }
        catch (IOException erro)
        {
            throw new Exception("Erro ao enviar: " + erro);
        }
    }

    public Coisa recebe () throws Exception
    {        
        return (Coisa)this.receptor.readObject();
    }

    public void fechaTudo ()
    {
        try
        {
            this.transmissor.close();
            this.receptor.close();
            this.conexao.close();
    
        }
        catch (Exception erro)
        {
            System.err.println(erro);
        }
    }
    /*private String             nome;
    private Socket             conexao;
    private ObjectOutputStream transmissor;
    private ObjectInputStream  receptor;
    private Sala               sala;
*/
    public String toString()
    {
        String out;
        out = "Usuario: " + this.nome + "\n";
        out += "Conexao: " + this.conexao + "\n";
        out += "Sala: " + this.sala.getNome();
        
        return out;
    }
    
    public int hashcode()
    {
        int ret = 666;
        ret = ret * 2 + this.nome.hashCode();
        ret = ret * 2 + this.sala.hashCode();
        ret = ret * 2 + this.conexao.hashCode();
        ret = ret * 2 + this.receptor.hashCode();
        ret = ret * 2 + this.transmissor.hashCode();
        
        return ret;
    }
    
    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;
       
        if(obj==null)
            return false;
        
        if(obj.getClass() != this.getClass())
            return false;
        
        Usuario u = (Usuario)obj;
        
        if(!u.getNome().equals(this.getNome()))
            return false;
        
        if(!u.getConexao().equals(this.getConexao()))
            return false;
            
        if(!this.getTransmissor().equals(u.getTransmissor()))
            return false;
        
        if(!u.getReceptor().equals(this.getReceptor()))
            return false;
        
        if(!u.getSala().equals(this.getSala()))
            return false;
        
        return true;
    }

}
