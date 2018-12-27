package bd.dbos;

public class Salabd implements Cloneable
{
    private String    nome;
    private int capacidadeMaxima;
 
    
    public void setNome (String nome) throws Exception
    {
        if (nome==null || nome.equals(""))
            throw new Exception ("Nome não fornecido");

        this.nome = nome;
    }   

    public void setCapMax (int capacidadeMaxima) throws Exception
    {
        if (capacidadeMaxima <= 0)
            throw new Exception ("Capacidade invalida");

        this.capacidadeMaxima = capacidadeMaxima;
    }


    public String getNome ()
    {
        return this.nome;
    }

    public int getCapMax ()
    {
        return this.capacidadeMaxima;
    }


    public Salabd ( String nome, int capacidadeMaxima) throws Exception
    {
        this.setNome   (nome);
        this.setCapMax  (capacidadeMaxima);
    }

    public String toString ()
    {
        String ret="";

        ret+="Nome: "+this.nome+"\n";
        ret+="Capacidade Maxima: "+this.capacidadeMaxima;

        return ret;
    }

    public boolean equals (Object obj)
    {
        if (this==obj)
            return true;

        if (obj==null)
            return false;

        if (!(obj instanceof Salabd))
            return false;

        Salabd sal = (Salabd)obj;

        if (this.capacidadeMaxima != sal.capacidadeMaxima)
            return false;

        if (this.nome.equals(sal.nome))
            return false;

        return true;
    }

    public int hashCode ()
    {
        int ret=666;

        ret = 7*ret + new Integer(this.capacidadeMaxima).hashCode();
        ret = 7*ret + this.nome.hashCode();

        return ret;
    }


    public Salabd (Salabd modelo) throws Exception
    {
        this.capacidadeMaxima = modelo.capacidadeMaxima; // nao clono, pq nao eh objeto
        this.nome   = modelo.nome;   // nao clono, pq nao eh clonavel
    }

    public Object clone ()
    {
        Salabd ret=null;

        try
        {
            ret = new Salabd (this);
        }
        catch (Exception erro)
        {} // nao trato, pq this nunca � null e construtor de
           // copia da excecao qdo seu parametro for null
        return ret;
    }
}