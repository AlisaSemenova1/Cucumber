# language: ru
# encoding: UTF-8
@withdrawal
Функция: Динамически загружаемые элементы страницы

  @success
  Сценарий: Кликнуть на Example 2 и дождаться текста после лоадера
    Дано  открыть "https://the-internet.herokuapp.com/dynamic_loading"
    Когда кликаем по примеру номер два
    Тогда отоброжается кнопка старт, кликаем по ней
    Тогда ждем текста Hello World!
