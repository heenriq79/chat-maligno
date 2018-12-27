import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor
{
    public static void main (String[] args)
    {
        try
        {
            ServerSocket pedido = new ServerSocket (5555);
            System.out.println("Servidor operante...\n");

            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            String mensagem = "";
            Salas salas = new Salas();
            System.out.println("Salas instanciadas...\n");
            
            System.out.println("COMANDOS:");
            System.out.println("1 - FECHAR SERVIDOR\n");

            Thread rodar = new Thread()
            {
                public void run()
                {
                    for(;;)
                    {
                        try
                        {
                            Socket conexao = pedido.accept();
                            System.out.println("Conexão estabelecida");
                            CuidadoraDeUsuario cuidadora = new CuidadoraDeUsuario(conexao, salas);
                            cuidadora.start();
                        }
                        catch(IOException e)
                        {
                            System.out.println("Erro na leitura: " + e);
                        }
                        catch(Exception er)
                        {
                            try 
                            {
                                pedido.close();
                            } 
                            catch (IOException err) 
                            {
                                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, err);
                                System.out.println("Erro ao fechar o servidor: " + err);
                            }
                            System.out.println("Erro ao estabelecer conexão: " + er);
                        }
                    }
                }
            };
            rodar.start();

            while(!mensagem.equals("1"))
            {
                mensagem = teclado.readLine();
            }

            if(mensagem.equals("1"))
            {
                rodar.stop();
                pedido.close();
                System.out.println("Servidor fechado");
            }

        }
        catch(Exception erro)
        {
            System.out.println("ERRO: " + erro);
        }
    }
}