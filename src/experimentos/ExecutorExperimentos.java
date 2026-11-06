package experimentos;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import Estrutura.EstruturaDados;

public class ExecutorExperimentos {

    private static final int REPETICOES = 5;

    private final Supplier<EstruturaDados> fabrica;
    private final int[] dadosIniciais;
    private final int[] buscasPresentes;
    private final int[] buscasAusentes;
    private final int[] insercoes;
    private final int[] remocoes;
    private final int[][] intervalos;

    private final MedidorTempo medidor;
    private final Map<String, long[]> tempos;

    private EstruturaDados estrutura;

    public ExecutorExperimentos(
            Supplier<EstruturaDados> fabrica,
            int[] dadosIniciais,
            int[] buscasPresentes,
            int[] buscasAusentes,
            int[] insercoes,
            int[] remocoes,
            int[][] intervalos) {

        this.fabrica = fabrica;
        this.dadosIniciais = dadosIniciais.clone();
        this.buscasPresentes = buscasPresentes.clone();
        this.buscasAusentes = buscasAusentes.clone();
        this.insercoes = insercoes.clone();
        this.remocoes = remocoes.clone();

        this.intervalos = new int[intervalos.length][2];

        for (int i = 0; i < intervalos.length; i++) {
            this.intervalos[i] = intervalos[i].clone();
        }

        this.medidor = new MedidorTempo();
        this.tempos = new HashMap<>();
    }

    // Reconstrói a estrutura com os dados iniciais.
    private EstruturaDados reconstruirEstrutura() {

        EstruturaDados nova = fabrica.get();

        for (int valor : dadosIniciais) {
            nova.inserir(valor);
        }

        return nova;
    }

    // Executa aquecimento e cinco repetições.
    // A reconstrução e a validação ficam fora do cronômetro.
    private void executarTeste(
            String nome,
            Consumer<EstruturaDados> operacao,
            Consumer<EstruturaDados> validacao) {

        // Aquecimento.
        estrutura = reconstruirEstrutura();
        operacao.accept(estrutura);
        validacao.accept(estrutura);

        long[] resultados = new long[REPETICOES];

        for (int i = 0; i < REPETICOES; i++) {

            estrutura = reconstruirEstrutura();

            long inicio = medidor.iniciar();

            operacao.accept(estrutura);

            resultados[i] = medidor.finalizar(inicio);

            // Conferência após parar o cronômetro.
            validacao.accept(estrutura);
        }

        tempos.put(nome, resultados);
    }

    // 1. CONSTRUÇÃO
    // Mede desde a criação até o fim das inserções.

    public void executarConstrucao() {

        // Aquecimento.
        EstruturaDados aquecimento = fabrica.get();

        for (int valor : dadosIniciais) {
            aquecimento.inserir(valor);
        }

        if (aquecimento.tamanho() != dadosIniciais.length) {
            throw new IllegalStateException("Erro no aquecimento.");
        }

        long[] resultados = new long[REPETICOES];

        for (int i = 0; i < REPETICOES; i++) {

            long inicio = medidor.iniciar();

            estrutura = fabrica.get();

            for (int valor : dadosIniciais) {
                estrutura.inserir(valor);
            }

            resultados[i] = medidor.finalizar(inicio);

            if (estrutura.tamanho() != dadosIniciais.length) {
                throw new IllegalStateException("Erro na construção.");
            }
        }

        tempos.put("construcao", resultados);
    }

    // 2. BUSCA PRESENTE

    public void executarBuscaPresente() {

        int[] encontrados = new int[1];

        executarTeste(
            "busca_presente",

            estrutura -> {
                encontrados[0] = 0;

                for (int valor : buscasPresentes) {
                    if (estrutura.buscar(valor)) {
                        encontrados[0]++;
                    }
                }
            },

            estrutura -> {
                if (encontrados[0] != buscasPresentes.length) {
                    throw new IllegalStateException(
                        "Erro nas buscas presentes."
                    );
                }
            }
        );
    }

    // 3. BUSCA AUSENTE

    public void executarBuscaAusente() {

        int[] encontrados = new int[1];

        executarTeste(
            "busca_ausente",

            estrutura -> {
                encontrados[0] = 0;

                for (int valor : buscasAusentes) {
                    if (estrutura.buscar(valor)) {
                        encontrados[0]++;
                    }
                }
            },

            estrutura -> {
                if (encontrados[0] != 0) {
                    throw new IllegalStateException(
                        "Uma busca ausente encontrou um valor."
                    );
                }
            }
        );
    }

    // 4. INSERÇÃO

    public void executarInsercao() {

        int[] inseridos = new int[1];

        executarTeste(
            "insercao",

            estrutura -> {
                inseridos[0] = 0;

                for (int valor : insercoes) {
                    if (estrutura.inserir(valor)) {
                        inseridos[0]++;
                    }
                }
            },

            estrutura -> {

                int tamanhoEsperado =
                    dadosIniciais.length + insercoes.length;

                if (inseridos[0] != insercoes.length ||
                    estrutura.tamanho() != tamanhoEsperado) {

                    throw new IllegalStateException(
                        "Erro nas inserções."
                    );
                }

                for (int valor : insercoes) {
                    if (!estrutura.buscar(valor)) {
                        throw new IllegalStateException(
                            "Valor inserido não encontrado: " + valor
                        );
                    }
                }
            }
        );
    }

    // 5. REMOÇÃO

    public void executarRemocao() {

        int[] removidos = new int[1];

        executarTeste(
            "remocao",

            estrutura -> {
                removidos[0] = 0;

                for (int valor : remocoes) {
                    if (estrutura.remover(valor)) {
                        removidos[0]++;
                    }
                }
            },

            estrutura -> {

                int tamanhoEsperado =
                    dadosIniciais.length - remocoes.length;

                if (removidos[0] != remocoes.length ||
                    estrutura.tamanho() != tamanhoEsperado) {

                    throw new IllegalStateException(
                        "Erro nas remoções."
                    );
                }

                for (int valor : remocoes) {
                    if (estrutura.buscar(valor)) {
                        throw new IllegalStateException(
                            "Valor removido ainda encontrado: " + valor
                        );
                    }
                }
            }
        );
    }

    // 6. CONSULTA POR INTERVALO

    public void executarConsultaIntervalo() {

        int[] total = new int[1];

        executarTeste(
            "consulta_intervalo",

            estrutura -> {
                total[0] = 0;

                for (int[] intervalo : intervalos) {
                    total[0] += estrutura.consultarIntervalo(
                        intervalo[0],
                        intervalo[1]
                    );
                }
            },

            estrutura -> {

                int esperado = 0;

                for (int[] intervalo : intervalos) {
                    for (int valor : dadosIniciais) {
                        if (valor >= intervalo[0] &&
                            valor <= intervalo[1]) {
                            esperado++;
                        }
                    }
                }

                if (total[0] != esperado) {
                    throw new IllegalStateException(
                        "Resultado incorreto na consulta por intervalo."
                    );
                }
            }
        );
    }

    // Retorna os cinco tempos de um teste.
    public long[] getTempos(String teste) {

        long[] resultado = tempos.get(teste);

        if (resultado == null) {
            throw new IllegalArgumentException(
                "Teste ainda não executado: " + teste
            );
        }

        return resultado.clone();
    }

    public Map<String, long[]> getTodosTempos() {

        Map<String, long[]> copia = new HashMap<>();

        for (Map.Entry<String, long[]> entrada : tempos.entrySet()) {
            copia.put(entrada.getKey(), entrada.getValue().clone());
        }

        return copia;
    }
}

