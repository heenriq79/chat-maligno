
import java.io.Serializable;
import java.util.*;
import javax.swing.*;

public class Sala
{
  protected String              nomeDaSala;
  protected int                 capacidadeMaxima;
  protected int                 capacidadeUsada;
  protected ArrayList<Usuario>  usuariosNaSala;

  public Sala(String nomeDaSala, int capacidadeMaxima) throws Exception
  {
    if(nomeDaSala == null || nomeDaSala.equals(""))
      throw new Exception("Nome de sala inválido");

    this.nomeDaSala = nomeDaSala;
    this.capacidadeMaxima = capacidadeMaxima;
    this.usuariosNaSala = new ArrayList(capacidadeMaxima);
  }
  
    public String getNome()
  {
      return this.nomeDaSala;
  }
  
  public int getCapacidadeUsada()
  {
      return this.capacidadeUsada;
  }
  
  public int getCapacidadeMaxima()
  {
      return this.capacidadeMaxima;
  }
  
  public ArrayList<String> getListaUsuariosNaSala() throws Exception
  {
      ArrayList<String> listaUsuariosNaSala = new ArrayList<String>(this.getCapacidadeMaxima());
      
      for (int i = 0; i < this.getCapacidadeUsada(); i++)
          listaUsuariosNaSala.add(this.getUsuario(i).getNome());
      
      return listaUsuariosNaSala;
  }
  
  public String[] getStringsUsuariosNaSala()
  {
      String[] usuariosStr = new String[usuariosNaSala.size()];
      for (int i = 0; i < usuariosNaSala.size(); i++)
          usuariosStr[i] = usuariosNaSala.get(i).getNome();
      return usuariosStr;
  }
          
  public Usuario getUsuario(String nome) throws Exception
  {
    if(nome == null || nome == "")
	throw new Exception("Parâmetro null");

    for(int i = 0; i < this.capacidadeUsada; i++)
        if(this.usuariosNaSala.get(i).getNome().trim().equals(nome.trim()))
            return usuariosNaSala.get(i);

    throw new Exception("Usuario não está nessa sala");
  }
  
  public Usuario getUsuario(int index) throws Exception
  {
      if (index < 0 || index > capacidadeUsada - 1)
          throw new Exception("Fora dos limites do vetor");
      
      return usuariosNaSala.get(index);
  }

  public void adicionarUsuario(Usuario usuario) throws Exception
  {
    if(usuario == null)
      throw new Exception("Usuário nulo");

    if(!this.isCheia())
    {
      this.usuariosNaSala.add(usuario);
      this.capacidadeUsada++;
    }
    else
      JOptionPane.showMessageDialog(null, "A sala que você selecionou está cheia." , "Aviso", JOptionPane.WARNING_MESSAGE);
  }
  
  public void removerUsuario(Usuario usuario)throws Exception
  {
    if(!(this.usuariosNaSala.contains(usuario)))
      throw new Exception("Este usuário não está nessa sala");

    this.usuariosNaSala.remove(usuario);
    this.capacidadeUsada--;
  }
  
  public boolean isCheia()
  {
      return this.capacidadeMaxima == this.capacidadeUsada;
  }
  
  public boolean existeUsuario(String nome) throws Exception
  {
    if (nome == null || nome.equals(""))
        throw new Exception("Nome de usuário inválido");
    
    for(int i = 0; i < this.capacidadeUsada; i++)
        if(this.usuariosNaSala.get(i).getNome().trim().equals(nome.trim()))
            return true;
    
    return false;
  }

}
