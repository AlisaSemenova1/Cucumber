# language: ru
# encoding: UTF-8
@withdrawal
Функция: Нажать в поле для ввода на клавишу и посмотреть, что ввели

  @success
  Сценарий: Отправить команду и посмотреть , что ввели
    Дано  открываем "https://the-internet.herokuapp.com/key_presses"
    Когда в поле для ввода нажимаем клавишу "ENTER"
    Тогда текст не появился
    Когда в поле для ввода нажимаем клавишу "TAB"
    Тогда в поле ввода появилось "You entered: TAB"
    Когда в поле для ввода вводим текст "k"
    Тогда в поле ввода появилось "You entered: K"
