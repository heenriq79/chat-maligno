public class Chat 
{
    public static void main(String[] args) 
    {
        try 
        {
            JanelaPrincipal window = new JanelaPrincipal();
            window.frmChat.setVisible(true);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}

