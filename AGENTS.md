# Proteção deste arquivo

- O Codex executado dentro dos projetos dos mods não pode alterar, formatar, mover, renomear ou excluir este `AGENTS.md`; deve apenas explicar ao desenvolvedor qualquer mudança necessária.
- O Codex responsável pela manutenção compartilhada dos quatro mods pode alterar este `AGENTS.md` mediante solicitação explícita do desenvolvedor, exclusivamente para atualizar o modelo comum. O Codex que estiver trabalhando em uma tarefa específica de apenas um dos projetos continua proibido de alterar, formatar, mover, renomear ou excluir este arquivo.
- O Codex responsável pela manutenção compartilhada dos quatro mods pode replicar alterações do modelo entre os respectivos arquivos `AGENTS.md` mediante solicitação explícita do desenvolvedor, garantindo que as regras compartilhadas permaneçam idênticas. O Codex que estiver trabalhando em uma tarefa específica de apenas um dos projetos continua sem autorização para realizar essa replicação.

Leia este arquivo antes de alterar, compilar, testar ou publicar o projeto. Instruções específicas prevalecem quando complementarem ou substituírem explicitamente estas regras.

# Configuração compartilhada

- Minecraft: `26.2`.
- Fabric Loader: `0.19.3`.
- Fabric API: `0.158.0+26.2`.
- Mod Menu: `20.0.1`.
- Java/JDK: Temurin `25.0.3 LTS`, build `25.0.3+9`.
- Localização do JDK: `D:\GAMES\minecraft\java\jdk-25.0.3`.
- Identificador do projeto: nome do projeto em PascalCase, sem espaços nem hífens, preservando a separação semântica das palavras. Exemplo: `controlfood` → `ControlFood`.
- Versão do projeto: use exatamente dois componentes numéricos no formato `<principal>.<decimal>`, com apenas um número após o ponto. Exemplo: `1.0`.
- JAR atual: `<identificador>-*.jar`.
- JAR da release: `<identificador>-<versão>.jar`.
- Instância principal de jogo e testes:
  - `D:\GAMES\minecraft\instances\NEBULOSA\minecraft\mods`
  - A NEBULOSA é a instância em que o desenvolvedor joga habitualmente e também realiza os testes manuais dos mods. Ela possui backups regulares, portanto pode receber builds destinadas a testes.

## Modelo técnico das releases

Substitua os valores pelas versões específicas do projeto:

```markdown
<descrição da versão em uma única estrofe>

---

**Compatibility:** Minecraft <versão> / [Fabric Loader <versão>+](https://fabricmc.net/use/installer/)
**Dependencies:** [Fabric API <versão>](https://modrinth.com/mod/fabric-api) / [Mod Menu <versão>+](https://modrinth.com/mod/modmenu)
```

# Ambiente

- Trabalhe no Windows com PowerShell.
- Antes do build, confira as versões exigidas pelo projeto e use somente o JDK e as ferramentas definidos.
- Não atualize dependências sem solicitação ou necessidade técnica.

# Operações demoradas

Antes de executar build, varredura ampla ou outra operação demorada, informe brevemente por que ela é necessária. Não execute tarefas pesadas sem necessidade técnica.

# Build

Configure o JDK indicado e use o Gradle Wrapper:

```powershell
.\gradlew.bat clean build --warning-mode all
```

- Mudanças em código, recursos, dependências, configuração de build ou conteúdo do JAR exigem um build bem-sucedido.
- Se o build falhar, investigue a causa e não apresente o artefato como validado.

## Exceção para documentação

- Alterações somente em arquivos informativos, como `AGENTS.md` e `README.md`, não exigem build nem instalação.
- Se o conjunto também alterar código, recursos, dependências ou configuração, execute normalmente o build e a instalação.

# Instalação na instância de jogo e testes

Depois de cada build bem-sucedido:

1. Valide os destinos antes de alterar arquivos.
2. Localize e remova somente os JARs do mod.
3. Copie o novo JAR de `build\libs` para cada destino.
4. Se a cópia terminar sem erro, considere o JAR instalado; não compare hash, tamanho ou conteúdo entre o arquivo de origem e a cópia. Investigue somente se a própria operação de cópia falhar.

Nunca remova, mova ou substitua outros mods.

# Validação

- Nunca abra ou controle o Minecraft, nem execute `runClient` ou qualquer tarefa que inicialize o jogo.
- Valide por build, revisão estática, inspeção dos arquivos gerados e, quando disponíveis, logs do teste manual do usuário.
- Deixe confirmações de interface e gameplay dentro do jogo para o usuário e informe o que deve ser observado.
- Projete a interface para fullscreen, GUI Scale `2x`, identidade visual vanilla e ausência de cortes ou sobreposições.

# Controle de versão

- Faça commit somente mediante solicitação explícita do usuário.
- Reúna implementação, correções e refinamentos da mesma entrega em um único commit coerente.
- Finalize, valide e registre uma entrega estrutural antes de iniciar outra; não misture entregas distintas.
- Escreva mensagens descritivas em inglês, com aproximadamente 45 caracteres.
- Commit não autoriza push. Faça push somente mediante solicitação explícita.

# Releases no GitHub

