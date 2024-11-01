Экосистема

Кеда Александр

Приложение позволяет:
1) Создавать/загружать из файлов экосистемы
2) Добавлять новые виды животных и растений
3) Получать данные по экосистеме и животным, растениям в ней
4) Обновлять данные экосистемы и животных, растений в ней
5) Удалять животных и растения из экосистемы.

При запуске программы необходимо создать экосистему один из этих способов:
1) Для открытия существующей ввести: open [путь до файла с данными экосистемы] [путь до места хранения файлов]
2) Для создания новой ввести: new [путь до места хранения файлов] название_экосистемы температура влажность объём_воды

Важно: В названиях (экосистемы и видов) можно использовать только латинские буквы, цифры, нижнее подчеркивание и дефис.
В указанной при создании/открытии экосистемы папке (путь до места хранения файлов) будет создана папка с именем экосистемы.
В папке будет хратиться txt файл с данными по экосистеме и две папки с txt файлами с данными о животных и растениях.

После создания экосистемы доступны следующие действия:
1) Добавление новых видов, формат: -c [animal или plant] [все параметры выбранного типа через пробел];
2) Чтение данных, формат: -r [ecosystem, animal или plant] [название вида, если выбрана не экосистема];
3) Обновление данных, формат: -u [ecosystem, animal или plant] [все параметры выбранного типа через пробел];
4) Удаление видов, формат: -d [animal или plant] [название вида].

Набор полей класса Animal, необходимых для создания/обновления, и ограничения :
1) nameOfSpecies - название вида, неизменяемое поле (String, можно использовать только латинские буквы, цифры, пробел и дефис)
2) count - число представителей (int, больше 0)
3) reproductionRate - число новых представителей на пару в год (double, больше 0)
4) lifeTime - время жизни в годах (int, больше 0, меньше 1000)
5) weight - вес взрослого представителя в кг (double, больше 0)
6) humidityComf - комфортная влажность в % (int, от 0 до 100)
7) temperatureComf - комфортная температура в град цельсия (int, от -100 до 100)
8) waterForDay - потребление воды в день в литрах (int, от 0 до weight * 0.4)
9) foodForDay - потребление питательных веществ в день в кг (int, от 0 до weight * 0.4))
10) speed - скорость в км/ч (int, от 0 до 500)
11) carnivorous - признак плотоядности (boolean, хотя бы один из признаков пищевых предпочтений должен быть true)
12) herbivorous - признак травоядности (boolean, хотя бы один из признаков пищевых предпочтений должен быть true)

Пример команды создания Animal:
-c animal Cat 500 3.0 10 3.0 75 25 1 1 30 true false

Набор полей класса Plant, необходимых для создания/обновления, и ограничения :
1) nameOfSpecies - название вида, неизменяемое поле (String, можно использовать только латинские буквы, цифры, пробел и дефис)
2) count - число представителей (int, больше 0)
3) reproductionRate - число новых представителей на пару в год (double, больше 0)
4) lifeTime - время жизни в годах (int, больше 0, меньше 1000)
5) weight - вес взрослого представителя в кг (double, больше 0)
6) humidityComf - комфортная влажность в % (int, от 0 до 100)
7) temperatureComf - комфортная температура в град цельсия (int, от -100 до 100)
8) waterForDay - потребление воды в день в литрах (int, от 0 до weight * 0.4)
9) foodForDay - потребление питательных веществ в день в кг (int, от 0 до weight * 0.4))
10) eatablePart - доля съедобной части в весе растения (double, от 0 до 1)

Пример команды создания Plant:
-c plant Birch 2000 2 150 300.0 50 20 30 5 0.0