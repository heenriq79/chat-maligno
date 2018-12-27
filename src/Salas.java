
import bd.*;
import bd.core.MeuResultSet;
import bd.daos.Salasbd;
import java.sql.*;

import java.util.*;

public class Salas
{
	protected int qtdSalas;
	protected ArrayList<Sala> salas;

	public Salas() throws Exception
        {
            try
            {
                this.salas  = new ArrayList<Sala>();

                String sql = "SELECT * FROM SALAS";
                BDSQLServer.COMANDO.prepareStatement(sql);
                MeuResultSet resultado = (MeuResultSet)BDSQLServer.COMANDO.executeQuery(); 
                
                
                sql = "SELECT COUNT(*) AS QTDSALAS FROM SALAS";
                BDSQLServer.COMANDO.prepareStatement(sql);
                MeuResultSet resultado2 = (MeuResultSet)BDSQLServer.COMANDO.executeQuery();
                
                if(resultado2.next())
                    this.qtdSalas = resultado2.getInt("QTDSALAS");
                
                while(resultado.next())
                {
                    salas.add(new Sala(resultado.getString("NOME"), resultado.getInt("CAPACIDADEMAXIMA")));
                }
            }
            catch(SQLException erro)
            {
                throw new Exception("Erro ao acessar o banco de dados: " + erro);
            }
	}
        
        public int getQtdSalas()
        {
            return this.qtdSalas;
        }
        
        public ArrayList<Sala> getSalas()
        {
            return this.salas;
        }
        
        public String[] getStringSalas()
        {
            String[] salasStr = new String[salas.size()];
            for (int i = 0; i < salas.size(); i++)
                salasStr[i] = salas.get(i).getNome();
            return salasStr;
        }
        
        
        public Sala getSala(int index) throws Exception
        {
            if(index < 0 || index > this.qtdSalas)
		throw new Exception("Index inválido");
            
            return this.salas.get(index);
        }
        
        public Sala getSala(String nome) throws Exception
	{
            for(int i=0; i < salas.size(); i++)
		if(this.getSala(i).getNome().trim().equals(nome.trim()))
			return salas.get(i);

            throw new Exception("Nome de sala inexistente");
	}
        
        public boolean existeSala(String nomeDaSala) throws Exception
        {
            for(int i = 0; i < this.qtdSalas; i++)
		if(this.getSala(i).getNome().trim().equals(nomeDaSala.trim()))
                    return true;
            return false;
        }


	/*List<Cliente> lista = new ArrayList<Cliente>();

	String query = "SELECT nome, endereco, telefone, empresa, email, registro from cliente";
	try {

		ResultSet rs = statement.executeQuery(query);

		while(rs.next()){
			Cliente cli = new Cliente(rs.getString(4), rs.getString(1), 
					rs.getString(2), rs.getString(5), rs.getString(3), rs.getString(6));
			lista.add(cli);
		}
		statement.close();
	}
	catch(SQLException e){
		e.printStackTrace();
	}
	return lista;*/

	
}
