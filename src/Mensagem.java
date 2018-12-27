public class Mensagem implements Coisa
{
    private String destinatario;
    private String mensagem;
    private String remetente;

    public Mensagem(String nomeDestinatario, String remetente, String mensagem) throws Exception
    {
	if(nomeDestinatario == null || nomeDestinatario.equals("") || mensagem == null || mensagem.equals("") || remetente == null || remetente.equals(""))
            throw new Exception("Valores não podem ser null ou vazios");

	this.destinatario = nomeDestinatario;
        this.remetente = remetente;
	this.mensagem = mensagem;
    }
    
    public String getDestinatario()
    {
            return this.destinatario;
    }

    public String getMensagem()
    {
            return this.mensagem;
    }    
    
    public void setRemetente(String remetente) throws Exception
    {
        if(remetente==null || remetente.equals(""))
            throw new Exception("Remetente inválido");
        
        this.remetente = remetente;
    }
    
    public String getRemetente()
    {
        return this.remetente;
    }
}