
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AvisoDeEntradaNaSala implements Coisa
{
    protected String remetente;
    
    public AvisoDeEntradaNaSala(String remetente) throws Exception
    {
        if (remetente == null || remetente.equals(""))
            throw new Exception("Remetente inválido");
            
        this.remetente = remetente;
    }
    
    public String getRemetente()
    {
        return this.remetente;
    }
    
    public String toString()
    {
        return "Aviso de entrada do remetente " + this.remetente;
    }
}