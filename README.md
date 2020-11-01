# Zoo
Docker нужно забилдить и запустить:
  1. Для билда нужен собранный jar файл, для его получения MVN clean install (lifecycle)
  2. Билд команда для докер: docker build -t <image name> .
  3. Для этого проекта нужны переменные среды, для докера можно использовать обычный txt файл, запись вида Var=Val
  4. Команда запуска: run command: docker run -p 127.0.0.1:8080:8080 —env-file <en v file .txt> <image name> env
