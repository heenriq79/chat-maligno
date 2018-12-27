package bd;

import bd.core.*;
import bd.daos.*;

public class BDSQLServer
{
    public static final MeuPreparedStatement COMANDO;

    static
    {
    	MeuPreparedStatement comando = null;

    	try
        {
            comando =
            new MeuPreparedStatement (
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "jdbc:sqlserver://regulus;databasename=BD18205",
            "BD18205", "S67EC83");
        }
        catch (Exception erro)
        {
            System.err.println ("Problemas de conexao com o BD: " + erro);
            System.exit(0); // aborta o programa
        }
        
        COMANDO = comando;
    }
}