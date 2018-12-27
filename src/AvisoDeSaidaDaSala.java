
import java.io.IOException;
import java.io.ObjectOutputStream;

public class AvisoDeSaidaDaSala implements Coisa
{
    
    protected String remetente;
    
    public AvisoDeSaidaDaSala(String remetente) throws Exception
    {
        if (remetente == null || remetente.equals(""))
            throw new Exception("Remetente nulo!");
            
        this.remetente = remetente;
    }
    
    public String getRemetente()
    {
        return this.remetente;
    }
    
    public String toString()
    {
        return "Aviso de saída do remetente " + this.remetente;
    }
}