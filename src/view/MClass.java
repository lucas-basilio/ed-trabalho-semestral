package view;

import model.professor.Professor;

public class MClass {
    public static void main(String[] args)
    {
        String[] names;
        Professor temp = new Professor();
        temp.setNome("Teste");
        temp.setPontos(12.0);
        temp.setCpf("12314241");
        temp.setArea("Matematica");

        String vals = temp.toCSV();
        names = temp.properties();
        var items = temp.toObject();

        System.out.println(vals);
    }
}