- Crie releases como rascunho e publique somente mediante autorização explícita.
- Use a tag `v<versão>` e anexe o JAR correspondente à mesma versão.
- Informe corretamente as versões compatíveis do Minecraft, loader, APIs e demais dependências.

## Título

- Escreva em inglês no formato `<emoji> <título temático>`.
- Represente a identidade ou principal mudança da versão em aproximadamente 30 caracteres, sem repetir o nome do mod ou a versão.

## Descrição

- Escreva em inglês, sem negrito, em uma única estrofe de aproximadamente 440 caracteres.
- Depois, insira uma linha horizontal e somente as linhas técnicas de compatibilidade e dependências.
- Contagens de caracteres são referências visuais, não limites que prejudiquem clareza ou precisão.

# README e documentação para o jogador

- Quando autorizado, mantenha o `README.md` sincronizado com o comportamento real do mod.
- Antes de escrever, confira constantes, listas, condições e valores no código. Não invente comportamentos.
- As regras de nome, lore e descrição funcional desta seção aplicam-se igualmente às opções da tela principal e às opções da tela de opções globais.
- Estruture o `README.md` com a seção `## Visão geral`, seguida pela seção `## Opções globais`.
- Escreva o nome de cada opção, principal ou global, com exatamente duas palavras.
- Escreva a Visão geral em uma única estrofe que combine a função técnica principal do mod com a fantasia proporcionada ao jogador.
- Restrinja a Visão geral à funcionalidade principal; não mencione nela as opções globais.
- Mantenha somente a tabela de opções globais. Não crie tabela ou lista detalhada para as opções principais.
- Na tabela de opções globais, explique claramente o que cada opção faz, o que afeta e suas condições, exceções e comportamentos especiais.
- Informe valores fixos relevantes, especialmente os não configuráveis: quantidades, multiplicadores, porcentagens, distâncias, alcances, durações, intervalos, limites, chances e tempos.
- Não use termos vagos quando houver valor, prazo, lista ou critério exato. Defina precisamente listas e categorias indispensáveis ao escopo.
- Não transforme descrições em tutoriais de Minecraft. Prefira afirmações positivas e diretas.
- Não use expressões como “caso ativada”, “quando estiver ativa” ou “ao ativar esta opção”; o contexto da interface já comunica essa condição.
- Revise obrigatoriamente descrições e tooltips acima de `300` caracteres sem espaços. Esse valor é um gatilho, não um limite rígido.
- Na revisão, remova redundâncias, explicações gerais, negativas desnecessárias, exemplos irrelevantes e detalhes internos, preservando informações e valores importantes.
- Ao alterar uma opção, revise também as descrições relacionadas dentro do escopo autorizado.
- Todo tooltip de opção, principal ou global, deve conter obrigatoriamente lore e descrição funcional separadas.
- Escreva a descrição funcional de cada opção, principal ou global, com precisão sobre o que ela faz, o que afeta e suas condições, exceções e comportamentos especiais.
- Escreva cada lore com `120` a `130` caracteres e tom narrativo. Compare-a com as demais lores do próprio mod para preservar uma identidade textual coerente.
- Escreva os nomes de todas as opções, principais e globais, e todas as lores como proclamações grandiosas que pareçam concebidas por Arthur Boyle, de Fire Force: lógica cavaleiresca delirante, certeza absoluta e referências recorrentes ao Cavaleiro Príncipe, à coroa, ao reino, a Excalibur, a dragões, a magia e a feitos heroicos.
- Nas traduções em inglês, preserve o sentido e a identidade cavaleiresca, mas use deliberadamente um inglês “imbromation”: confiante, compreensível e gramaticalmente suspeito, como se o próprio Cavaleiro Príncipe o tivesse traduzido.

# Sincronização da documentação

- O tooltip em português de cada opção global e a descrição correspondente no `README.md` devem ter o mesmo conteúdo e redação, exceto pela formatação do Markdown.
- Mudanças em regra, valor, limite, escopo, condição ou exceção de uma opção global exigem a atualização conjunta do README, do tooltip em português e das traduções correspondentes.
- Mudanças nas opções principais exigem a atualização conjunta do tooltip em português e das traduções correspondentes, sem criar tabela ou lista detalhada no README.
- Mantenha todas as traduções completas e semanticamente equivalentes.
- Use largura máxima de `425 px` e avanço vertical uniforme de `12 px`, sem duplicar o espaçamento vanilla após a primeira linha.
- Aplique essas dimensões somente aos tooltips das opções globais e das opções principais; preserve o comportamento vanilla nas demais interfaces.

# Escopo e compatibilidade

- Considere o mod client-side e focado em singleplayer, salvo indicação contrária.
- Considere a configuração acessível pelo Mod Menu quando essa integração existir.
- Não prometa suporte em servidores externos para comportamentos controlados pelo servidor apenas porque o mod está instalado no cliente.

# Padrões da primeira instalação

- Opções principais iniciam ativadas; opções globais, desativadas.
- Novas opções seguem os mesmos padrões, inclusive durante migrações.
- Preserve escolhas explicitamente salvas.
- Campo ausente não representa escolha explícita; use o padrão do tipo da opção.
- Migre formatos sem redefinir escolhas válidas existentes.
