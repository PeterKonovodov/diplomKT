<img src = "logo.png" width = 200px>
    
# Quotes! v0.5
 
## Краткое описание структуры дипломного проекта по курсу "Введение в Android на языке Kotlin"    

Ссылка на задание: https://github.com/netology-code/bkt-diploma/tree/light

Данное приложение основано на одной Activity и компоненте Navigation, который управляет     
переключеним активных экранов (фрагментов):
  
 - Экран просмотра списка цитат.
 - Экран просмотра списка цитат с сортировкой по автору.    
 - Экран ввода новой цитаты.

В приложении подразумевается, что существуют цитаты от владельца телефона
(User, имя жестко забито в ресурсах String), посторонних авторов.
User может копировать цитату постороннего автора, но не свою.
При вводе цитаты имя автора выбирать нельзя, но можно выбрать изображение из галереи и ссылку в интернет.
Для полноценного тестирования эмулятор должен содержать какие-либо изображения в галерее (чтобы их выбирать).



## О выборке и сохранении изображений.
В классе Quote имеются поля:
imagePath : String - путь к файлу изображения, хранимому в папке приложения
imageDrawable : Drawable - собственно изображение

Класс QuoteEntity имеет только поле imagePath.
При запуске приложения из базы данных берется только путь к файлу изображения, откуда оно считывается
(если есть) и адаптером выводится на экран.

imageDrawable хранит изображение, которое при чтении БД добывается из внутренней памяти,
если imagePath не пустое, и используется для вывода в карточке цитаты.
Также imageDrawable используется для временного хранения "захардкоженных" изображений,
которые изначально хранятся в res/drawable/

При этом логика хардкода такова:
Если БД пустая, то считаем, что произошел первый запуск приложения. Вызывается функция hardCodeData()
Метод saveQuote смотрит на состояние imagePath. Если там есть путь к файлу изображения - сохраняет
в БД и не смотрит далее.

Если imagePath = "", то смотрит на imageDrawable. Если там не null, то сохраняет в виде файла в
каталоге приложения и формирует imagePath, который сохраняется в БД.

### Сделано:

Почти весь основной функционал по работе с БД, загрузка/сохранение изображений, лайки/дизлайки, шаринг,
сортировка

### Осталось сделать:

удаление своей цитаты

выход по двойному нажатию BACK

Заголовок приложения

иконка приложения

единообразные стили текста и т.п.

порционная загрузка

тестирование быстродействия (проверить вызывается ли getAll при лайке одной цитаты. Если да - побороть! должно вызваться только saveQuote)

Найденные глюки:

фон кнопок лайк/дизлайк "гуляет"

поддержка языков

поддержка поворота экрана

тестирование с разными экранами


