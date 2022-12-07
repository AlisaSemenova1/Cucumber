# language: ru
# encoding: UTF-8
@withdrawal
Функция: Тесты the-internet

  @success
  Сценарий: Нажать чекбокс, проверить что он нажат
    Дано захожу на страницу "https://the-internet.herokuapp.com/checkboxes"
    Когда нажимаю чекбокс
    Тогда проверяю, что чекбокс нажат

  @success
  Структура сценария: Ввести правильный логин/пароль и неправильный, проверить результат
    Дано захожу на страницу "https://the-internet.herokuapp.com/login"
    Когда ввожу в соответствующие поля "<username>" и "<password>"
    Тогда нажимаю на кнопку логин
    Тогда если пароль верен вижу сообщение "You logged into a secure area!"
    Тогда если пароль не вререн вижу сообщение "Your username is invalid!"
    Примеры:
      | username  | password             |
      | tomsmith  | SuperSecretPassword! |
      | marysmith | NotSecretPassword!   |

  @success
  Сценарий: Навести курсор на нужного пользователя, проверить что отображается текст
    Дано  захожу на страницу "https://the-internet.herokuapp.com/hovers"
    Когда навожу курсор на пользователя
    Тогда отоброжается текст

  @success
  Сценарий: Кликнуть на Example 2 и дождаться текста после лоадера
    Дано  захожу на страницу "https://the-internet.herokuapp.com/dynamic_loading"
    Когда кликаем по примеру номер два
    Тогда отоброжается кнопка старт, кликаем по ней
    Тогда ждем текста Hello World!

  @success
  Сценарий: Отправить команду и посмотреть , что ввели
    Дано  захожу на страницу "https://the-internet.herokuapp.com/key_presses"
    Когда в поле для ввода нажимаем клавишу "ENTER"
    Тогда текст не появился
    Когда в поле для ввода нажимаем клавишу "TAB"
    Тогда в поле ввода появилось "You entered: TAB"
    Когда в поле для ввода вводим текст "k"
    Тогда в поле ввода появилось "You entered: K"

