
import java.util.ArrayList;

public class ListaDeUsuariosNaSala implements Coisa
{
    ArrayList<String> listaUsuariosSala;
    
    public ListaDeUsuariosNaSala(ArrayList<String> listaUsuariosSala) throws Exception
    {
        if(listaUsuariosSala == null)
            throw new Exception("Parâmetro inválido");
        
        this.listaUsuariosSala = listaUsuariosSala;
    }
    
    public ArrayList<String> getListaUsuariosSala()
    {
        return this.listaUsuariosSala;
    }
}
