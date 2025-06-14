package model.utils;

import java.lang.reflect.Field;

public class StreamableObject {
    private final Field[] fields = this.getClass().getDeclaredFields();

    //Um jeito de pegar todas os valores das propriedades de uma classe
    public String toCSV()
    {
        StringBuffer str = new StringBuffer();

        for (var item : this.fields)
        {
            item.setAccessible(true); //Libera o atributo pra acesso

            try
            {
                //Pega valor por valor e da append com virgula no final
                Object value = item.get(this);
                str.append(value.toString());
                str.append(",");

                item.setAccessible(false); //Bloqueia novamente
            }
            catch (Exception ex) { }
        }

        return str.toString(); //Retorna a string em formato de csv
    }

    //Retornar uma array com os nomes das propriedades da classe
    public String[] properties()
    {
        String[] names = new String[this.fields.length];

        for (int x = 0; x < this.fields.length; x++)
        {
            names[x] = this.fields[x].getName().substring(0, 1).toUpperCase() + this.fields[x].getName().substring(1);
        }

        return names; //Retorna a string em formato de csv
    }

    //Retorna uma array com os valores das propriedades da classe
    public Object[] toObject()
    {
        Object[] obj = new Object[this.fields.length];
        int index = 0;

        for (var item : this.fields)
        {
            item.setAccessible(true); //Libera o atributo pra acesso

            try
            {
                //Pega valor por valor e da append com virgula no final
                obj[index] = item.get(this);
                index++;

                item.setAccessible(false); //Bloqueia novamente
            }
            catch (Exception ex) { }
        }

        return obj;
    }
}
