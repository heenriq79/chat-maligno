public class EscolhaDeNome implements Coisa
{
    String nomeUsuarioDesejado;
    
    public EscolhaDeNome(String nomeUsuarioDesejado)
    {
        this.nomeUsuarioDesejado = nomeUsuarioDesejado;
    }
    
    public String getNome()
    {
        return this.nomeUsuarioDesejado;
    }
}
