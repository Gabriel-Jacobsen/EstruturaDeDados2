package dados;


import java.util.Random;

public class GeradorOperacoes {

    private final Random random;

    // Construtor com semente padrão
    public GeradorOperacoes() {
        this(42);
    }

    // Permite definir outra semente, caso necessário
    public GeradorOperacoes(long seed) {
        this.random = new Random(seed);
    }

    // Gera buscas de valores que existem nos dados
    public int[] gerarBuscasPresentes(int[] dados, int quantidade) {

        if (dados == null || dados.length == 0) {
            throw new IllegalArgumentException("Dados não podem estar vazios.");
        }

        int[] buscas = new int[quantidade];

        for (int i = 0; i < quantidade; i++) {
            int indice = random.nextInt(dados.length);
            buscas[i] = dados[indice];
        }

        return buscas;
    }

    // Gera buscas de valores ausentes (números ímpares)
    public int[] gerarBuscasAusentes(int n, int quantidade) {

        if (n <= 0) {
            throw new IllegalArgumentException("n deve ser positivo.");
        }

        int[] buscas = new int[quantidade];

        for (int i = 0; i < quantidade; i++) {
            // Ímpares entre 1 e 2n - 1
            buscas[i] = random.nextInt(n) * 2 + 1;
        }

        return buscas;
    }

    // Gera valores distintos para inserção
    public int[] gerarInsercoes(int n) {

        int quantidade = n / 10;

        // Cria os números ímpares possíveis
        int[] candidatos = new int[n];

        for (int i = 0; i < n; i++) {
            candidatos[i] = 2 * i + 1;
        }

        // Fisher-Yates parcial: sorteia sem repetição
        for (int i = 0; i < quantidade; i++) {

            int indice = i + random.nextInt(n - i);

            int temp = candidatos[i];
            candidatos[i] = candidatos[indice];
            candidatos[indice] = temp;
        }

        // Copia somente os valores sorteados
        int[] insercoes = new int[quantidade];

        System.arraycopy(
            candidatos, 0,
            insercoes, 0,
            quantidade
        );

        return insercoes;
    }

    // Gera valores distintos para remoção
    public int[] gerarRemocoes(int[] dados, int quantidade) {

        if (dados == null || quantidade > dados.length || quantidade < 0) {
            throw new IllegalArgumentException("Quantidade inválida.");
        }

        // Copia os dados para não modificar o array original
        int[] candidatos = dados.clone();

        // Fisher-Yates parcial: sorteia sem repetição
        for (int i = 0; i < quantidade; i++) {

            int indice = i + random.nextInt(candidatos.length - i);

            int temp = candidatos[i];
            candidatos[i] = candidatos[indice];
            candidatos[indice] = temp;
        }

        int[] remocoes = new int[quantidade];

        System.arraycopy(
            candidatos, 0,
            remocoes, 0,
            quantidade
        );

        return remocoes;
    }

    // Gera os limites das consultas por intervalo
    public int[][] gerarIntervalos(int n, int quantidade) {

        if (n < 51) {
            throw new IllegalArgumentException("n deve ser maior ou igual a 51.");
        }

        int[][] intervalos = new int[quantidade][2];

        for (int i = 0; i < quantidade; i++) {

            // Mínimo entre 1 e 2n - 100
            int minimo = random.nextInt(2 * n - 100) + 1;

            // Máximo sempre 100 unidades acima do mínimo
            int maximo = minimo + 100;

            intervalos[i][0] = minimo;
            intervalos[i][1] = maximo;
        }

        return intervalos;
    }
}
