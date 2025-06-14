package model.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class FileManager {
    private String path;

    public FileManager(String path)
    {
        this.path = path;
    }

    public String readFileToString() throws Exception
    {
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
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.path, overwrite));
        writer.write(value.trim());

        writer.close();
    }
}
