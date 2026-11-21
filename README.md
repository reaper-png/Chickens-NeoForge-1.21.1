# Chickens — NeoForge 1.21.1

Port **não oficial** do [CreeperHost Chickens](https://github.com/CreeperHost/Chickens) **1.0.32**, originalmente para Minecraft 1.18.2 / Forge, para **Minecraft 1.21.1 / NeoForge**. Este projeto mantém a identidade e as mecânicas do mod original e **não utiliza Modern Chickens**.

> **Estado: `0.0.1-wip` — em desenvolvimento.** Há funcionalidades testadas durante o port, mas ainda não existe validação completa de servidor dedicado, multiplayer, upgrades de mundos antigos ou distribuição pública. Faça backup do mundo antes de testar.

## Funcionalidades do port

- **46 espécies originais** com os mesmos IDs de registro, texturas e cruzamentos, incluindo estatísticas **Growth / Gain / Strength** e geração de recursos.
- Galinhas capturadas como **itens 3D**, preservando espécie, atributos, idade, estado de análise e tempo restante de postura. O **Chicken Staff/Catcher** transforma a galinha em item **dropado no chão**, como no original.
- **Spawn Eggs** específicos por espécie, **16 ovos coloridos** arremessáveis e **ovos líquidos** de água/lava (1.000 mB), com uso no mundo e em tanques compatíveis.
- Máquinas **Roost**, **Breeder** e **seis variantes de Henhouse**: interfaces, slots, automação, salvamento dos inventários e tratamento de saída cheia.
- Nascimento, reprodução, genética, postura, conversão em Smart Chicken e geração natural das espécies configuradas.
- Integrações **opcionais** com **JEI** e **Jade**, sem empacotá-los no JAR do Chickens.
- Idiomas **inglês, português brasileiro e francês**.

As regras de criação, tempo de postura, produtos, cruzamentos e ativação podem ser ajustadas por espécie no arquivo `config/chickens/chickens.json`, criado no primeiro carregamento. Pare o jogo/servidor e faça backup antes de editar.

## Requisitos

| Componente | Versão |
| --- | --- |
| Minecraft | **1.21.1** |
| NeoForge | **21.1.240** (versão de desenvolvimento configurada; requisitos do mod declarados em `neoforge.mods.toml`) |
| Java / JDK | **21** |
| Versão do port | **0.0.1-wip** (em `gradle.properties`) |

## Compilar

Use o Gradle com JDK 21 e acesso às dependências Maven. No estado atual do repositório, **o Gradle Wrapper (`gradlew`/`gradlew.bat`) não está versionado**: use uma instalação local do Gradle compatível ou o wrapper do seu ambiente de desenvolvimento, se houver.

```powershell
gradle clean build
```

Se o seu projeto local contiver o wrapper:

```powershell
.\gradlew.bat clean build
```

O artefato é gerado em `build/libs/`. O build inclui verificações de testes JUnit, versão empacotada e presença no JAR de recursos obrigatórios (texturas, loot tables e blockstates). **Build bem-sucedido não garante compatibilidade no jogo.**

Para executar nos ambientes de desenvolvimento:

```powershell
gradle runClient
gradle runServer
```

Para instalar em um perfil de testes, copie **somente o JAR** de `build/libs/` para a pasta `mods/` de um cliente ou servidor NeoForge 1.21.1. Remova versões anteriores do mesmo port da pasta `mods/`; não instale vários JARs com o ID `chickens`.

## Organização

| Caminho | Conteúdo |
| --- | --- |
| `src/main/java/` | Implementação Java do mod |
| `src/main/resources/` | Texturas, modelos, idiomas, receitas e tabelas de loot |
| `src/test/java/` | Testes de regressão JUnit |
| `docs/` | Auditorias, matrizes de validação e documentação técnica |
| `docs/archive/` | Documentos históricos das entregas incrementais, **não** instruções atuais de instalação |
| `legacy_species_manifest.json` | Manifesto de referência das espécies originais |
| `LICENSE-original-MIT.txt` | Aviso de licença original preservado para conferência |

**Arquivos gerados** pelo Gradle (`.gradle/`, `build/`, `*.zip` e `*.jar`) não fazem parte dos fontes e não devem ser commitados.

## Testes antes de uma release

O checklist completo está em [docs/STAGE-51-RELEASE-CHECKLIST.md](docs/STAGE-51-RELEASE-CHECKLIST.md). As verificações mais importantes são:

1. Executar `gradle clean build` com todos os testes.
2. Iniciar **servidor dedicado** NeoForge 1.21.1, Java 21, sem JEI/Jade instalados no servidor, e conectar um cliente.
3. Testar captura e soltura de galinhas, texturas (inclusive filhotes), genética, postura e **46 espécies**.
4. Testar Roost, Breeder, Henhouse, saída cheia, hoppers/tubulações, salvamento e recarregamento de mundos.
5. Verificar ovos líquidos em pilhas, transferências para tanques de outros mods e consumo em caso de falha.
6. Testar backup de mundos antigos e avaliar MSPT/TPS com muitas entidades e máquinas.
7. Conferir licenças e atribuição do código, das texturas e dos recursos originais **antes da publicação pública**.

## Créditos e licença

Projeto original: **CreeperHost Chickens**, créditos a **CreeperHost, gigabit101 e aos autores originais**. Este é um port comunitário não afiliado aos autores do mod original.

O arquivo de metadados atualmente declara `MIT` e o repositório mantém `LICENSE-original-MIT.txt`. A atribuição e os termos de redistribuição de **todo** o material reutilizado ainda precisam ser confirmados antes de publicar uma versão estável.
