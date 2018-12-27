public class EscolhaDeSala implements Coisa
{
    protected String nomeDaSala;
    
    public EscolhaDeSala(String nomeDaSala) throws Exception
    {
        if (nomeDaSala == null || nomeDaSala.equals(""))
            throw new Exception("Nome de sala inválido");
        
        this.nomeDaSala = nomeDaSala;
    }
    
    public String getNomeDaSala()
    {
        return this.nomeDaSala;
    }
}