# Chickens 1.0.32 — port original NeoForge 1.21.1 / Etapa 3

Este projeto **não usa Modern Chickens**. Continua baseado no código CreeperHost Chickens 1.0.32 e nos recursos do JAR 1.18.2 enviado.

## Correção visual

- O item `chickens:chicken_item` agora recebe `RegisterColorHandlersEvent.Item`, assim como os ovos.
- Seu modelo usa duas camadas **das texturas originais**, tintadas pelos atributos RGB da espécie; as galinhas com cor de dye respeitam a cor de seu corante.
- As 46 variantes continuam no mesmo item com `ChickenType.id` em `CUSTOM_DATA`.
- **Não é ainda a renderização 3D original do item capturado**; isso requer adaptar o antigo renderizador `RenderChickenItem`.

## Avanço funcional

### Roost
- `BlockRoost`, `BlockEntityRoost`, `RoostMenu`, `RoostScreen` usando textura GUI original `textures/gui/roost.png`.
- Um slot de entrada de até 16 galinhas, quatro de saída.
- Tempo/progresso legado de 1000 pontos; crescimento acelera a produção e ganho aumenta a quantidade.
- Produtos da tabela original de espécies, inclusive tipo de ovo líquido em `CUSTOM_DATA`.
- Inventário e progresso persistidos via NBT com `HolderLookup.Provider`.
- Automação com capability: insere apenas na entrada; extrai apenas dos slots de saída.
- Não cria produtos parcialmente quando as saídas não comportam o lote.

### Breeder
- `BlockBreeder`, `BlockEntityBreeder`, `BreederMenu`, `BreederScreen` usando a GUI original.
- Dois slots de pais, um de sementes (`c:seeds`), três de saída.
- Cruzamentos ponderados do catálogo original e herança/mutação dos atributos de galinhas.
- Consome uma semente só depois da saída ser aceita integralmente.
- Preserva estados originais `has_seeds` e `is_breeding` para os modelos do bloco.
- Inventário/progresso persistidos e capability para automação.

## Como aplicar

**Preferível:** faça backup de `C:\Users\Allan\Desktop\chickensNeo` e sobreponha o conteúdo de `Chickens-NeoForge-1.21.1-ETAPA-3-PATCH.zip` nessa pasta (os diretórios `src` são mesclados). Isso não substitui desnecessariamente seu `build.gradle` nem seus outros arquivos.

Depois execute:

```powershell
gradle clean build
```

Copie **somente o novo JAR gerado** de `build\libs` para o perfil Minecraft 1.21.1 + NeoForge; retire o JAR anterior para não manter dois mods `chickens`.

### Roteiro de teste no jogo

1. Abra a aba Chickens e confirme se os ovos estão coloridos diferentemente.
2. Posicione um Roost, abra com botão direito e coloque uma galinha em item no slot de entrada. Aguarde a produção.
3. Verifique se os produtos são empilhados ou distribuídos nos quatro slots de saída; teste saídas cheias.
4. Coloque Breeder, insira duas galinhas e sementes. Verifique progresso, filho obtido, consumo de sementes e atributos.
5. Saia e entre de novo no mundo; confira itens e progresso.
6. Quebre os blocos e confirme que não há perda ou duplicação de itens.
7. Opcional: teste hoppers/tubulações de automação nas entradas e saídas.

## Limitações explícitas

- **O build oficial NeoForge/Gradle não foi executado neste ambiente**: distribuição Gradle e dependências NeoForge não estão instaladas/cached e não há resolução DNS. Os métodos de API foram conferidos contra a documentação 1.21.1 e os algoritmos isolados foram testados, mas a integração real continua dependendo do seu build.
- O Henhouse **continua sem comportamento funcional**; apenas posicionamento e textura foram portados.
- `colored_egg` arremessável, integração JEI/TOP, renderização 3D do item galinha, spawning natural e migração integral de saves da 1.18.2 ainda não estão homologados.
- A API `ItemStackHandler` em NeoForge 1.21.1 exige testes de movimentação real com componentes de item, especialmente clique com Shift e automação.
- Recomenda-se testar **em um mundo descartável** até confirmar o salvamento dos block entities e inventários.

## Validação realizada

- 1.006 verificações independentes do domínio (46 tipos, genética e cruzamentos).
- 16 verificações de inserção de saída com stubs, sem alterações parciais ou misturas de componentes.
- 91 PNGs preservados byte a byte em relação à Etapa 2; todos os 116 JSONs parseados com sucesso.
- Arquivos do projeto empacotados e verificados por SHA-256.
