package dados;

public class Operacoes {

    private int[] buscasPresentes;
    private int[] buscasAusentes;
    private int[] insercoes;
    private int[] remocoes;
    private int[][] intervalos;

    public Operacoes(
            int[] buscasPresentes,
            int[] buscasAusentes,
            int[] insercoes,
            int[] remocoes,
            int[][] intervalos) {

        this.buscasPresentes = buscasPresentes;
        this.buscasAusentes = buscasAusentes;
        this.insercoes = insercoes;
        this.remocoes = remocoes;
        this.intervalos = intervalos;
    }

    public int[] getBuscasPresentes() {
        return buscasPresentes;
    }

    public int[] getBuscasAusentes() {
        return buscasAusentes;
    }

    public int[] getInsercoes() {
        return insercoes;
    }

    public int[] getRemocoes() {
        return remocoes;
    }

    public int[][] getIntervalos() {
        return intervalos;
    }
}
