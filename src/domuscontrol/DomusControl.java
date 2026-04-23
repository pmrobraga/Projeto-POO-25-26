package domuscontrol;

import domuscontrol.automacoes.GestorAutomacoes;
import domuscontrol.casa.Casa;
import domuscontrol.casa.Divisao;
import domuscontrol.dispositivos.Dispositivo;
import domuscontrol.excecoes.*;
import domuscontrol.utilizadores.TipoUtilizador;
import domuscontrol.utilizadores.Utilizador;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe principal (fachada) do sistema DomusControl.
 *
 * É aqui que se centraliza toda a lógica de negócio:
 * criação de utilizadores, casas, divisões, associação de dispositivos,
 * estatísticas e serialização.
 *
 * Segue o padrão Singleton para garantir uma única instância do sistema.
 */
public class DomusControl implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String FICHEIRO_ESTADO = "domuscontrol.dat";

    // Singleton
    private static DomusControl instancia;

    // Dados do sistema
    private final Map<String, Utilizador> utilizadores;
    private final Map<String, Casa>       casas;
    private final Map<String, Dispositivo> dispositivos; // catálogo global
    private final Map<String, GestorAutomacoes> gestores; // gestor por casa (idCasa -> gestor)

    // Simulação de tempo
    private LocalDateTime relogioInterno;

    // -------------------------------------------------------------------------
    // Construtor privado (Singleton)
    // -------------------------------------------------------------------------

    private DomusControl() {
        this.utilizadores   = new LinkedHashMap<>();
        this.casas          = new LinkedHashMap<>();
        this.dispositivos   = new LinkedHashMap<>();
        this.gestores       = new LinkedHashMap<>();
        this.relogioInterno = LocalDateTime.now();
    }

    public static DomusControl getInstance() {
        if (instancia == null) instancia = new DomusControl();
        return instancia;
    }

    // -------------------------------------------------------------------------
    // Gestão de Utilizadores
    // -------------------------------------------------------------------------

    public Utilizador criarUtilizador(String id, String nome, String email, String password)
            throws UtilizadorJaExisteException {
        if (utilizadores.containsKey(id))
            throw new UtilizadorJaExisteException("Já existe um utilizador com ID '" + id + "'.");
        Utilizador u = new Utilizador(id, nome, email, password);
        utilizadores.put(id, u);
        return u;
    }

    public Utilizador getUtilizador(String id) throws UtilizadorNaoEncontradoException {
        Utilizador u = utilizadores.get(id);
        if (u == null)
            throw new UtilizadorNaoEncontradoException("Utilizador '" + id + "' não encontrado.");
        return u;
    }

    public Collection<Utilizador> getTodosUtilizadores() {
        return Collections.unmodifiableCollection(utilizadores.values());
    }

    // -------------------------------------------------------------------------
    // Gestão de Casas
    // -------------------------------------------------------------------------

    public Casa criarCasa(String id, String nome, String morada,
                          Utilizador proprietario)
            throws CasaJaExisteException {
        if (casas.containsKey(id))
            throw new CasaJaExisteException("Já existe uma casa com ID '" + id + "'.");
        Casa c = new Casa(id, nome, morada);
        casas.put(id, c);
        proprietario.adicionarCasa(c, TipoUtilizador.ADMINISTRADOR);
        return c;
    }

    public Casa getCasa(String id) throws CasaNaoEncontradaException {
        Casa c = casas.get(id);
        if (c == null)
            throw new CasaNaoEncontradaException("Casa '" + id + "' não encontrada.");
        return c;
    }

    public Collection<Casa> getTodasCasas() {
        return Collections.unmodifiableCollection(casas.values());
    }

    // -------------------------------------------------------------------------
    // Gestão de Divisões
    // -------------------------------------------------------------------------

    public Divisao criarDivisao(String idDivisao, String nomeDivisao,
                                Casa casa, Utilizador quemPede)
            throws PermissaoInsuficienteException, DivisaoJaExisteException {
        if (!quemPede.isAdministradorDe(casa))
            throw new PermissaoInsuficienteException(
                    "O utilizador '" + quemPede.getNome() + "' não é administrador desta casa.");
        Divisao d = new Divisao(idDivisao, nomeDivisao);
        casa.adicionarDivisao(d);
        return d;
    }

    // -------------------------------------------------------------------------
    // Gestão de Dispositivos
    // -------------------------------------------------------------------------

    public void registarDispositivo(Dispositivo d) {
        if (dispositivos.containsKey(d.getId()))
            throw new IllegalArgumentException("Já existe um dispositivo com ID '" + d.getId() + "'.");
        dispositivos.put(d.getId(), d);
    }

    public void associarDispositivoADivisao(String idDispositivo, Casa casa,
                                            String idDivisao, Utilizador quemPede)
            throws PermissaoInsuficienteException, DivisaoNaoEncontradaException,
                   DispositivoJaExisteException, DispositivoNaoEncontradoException {
        if (!quemPede.isAdministradorDe(casa))
            throw new PermissaoInsuficienteException("Sem permissão para adicionar dispositivos.");
        Dispositivo d = getDispositivoCatalogo(idDispositivo);
        Divisao div = casa.getDivisaoPorId(idDivisao);
        div.adicionarDispositivo(d);
    }

    public Dispositivo getDispositivoCatalogo(String id) throws DispositivoNaoEncontradoException {
        Dispositivo d = dispositivos.get(id);
        if (d == null)
            throw new DispositivoNaoEncontradoException("Dispositivo '" + id + "' não encontrado no catálogo.");
        return d;
    }

    // -------------------------------------------------------------------------
    // Estatísticas
    // -------------------------------------------------------------------------

    /**
     * Retorna a casa que mais consumiu (em Wh totais acumulados).
     */
    public Optional<Casa> getCasaQueMaisConsome() {
        return casas.values().stream()
                .max(Comparator.comparingDouble(Casa::getConsumoTotal));
    }

    /**
     * Top N dispositivos mais utilizados numa casa — por tempo ligado.
     */
    public List<Dispositivo> getTopDispositivosPorTempo(Casa casa, int n) {
        return casa.getTodosDispositivos().stream()
                .sorted((a, b) -> Long.compare(b.getTempoLigadoMinutos(), a.getTempoLigadoMinutos()))
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Top N dispositivos mais utilizados numa casa — por número de activações.
     */
    public List<Dispositivo> getTopDispositivosPorActivacoes(Casa casa, int n) {
        return casa.getTodosDispositivos().stream()
                .sorted((a, b) -> Integer.compare(b.getNumeroActivacoes(), a.getNumeroActivacoes()))
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Top N divisões (de todas as casas) com mais dispositivos.
     * Retorna pares (Casa, Divisao).
     */
    public List<Map.Entry<Casa, Divisao>> getTopDivisoesPorDispositivos(int n) {
        List<Map.Entry<Casa, Divisao>> pares = new ArrayList<>();
        for (Casa c : casas.values()) {
            for (Divisao d : c.getDivisoes()) {
                pares.add(new AbstractMap.SimpleEntry<>(c, d));
            }
        }
        pares.sort((a, b) ->
                Integer.compare(b.getValue().getNumeroDispositivos(), a.getValue().getNumeroDispositivos()));
        return pares.subList(0, Math.min(n, pares.size()));
    }

    // -------------------------------------------------------------------------
    // Simulação de tempo
    // -------------------------------------------------------------------------

    public LocalDateTime getRelogio() { return relogioInterno; }

    public void avancarTempo(int minutos) {
        relogioInterno = relogioInterno.plusMinutes(minutos);
        for (Dispositivo d : dispositivos.values()) {
            d.registarTempoFuncionamento(minutos);
        }
        for (GestorAutomacoes g : gestores.values()) {
            g.tick(relogioInterno);
        }
    }

    // Gestores de automacoes
    public void setGestorAutomacoes(Casa casa, GestorAutomacoes gestor) {
        gestores.put(casa.getId(), gestor);
    }

    public GestorAutomacoes getGestorAutomacoes(Casa casa) {
        GestorAutomacoes g = gestores.get(casa.getId());
        if (g == null) { g = new GestorAutomacoes(); gestores.put(casa.getId(), g); }
        return g;
    }

    // -------------------------------------------------------------------------
    // Serialização (gravar / carregar estado)
    // -------------------------------------------------------------------------

    public void gravarEstado() throws IOException {
        gravarEstado(FICHEIRO_ESTADO);
    }

    public void gravarEstado(String ficheiro) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheiro))) {
            oos.writeObject(this);
            System.out.println("Estado gravado em '" + ficheiro + "'.");
        }
    }

    public static DomusControl carregarEstado() throws IOException, ClassNotFoundException {
        return carregarEstado(FICHEIRO_ESTADO);
    }

    public static DomusControl carregarEstado(String ficheiro)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheiro))) {
            instancia = (DomusControl) ois.readObject();
            System.out.println("Estado carregado de '" + ficheiro + "'.");
            return instancia;
        }
    }
}
