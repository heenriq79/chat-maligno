import java.util.ArrayList;

public class SalasDisponiveis implements Coisa
{
	private ArrayList<String> salas;

	public SalasDisponiveis(ArrayList<String> salas) throws Exception
	{
            if(salas == null)
                throw new Exception("Lista de salas inválida");

            this.salas = salas;
	}

	public ArrayList<String> getNomeSala()
	{
		return this.salas;
	}
}