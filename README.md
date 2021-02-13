# Zoo
### Для запуска проекта
  - JDK 1.8
  - Idea-> Plugins -> Lombok -> Install
  - Idea -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Proccesor -> Enable annotation Proccesing
### Git Branching 
  - Новые ветки с изменениями должны называться feature/task-name! (пример: feature/telegram-bot)
  - Изменения в master только через pull request, в тайтле указать суть изменений, либо название задачи из trello
  - Прежде чем начать работу над задачей :
      - git checkout master
      - git pull origin master
      - git checkout -b feature/task-name
  - По завершении создать пул реквест и прикрепить в trello в комментарий к задаче
  
### Полезные ссылки
**Trello**  https://trello.com/b/TACgpFnq/zoo  
**Тестовая H2 DB**  http://localhost:8080/h2-console

### Как готовить Docker
  1. Для билда нужен собранный jar файл, для его получения <code>mvn clean install</code>
  2. Билд команда для докер: <code>docker build -t <imageName> . </code>
  3. Для этого проекта нужны переменные среды, для докера можно использовать обычный txt файл, запись вида Var=Val, можно найти в Trello
  4. Команда запуска: <code> docker run -p 127.0.0.1:8080:8080 —env-file <envfile.txt> <imageName> env </code>

Layering

![alt text](https://i.imgur.com/h6IBLvF.png)

