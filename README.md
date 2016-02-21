# jgrep
Реализация утилиты grep на Java

## Сборка
Для того, что бы собрать приложение из исходников надо предварительно установить Maven и выполнить в консоли:
```
mvn clean package
```

## Запуск
Первым параметром утилита принимает строку для поиска, вторым и последующими параметрами следуют имена файлов для поиска строки.
Для того что бы запустить собранный проект надо выполнить в консоли:
```
mvn exec:java -q \
  -Dexec.mainClass=ru.kurtov.jgrep.Main \
  -Dexec.args="Письмо resources/WarAndPeace.txt resources/EugeneOnegin.txt"
```

## Реализация
### Поиск строки в файле
Для поиска строки в файле реализовано два механизма поиска:
- `SimpleSearcher` - обычный поиск подстроки, основанный на методе String.contains(),
- `KMPSearcher` - поиск, основанный на алгоритме [Кнута-Морриса-Пратта](https://ru.wikipedia.org/wiki/Алгоритм_Кнута_—_Морриса_—_Пратта).
Реализация алгоритма взята [отсюда](https://ru.wikibooks.org/wiki/Реализации_алгоритмов/Алгоритм_Кнута_—_Морриса_—_Пратта)
и с незначительными изменениями представлена в классе `Kmp`.

Оба мезанизма поиска реализую интерфейс `Searcher`.

### Считывание файла
Считывание файла и передача его на вход реализации интерфейс `Searcher` выполнено в трех видах:
- `JGrepCharBuffer` - последовательное считывание файла небольними порциями в буффер и поиск строки в файле
- `JGrepMappedByteBuffer` - последовательное отображение файла целиком на буффер и поиск строки в файле
- `JGrepCharBufferMultiThred` - многопоточная реализация. Один поток считывает файл небольними порциями
в буффер и считанный буффер помещает в синхронизированную очередь. Второй поток читает из этой очереди

## Замер производительности
Для замера производительности используется библиотека [JMH](http://openjdk.java.net/projects/code-tools/jmh/).
Машина для тестирования:

Характеристика | Значение
-------------- | -------------
Процессора | Intel Core i5
Скорость процессора | 1,3 GHz
Количество процессоров | 1
Общее количество ядер | 2
Память | 4 ГБ

Для запуска теста для поиска слова в серии из 10 больших текстов (найти слово "Письмо" в "Войне и Мире") выполнить в консоли:
```
java -cp target/benchmarks.jar ru.kurtov.jmh.JMHTenBigFiles
```

Для запуска теста для поиска слова в серии из 10 маленьких текстов (найти слово "delicatissimi" в LoremIpsum-подомном тексте) выполнить в консоли:
```
java -cp target/benchmarks.jar ru.kurtov.jmh.JMHTenSmallFiles
```

### Результаты
Имя метода в тестовом классе состоит из имени реализации Searcher (`kmpSearcher` или `simpleSearcher`) и имени способа
считывания файла без префикса JGrep (`CharBuffer`, `MappedByteBuffer` или `CharBufferMultiThred`) 

Результаты для серии из больших файлов:
```
Benchmark                                          Mode  Cnt    Score    Error  Units
JMHTenBigFiles.kmpSearcherCharBuffer               avgt   30  501,398 ± 17,299  ms/op
JMHTenBigFiles.kmpSearcherCharBufferMultiThred     avgt   30  378,486 ±  4,338  ms/op
JMHTenBigFiles.kmpSearcherMappedByteBuffer         avgt   30  572,515 ± 19,778  ms/op
JMHTenBigFiles.simpleSearcherCharBufferMultiThred  avgt   30  424,743 ± 44,678  ms/op
JMHTenBigFiles.simpleSearcherJGrepCharBuffer       avgt   30  468,360 ± 22,650  ms/op
JMHTenBigFiles.simpleSearcherMappedByteBuffer      avgt   30  526,474 ± 20,325  ms/op
```

Многократный запуск тестов показывает, что kmpSearcherCharBufferMultiThred и simpleSearcherCharBufferMultiThred
меняюстся местами, но всегда разделяют первое и второе место.
Таким ображом, для больших файлов многопоточная реализация работает быстрее остальных.

Результ для серии из маленьких файлов:
```
Benchmark                                            Mode  Cnt  Score   Error  Units
JMHTenSmallFiles.kmpSearcherCharBuffer               avgt   30  1,343 ± 0,610  ms/op
JMHTenSmallFiles.kmpSearcherCharBufferMultiThred     avgt   30  1,528 ± 0,490  ms/op
JMHTenSmallFiles.kmpSearcherMappedByteBuffer         avgt   30  0,720 ± 0,363  ms/op
JMHTenSmallFiles.simpleSearcherCharBufferMultiThred  avgt   30  1,419 ± 0,358  ms/op
JMHTenSmallFiles.simpleSearcherJGrepCharBuffer       avgt   30  1,420 ± 1,309  ms/op
JMHTenSmallFiles.simpleSearcherMappedByteBuffer      avgt   30  0,685 ± 0,210  ms/op
```

Многократный запуск тестов показывает, что kmpSearcherMappedByteBuffer и simpleSearcherMappedByteBuffer
меняюстся местами, но всегда разделяют первое и второе место.
Таким ображом, для маленьких файлов реализация с отображением файла целиком на буффер работает быстрее остальных.

## Выводы
Для больших файлов многопоточная реализация работает быстрее остальных. 
Для маленьких файлов отображение файла целиком на буффер работает быстрее. Реализация Searcher-а особой роли не играет.

В классе `Main` используется многопаточный поиск с `KMPSearcher`.
