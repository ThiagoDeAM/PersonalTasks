# PersonalTasks

Aplicativo Android desenvolvido em Kotlin que permite ao usuário **criar, visualizar, editar e remover tarefas** pessoais com data limite.

## Funcionalidades

- Adicionar tarefas com título, descrição e data limite
- Visualizar tarefa em modo somente leitura
- Editar tarefas existentes
- Remover tarefas com confirmação
- Persistência local utilizando Room (SQLite)

## Tecnologias Utilizadas

- Kotlin
- Android Jetpack (Room, Lifecycle, ViewBinding)
- Coroutines
- RecyclerView
- ConstraintLayout

## Como executar o projeto

### Pré-requisitos

- Compatibilidade mínima com Android 8.0 (API 26)
- Android Studio LadyBug ou superior

### Passos

1. Clone este repositório:

```bash
git clone https://github.com/ThiagoDeAM/personaltasks.git
```

2. Abra o projeto no Android Studio
3. Aguarde a sincronização das dependências
4. Execute o app em um emulador ou dispositivo físico

## Estrutura de Pastas

```bash
com.example.personaltasks
│
├── adapter          # Adapters (TaskAdapter, TaskRvAdapter)
├── model            # Entidades e DAO (Task, TaskDao, Converters)
├── controller       # MainController
├── ui               # Activities (MainActivity, TaskActivity)
└── res/layout       # Layouts XML
```

## Persistência

- Utiliza Room (ORM do Jetpack)
- Task é a entidade principal, persistida localmente
- Converters permitem salvar objetos Date

##  Exemplo de uso no App

- Clique no ícone **"+"** no topo para adicionar uma tarefa pessoal
- Preencha os campos de título, descrição e selecione a data limite 
- Clique em **Salvar** para registrar a tarefa ou em **Cancelar** para ignorar os dados preenchidos e voltar à Tela 1
- Clique sobre uma tarefa para visualizá-la
- Use o menu de contexto para editar ou remover uma tarefa

## Vídeo

- [Execução final do Aplicativo - Entrega 1](https://tinyurl.com/554e29er)
- [Execução final do Aplicativo - Entrega 2](https://tinyurl.com/y866csc6)
