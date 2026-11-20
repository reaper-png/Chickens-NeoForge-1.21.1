# Chickens 1.0.32 -> NeoForge 1.21.1 | Etapa 4 (WIP)

Base: projeto da Etapa 3, port direto do CreeperHost Chickens 1.0.32; **sem Modern Chickens**.

## Correção da textura invisível dos itens

`ChickensClientEvents.onColors` retornava cores `0xRRGGBB`, com alpha `0x00`. No renderizador da 1.21.1, é necessário `0xFFRRGGBB`: as 46 variantes estavam transparentes apesar de modelos e PNGs existirem.

- Nova `OpaqueTint.fromRgb` aplica alpha 255 a todos os `ItemColor` registrados.
- Mantidos os PNGs, modelos 2D, texturas de entidades e nomes originais.
- A correção também afeta `spawn_egg` e `colored_egg`.

## Etapa 4 funcional

### Henhouse

- Registrado `BlockEntityHenhouse` nos seis IDs existentes (henhouse + cinco madeiras).
- 11 slots originais: feno, terra, 9 saídas; interface `textures/gui/henhouse.png` (176×166).
- Produção de galinhas adultas próximas é encaminhada ao Henhouse, com sobras depositadas no mundo.
- 100 unidades de energia por fardo de feno, geração de terra, transferência parcial apenas quando há combustível, espaço para terra e saída suficiente.
- Inventário e energia persistidos; suporte aos dados antigos de inventário `Items` quando presentes.
- Capabilities NeoForge permitem inserir feno e extrair terra/produção (não extrair feno nem inserir nas saídas).
- O coletor só examina posições em chunks já carregados, sem forçar carregamento de chunks.

### Ovo colorido arremessável

- Entidade `chickens:egg` e renderer `ThrownItemRenderer`.
- Preservada a chance do original: 1/8 de eclodir; chance 1/32 de produzir quatro galinhas, quando eclodir.
- As variantes de ovo colorido são listadas no criativo para galinhas de corante.
- O leitor de tipos suporta tanto `CUSTOM_DATA.ChickenType.id` como o `CUSTOM_DATA.id` usado por ovos legados.

## Validação e limitações

- Java 21: classes `OpaqueTint` e `HenhouseFuel` compiladas e verificadas com **155.746 asserções** numéricas.
- 15 modelos de item verificados contra os PNGs correspondentes: nenhuma referência ausente.
- As três interfaces originais `henhouse`, `roost` e `breeder` estão presentes.
- **NÃO foi possível executar `gradle build` com as dependências NeoForge** neste ambiente, por ausência da distribuição e falha de DNS de `services.gradle.org`. Compilar/testar no Windows ainda é obrigatório.
- Não há homologação de jogo, loot/receitas antigas, persistência de mundos Forge antigos, compatibilidade de automação com outros mods ou representação 3D de galinhas como itens. Esses itens permanecem pendentes.
- Usar somente em mundo de teste, com backup. O arquivo completo ainda é um WIP.

## Aplicação

1. Faça backup de `C:\Users\Allan\Desktop\chickensNeo`.
2. Extraia **Etapa 4 PATCH** diretamente na raiz do projeto, sobrepondo os arquivos e mesclando as pastas. Não coloque arquivos `.java` em `mods/`.
3. Execute `gradle clean build` com Java 21.
4. Retire o JAR antigo do port da pasta `mods`, coloque o JAR compilado em `build/libs` e reinicie o Minecraft.
5. Verifique primeiro os itens/ovos no criativo; depois Henhouse com um fardo de feno e uma galinha adulta por perto; por último experimente lançar um ovo colorido.

Se o objetivo for **apenas recuperar os ícones**, use `Chickens-NeoForge-1.21.1-ETAPA-4-SOMENTE-CORES.zip` com os dois arquivos necessários, sem integrar o Henhouse e o projetil.
