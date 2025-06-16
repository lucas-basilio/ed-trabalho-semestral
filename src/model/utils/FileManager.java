package model.utils;

import java.io.*;

public class FileManager {
    private String path;

    public FileManager(String path)
    {
        this.path = path;
    }

    public String readFileToString() throws Exception
    {
        verifyFile();

        BufferedReader reader = new BufferedReader(new FileReader(this.path));
        String data;
        StringBuffer str = new StringBuffer();

         while ((data = reader.readLine()) != null)
        {
            str.append(data);

            if (data.charAt(data.length() - 1) != ',') //Caso o CSV possua quebra de linhas e não tenha virgula no final ;-;
            {
                str.append(",");
            }
        }

        reader.close();
        return str.toString();
    }

    /**
     @param value Valor a ser inserido :v
     @param overwrite True pra adicionar o texto (sem sobreescrever o arquivo), False para sobreescrever o arquivo
    */
    public void writeIntoFile(String value, boolean overwrite) throws Exception
    {
        verifyFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(this.path, overwrite));
        writer.write(value.trim());

        writer.close();
    }

    public void verifyFile() throws Exception
    {
        File file = new File(this.path);

        try
        {
            if (!file.exists())
            {
                file.createNewFile();
            }
        }
        catch (Exception e)
        {
            throw new Exception("Não foi possível criar o arquivo.");
        }
    }
}
